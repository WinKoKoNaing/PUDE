package com.porlar.techhousestudio.pude.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.porlar.techhousestudio.pude.R;
import com.porlar.techhousestudio.pude.admin.AdminActivity;
import com.porlar.techhousestudio.pude.listeners.SetOnSyncListener;
import com.porlar.techhousestudio.pude.ui.navview.AboutFragment;
import com.porlar.techhousestudio.pude.ui.navview.AdminDialogFragment;
import com.porlar.techhousestudio.pude.ui.navview.AllMajorMarkFragment;
import com.porlar.techhousestudio.pude.ui.navview.NewsFragment;
import com.porlar.techhousestudio.pude.ui.navview.PhoneNumberFragment;

import me.myatminsoe.mdetect.MDetect;
import me.myatminsoe.mdetect.Rabbit;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    //Widgets
    private TextView tvPageTitle, tvWebLink;
    private Toolbar toolbar;
    private LinearLayout fbPageLink;
    //var
//    List<Admin> admins = new ArrayList<>();
    private String android_id;
    private SetOnSyncListener onSyncListener;
    private DatabaseReference adminRef = FirebaseDatabase.getInstance().getReference().child("admin").child("users");
    private DatabaseReference appLinkRef = FirebaseDatabase.getInstance().getReference().child("admin").child("updateUri");

    public void setOnSyncListener(SetOnSyncListener onSyncListener) {
        this.onSyncListener = onSyncListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("PUDE News");

        //FindViewById
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        View headerLayout = navigationView.getHeaderView(0);
        tvPageTitle = headerLayout.findViewById(R.id.tvPageTitle);
        tvWebLink = headerLayout.findViewById(R.id.tvWebLink);
        fbPageLink = headerLayout.findViewById(R.id.layout_fbPageLink);

        tvPageTitle.setSelected(true);
        tvWebLink.setOnClickListener(this);

        android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);


        new Handler().post(new Runnable() {
            @Override
            public void run() {
                adminRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        String roles = (String) dataSnapshot.child("roles").getValue();
                        String androidId = (String) dataSnapshot.child("androidId").getValue();
//               Admin admin = dataSnapshot.getValue(Admin.class);
                        if (androidId.equals(android_id) && roles.equals("admin")) {
                            MenuItem menuItem = navigationView.getMenu().findItem(R.id.nav_admin);
                            menuItem.setVisible(true);
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
        });


        Menu menuItems = navigationView.getMenu();

        for (int i = 0; i < menuItems.size(); i++) {
            MenuItem mi = menuItems.getItem(i);
            if (MDetect.INSTANCE.isUnicode()) {
                String zawgyiTitle = Rabbit.zg2uni(mi.getTitle().toString());
                mi.setTitle(zawgyiTitle);
            }
            //for applying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {

                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);

                    if (MDetect.INSTANCE.isUnicode()) {
                        String zawgyiTitle = Rabbit.zg2uni(subMenuItem.getTitle().toString());
                        subMenuItem.setTitle(zawgyiTitle);
                    } else {

                    }

                }
            }

        }

        fbPageLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(getOpenFacebookIntent(MainActivity.this));
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().add(R.id.layout_main_frame, new NewsFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view  item clicks here.
        Fragment currentFragment = null;

        switch (item.getItemId()) {
            case R.id.nav_all_major:
                getSupportActionBar().show();
                if (MDetect.INSTANCE.isUnicode()) {
                    toolbar.setTitle(Rabbit.zg2uni("ေအာင္စာရင္းမ်ား"));
                } else {
                    toolbar.setTitle("ေအာင္စာရင္းမ်ား");
                }

                currentFragment = new AllMajorMarkFragment();
                break;
            case R.id.nav_pude_news:
                toolbar.setTitle("PUDE News");
                currentFragment = new NewsFragment();
                break;
            case R.id.nav_about:
                toolbar.setTitle("About");
                currentFragment = new AboutFragment();
                break;
            case R.id.nav_connect:
                Intent intent;
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.messenger.com/t/PUDEstudentsmessage"));
                startActivity(intent);
                break;
            case R.id.nav_sync:
                onSyncListener.onSyncNow();
                break;
            case R.id.nav_phone_no:

                toolbar.setTitle("Phone Numbers");
                currentFragment = new PhoneNumberFragment();
                break;
            case R.id.nav_admin_call:
                AdminDialogFragment adminDialogFragment = new AdminDialogFragment();
                adminDialogFragment.show(getSupportFragmentManager(), "admin");
                break;

            case R.id.nav_admin:
                startActivity(new Intent(MainActivity.this, AdminActivity.class));
                break;
            case R.id.nav_share:
                appLinkRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT,dataSnapshot.getValue(String.class) );
                        sendIntent.setType("text/plain");
                        startActivity(Intent.createChooser(sendIntent, "Share Link"));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                break;

        }

        if (currentFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.layout_main_frame, currentFragment).commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvWebLink:
                Intent webLinkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.yude.edu.mm/public/"));
//                webLinkIntent.putExtra("uri", "http://www.yude.edu.mm/public/");
//                webLinkIntent.putExtra("uri", "http://www.yude.edu.mm/public/");
                startActivity(webLinkIntent);
                break;
        }
    }


    private Intent getOpenFacebookIntent(Context context) {

        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/586355188217985"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/winkokonaing2122016"));
        }
    }

}
