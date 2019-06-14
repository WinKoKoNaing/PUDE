package com.porlar.techhousestudio.pude.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.porlar.techhousestudio.pude.R;

import java.io.File;
import java.util.List;

/**
 * Created by USER on 2/3/2019.
 */

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private List<String> imagelist;

    public ImageAdapter(Context context, List<String> imagelist) {
        this.context = context;
        this.imagelist = imagelist;
    }

    @Override
    public int getCount() {
        return imagelist.size();
    }

    @Override
    public Object getItem(int position) {
        return imagelist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_image_list, parent, false);
        }
        ImageView img = convertView.findViewById(R.id.ivImg);
        img.setImageBitmap(BitmapFactory.decodeFile(new File(imagelist.get(position)).getAbsolutePath()));
        return convertView;
    }
}
