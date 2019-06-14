package com.porlar.techhousestudio.pude.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.porlar.techhousestudio.pude.R;
import com.porlar.techhousestudio.pude.adapters.MajorPostAdapter;
import com.porlar.techhousestudio.pude.firebasedb.fmodels.Post;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.myatminsoe.mdetect.MDetect;
import me.myatminsoe.mdetect.Rabbit;

public class GridPostFragment extends Fragment {
    // Widget
    RecyclerView rvImageList;
    private Toolbar toolbar;
    private TextView tvPostCount, tvViewCount, tvNoData;
    // FireBase
    private DatabaseReference rootDef = FirebaseDatabase.getInstance().getReference();
    //var
    List<Post> postList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_grid_post, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        if (MDetect.INSTANCE.isUnicode()){
            getActivity().setTitle(Rabbit.zg2uni(getArguments().getString("title")));
        }else {
            getActivity().setTitle(getArguments().getString("title"));
        }

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvNoData = view.findViewById(R.id.tvNoData);
        tvViewCount = view.findViewById(R.id.tvViewCount);

        tvPostCount = view.findViewById(R.id.tvPostCount);
        rvImageList = view.findViewById(R.id.listView);


        rvImageList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvImageList.setHasFixedSize(true);

        tvNoData.setText("No Data");

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int preYear = year - 1;
        tvViewCount.setText(preYear + " - " + year + " Year Results");

        fetchData();
        tvViewCount.setSelected(true);
    }


    public void fetchData() {
        if (!postList.isEmpty()) {
            postList.clear();
        }
        rootDef.child("majors").orderByChild("majorCode").equalTo(getArguments().getString("majorcode")).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Post post = dataSnapshot.getValue(Post.class);


                postList.add(post);


                if (postList.size() > 0) {
                    rvImageList.setVisibility(View.VISIBLE);
                    tvNoData.setVisibility(View.GONE);
                    tvPostCount.setText(postList.size() + " Posts");
                    MajorPostAdapter majorPostAdapter = new MajorPostAdapter(getActivity(), postList);
                    rvImageList.setAdapter(majorPostAdapter);

                } else {
                    rvImageList.setVisibility(View.GONE);
                    tvNoData.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
