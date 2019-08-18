package com.sherman.lital.phonecallinfo.model;

import java.io.Serializable;
import java.util.Date;

public class CallTypeInfo implements Serializable{

    public CallTypeInfo(String phoneNumber, String callType, Date date) {
        this.phoneNumber = phoneNumber;
        this.callType = callType;
        this.date = date;
    }

    private String phoneNumber;
    private String callType;
    private Date date;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
