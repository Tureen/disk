<%@page import="java.net.InetAddress"%>
<%@page import="com.yunip.utils.date.DateUtil"%>
<%@ page contentType="text/html;charset=gb2312" %>
<%@ page import="java.util.*,java.io.*,javax.servlet.*,javax.servlet.http.*,java.lang.*" %>
<%
	/********************************************************************
	*	Title: JspEnv v
	*	Description : JSP环境探针
	*	CopyRight:(c)	2005 	www.soho.net.ru
	*	@author:	若影
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
<title>运行环境</title>
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
eval("txt" + sid + ".innerHTML=\"<a href='#' title='关闭此项'><font face='Wingdings' color=#FFFFFF>x</font></a>\";");
}
else
{
eval("submenu" + sid + ".style.display=\"none\";");
eval("txt" + sid + ".innerHTML=\"<a href='#' title='打开此项'><font face='Wingdings' color=#FFFFFF>y</font></a>\";");
}
}
</SCRIPT>
</head>
<body topmargin="0" leftmargin="0">
<CENTER>
	<br>

  <table border="0" cellpadding="0" cellspacing="1" class="tableBorder">
  <tr> 
    <td height="22" align="left" bgcolor="#f5fafe" onclick="showsubmenu(0)"><font color=#000000><strong>&nbsp;服务器信息</strong></font> 
      
	</td>
  </tr>
  <tr> 
    <td style="display" id='submenu0'><table border=0 width=100% cellspacing=1 cellpadding=3 bgcolor="#ddd">
    	  <tr bgcolor="#FFFFFF" height="22"> 
            <td>&nbsp;服务器操作系统</td>
            <td colspan="3">&nbsp;<%=env.queryHashtable("os.name")%> <%=env.queryHashtable("os.version")%> 
              <%=env.queryHashtable("sun.os.patch.level")%></td>
          </tr>
          <tr bgcolor="#FFFFFF" height="22"> 
            <td width="20%">&nbsp;服务器IP</td>
            <td width="30%">&nbsp;<%= getIp() %></td>
            <td width="20%">&nbsp;服务器端口</td>
            <td width="30%">&nbsp;<%= request.getServerPort() %></td>
          </tr>
          
          <tr bgcolor="#FFFFFF" height="22"> 
            <td>&nbsp;服务器操作系统类型</td>
            <td>&nbsp;<%=env.queryHashtable("os.arch")%></td>
            <td>&nbsp;服务器操作系统模式</td>
            <td>&nbsp;<%=env.queryHashtable("sun.arch.data.model")%>位</td>
          </tr>     
          <tr bgcolor="#FFFFFF" height="22"> 
            <td>&nbsp;服务器时区</td>
            <td>&nbsp;<%=env.queryHashtable("user.timezone")%></td>
            <td>&nbsp;服务器时间</td>
            <td>&nbsp;<%= DateUtil.getDateFormat(new java.util.Date(), DateUtil.YMDTHMSGB_DATA) %> </td>
          </tr>
          <tr bgcolor="#FFFFFF" height="22"> 
            <td align=left bgcolor="#FFFFFF">&nbsp;当前程序目录</td>
            <td height="8" colspan="3" bgcolor="#FFFFFF">&nbsp;<%=env.queryHashtable("user.dir").substring(0, env.queryHashtable("user.dir").lastIndexOf("\\"))%></td>
          </tr>
        </table>
    </td>
  </tr>
</table>
<br><br>
<table border="0" cellpadding="0" cellspacing="1" class="tableBorder">
  <tr> 
      <td height="22" align="left" bgcolor="#f5fafe" onclick="showsubmenu(1)"><font color=#000000><strong>&nbsp;程序信息</strong></font> 
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
	          float totalCapacity = diskPartition.getTotalSpace(); //总大小
	          float freePartitionSpace = diskPartition.getFreeSpace(); //剩余大小
	          float usablePatitionSpace = totalCapacity-freePartitionSpace; //使用大小
	          float tPercent=freePartitionSpace/totalCapacity*100;
	          java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
          %>
          <tr bgcolor="#FFFFFF" height="22"> 
            <td width="20%">&nbsp;文件存放位置</td>
            <td width="30%">&nbsp;<%=path %></td>
            <td width="20%">&nbsp;用户文件占用量</td>
            <td width="30%">&nbsp;${fileSizeNumber }</td>
          </tr>
          <tr bgcolor="#FFFFFF" height="22"> 
		  	<td width="20%">&nbsp;剩余磁盘量(<%=zpath %>)</td>
            <td height="22" colspan="3"><img align=absmiddle class=PicBar width='<%=0.7*tPercent%>%'>&nbsp;<%=df.format(freePartitionSpace/1024/1024)%>M (<%=df.format(freePartitionSpace/1024/1024/1024)%>G)
            </td>
          </tr>
          <tr bgcolor="#FFFFFF" height="22"> 
		  	<td width="20%">&nbsp;总共磁盘量(<%=zpath %>)</td>
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
      <td height="22" align="left" bgcolor="#f5fafe" onclick="showsubmenu(2)"><font color=#000000><strong>&nbsp;运行环境信息</strong></font> 
      </td>
  </tr>
  <tr> 
    <td style="display" id='submenu2'>
		<table border=0 width=100% cellspacing=1 cellpadding=3 bgcolor="#ddd">
          <tr bgcolor="#FFFFFF" height="22"> 
            <td width="20%">&nbsp;JAVA运行环境名称</td>
            <td width="30%" height="22">&nbsp;<%=env.queryHashtable("java.runtime.name")%></td>
            <td width="20%" height="22">&nbsp;JDK版本</td>
            <td width="30%" height="22">&nbsp;<%=env.queryHashtable("java.runtime.version")%></td>
          </tr>
          <tr bgcolor="#FFFFFF" height="22"> 
            <td align=left bgcolor="#FFFFFF">&nbsp;JAVA运行环境路径</td>
            <td height="8" colspan="3" bgcolor="#FFFFFF">&nbsp;<%=env.queryHashtable("java.home")%></td>
          </tr>
		  <%
		  	float fFreeMemory=(float)Runtime.getRuntime().freeMemory();
			float fTotalMemory=(float)Runtime.getRuntime().totalMemory();
			float fPercent=fFreeMemory/fTotalMemory*100;
		  %>
          <tr bgcolor="#FFFFFF" height="22"> 
		  	<td height="22">&nbsp;JAVA虚拟机剩余内存：</td>
            <td height="22" colspan="3"><img align=absmiddle class=PicBar width='<%=0.7*fPercent%>%'>&nbsp;<%=df.format(fFreeMemory/1024/1024)%>M 
            </td>
          </tr>
          <tr bgcolor="#FFFFFF" height="22"> 
		  	<td height="22">&nbsp;JAVA虚拟机分配内存</td>
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
    <td height="22" align="left" bgcolor="#f5fafe" onclick="showsubmenu(0)"><font color=#000000><strong>&nbsp;版本信息</strong></font> 
      
	</td>
  </tr>
  <tr> 
    <td style="display" id='submenu0'><table border=0 width=100% cellspacing=1 cellpadding=3 bgcolor="#ddd">
    	  <tr bgcolor="#FFFFFF" height="22"> 
            <td>&nbsp;版权所有</td>
            <td colspan="3">&nbsp;深圳银澎云计算有限公司
          </tr>
          <tr bgcolor="#FFFFFF" height="22"> 
            <td>&nbsp;官方网站</td>
            <td colspan="3">&nbsp;</td>
          </tr>
          <tr bgcolor="#FFFFFF" height="22"> 
            <td>&nbsp;技术支持论坛</td>
            <td colspan="3">&nbsp;</td>
          </tr>
          <tr bgcolor="#FFFFFF" height="22"> 
            <td>&nbsp;产品使用帮助</td>
            <td colspan="3">&nbsp;</td>
          </tr>
          
        </table>
    </td>
  </tr>
</table>
<br><br>
<table border="0" cellpadding="0" cellspacing="1" class="tableBorder">
  <tr> 
      <td height="22" align="left" bgcolor="#f5fafe" onclick="showsubmenu(2)"><font color=#000000><strong>&nbsp;管理小贴士</strong></font> 
      </td>
  </tr>
  <tr> 
    <td style="display" id='submenu2'>
		<table border=0 width=100% cellspacing=1 cellpadding=3 bgcolor="#ddd">
          <tr bgcolor="#FFFFFF" height="22"> 
            <td width="30%" height="22">&nbsp;1.若想限制每个用户的空间使用大小可以在修改用户信息中设置。</td>
          </tr>
          <tr bgcolor="#FFFFFF" height="22"> 
            <td align=left bgcolor="#FFFFFF">&nbsp;2.若想限制登录的IP地址，可以在"IP限制"中设置。</td>
          </tr>
		 
          <tr bgcolor="#FFFFFF" height="22"> 
		  	<td height="22">&nbsp;3.若想设置用户预览文件水印，请使用"水印设置"。</td>
            </td>
          </tr>
          <tr bgcolor="#FFFFFF" height="22"> 
		  	<td height="22">&nbsp;4.如有离职员工交接，请使用"合并用户帐户"。</td>
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