package com.porlar.techhousestudio.pude.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.porlar.techhousestudio.pude.R;
import com.porlar.techhousestudio.pude.helpers.GridSpacingItemDecoration;
import com.porlar.techhousestudio.pude.firebasedb.fmodels.Post;

import java.util.List;


public class MajorPostAdapter extends RecyclerView.Adapter<MajorPostAdapter.MajorPostViewHOlder> {
    private Context context;
    private List<Post> postList;

    public MajorPostAdapter(Context context, List<Post> postList) {
        this.postList = postList;
        this.context = context;
    }

    public List<Post> getPostList() {
        return postList;
    }

    @NonNull
    @Override
    public MajorPostAdapter.MajorPostViewHOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MajorPostViewHOlder(LayoutInflater.from(context).inflate(R.layout.row_major_post, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MajorPostAdapter.MajorPostViewHOlder holder, int position) {
        Post post = postList.get(position);
        holder.tvContent.setText(post.content);
        holder.tvPostDate.setText(post.postDate);
        holder.tvMajorName.setText(post.imageUrs.size() + " Photos");
        ImageGalleryAdapter imageGalleryAdapter = new ImageGalleryAdapter(context, post.imageUrs);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        holder.rvMajorPost.setLayoutManager(gridLayoutManager);
        holder.rvMajorPost.addItemDecoration(new GridSpacingItemDecoration(2, 10, false));
        holder.rvMajorPost.setAdapter(imageGalleryAdapter);

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class MajorPostViewHOlder extends RecyclerView.ViewHolder {
        private TextView tvContent, tvPostDate, tvMajorName;
        private RecyclerView rvMajorPost;

        public MajorPostViewHOlder(View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tvViewCount);
            tvPostDate = itemView.findViewById(R.id.tvDateTime);
            rvMajorPost = itemView.findViewById(R.id.rvMajorPost);
            tvMajorName = itemView.findViewById(R.id.tvMajorName);
        }
    }
}
