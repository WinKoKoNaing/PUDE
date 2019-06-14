package com.porlar.techhousestudio.pude.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.porlar.techhousestudio.pude.R;
import com.porlar.techhousestudio.pude.firebasedb.fmodels.Bus;

import java.util.List;

import me.myatminsoe.mdetect.MDetect;
import me.myatminsoe.mdetect.Rabbit;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.BusViewHolder> {
    private List<Bus> busList;
    private Context context;

    public BusAdapter(Context context, List<Bus> busList) {
        this.context = context;
        this.busList = busList;
    }

    @NonNull
    @Override
    public BusAdapter.BusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BusViewHolder(LayoutInflater.from(context).inflate(R.layout.row_bus, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BusAdapter.BusViewHolder holder, int position) {
        Bus bus = busList.get(position);

        if(MDetect.INSTANCE.isUnicode()){
            holder.tvBusName.setText(Rabbit.zg2uni(bus.busName));
        }else {
            holder.tvBusName.setText(bus.busName);
        }
        holder.rvPhoneList.setLayoutManager(new LinearLayoutManager(context));
        PhoneNumberAdapter phoneNumberAdapter = new PhoneNumberAdapter(context, bus.phoneNos);
        holder.rvPhoneList.setAdapter(phoneNumberAdapter);

    }

    @Override
    public int getItemCount() {
        return busList.size();
    }

    public class BusViewHolder extends RecyclerView.ViewHolder {
        TextView tvBusName;
        RecyclerView rvPhoneList;

        public BusViewHolder(View itemView) {
            super(itemView);
            tvBusName = itemView.findViewById(R.id.tvBusName);
            rvPhoneList = itemView.findViewById(R.id.rvPhoneList);
        }
    }
}
