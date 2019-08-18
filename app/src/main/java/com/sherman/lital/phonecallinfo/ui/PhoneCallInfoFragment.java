package com.sherman.lital.phonecallinfo.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sherman.lital.phonecallinfo.R;
import com.sherman.lital.phonecallinfo.model.PhoneCallInfo;

import static com.sherman.lital.phonecallinfo.ui.MainActivity.PHONE_CALL_INFO;

public class PhoneCallInfoFragment extends Fragment {

    public static PhoneCallInfoFragment newInstance(Bundle bundle) {
        PhoneCallInfoFragment fragment = new PhoneCallInfoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.phone_call_info_fragment, container, false);

        PhoneCallInfo phoneCallInfo = (PhoneCallInfo) getArguments().getSerializable(PHONE_CALL_INFO);
        setContactNamePhone(view, phoneCallInfo);

        RecyclerView recyclerView = view.findViewById(R.id.phone_call_info_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new PhoneCallInfoAdapter(phoneCallInfo.getPhoneCallInfoList()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        return view;
    }

    private void setContactNamePhone(View view, PhoneCallInfo phoneCallInfo){
        TextView name = view.findViewById(R.id.contact_name);
        TextView phone = view.findViewById(R.id.contact_phone);

        phone.setText(phoneCallInfo.getContactPhone().getPhoneNumber());
        if(phoneCallInfo != null && !phoneCallInfo.getContactPhone().getName().isEmpty()){
            name.setText(phoneCallInfo.getContactPhone().getName());
        }else {
            name.setText(getString(R.string.default_name));
        }

        phone.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        name.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
    }
}
