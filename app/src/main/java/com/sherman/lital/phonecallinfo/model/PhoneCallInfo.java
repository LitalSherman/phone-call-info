package com.sherman.lital.phonecallinfo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PhoneCallInfo implements Serializable {

    public PhoneCallInfo(ContactPhone contactPhone, List<CallTypeInfo> callTypeInfoList) {
        this.contactPhone = contactPhone;
        this.callTypeInfoList = callTypeInfoList;
    }

    private ContactPhone contactPhone;
    private List<CallTypeInfo> callTypeInfoList = new ArrayList<>();

    public ContactPhone getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(ContactPhone contactPhone) {
        this.contactPhone = contactPhone;
    }

    public List<CallTypeInfo> getPhoneCallInfoList() {
        return callTypeInfoList;
    }

    public void setPhoneCallInfoList(List<CallTypeInfo> phoneCallInfoList) {
        this.callTypeInfoList = phoneCallInfoList;
    }
}
