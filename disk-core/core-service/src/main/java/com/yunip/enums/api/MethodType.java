package com.yunip.enums.api;

public enum MethodType {

    /**POST**/
    POST(1, "POST"),

    /**GET**/
    GET(2, "GET");

    private int    type;

    private String desc;

    private MethodType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
