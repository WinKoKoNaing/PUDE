package com.porlar.techhousestudio.pude.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.porlar.techhousestudio.pude.R;
import com.porlar.techhousestudio.pude.Blog.rmodel.ItemR;
import com.porlar.techhousestudio.pude.ui.activities.BlogPostDetailActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;

import me.myatminsoe.mdetect.MDetect;
import me.myatminsoe.mdetect.Rabbit;

public class BlogPostAdapter extends RecyclerView.Adapter<BlogPostAdapter.PostViewHolder> {

    private Context context;
    private List<ItemR> items;


    public void setItems(List<ItemR> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public BlogPostAdapter(Context context) {
        this.context = context;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_blog_post_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {

        final ItemR item = items.get(position);

        Document document = Jsoup.parse(item.getContent());
        if (MDetect.INSTANCE.isUnicode()) {
            holder.postDescription.setText(Rabbit.zg2uni(document.text()));
            holder.postTitle.setText(Rabbit.zg2uni(item.getTitle()));
        } else {
            holder.postDescription.setText(document.text());
            holder.postTitle.setText(item.getTitle());
        }


        Elements elements = document.select("img");
        if (!elements.isEmpty()) {
            holder.postImage.setVisibility(View.VISIBLE);
            Glide.with(context).load(elements.get(0).attr("src")).into(holder.postImage);
        } else {

            holder.postImage.setVisibility(View.GONE);
        }


        holder.tvPostDate.setText(item.getUpdated().substring(0, 10));
//        holder.tvComment.setText(item.getReplies().getTotalItems() + " COMMENTS");
        holder.tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, item.getUrl());
                sendIntent.setType("text/plain");
                context.startActivity(Intent.createChooser(sendIntent, "Share Link"));
            }
        });
        holder.tvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Not Available Now", Toast.LENGTH_SHORT).show();
            }
        });
        holder.tvReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("content", item.getContent());
                bundle.putString("title", item.getTitle());
                bundle.putString("date", item.getUpdated());
                bundle.putString("url", item.getUrl());
                Intent intent = new Intent(context, BlogPostDetailActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (items.size() > 0)
            return items.size();
        return 0;
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView postImage;
        TextView postTitle;
        TextView postDescription, tvPostDate, tvComment, tvShare, tvReadMore;

        public PostViewHolder(View itemView) {
            super(itemView);
            postImage = itemView.findViewById(R.id.postImage);
            postTitle = itemView.findViewById(R.id.postTitle);
            postDescription = itemView.findViewById(R.id.postDescription);
            tvPostDate = itemView.findViewById(R.id.tvPostDate);
            tvComment = itemView.findViewById(R.id.tvComment);
            tvShare = itemView.findViewById(R.id.tvShare);
            tvReadMore = itemView.findViewById(R.id.tvReadMore);
            postTitle.setSelected(true);
        }
    }

}
