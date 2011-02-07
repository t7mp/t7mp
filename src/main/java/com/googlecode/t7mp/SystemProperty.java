package com.googlecode.t7mp;

public class SystemProperty {

    private String key;
    private String value;

    public SystemProperty() {
        //
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getName());
        sb.append("[").append("key=").append(getKey());
        sb.append(",value=").append(getValue());
        sb.append("]");
        return sb.toString();
    }

}
