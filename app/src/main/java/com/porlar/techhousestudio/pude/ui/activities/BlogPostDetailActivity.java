package com.porlar.techhousestudio.pude.ui.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.porlar.techhousestudio.pude.R;

import me.myatminsoe.mdetect.MDetect;
import me.myatminsoe.mdetect.Rabbit;
import us.feras.mdv.MarkdownView;

public class BlogPostDetailActivity extends AppCompatActivity {
    // widget
    private AppBarLayout appBarLayout;
    private ProgressBar progressBar;
    private MarkdownView webView;
    private ImageView ivWebsite, ivUpArrow;
    private Switch swFontChange;
    private TextView tvTitle, tvDate;
    private SeekBar seekBar;
    private BottomSheetBehavior bottomSheetBehavior;
    private Bundle item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        progressBar = findViewById(R.id.progressBar);
        webView = findViewById(R.id.tvWebText);
        tvDate = findViewById(R.id.tvDate);
        tvTitle = findViewById(R.id.tvTitle);
        appBarLayout = findViewById(R.id.appbar);
        ivWebsite = findViewById(R.id.ivWebSite);


        final View bottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        seekBar = bottomSheet.findViewById(R.id.seekBar);
        ivUpArrow = bottomSheet.findViewById(R.id.ivUpArrow);

        swFontChange = bottomSheet.findViewById(R.id.swFontChange);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        showToolBar(true);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        //show toolbar
                        showToolBar(false);
                        Glide.with(BlogPostDetailActivity.this).load(R.drawable.ic_down).into(ivUpArrow);
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        showToolBar(true);
                        Glide.with(BlogPostDetailActivity.this).load(R.drawable.ic_up).into(ivUpArrow);
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING: {
                        showToolBar(true);
                    }
                    case BottomSheetBehavior.STATE_SETTLING: {
                        showToolBar(true);

                    }
                    break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        ivUpArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }

            }
        });
        item = getIntent().getExtras();
        ivWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(item.getString("url"))));
            }
        });

        if (MDetect.INSTANCE.isUnicode()) {
            openWithUnicode();
            swFontChange.setChecked(true);

        } else {
            openWithZawgyi();
        }
        swFontChange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    openWithUnicode();
                } else {
                    openWithZawgyi();
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                webView.getSettings().setTextZoom(80 + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        tvTitle.setSelected(true);


        webView.setWebViewClient(new MyWebViewClient(progressBar));

    }

    private void openWithZawgyi() {
        tvTitle.setText(item.getString("title"));
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/zawgyi_one.ttf");
        tvTitle.setTypeface(font);
//        webView.loadData(item.getString("content"), "text/html", "UTF-8");
        tvDate.setText(item.getString("date").substring(0, 10));
//        htmlTextView.setHtml(item.getString("content"), new HtmlHttpImageGetter(htmlTextView));
        webView.loadMarkdown(item.getString("content"), "file:///android_asset/alt.css");

    }

    private void openWithUnicode() {
        String title = Rabbit.zg2uni(item.getString("title"));
        String content = Rabbit.zg2uni(item.getString("content"));
        tvTitle.setText(title);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/pyidaungsu.ttf");
        tvTitle.setTypeface(font);
//        webView.loadData(content, "text/html", "UTF-8");
        tvDate.setText(item.getString("date").substring(0, 10));

        webView.loadMarkdown(content, "file:///android_asset/paperwhite.css");

    }

    private void showToolBar(boolean show) {

        if (show)
            appBarLayout.setExpanded(true, true);
        else
            appBarLayout.setExpanded(false, false);
    }

    @Override
    public void onBackPressed() {

        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    private class MyWebViewClient extends WebViewClient {
        private ProgressBar progressBar;

        public MyWebViewClient(ProgressBar progressBar) {
            this.progressBar=progressBar;
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {


            if (!url.endsWith(".html")){
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(url)));
            }else {
                view.loadUrl(url);
            }


            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
    }
}















