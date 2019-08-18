package com.sherman.lital.phonecallinfo.viewModel;

import com.sherman.lital.phonecallinfo.model.CallTypeInfo;
import com.sherman.lital.phonecallinfo.model.ContactPhone;
import com.sherman.lital.phonecallinfo.model.PhoneCallInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vlad on 8/17/2019.
 */

public class ViewModelHelper {

    public List<ContactPhone> cleanObjectWithoutPhone(List<ContactPhone> contactPhoneList) {
        List<ContactPhone> newList = new ArrayList<>();
        for (ContactPhone contactPhone : contactPhoneList) {
            String phone = contactPhone.getPhoneNumber();
            if (phone != null && !phone.isEmpty()) {
                newList.add(contactPhone);
            }
        }
        return newList;
    }

    //Create phone call info from list of call type info and contact phone
    public PhoneCallInfo confPhoneCallInfo(List<CallTypeInfo> list, ContactPhone contactPhone) {
        List<CallTypeInfo> callTypeList = getCallTypeList(list, contactPhone);
        if (callTypeList.isEmpty()) {
            return null;
        }
        return new PhoneCallInfo(contactPhone, callTypeList);
    }

    public List<CallTypeInfo> getCallTypeList(List<CallTypeInfo> list, ContactPhone contactPhone) {
        List<CallTypeInfo> sortedList = new ArrayList<>();
        for (CallTypeInfo callTypeInfo : list) {
            if (callTypeInfo.getPhoneNumber().equals(contactPhone.getPhoneNumber())) {
                sortedList.add(new CallTypeInfo(callTypeInfo.getPhoneNumber(), callTypeInfo.getCallType(), callTypeInfo.getDate()));
            }
        }
        return sortedList;
    }

    //Remove not numerical characters
    public String createStandardPhoneNumber(String phoneNumber) {
        return phoneNumber.replaceAll("[^0-9]", "");
    }
}
