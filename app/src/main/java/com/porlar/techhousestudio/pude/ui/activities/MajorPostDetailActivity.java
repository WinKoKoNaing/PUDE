package com.porlar.techhousestudio.pude.ui.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;

import com.porlar.techhousestudio.pude.R;
import com.porlar.techhousestudio.pude.ui.GridPostFragment;

public class MajorPostDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        Bundle b = new Bundle();
        b.putString("title", getIntent().getExtras().getString("major"));
        b.putString("majorcode", getIntent().getExtras().getString("code"));
        GridPostFragment gridPostFragment = new GridPostFragment();
        gridPostFragment.setArguments(b);
        getSupportFragmentManager().beginTransaction().add(R.id.layout_result_container, gridPostFragment).commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
