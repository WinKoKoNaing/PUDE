package com.porlar.techhousestudio.pude.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.porlar.techhousestudio.pude.R;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.List;

/**
 * Created by USER on 2/7/2019.
 */
@SuppressWarnings("unchecked")
public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.ImageGalleryViewHolder> {
    private Context context;
    private List<String> imageList;

    ImageGalleryAdapter(Context context, List<String> imageList) {
        this.imageList = imageList;
        this.context = context;

    }

    @NonNull
    @Override
    public ImageGalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageGalleryViewHolder(LayoutInflater.from(context).inflate(R.layout.row_grid_image, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageGalleryViewHolder holder, int position) {
        Glide.with(context).load(imageList.get(position)).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImageViewer.Builder(context, imageList)
                        .setStartPosition(holder.getAdapterPosition())
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    class ImageGalleryViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ImageGalleryViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivSlide);

        }
    }
}
