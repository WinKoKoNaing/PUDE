package com.porlar.techhousestudio.pude.ui.navview;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.porlar.techhousestudio.pude.BuildConfig;
import com.porlar.techhousestudio.pude.R;
import com.porlar.techhousestudio.pude.helpers.PSHLoading;


public class AboutFragment extends Fragment {
    // widget
    private TextView tvVersionName;
    private Button btnCheckUpdate;
    private String androidId;
    private PSHLoading pshLoading = new PSHLoading();
    private DatabaseReference appLinkRef = FirebaseDatabase.getInstance().getReference().child("admin");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final String versionName = BuildConfig.VERSION_NAME;

        btnCheckUpdate = view.findViewById(R.id.btnUpdate);
        tvVersionName = view.findViewById(R.id.tvVersionName);
        tvVersionName.setText("App Version " + versionName);
        androidId = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        TextView tvAndroidId = view.findViewById(R.id.tvAndroidId);
        tvAndroidId.setText(androidId);
        btnCheckUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pshLoading.show(getFragmentManager(), "loading");
                appLinkRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                        final String updateUri = dataSnapshot.child("updateUri").getValue(String.class);
                        String appVersion = dataSnapshot.child("appVersion").getValue(String.class);
                        if (versionName.equals(appVersion)) {
                            pshLoading.dismiss();
                            Toast.makeText(getContext(), "No Update Now", Toast.LENGTH_SHORT).show();
                        } else {
                            pshLoading.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Go a link");
                            builder.setMessage("Download update, new version " + appVersion);
                            builder.setIcon(R.drawable.ic_app_logo);
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent sendIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUri));
                                    startActivity(sendIntent);
                                }
                            });
                            builder.setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            builder.show();
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        view.findViewById(R.id.tvDevelperName).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(getFacebookIntent("https://www.facebook.com/winkokonaing2122016"));
            }
        });

    }


    public Intent getFacebookIntent(String url) {

        PackageManager pm = getActivity().getPackageManager();
        Uri uri = Uri.parse(url);

        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                uri = Uri.parse("fb://facewebmodal/f?href=" + url);
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }

        return new Intent(Intent.ACTION_VIEW, uri);
    }
}
