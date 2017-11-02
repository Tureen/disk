/**
 * 浏览器工具类
 */
 var BrowserUtils = {
 	getUAMatch : function(){//获得浏览器UA
 		var rMsie = /(msie\s|trident.*rv:)([\w.]+)/, 
			rFirefox = /(firefox)\/([\w.]+)/, 
			rOpera = /(opera).+version\/([\w.]+)/, 
			rChrome = /(chrome)\/([\w.]+)/, 
			rSafari = /version\/([\w.]+).*(safari)/;
		
		var ua = navigator.userAgent.toLowerCase();
		var match = rMsie.exec(ua);
		if (match != null) {
			return { browserName : "IE", browserVersion : match[2] || "0" };
		}
		var match = rFirefox.exec(ua);
		if (match != null) {
			return { browserName : match[1] || "", browserVersion : match[2] || "0" };
		}
		var match = rOpera.exec(ua);
		if (match != null) {
			return { browserName : match[1] || "", browserVersion : match[2] || "0" };
		}
		var match = rChrome.exec(ua);
		if (match != null) {
			return { browserName : match[1] || "", browserVersion : match[2] || "0" };
		}
		var match = rSafari.exec(ua);
		if (match != null) {
			return { browserName : match[2] || "", browserVersion : match[1] || "0" };
		}
		if (match == null) {
			return { browserName : "", browserVersion : "0" };
		}
 	},
	getBrowerName : function(){//获得浏览器名称
		return this.getUAMatch().browserName;
	},
	getBrowerVersion : function(){//获得浏览器版本号
		return this.getUAMatch().browserVersion;
	},
	/**
	 * 判断是否为IE浏览器的某个版本
	 * @param version 进行判断的版本号
	 * @return 浏览器正确返回:true, 浏览器版本不正确返回:false
	 */
	isIEBrowerVersion : function(version){
		if(this.getBrowerName() == "IE" && parseFloat(this.getBrowerVersion()) == parseFloat(version)){
			return true;
		}else{
			return false;
		}
	},
	/**
	 * 判断是否小于或等于IE浏览器的某个版本
	 * @param version 进行判断的版本号
	 * @return 浏览器正确返回:true, 浏览器版本不正确返回:false
	 */
	isLessThanOrEqualIEBrowerVersion : function(version){
		if(this.getBrowerName() == "IE" && parseFloat(this.getBrowerVersion()) <= parseFloat(version)){
			return true;
		}else{
			return false;
		}
	}
 }