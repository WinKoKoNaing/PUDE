package com.porlar.techhousestudio.pude.ui.navview;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.porlar.techhousestudio.pude.R;
import com.porlar.techhousestudio.pude.adapters.BusAdapter;
import com.porlar.techhousestudio.pude.firebasedb.fmodels.Bus;
import com.porlar.techhousestudio.pude.firebasedb.fmodels.BusPhoneNo;

import java.util.ArrayList;
import java.util.List;

public class PhoneNumberFragment extends Fragment {
    private RecyclerView rvBusList;
    private DatabaseReference phoneDef = FirebaseDatabase.getInstance().getReference().child("phoneNumbers");
    List<Bus> buses = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_phone_number, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvBusList = view.findViewById(R.id.rvBusList);
        rvBusList.setLayoutManager(new LinearLayoutManager(getContext()));

        phoneDef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final List<BusPhoneNo> busPhoneNoList = new ArrayList<>();

                if (!busPhoneNoList.isEmpty()){
                    busPhoneNoList.clear();
                }
                if (!buses.isEmpty()) {
                    buses.clear();
                }
                final Bus bus = dataSnapshot.getValue(Bus.class);
//                Log.d("WKKN",bus.phoneNos.get(0).phoneNo+" p");
                phoneDef.child(bus.busId).child("phoneNos").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        BusPhoneNo busPhoneNo = dataSnapshot.getValue(BusPhoneNo.class);
                        busPhoneNoList.add(busPhoneNo);


                        bus.setPhoneNos(busPhoneNoList);
                        if (!buses.contains(bus)){
                            buses.add(bus);
                        }


                        BusAdapter adapter = new BusAdapter(getContext(), buses);
                        rvBusList.setAdapter(adapter);
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
