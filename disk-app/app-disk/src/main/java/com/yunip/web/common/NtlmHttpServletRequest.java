package com.yunip.web.common;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class NtlmHttpServletRequest extends HttpServletRequestWrapper {

    Principal principal;

    NtlmHttpServletRequest(HttpServletRequest req, Principal principal) {
        super(req);
        this.principal = principal;
    }

    public String getRemoteUser() {
        return principal.getName();
    }

    public Principal getUserPrincipal() {
        return principal;
    }

    public String getAuthType() {
        return "NTLM";
    }

}
