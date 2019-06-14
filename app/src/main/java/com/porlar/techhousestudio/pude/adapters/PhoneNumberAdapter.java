package com.porlar.techhousestudio.pude.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.porlar.techhousestudio.pude.R;
import com.porlar.techhousestudio.pude.firebasedb.fmodels.BusPhoneNo;

import java.util.List;

import me.myatminsoe.mdetect.MDetect;
import me.myatminsoe.mdetect.Rabbit;

public class PhoneNumberAdapter extends RecyclerView.Adapter<PhoneNumberAdapter.PhoneNumberViewHolder> {
    List<BusPhoneNo> busPhoneNoList;
    Context context;

    public PhoneNumberAdapter(Context context, List<BusPhoneNo> busPhoneNoList) {
        this.context = context;
        this.busPhoneNoList = busPhoneNoList;
    }

    @NonNull
    @Override
    public PhoneNumberAdapter.PhoneNumberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PhoneNumberViewHolder(LayoutInflater.from(context).inflate(R.layout.row_phone_no, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneNumberAdapter.PhoneNumberViewHolder holder, final int position) {
        holder.tvPhoneNo.setText(busPhoneNoList.get(position).phoneNo);

        if(MDetect.INSTANCE.isUnicode()){
            holder.tvGateName.setText(Rabbit.zg2uni(busPhoneNoList.get(position).gateName));
        }else {
            holder.tvGateName.setText(busPhoneNoList.get(position).gateName);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
                phoneIntent.setData(Uri.parse("tel:"+busPhoneNoList.get(position).phoneNo));
                context.startActivity(phoneIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return busPhoneNoList.size();
    }

    public class PhoneNumberViewHolder extends RecyclerView.ViewHolder {
        TextView tvPhoneNo, tvGateName;

        public PhoneNumberViewHolder(View itemView) {

            super(itemView);
            tvGateName = itemView.findViewById(R.id.tvGateName);
            tvPhoneNo = itemView.findViewById(R.id.tvGatePhone);
            tvGateName.setSelected(true);
        }
    }
}
