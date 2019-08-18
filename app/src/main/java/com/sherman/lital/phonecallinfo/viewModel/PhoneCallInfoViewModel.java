package com.sherman.lital.phonecallinfo.viewModel;

import android.Manifest;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.sherman.lital.phonecallinfo.R;
import com.sherman.lital.phonecallinfo.model.CallTypeInfo;
import com.sherman.lital.phonecallinfo.model.ContactPhone;
import com.sherman.lital.phonecallinfo.model.PhoneCallInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PhoneCallInfoViewModel extends AndroidViewModel {

    private MutableLiveData<List<ContactPhone>> contactPhoneListLiveData;
    private MutableLiveData<PhoneCallInfo> phoneCallInfoLiveData;
    private ContactPhone contactPhone;
    private ViewModelHelper helper;


    public PhoneCallInfoViewModel(@NonNull Application application) {
        super(application);

        helper = new ViewModelHelper();
    }

    public MutableLiveData<List<ContactPhone>> getContactPhoneList() {
        if (contactPhoneListLiveData == null) {
            contactPhoneListLiveData = new MutableLiveData<>();
            loadContactPhone();
        }

        return contactPhoneListLiveData;
    }

    public MutableLiveData<PhoneCallInfo> getPhoneCallInfo() {

        if (phoneCallInfoLiveData == null) {
            phoneCallInfoLiveData = new MutableLiveData<>();
        }
        List<CallTypeInfo> callTypeInfoList = loadCallTypeInfoList();
        if (callTypeInfoList != null) {
            PhoneCallInfo phoneCallInfo = helper.confPhoneCallInfo(callTypeInfoList, getContactPhone());
            phoneCallInfoLiveData.setValue(phoneCallInfo);
        }

        return phoneCallInfoLiveData;
    }

    public ContactPhone getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(ContactPhone contactPhone) {
        this.contactPhone = contactPhone;
    }

    //Load and filter to list of contact and phone numbers
    private void loadContactPhone() {
        List<ContactPhone> contactPhoneList = new ArrayList<>();
        ContentResolver cr = getApplication().getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                ContactPhone contact = new ContactPhone();
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                contact.setName(name);
                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur != null && pCur.moveToNext()) {
                        String phoneNumber = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contact.setPhoneNumber(helper.createStandardPhoneNumber(phoneNumber));
                    }
                    if (pCur != null) {
                        pCur.close();
                    }
                }
                contactPhoneList.add(contact);
            }
        }
        if (cur != null) {
            cur.close();
        }

        contactPhoneListLiveData.setValue(helper.cleanObjectWithoutPhone(contactPhoneList));
    }

    // Load call log and filter to a list of call type info
    private List<CallTypeInfo> loadCallTypeInfoList() {
        StringBuffer sb = new StringBuffer();
        List<CallTypeInfo> callTypeInfoList = new ArrayList<>();
        ContentResolver cr = getApplication().getContentResolver();
        if (ActivityCompat.checkSelfPermission(this.getApplication(), Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI,
                null, null, null, CallLog.Calls.DATE + " DESC");
        int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = cursor.getColumnIndex(CallLog.Calls.DATE);
        sb.append("Call Details :");
        while (cursor.moveToNext()) {
            String phoneNumber = cursor.getString(number);
            String dir = cursor.getString(type);
            String callDate = cursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callType = null;
            int dircode = Integer.parseInt(dir);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    callType = getApplication().getString(R.string.outgoing);
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    callType = getApplication().getString(R.string.incoming);
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    callType = getApplication().getString(R.string.missed);
                    break;

            }
            CallTypeInfo callTypeInfo = new CallTypeInfo(helper.createStandardPhoneNumber(phoneNumber), callType, callDayTime);
            callTypeInfoList.add(callTypeInfo);
        }

        cursor.close();
        return callTypeInfoList;
    }
}

