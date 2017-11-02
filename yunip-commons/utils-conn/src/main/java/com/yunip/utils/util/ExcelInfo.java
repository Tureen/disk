package com.yunip.utils.util;

public class ExcelInfo {
	private String header;
	private String value;
	
	public ExcelInfo(String header, String value){
		this.header = header;
		this.value = value;
	}
	
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
