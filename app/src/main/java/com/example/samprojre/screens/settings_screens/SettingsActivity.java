package com.example.samprojre.screens.settings_screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.LayoutInflaterCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.samprojre.R;
import com.example.samprojre.apptheme.MyLayoutInflater;
import com.example.samprojre.screens.settings_screens.settings_screen.SettingsFragment;
import com.example.samprojre.screens.settings_screens.settings_screen.SettingsViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SettingsActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private final String settingsFragmentTag = "Settings Fragment";
    private SettingsFragment settingsFragment;
    private ViewModelSettingsActivity viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory2(
                LayoutInflater.from(this),
                new MyLayoutInflater(getDelegate())
        );
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ViewModelSettingsActivity.class);
        setTheme(viewModel.getTheme());
        setContentView(R.layout.activity_settings);
        fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(settingsFragmentTag) == null) {
            settingsFragment = new SettingsFragment();
            fragmentManager.beginTransaction().add(R.id.settings_fragment_container, settingsFragment, settingsFragmentTag).commit();
        } else {
            settingsFragment
                    = (SettingsFragment) fragmentManager.findFragmentByTag(settingsFragmentTag);
            fragmentManager.beginTransaction().attach(settingsFragment);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_UP) {
            final View view = getCurrentFocus();
            if(view != null) {
                final boolean consumed = super.dispatchTouchEvent(ev);
                final View viewTmp = getCurrentFocus();
                final View viewNew = viewTmp != null ? viewTmp : view;
                if(viewNew.equals(view)) {
                    final Rect rect = new Rect();
                    final int[] coordinates = new int[2];

                    view.getLocationOnScreen(coordinates);

                    rect.set(coordinates[0], coordinates[1], coordinates[0] + view.getWidth(), coordinates[1] + view.getHeight());

                    final int x = (int) ev.getX();
                    final int y = (int) ev.getY();

                    if(rect.contains(x, y)) {
                        return consumed;
                    }
                }
                else if(viewNew instanceof EditText) {
                    return consumed;
                }

                final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                inputMethodManager.hideSoftInputFromWindow(viewNew.getWindowToken(), 0);

                viewNew.clearFocus();

                return consumed;
            }
        }

        return super.dispatchTouchEvent(ev);
    }


}