<!-- 设置提取码2016-6-14 -->
<div class="outlayer con_tiqu" id="con_settqm">
    <p class="color_7d"><fmt:message key="generate_link_prompt" bundle="${i18n}"/></p>
    <div class="set_tqmbg">
       <p class="p1">
       		<a class="set_tiqu" href="javascript:void(0);"><fmt:message key="generate_link" bundle="${i18n}"/></a>
       		<input id="effectivetime" onFocus="WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM-dd HH:mm',minDate:'%y-%M-\#{%d+1}',isShowToday:false})" class="set_input" type="text" placeholder="<fmt:message key="valid_time" bundle="${i18n}"/>">
       		<input maxlength="6" class="set_input3" onkeyup='this.value=this.value.replace(/\D/gi,"")' type="text"  value="<fmt:message key="t_remain_download_num" bundle="${i18n}"/>" onfocus="if(this.value=='<fmt:message key="t_remain_download_num" bundle="${i18n}"/>'){this.value='';this.id='remainDownloadNum';this.style.color='#333';}" onblur="if(this.value==''){this.value='<fmt:message key="t_remain_download_num" bundle="${i18n}"/>';this.id='';this.style.color='#999';}">
       </p>
       <p style="margin-top: -10px">
       <input maxlength="64" class="set_input4" type="text"  value="<fmt:message key="t_remark" bundle="${i18n}"/>" onfocus="if(this.value=='<fmt:message key="t_remark" bundle="${i18n}"/>'){this.value='';this.id='remark';this.style.color='#333';}" onblur="if(this.value==''){this.value='<fmt:message key="t_remark" bundle="${i18n}"/>';this.id='';this.style.color='#999';}"></p>
       <p style="margin-top: 25px"><fmt:message key="prompt1" bundle="${i18n}"/><br/><fmt:message key="prompt2" bundle="${i18n}"/><br/><fmt:message key="prompt3" bundle="${i18n}"/></p>
    </div>
</div>