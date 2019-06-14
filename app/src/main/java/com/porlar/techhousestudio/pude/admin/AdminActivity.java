package com.porlar.techhousestudio.pude.admin;

import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.porlar.techhousestudio.pude.R;
import com.porlar.techhousestudio.pude.adapters.MajorPostAdapter;
import com.porlar.techhousestudio.pude.helpers.PSHLoading;
import com.porlar.techhousestudio.pude.firebasedb.fmodels.Post;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    RecyclerView rvAdminPostList;
    private Toolbar toolbar;
    private Button btnLogin;
    private TextInputEditText etPass, etEmail;
    // FireBase
    private FirebaseAuth mAuth;
    private DatabaseReference rootDef = FirebaseDatabase.getInstance().getReference();
    //var
    List<Post> postList = new ArrayList<>();
    private PSHLoading loading = new PSHLoading();

    private MajorPostAdapter adminMajorPostAdapter;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user) {

        if (user == null) {
            findViewById(R.id.layout_login).setVisibility(View.VISIBLE);
            rvAdminPostList.setVisibility(View.GONE);
        } else {
            findViewById(R.id.layout_login).setVisibility(View.GONE);
            rvAdminPostList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loading.setCancelable(false);
        fetchData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        rvAdminPostList = findViewById(R.id.rvAdminPost);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Admin Role");

        btnLogin = findViewById(R.id.btnLogin);
        etEmail = findViewById(R.id.tetEmail);
        etPass = findViewById(R.id.tetPass);

        mAuth = FirebaseAuth.getInstance();


        rvAdminPostList.setLayoutManager(new LinearLayoutManager(this));


        ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT
        ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return true;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
                builder.setTitle("Warning...");
                builder.setIcon(R.drawable.ic_delete);
                builder.setMessage("Are you sure to delete?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        loading.show(getSupportFragmentManager(),"load");
                        deletePost(adminMajorPostAdapter.getPostList().get(viewHolder.getAdapterPosition()));
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        adminMajorPostAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                    }
                });

                builder.show();

            }
        };
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(rvAdminPostList);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etEmail.getText()) || TextUtils.isEmpty(etPass.getText())) {
                    Toast.makeText(getApplicationContext(), "Enter Text", Toast.LENGTH_LONG).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPass.getText().toString())
                        .addOnCompleteListener(AdminActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(AdminActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }

                            }
                        });
            }
        });
    }

    public void fetchData() {
        if (!postList.isEmpty()) {
            postList.clear();
        }
        rootDef.child("majors").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Post post = dataSnapshot.getValue(Post.class);


                postList.add(post);
                adminMajorPostAdapter = new MajorPostAdapter(AdminActivity.this, postList);
                rvAdminPostList.setAdapter(adminMajorPostAdapter);


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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_menu, menu);
        MenuItem post = menu.findItem(R.id.action_post);
        if (mAuth.getCurrentUser() == null) {
            post.setVisible(false);
        } else {
            post.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_post:
                startActivity(new Intent(AdminActivity.this, CreatePostActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deletePost(final Post post) {
        final DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("majors").child(post.majorId);
        postRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                for (int i = 0; i < post.imageUrs.size(); i++) {
                    StorageReference imgRef = FirebaseStorage.getInstance().getReferenceFromUrl(post.imageUrs.get(i));
                    final int finalI = i;
                    imgRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            loading.dismiss();
                            Toast.makeText(AdminActivity.this, "Success " + post.imageUrs.get(finalI), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
        fetchData();

    }

    public void onBackClick(View view) {
        finish();
    }
}
