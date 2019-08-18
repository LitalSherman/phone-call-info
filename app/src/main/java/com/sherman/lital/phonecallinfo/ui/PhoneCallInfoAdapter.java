package com.sherman.lital.phonecallinfo.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sherman.lital.phonecallinfo.R;
import com.sherman.lital.phonecallinfo.model.CallTypeInfo;

import java.text.SimpleDateFormat;
import java.util.List;

public class PhoneCallInfoAdapter  extends RecyclerView.Adapter<PhoneCallInfoAdapter.ViewHolder> {

    private List<CallTypeInfo> callTypeInfoList;

    public PhoneCallInfoAdapter(List<CallTypeInfo> list) {
        this.callTypeInfoList = list;
    }

    @NonNull
    @Override
    public PhoneCallInfoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.phone_call_info_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneCallInfoAdapter.ViewHolder holder, final int position) {
        final CallTypeInfo currentCallTypeInfo = callTypeInfoList.get(position);

        holder.callType.setText(currentCallTypeInfo.getCallType());

        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM HH:mm:ss ");
        String stringDate = formatter.format(currentCallTypeInfo.getDate());

        holder.date.setText(stringDate);
    }

    @Override
    public int getItemCount() {
        return callTypeInfoList == null ? 0 : callTypeInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView callType;
        TextView date;

        public ViewHolder(View itemView) {
            super(itemView);
            callType = itemView.findViewById(R.id.call_type);
            date = itemView.findViewById(R.id.date);
        }
    }
}
