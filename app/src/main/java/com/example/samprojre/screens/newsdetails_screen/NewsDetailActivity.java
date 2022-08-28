package com.example.samprojre.screens.newsdetails_screen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.samprojre.R;
import com.example.samprojre.databinding.ActivityNewsDetailBinding;
import com.example.samprojre.utils.UiUtils;
import com.google.android.material.appbar.AppBarLayout;

import java.io.File;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NewsDetailActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private boolean isHideToolbarView = false;
    private NewsDetailViewModel viewModel;
    private int isProgressIndicatorVisible;
    private ActivityNewsDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(NewsDetailViewModel.class);
        setTheme(viewModel.getTheme());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_news_detail);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbarNewsDetail);
        Objects.requireNonNull(getSupportActionBar()).setTitle(" ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.nestedScrollView.setNestedScrollingEnabled(false);
        binding.collapsingToolbar.setTitle("");
        binding.appBar.addOnOffsetChangedListener(this);

        viewModel.path.observe(this, pathName -> {
            String path = getFilesDir().getAbsolutePath() + File.separator + pathName + ".mht";
            binding.webView.saveWebArchive(path, false, result -> {
                if (result != null) {
                    viewModel.insertDownloaded(path);
                } else {
                    UiUtils.showToastMessage("Error while saving the webpage",
                            getLayoutInflater(), this);
                }
                Log.d("WebArchive", "Saved file" + result);
            });
        });

        viewModel.isLoading.observe(this, isLoading -> {
            switch (isLoading) {
                case LOADING:
                    binding.progressBar.setVisibility(View.VISIBLE);
                    break;
                case FINISHED:
                    binding.progressBar.setVisibility(View.GONE);
                    break;
                case ERROR:
                    UiUtils.showToastMessage("Oops, something went wrong",
                            getLayoutInflater(), this);
                    binding.progressBar.setVisibility(View.GONE);
                    break;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
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


    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.news_details_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        if (id == R.id.view_web) {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(viewModel.getUrl()));
            startActivity(intent);
        }
        if (id == R.id.share) {
            try {
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plan");
                intent.putExtra(Intent.EXTRA_SUBJECT, viewModel.getSource());
                String body = viewModel.getTitle() + "\n" + viewModel.getUrl() + "\n" + getString(R.string.shareMessage) + "\n";
                intent.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(intent, getString(R.string.shareChooser)));
            } catch (Exception e) {
                UiUtils.showToastMessage(getString(R.string.toastMessageCannotBeShared), getLayoutInflater(), this);
            }
        }
        if (id == R.id.download) {
            if (binding.progressBar.getVisibility() != View.VISIBLE)
                viewModel.getBitmapFromUrl();
            else {
                UiUtils.showToastMessage("Wait for the webpage to fully load",
                        getLayoutInflater(), this);
            }

        }
        return super.onOptionsItemSelected(item);
    }


}
