<%@page import="java.net.InetAddress"%>
<%@page import="com.yunip.utils.date.DateUtil"%>
<%@ page contentType="text/html;charset=gb2312" %>
<%@ page import="java.util.*,java.io.*,javax.servlet.*,javax.servlet.http.*,java.lang.*" %>
<%
	/********************************************************************
	*	Title: JspEnv v
	*	Description : JSP����̽��
	*	CopyRight:(c)	2005 	www.soho.net.ru
	*	@author:	��Ӱ
	*	@version:	1.0
	*	@Data:	2005-1-1 12:00:00
	*********************************************************************/
	
	long timePageStart = System.currentTimeMillis();
%>
<%!
private String getIp()
{
   String strTmp="";
   try
   {
   strTmp =InetAddress.getLocalHost().getHostAddress();
   return strTmp;
   }
   catch(Exception e)
   {
   return strTmp;
   }
}
%>
<%
class EnvServlet
{
	public long timeUse=0;
	public Hashtable htParam=new Hashtable();
	private Hashtable htShowMsg=new Hashtable();
	public void setHashtable()
	{
		Properties me=System.getProperties();
		Enumeration em=me.propertyNames();
		while(em.hasMoreElements())
		{
			String strKey=(String)em.nextElement();
			String strValue=me.getProperty(strKey);
			htParam.put(strKey,strValue);
			//log(strKey + "-- " + strValue);
		}
	}	
	public void getHashtable(String strQuery)
	{
		Enumeration em=htParam.keys();
		while(em.hasMoreElements())
		{
			String strKey=(String)em.nextElement();
			String strValue=new String();
			if(strKey.indexOf(strQuery,0)>=0)
			{
				strValue=(String)htParam.get(strKey);
				htShowMsg.put(strKey,strValue);
			}
		}
	}
	public String queryHashtable(String strKey)
	{
		strKey=(String)htParam.get(strKey);
		return strKey;
	}
	public long test_int()
	{
		long timeStart = System.currentTimeMillis();
		int i=0;
		while(i<3000000)i++;
		long timeEnd = System.currentTimeMillis();
		long timeUse=timeEnd-timeStart;
		return timeUse;
	}
	public long test_sqrt()
	{
		long timeStart = System.currentTimeMillis();
		int i=0;
		double db=(double)new Random().nextInt(1000);
		while(i<200000){db=Math.sqrt(db);i++;}
		long timeEnd = System.currentTimeMillis();
		long timeUse=timeEnd-timeStart;
		return timeUse;
	}
}
%>
<%
	EnvServlet env=new EnvServlet();
	env.setHashtable();
	String action=new String(" ");
	String act=new String("action");
	if(request.getQueryString()!=null&&request.getQueryString().indexOf(act,0)>=0)action=request.getParameter(act);
%>
<html>
<head>
<title>���л���</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<style>
A       { COLOR: #000000; TEXT-DECORATION: none}
A:hover { COLOR: #f58200}
body,td,span { font-size: 9pt}
.input  { BACKGROUND-COLOR: #ffffff;BORDER:#f58200 1px solid;FONT-SIZE: 9pt}
.backc  { BACKGROUND-COLOR: #f58200;BORDER:#f58200 1px solid;FONT-SIZE: 9pt;color:white}
.PicBar { background-color: #00DB00; border: 0px solid #00DB00; height: 12px;}
.tableBorder {BORDER-RIGHT: #ddd 0px solid; BORDER-TOP: #ddd 0px solid; BORDER-LEFT: #ddd 0px solid; BORDER-BOTTOM: #ddd 0px solid; BACKGROUND-COLOR: #ffffff; WIDTH: 98%;}
.divcenter {
	position:absolute;
	height:30px;
	z-index:1000;
	left: 101px;
	top: 993px;
}
</STYLE>
<script language="javascript">
function showsubmenu(sid)
{
whichEl = eval("submenu" + sid);
if (whichEl.style.display == "none")
{
eval("submenu" + sid + ".style.display=\"\";");
eval("txt" + sid + ".innerHTML=\"<a href='#' title='�رմ���'><font face='Wingdings' color=#FFFFFF>x</font></a>\";");
}
else
{
eval("submenu" + sid + ".style.display=\"none\";");
eval("txt" + sid + ".innerHTML=\"<a href='#' title='�򿪴���'><font face='Wingdings' color=#FFFFFF>y</font></a>\";");
}
}
</SCRIPT>
</head>
<body topmargin="0" leftmargin="0">
<CENTER>
	<br>

  <table border="0" cellpadding="0" cellspacing="1" class="tableBorder">
  <tr> 
    <td height="22" align="left" bgcolor="#f5fafe" onclick="showsubmenu(0)"><font color=#000000><strong>&nbsp;��������Ϣ</strong></font> 
      
	</td>
  </tr>
  <tr> 
    <td style="display" id='submenu0'><table border=0 width=100% cellspacing=1 cellpadding=3 bgcolor="#ddd">
    	  <tr bgcolor="#FFFFFF" height="22"> 
            <td>&nbsp;����������ϵͳ</td>
            <td colspan="3">&nbsp;<%=env.queryHashtable("os.name")%> <%=env.queryHashtable("os.version")%> 
              <%=env.queryHashtable("sun.os.patch.level")%></td>
          </tr>
          <tr bgcolor="#FFFFFF" height="22"> 
            <td width="20%">&nbsp;������IP</td>
            <td width="30%">&nbsp;<%= getIp() %></td>
            <td width="20%">&nbsp;�������˿�</td>
            <td width="30%">&nbsp;<%= request.getServerPort() %></td>
          </tr>
          
          <tr bgcolor="#FFFFFF" height="22"> 
            <td>&nbsp;����������ϵͳ����</td>
            <td>&nbsp;<%=env.queryHashtable("os.arch")%></td>
            <td>&nbsp;����������ϵͳģʽ</td>
            <td>&nbsp;<%=env.queryHashtable("sun.arch.data.model")%>λ</td>
          </tr>     
          <tr bgcolor="#FFFFFF" height="22"> 
            <td>&nbsp;������ʱ��</td>
            <td>&nbsp;<%=env.queryHashtable("user.timezone")%></td>
            <td>&nbsp;������ʱ��</td>
            <td>&nbsp;<%= DateUtil.getDateFormat(new java.util.Date(), DateUtil.YMDTHMSGB_DATA) %> </td>
          </tr>
          <tr bgcolor="#FFFFFF" height="22"> 
            <td align=left bgcolor="#FFFFFF">&nbsp;��ǰ����Ŀ¼</td>
            <td height="8" colspan="3" bgcolor="#FFFFFF">&nbsp;<%=env.queryHashtable("user.dir").substring(0, env.queryHashtable("user.dir").lastIndexOf("\\"))%></td>
          </tr>
        </table>
    </td>
  </tr>
</table>
<br><br>
<table border="0" cellpadding="0" cellspacing="1" class="tableBorder">
  <tr> 
      <td height="22" align="left" bgcolor="#f5fafe" onclick="showsubmenu(1)"><font color=#000000><strong>&nbsp;������Ϣ</strong></font> 
      </td>
  </tr>
  <tr> 
    <td style="display" id='submenu1'>
		<table border=0 width=100% cellspacing=1 cellpadding=3 bgcolor="#ddd">
			<%
	          String path = com.yunip.config.SysConfigHelper.getValue(com.yunip.constant.SystemContant.OPENCODE, com.yunip.constant.SystemContant.FILEROOTPATH);
			 String[] strArr = path.split("/");
				String zpath = "";
				if(strArr.length>1){
				  zpath = path.split("/")[0];
				}else{
				  zpath = path.split("\\\\")[0];
				}
	          File diskPartition = new File(zpath);
	          float totalCapacity = diskPartition.getTotalSpace(); //�ܴ�С
	          float freePartitionSpace = diskPartition.getFreeSpace(); //ʣ���С
	          float usablePatitionSpace = totalCapacity-freePartitionSpace; //ʹ�ô�С
	          float tPercent=freePartitionSpace/totalCapacity*100;
	          java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
          %>
          <tr bgcolor="#FFFFFF" height="22"> 
            <td width="20%">&nbsp;�ļ����λ��</td>
            <td width="30%">&nbsp;<%=path %></td>
            <td width="20%">&nbsp;�û��ļ�ռ����</td>
            <td width="30%">&nbsp;${fileSizeNumber }</td>
          </tr>
          <tr bgcolor="#FFFFFF" height="22"> 
		  	<td width="20%">&nbsp;ʣ�������(<%=zpath %>)</td>
            <td height="22" colspan="3"><img align=absmiddle class=PicBar width='<%=0.7*tPercent%>%'>&nbsp;<%=df.format(freePartitionSpace/1024/1024)%>M (<%=df.format(freePartitionSpace/1024/1024/1024)%>G)
            </td>
          </tr>
          <tr bgcolor="#FFFFFF" height="22"> 
		  	<td width="20%">&nbsp;�ܹ�������(<%=zpath %>)</td>
            <td height="22" colspan="3"><img align=absmiddle class=PicBar width='70%'>&nbsp;<%=df.format(totalCapacity/1024/1024)%>M (<%=df.format(totalCapacity/1024/1024/1024)%>G)
            </td>
          </tr>
        </table>
    </td>
  </tr>
</table>
<br><br>

<table border="0" cellpadding="0" cellspacing="1" class="tableBorder">
  <tr> 
      <td height="22" align="left" bgcolor="#f5fafe" onclick="showsubmenu(2)"><font color=#000000><strong>&nbsp;���л�����Ϣ</strong></font> 
      </td>
  </tr>
  <tr> 
    <td style="display" id='submenu2'>
		<table border=0 width=100% cellspacing=1 cellpadding=3 bgcolor="#ddd">
          <tr bgcolor="#FFFFFF" height="22"> 
            <td width="20%">&nbsp;JAVA���л�������</td>
            <td width="30%" height="22">&nbsp;<%=env.queryHashtable("java.runtime.name")%></td>
            <td width="20%" height="22">&nbsp;JDK�汾</td>
            <td width="30%" height="22">&nbsp;<%=env.queryHashtable("java.runtime.version")%></td>
          </tr>
          <tr bgcolor="#FFFFFF" height="22"> 
            <td align=left bgcolor="#FFFFFF">&nbsp;JAVA���л���·��</td>
            <td height="8" colspan="3" bgcolor="#FFFFFF">&nbsp;<%=env.queryHashtable("java.home")%></td>
          </tr>
		  <%
		  	float fFreeMemory=(float)Runtime.getRuntime().freeMemory();
			float fTotalMemory=(float)Runtime.getRuntime().totalMemory();
			float fPercent=fFreeMemory/fTotalMemory*100;
		  %>
          <tr bgcolor="#FFFFFF" height="22"> 
		  	<td height="22">&nbsp;JAVA�����ʣ���ڴ棺</td>
            <td height="22" colspan="3"><img align=absmiddle class=PicBar width='<%=0.7*fPercent%>%'>&nbsp;<%=df.format(fFreeMemory/1024/1024)%>M 
            </td>
          </tr>
          <tr bgcolor="#FFFFFF" height="22"> 
		  	<td height="22">&nbsp;JAVA����������ڴ�</td>
            <td height="22" colspan="3"><img align=absmiddle class=PicBar width='70%'>&nbsp;<%=df.format(fTotalMemory/1024/1024)%>M 
            </td>
          </tr>
        </table>
    </td>
  </tr>
</table>
<br>
<br>
<table border="0" cellpadding="0" cellspacing="1" class="tableBorder">
  <tr> 
    <td height="22" align="left" bgcolor="#f5fafe" onclick="showsubmenu(0)"><font color=#000000><strong>&nbsp;�汾��Ϣ</strong></font> 
      
	</td>
  </tr>
  <tr> 
    <td style="display" id='submenu0'><table border=0 width=100% cellspacing=1 cellpadding=3 bgcolor="#ddd">
    	  <tr bgcolor="#FFFFFF" height="22"> 
            <td>&nbsp;��Ȩ����</td>
            <td colspan="3">&nbsp;���������Ƽ������޹�˾
          </tr>
          <tr bgcolor="#FFFFFF" height="22"> 
            <td>&nbsp;�ٷ���վ</td>
            <td colspan="3">&nbsp;</td>
          </tr>
          <tr bgcolor="#FFFFFF" height="22"> 
            <td>&nbsp;����֧����̳</td>
            <td colspan="3">&nbsp;</td>
          </tr>
          <tr bgcolor="#FFFFFF" height="22"> 
            <td>&nbsp;��Ʒʹ�ð���</td>
            <td colspan="3">&nbsp;</td>
          </tr>
          
        </table>
    </td>
  </tr>
</table>
<br><br>
<table border="0" cellpadding="0" cellspacing="1" class="tableBorder">
  <tr> 
      <td height="22" align="left" bgcolor="#f5fafe" onclick="showsubmenu(2)"><font color=#000000><strong>&nbsp;����С��ʿ</strong></font> 
      </td>
  </tr>
  <tr> 
    <td style="display" id='submenu2'>
		<table border=0 width=100% cellspacing=1 cellpadding=3 bgcolor="#ddd">
          <tr bgcolor="#FFFFFF" height="22"> 
            <td width="30%" height="22">&nbsp;1.��������ÿ���û��Ŀռ�ʹ�ô�С�������޸��û���Ϣ�����á�</td>
          </tr>
          <tr bgcolor="#FFFFFF" height="22"> 
            <td align=left bgcolor="#FFFFFF">&nbsp;2.�������Ƶ�¼��IP��ַ��������"IP����"�����á�</td>
          </tr>
		 
          <tr bgcolor="#FFFFFF" height="22"> 
		  	<td height="22">&nbsp;3.���������û�Ԥ���ļ�ˮӡ����ʹ��"ˮӡ����"��</td>
            </td>
          </tr>
          <tr bgcolor="#FFFFFF" height="22"> 
		  	<td height="22">&nbsp;4.������ְԱ�����ӣ���ʹ��"�ϲ��û��ʻ�"��</td>
            </td>
          </tr>
        </table>
    </td>
  </tr>
</table>
</CENTER>
<br>
<div>

</div>
</body>
</html>