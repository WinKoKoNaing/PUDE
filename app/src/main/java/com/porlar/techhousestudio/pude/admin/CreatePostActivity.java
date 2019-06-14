package com.porlar.techhousestudio.pude.admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.ImageQuality;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.porlar.techhousestudio.pude.R;
import com.porlar.techhousestudio.pude.adapters.ImageAdapter;
import com.porlar.techhousestudio.pude.helpers.PSHLoading;
import com.porlar.techhousestudio.pude.firebasedb.fmodels.Post;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreatePostActivity extends AppCompatActivity {
    // widget
    private ImageView ivSelectImage;
    private ListView lvImageList;
    private Toolbar toolbar;
    private TextView tvChooseCategory, etContent, tvMonthYear;

    //FireBase
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    UploadTask uploadTask;

    // var
    private String categories[];
    private int radio = 0;
    private String majorCode = "-1";
    private ArrayList<String> imageList = new ArrayList<>();
    private PSHLoading loading = new PSHLoading();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        loading.setCancelable(false);

        ivSelectImage = findViewById(R.id.ivImage);
        lvImageList = findViewById(R.id.lvImageList);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvMonthYear = findViewById(R.id.tvMonthYear);
        tvChooseCategory = findViewById(R.id.tvChooseCategory);
        etContent = findViewById(R.id.etContent);
        initData();


        categories = getResources().getStringArray(R.array.majorList);

        ivSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Options options = Options.init()
                        .setRequestCode(1)                                                 //Request code for activity results
                        .setCount(10)                                                         //Number of images to restict selection count
                        .setImageQuality(ImageQuality.HIGH);
                Pix.start(CreatePostActivity.this,
                        options);
            }
        });
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        tvMonthYear.setText(day + " " + getResources().getStringArray(R.array.months)[month] + " " + year);
        tvMonthYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
//            List<String> mPaths = data.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH);
//            ImageAdapter imageAdapter = new ImageAdapter(CreatePostActivity.this, mPaths);
//            gvImageList.setAdapter(imageAdapter);
//        }
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            if (!imageList.isEmpty()) {
                imageList.clear();
            }

            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);

            imageList.addAll(returnValue);

            ImageAdapter imageAdapter = new ImageAdapter(CreatePostActivity.this, returnValue);
            lvImageList.setAdapter(imageAdapter);
            setListViewHeightBasedOnChildren(lvImageList);
            Log.d("WKKN", returnValue.size() + " size");
        }
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        BaseAdapter listAdapter = (BaseAdapter) listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public void onChooseClick(View view) {
        alertDialog();
    }

    public void alertDialog() {
        final AlertDialog alertDialog;
        LayoutInflater inflater = this.getLayoutInflater();
        View layout = inflater.inflate(R.layout.category_alert, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(CreatePostActivity.this);
        builder.setView(layout);

        final RadioGroup radioGroup = layout.findViewById(R.id.radioGroup);
        radioGroup.check(0);
//        RadioButton[] rbArray = {takeRButton(0, radioGroup), takeRButton(1, radioGroup),
//                takeRButton(2, radioGroup), takeRButton(3, radioGroup), takeRButton(4, radioGroup),
//                takeRButton(5, radioGroup), takeRButton(6, radioGroup), takeRButton(7, radioGroup)};
//        rbArray[radio].setChecked(true);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rbZero:
                        majorCode = "0";
                        radio = 0;
                        break;
                    case R.id.rbOne:
                        radio = 1;
                        majorCode = "1";
                        break;
                    case R.id.rbTwo:
                        radio = 2;
                        majorCode = "2";
                        break;
                    case R.id.rbThree:
                        radio = 3;
                        majorCode = "3";
                        break;
                    case R.id.rbFour:
                        radio = 4;
                        majorCode = "4";
                        break;
                    case R.id.rbFive:
                        radio = 5;
                        majorCode = "5";
                        break;
                    case R.id.rbSix:
                        radio = 6;
                        majorCode = "6";
                        break;
                    case R.id.rbSeven:
                        radio = 7;
                        majorCode = "7";
                        break;
                    case R.id.rbEight:
                        radio = 8;
                        majorCode = "8";
                        break;
                    case R.id.rbNine:
                        radio = 9;
                        majorCode = "9";
                        break;
                    case R.id.rbTen:
                        radio = 10;
                        majorCode = "10";
                        break;
                    case R.id.rb11:
                        radio = 11;
                        majorCode = "11";
                        break;
                    case R.id.rb12:
                        radio = 12;
                        majorCode = "12";
                        break;
                    case R.id.rb13:
                        radio = 13;
                        majorCode = "13";
                        break;
                    case R.id.rb14:
                        radio = 14;
                        majorCode = "14";
                        break;
                    case R.id.rb15:
                        radio = 15;
                        majorCode = "15";
                        break;

                }
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("WKKN", radio + " radio");
                tvChooseCategory.setText(categories[radio]);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        alertDialog = builder.create();
        alertDialog.show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_post_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_post:

                postToFireBase();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initData() {
        Calendar c = Calendar.getInstance();

        tvMonthYear.setText(c.get(Calendar.DAY_OF_MONTH) + " " + getResources().getStringArray(R.array.months)[c.get(Calendar.MONTH)] + " " + c.get(Calendar.YEAR));
    }

    public void postToFireBase() {
        if (TextUtils.isEmpty(etContent.getText().toString())) {
            Toast.makeText(CreatePostActivity.this, "Enter Text", Toast.LENGTH_SHORT).show();
            return;
        }
        if (majorCode == "-1") {
            Toast.makeText(CreatePostActivity.this, "Select Major", Toast.LENGTH_SHORT).show();
            return;
        }
        if (imageList.size() < 1) {
            Toast.makeText(CreatePostActivity.this, "Select Images", Toast.LENGTH_SHORT).show();
            return;
        }
        loading.show(getSupportFragmentManager(), "LoadingPost");
        final DatabaseReference majors = rootRef.child("majors");
        final String majorId = majors.push().getKey();
        final List<String> imageUri = new ArrayList<>();

        final String majorName = tvChooseCategory.getText().toString();
        final String content = etContent.getText().toString();

        for (int i = 0; i < imageList.size(); i++) {
            Uri file = Uri.fromFile(new File(imageList.get(i)));
            final StorageReference majorRef = storageRef.child("majorImage").child(majorId).child(file.getLastPathSegment());
            uploadTask = majorRef.putFile(file);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return majorRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();


                        imageUri.add(downloadUri.toString());
                        if (imageUri.size() == imageList.size()) {
                            Post post = new Post(majorId, majorCode, tvMonthYear.getText().toString(), content, majorName, imageUri);

                            majors.child(majorId).setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    finish();
                                    Toast.makeText(CreatePostActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                    loading.dismiss();

                                }
                            });
                        }
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });

        }

    }
}
