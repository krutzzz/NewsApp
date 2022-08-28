package com.example.samprojre.screens.about_screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.samprojre.R;
import com.example.samprojre.screens.newsdetails_screen.NewsDetailViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AboutActivity extends AppCompatActivity {


    private AboutViewModel viewModel;
    private ImageView arrowBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AboutViewModel.class);
        setTheme(viewModel.getTheme());
        setContentView(R.layout.activity_about);

        arrowBack=findViewById(R.id.arrowBack_imageView);
        arrowBack.setOnClickListener(view -> {
            onBackPressed();
        });


    }
}
