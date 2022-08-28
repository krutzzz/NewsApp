package com.example.samprojre.screens.newsdetails_downloaded_screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.samprojre.R;
import com.example.samprojre.databinding.ActivityNewsDetailDownloadedBinding;
import com.example.samprojre.screens.newsdetails_screen.NewsDetailViewModel;
import com.google.android.material.appbar.AppBarLayout;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NewsDetailDownloaded extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private ActivityNewsDetailDownloadedBinding binding;
    private boolean isHideToolbarView = false;
    private NewsDetailDownloadedViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(NewsDetailDownloadedViewModel.class);
        setTheme(viewModel.getTheme());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_news_detail_downloaded);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());


        setSupportActionBar(binding.toolbarNewsDetail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        binding.nestedScrollView.setNestedScrollingEnabled(false);
        binding.collapsingToolbar.setTitle("");
        binding.appBar.addOnOffsetChangedListener(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_downloaded_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
        if (percentage == 1f && isHideToolbarView) {
            binding.dateBehavior.setVisibility(View.GONE);
            binding.titleAppbar.setVisibility(View.VISIBLE);
            isHideToolbarView = !isHideToolbarView;

        } else if (percentage < 1f && !isHideToolbarView) {
            binding.dateBehavior.setVisibility(View.VISIBLE);
            binding.titleAppbar.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;
        }
    }

}
