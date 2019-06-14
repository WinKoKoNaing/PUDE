package com.porlar.techhousestudio.pude.Blog;

import com.porlar.techhousestudio.pude.Blog.gmodel.PostList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;

public class BloggerAPI {

    public static final String key = "AIzaSyAETkbxNOW9Kh5D5PJQ-FhLYPHn6NI6-UM";
    public static final String url = "https://www.googleapis.com/blogger/v3/blogs/4702739142251259058/posts/";

    public static PostService postService = null;

    public static PostService getService()
    {
        if(postService == null)
        {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            postService = retrofit.create(PostService.class);
        }
        return postService;
    }

    public interface PostService {
        @GET
        Call<PostList> getPostList(@Url String url);
    }


}
