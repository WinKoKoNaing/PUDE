package com.porlar.techhousestudio.pude.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.porlar.techhousestudio.pude.R;
import com.porlar.techhousestudio.pude.firebasedb.fmodels.Major;
import com.porlar.techhousestudio.pude.ui.activities.MajorPostDetailActivity;

import java.util.List;

import me.myatminsoe.mdetect.MDetect;
import me.myatminsoe.mdetect.MMTextView;
import me.myatminsoe.mdetect.Rabbit;

public class MajorNameAdapter extends BaseAdapter {
    private Context context;
    private List<Major> majorList;
    private LayoutInflater inflater;
    private String majorCode[];

    public MajorNameAdapter(Context context, List<Major> majorList) {
        this.context = context;
        this.majorList = majorList;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return majorList.size();
    }

    @Override
    public Object getItem(int position) {
        return majorList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_major_name_list, parent, false);
        }

        MMTextView tvMajorName = convertView.findViewById(R.id.tvMajorName);
        if (MDetect.INSTANCE.isUnicode()){
            String gyMajorName = Rabbit.zg2uni(majorList.get(position).majorName);
            tvMajorName.setText(gyMajorName);
        }else {
            tvMajorName.setText(majorList.get(position).majorName);
        }


        LinearLayout linearLayout = convertView.findViewById(R.id.layout_major);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                majorCode = context.getResources().getStringArray(R.array.majorCodes);
                Intent intent = new Intent(context, MajorPostDetailActivity.class);
                intent.putExtra("code", majorCode[position]);
                intent.putExtra("major",majorList.get(position).majorName);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
