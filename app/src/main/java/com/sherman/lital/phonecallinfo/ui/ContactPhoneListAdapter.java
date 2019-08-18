package com.sherman.lital.phonecallinfo.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sherman.lital.phonecallinfo.R;
import com.sherman.lital.phonecallinfo.model.ContactPhone;

import java.util.List;

public class ContactPhoneListAdapter extends RecyclerView.Adapter<ContactPhoneListAdapter.ViewHolder> {

    private List<ContactPhone> contactPhoneList;
    ElementClickListener listener;

    public ContactPhoneListAdapter(List<ContactPhone> list, ElementClickListener listener) {
        this.contactPhoneList = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ContactPhoneListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_phone_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactPhoneListAdapter.ViewHolder holder, final int position) {
        final ContactPhone currentContactPhone = contactPhoneList.get(position);

        holder.contactName.setText(currentContactPhone.getName());
        holder.contactPhone.setText(currentContactPhone.getPhoneNumber());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnElementClick(currentContactPhone);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactPhoneList == null ? 0 : contactPhoneList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView contactName;
        TextView contactPhone;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contact_name);
            contactPhone = itemView.findViewById(R.id.contact_phone);
            view = itemView;
        }
    }
}
