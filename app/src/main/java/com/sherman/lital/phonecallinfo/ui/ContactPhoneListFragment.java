package com.sherman.lital.phonecallinfo.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sherman.lital.phonecallinfo.R;
import com.sherman.lital.phonecallinfo.model.ContactPhone;
import com.sherman.lital.phonecallinfo.viewModel.PhoneCallInfoViewModel;

import java.util.ArrayList;

import static com.sherman.lital.phonecallinfo.ui.MainActivity.CONTACT_PHONE_LIST;

public class ContactPhoneListFragment extends Fragment implements ElementClickListener{

    private PhoneCallInfoViewModel viewModel;

    public static ContactPhoneListFragment newInstance(Bundle bundle) {
        ContactPhoneListFragment fragment = new ContactPhoneListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(PhoneCallInfoViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.contact_phone_list_fragment, container, false);
        ArrayList<ContactPhone> contactPhoneList = (ArrayList<ContactPhone>) getArguments().getSerializable(CONTACT_PHONE_LIST);

        RecyclerView recyclerView = view.findViewById(R.id.contact_phone_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new ContactPhoneListAdapter(contactPhoneList, this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        return view;
    }

    //Get item click from RV and start observe on MutableLiveData<PhoneCallInfo> change in viewModel
    @Override
    public void OnElementClick(ContactPhone contactPhone) {
        viewModel.setContactPhone(contactPhone);
        ((MainActivity) getActivity()).initPhoneCallInfoObserver();
    }
}
