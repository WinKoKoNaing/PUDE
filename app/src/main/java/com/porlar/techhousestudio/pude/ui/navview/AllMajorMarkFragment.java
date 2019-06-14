package com.porlar.techhousestudio.pude.ui.navview;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.porlar.techhousestudio.pude.R;
import com.porlar.techhousestudio.pude.adapters.MajorNameAdapter;
import com.porlar.techhousestudio.pude.firebasedb.fmodels.Major;

import java.util.ArrayList;
import java.util.List;


public class AllMajorMarkFragment extends Fragment {

    //widget
    private GridView rvMajorList;


    private String[] majorName = null;
    private String[] startMajorName = null;

    private List<Major> majorList;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        majorList = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_major_mark, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvMajorList = view.findViewById(R.id.rvMajorList);

        majorName = getResources().getStringArray(R.array.majorList);
        startMajorName = getResources().getStringArray(R.array.startMajorName);
        Log.d("WKKN", "onViewCreated: " + majorName.length);

        for (int i = 0; i < majorName.length; i++) {
            majorList.add(new Major(majorName[i], startMajorName[i],false));
        }



        MajorNameAdapter adapter = new MajorNameAdapter(getActivity(), majorList);
        rvMajorList.setAdapter(adapter);


    }
}
