package com.yunip.web.common;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jcifs.Config;
import jcifs.UniAddress;
import jcifs.http.NtlmSsp;
import jcifs.smb.NtlmChallenge;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbAuthException;
import jcifs.smb.SmbSession;
import jcifs.util.Base64;
import jcifs.util.LogStream;

import org.apache.log4j.Logger;

/***
 * ad域登录类
 */
public class AdLoginService {

    Logger                   logger = Logger.getLogger(AdLoginService.class);

    private static LogStream log    = LogStream.getInstance();

    private String           defaultDomain;

    private String           domainController;

    private boolean          loadBalance;

    private boolean          enableBasic;

    private boolean          insecureBasic;

    private String           realm;

    public void initJcifsConfig() {
        int level;
        Config.setProperty("jcifs.http.domainController", "192.168.11.245");
        Config.setProperty("jcifs.smb.client.domain", "123.com");
        Config.setProperty("jcifs.smb.client.soTimeout", "6000");
        Config.setProperty("jcifs.netbios.cachePolicy", "1200");
        Config.setProperty("jcifs.smb.lmCompatibility", "0");
        Config.setProperty("jcifs.smb.client.useExtendedSecurity", "false");
        Config.setProperty("jcifs.util.loglevel", "1");
        enableBasic = Boolean.valueOf(
                Config.getProperty("jcifs.http.enableBasic")).booleanValue();
        insecureBasic = Boolean.valueOf(
                Config.getProperty("jcifs.http.insecureBasic")).booleanValue();
        realm = Config.getProperty("jcifs.http.basicRealm");

        defaultDomain = Config.getProperty("jcifs.smb.client.domain");
        domainController = Config.getProperty("jcifs.http.domainController");
        if (domainController == null) {
            domainController = defaultDomain;
            loadBalance = Config.getBoolean("jcifs.http.loadBalance", true);
        }
        if (realm == null)
            realm = "jCIFS";

        if ((level = Config.getInt("jcifs.util.loglevel", -1)) != -1) {
            LogStream.setLevel(level);
        }
        if (LogStream.level > 2) {
            try {
                Config.store(log, "JCIFS PROPERTIES");
            }
            catch (IOException ioe) {}
        }
    }

    /**
     * Negotiate password hashes with MSIE clients using NTLM SSP
     * @param req The servlet request
     * @param resp The servlet response
     * @param skipAuthentication If true the negotiation is only done if it is
     * initiated by the client (MSIE post requests after successful NTLM SSP
     * authentication). If false and the user has not been authenticated yet
     * the client will be forced to send an authentication (server sends
     * HttpServletResponse.SC_UNAUTHORIZED).
     * @return True if the negotiation is complete, otherwise false
     */
    public NtlmPasswordAuthentication negotiate(HttpServletRequest req,
            HttpServletResponse resp, boolean skipAuthentication)
            throws IOException, ServletException {
        UniAddress dc;
        String msg;
        NtlmPasswordAuthentication ntlm = null;
        msg = req.getHeader("Authorization");
        boolean offerBasic = enableBasic && (insecureBasic || req.isSecure());

        if (msg != null
                && (msg.startsWith("NTLM ") || (offerBasic && msg.startsWith("Basic ")))) {
            if (msg.startsWith("NTLM ")) {
                HttpSession ssn = req.getSession();
                byte[] challenge;

                if (loadBalance) {
                    NtlmChallenge chal = (NtlmChallenge) ssn.getAttribute("NtlmHttpChal");
                    if (chal == null) {
                        chal = SmbSession.getChallengeForDomain();
                        ssn.setAttribute("NtlmHttpChal", chal);
                    }
                    dc = chal.dc;
                    challenge = chal.challenge;
                }
                else {
                    dc = UniAddress.getByName(domainController, true);
                    challenge = SmbSession.getChallenge(dc);
                }

                if ((ntlm = NtlmSsp.authenticate(req, resp, challenge)) == null) {
                    return null;
                }
                /* negotiation complete, remove the challenge object */
                ssn.removeAttribute("NtlmHttpChal");
            }
            else {
                String auth = new String(Base64.decode(msg.substring(6)),
                        "US-ASCII");
                int index = auth.indexOf(':');
                String user = (index != -1) ? auth.substring(0, index) : auth;
                String password = (index != -1) ? auth.substring(index + 1) : "";
                index = user.indexOf('\\');
                if (index == -1)
                    index = user.indexOf('/');
                String domain = (index != -1) ? user.substring(0, index) : defaultDomain;
                user = (index != -1) ? user.substring(index + 1) : user;
                ntlm = new NtlmPasswordAuthentication(domain, user, password);
                dc = UniAddress.getByName(domainController, true);
            }
            try {

                SmbSession.logon(dc, ntlm);
                //在catch 中做下修改 就不会弹出验证框了，直接当做验证失败跳到登录页面，我忘了有没有改了，反正是头信息，与源码对下吧
                if (LogStream.level > 2) {
                    log.println("NtlmHttpFilter: " + ntlm
                            + " successfully authenticated against " + dc);
                }
            }
            catch (SmbAuthException sae) {
                if (LogStream.level > 1) {
                    log.println("NtlmHttpFilter: "
                            + ntlm.getName()
                            + ": 0x"
                            + jcifs.util.Hexdump.toHexString(sae.getNtStatus(),
                                    8) + ": " + sae);
                }
                if (sae.getNtStatus() == SmbAuthException.NT_STATUS_ACCESS_VIOLATION) {
                    /* Server challenge no longer valid for
                     * externally supplied password hashes.
                     */
                    HttpSession ssn = req.getSession(false);
                    if (ssn != null) {
                        ssn.removeAttribute("NtlmHttpAuth");
                    }
                }
                resp.setHeader("WWW-Authenticate", "NTLM");
                if (offerBasic) {
                    resp.addHeader("WWW-Authenticate", "Basic realm=\"" + realm
                            + "\"");
                }
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.setContentLength(0); /* Marcel Feb-15-2005 */
                resp.flushBuffer();
                return null;
            }
            req.getSession().setAttribute("NtlmHttpAuth", ntlm);
        }
        else {
            if (!skipAuthentication) {
                HttpSession ssn = req.getSession(false);
                if (ssn == null
                        || (ntlm = (NtlmPasswordAuthentication) ssn.getAttribute("NtlmHttpAuth")) == null) {
                    resp.setHeader("WWW-Authenticate", "NTLM");
                    if (offerBasic) {
                        resp.addHeader("WWW-Authenticate", "Basic realm=\""
                                + realm + "\"");
                    }
                    resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    resp.setContentLength(0);
                    resp.flushBuffer();
                    return null;
                }
            }
        }

        return ntlm;
    }
}
