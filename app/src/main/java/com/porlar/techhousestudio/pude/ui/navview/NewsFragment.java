package com.porlar.techhousestudio.pude.ui.navview;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.porlar.techhousestudio.pude.Blog.BloggerAPI;
import com.porlar.techhousestudio.pude.Blog.gmodel.Item;
import com.porlar.techhousestudio.pude.Blog.gmodel.PostList;
import com.porlar.techhousestudio.pude.Blog.rmodel.ItemR;
import com.porlar.techhousestudio.pude.R;
import com.porlar.techhousestudio.pude.adapters.BlogPostAdapter;
import com.porlar.techhousestudio.pude.listeners.SetOnSyncListener;
import com.porlar.techhousestudio.pude.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFragment extends Fragment {
    // widget
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private BlogPostAdapter adapter;
    private List<ItemR> items = new ArrayList<>();
    private SpinKitView progress;
    private Realm realm;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        sharedPreferences = getContext().getSharedPreferences("loading", Context.MODE_PRIVATE);


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progress = view.findViewById(R.id.spin_kit);
        recyclerView = view.findViewById(R.id.postList);
        manager = new LinearLayoutManager(getContext());
        adapter = new BlogPostAdapter(getContext());
        adapter.setItems(items);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);

        progress.setVisibility(View.VISIBLE);

        try {
            realm = Realm.getDefaultInstance();

        } catch (Exception e) {

            // Get a Realm instance for this thread
            RealmConfiguration config = new RealmConfiguration.Builder()
                    .deleteRealmIfMigrationNeeded()
                    .build();
            realm = Realm.getInstance(config);

        }



        ((MainActivity) getActivity()).setOnSyncListener(new SetOnSyncListener() {
            @Override
            public void onSyncNow() {
                getData();
            }
        });




        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (sharedPreferences.getBoolean("loading", true)) {
                    getData();
                    Log.d("WKKN","load data online");
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("loading", false);
                    editor.apply();
                } else {
                    Log.d("WKKN","load data offline");
                    RealmResults<ItemR> itemR = realm.where(ItemR.class).findAll();
                    items.addAll(itemR);
                    adapter.setItems(items);
                    progress.setVisibility(View.GONE);
                }
            }
        });



    }

    private void getData() {

        String url = BloggerAPI.url + "?key=" + BloggerAPI.key;
        progress.setVisibility(View.VISIBLE);
        final Call<PostList> postList = BloggerAPI.getService().getPostList(url);
        postList.enqueue(new Callback<PostList>() {
            @Override
            public void onResponse(Call<PostList> call, Response<PostList> response) {
                PostList list = response.body();


                final RealmResults<ItemR> itemRS = realm.where(ItemR.class).findAll();

                if (list.getItems().size() == itemRS.size()) {
                    if (getActivity()!=null){
                        Toast.makeText(getActivity(), "No Update", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            itemRS.deleteAllFromRealm();
                        }
                    });


                    for (int i = 0; i < list.getItems().size(); i++) {
                        final Item item = list.getItems().get(i);
                        final ItemR itemR = new ItemR();
                        item.setId(item.getId());
                        itemR.setUpdated(item.getUpdated());
                        itemR.setTitle(item.getTitle());
                        itemR.setContent(item.getContent());
                        itemR.setUrl(item.getUrl());

                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealmOrUpdate(itemR);
                            }
                        });
                    }
                    if (getActivity()!=null){
                        Toast.makeText(getActivity(), "sync complete", Toast.LENGTH_SHORT).show();
                    }

                    if (!items.isEmpty()) {
                        items.clear();
                        RealmResults<ItemR> itemR = realm.where(ItemR.class).findAll();
                        if (itemR.size() > 0) {
                            items.addAll(itemR);
                            adapter.setItems(items);
                        } else getData();
                    } else {
                        RealmResults<ItemR> itemR = realm.where(ItemR.class).findAll();
                        if (itemR.size() > 0) {
                            items.addAll(itemR);
                            adapter.setItems(items);
                        }
                    }
                }


                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<PostList> call, Throwable t) {
                Toast.makeText(getActivity(), "Something wrong , check internet", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
