package com.example.samprojre.screens.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.LayoutInflaterCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.samprojre.R;
import com.example.samprojre.apptheme.MyLayoutInflater;
import com.example.samprojre.apptheme.Theme;
import com.example.samprojre.apptheme.ThemeManager;
import com.example.samprojre.databinding.ActivityMainBinding;
import com.example.samprojre.screens.downloaded_screen.DownloadedFragment;
import com.example.samprojre.screens.favorites_screen.FavouritesFragment;
import com.example.samprojre.screens.newsdetails_screen.NewsDetailViewModel;
import com.example.samprojre.screens.newsfeed_screen.NewsFeedFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;
import de.hdodenhof.circleimageview.CircleImageView;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {


    private FragmentManager fragmentManager;


    //Fragments

    private final String tagNewsFeedFragment = "NewsFeedFragment";
    private final String tagFavoritesFragment = "FavoritesFragment";
    private final String tagDownloadsFragment = "DownloadsFragment";

    private MainActivityViewModel viewModel;
    private ActivityMainBinding binding;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LayoutInflaterCompat.setFactory2(
                LayoutInflater.from(this),
                new MyLayoutInflater(getDelegate())
        );

        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        int theme = viewModel.getTheme();

        setTheme(theme);
        if (theme == R.style.AppThemeBlue) {
            ThemeManager.INSTANCE.setTheme(Theme.BLUE);
            Log.d("Set theme", "onCreate: blue");
        }
        if (theme == R.style.AppThemeGreen) {
            ThemeManager.INSTANCE.setTheme(Theme.GREEN);
            Log.d("Set theme", "onCreate: green");
        }
        if (theme == R.style.AppThemeBrown) {
            ThemeManager.INSTANCE.setTheme(Theme.BROWN);
            Log.d("Set theme", "onCreate: brown");
        }
        if (theme == R.style.AppThemePink) {
            ThemeManager.INSTANCE.setTheme(Theme.PINK);
            Log.d("Set theme", "onCreate: pink");
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setContentView(binding.getRoot());


        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());
        fragmentManager = getSupportFragmentManager();
        setUpBottomNavigation();

    }


    public void setUpBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.NewsFeedFragment) {
                replaceFragment(tagNewsFeedFragment);
                return true;
            }
            if (item.getItemId() == R.id.FavoritesFragment) {
                replaceFragment(tagFavoritesFragment);
                return true;
            }
            if (item.getItemId() == R.id.DownloadedFragment) {
                replaceFragment(tagDownloadsFragment);
                return true;
            }

            return false;
        });

        binding.bottomNavigation.setSelectedItemId(R.id.NewsFeedFragment);
    }


    private void replaceFragment(String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);
        Fragment nextFragment = fragmentManager.findFragmentByTag(tag);
        if (currentFragment != null) {
            transaction.detach(currentFragment);
        }

        if (nextFragment == null) {
            nextFragment = createFragment(tag);
            transaction.add(R.id.fragment_container, nextFragment, tag);
        } else {
            Log.d("ReplaceFragment", "f attach: " + nextFragment);
            transaction.attach(nextFragment);
        }
        transaction.commit();
    }

    private Fragment createFragment(String tag) {
        Fragment result = null;
        switch (tag) {
            case tagNewsFeedFragment:
                result = new NewsFeedFragment();
                break;
            case tagFavoritesFragment:
                result = new FavouritesFragment();
                break;
            case tagDownloadsFragment:
                result = new DownloadedFragment();
                break;
        }
        return result;
    }


}







