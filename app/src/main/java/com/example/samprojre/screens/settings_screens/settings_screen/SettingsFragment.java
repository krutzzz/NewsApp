package com.example.samprojre.screens.settings_screens.settings_screen;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.samprojre.R;
import com.example.samprojre.databinding.FragmentSettingsBinding;
import com.example.samprojre.screens.settings_screens.addsource_screen.SourcesFragment;
import com.example.samprojre.screens.settings_screens.choose_category_or_country_screen.ChooseCountryOrCategoryFragment;
import com.example.samprojre.utils.UiUtils;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private SettingsViewModel settingsViewModel;
    private AdapterSourcesSettings adapterSourcesSettings;

    private final String sourceFragmentTag = "Source Fragment";
    private final String settingsFragmentTag = "Settings Fragment";
    private final String countryTag = "Country Fragment";
    private final String categoryTag = "Category Fragment";


    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("SettingsFragment", "onCreateView: was called");
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        settingsViewModel.getSettings();
        settingsViewModel.getSourcesSettings();
        settingsViewModel.getCountryAndCategorySettings();
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_settings, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(settingsViewModel);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        settingsViewModel.sources.observe(getViewLifecycleOwner(), sources -> {
            if (!sources.isEmpty()) {
                binding.explainTextView.setVisibility(View.GONE);
                if (binding.recyclerViewSources.getAdapter() == null) {
                    adapterSourcesSettings = new AdapterSourcesSettings(sources,
                            settingsViewModel, settingsViewModel.sourceChecked.getValue());
                    binding.setAdapter(adapterSourcesSettings);
                }
            } else {
                binding.explainTextView.setVisibility(View.VISIBLE);
            }
        });

        settingsViewModel.isLoading.observe(getViewLifecycleOwner(), loadingState -> {
            switch (loadingState) {
                case LOADING:
                    break;
                case ERROR:
                    UiUtils.showToastMessage(settingsViewModel.getErrorMessage(),getLayoutInflater(),requireContext());
                    break;
                case FINISHED:
                    break;
            }
        });


        initClickListeners();


    }





    private void initClickListeners() {
        binding.fab.setOnClickListener(v -> {
            navigate(sourceFragmentTag);
        });

        binding.countryAndCategorySwitch.setOnTouchListener((v, motionEvent) -> {
            v.performClick();
            return motionEvent.getActionMasked() == MotionEvent.ACTION_MOVE;
        });

        binding.sourceSwitch.setOnTouchListener((v, motionEvent) -> {
            v.performClick();
            return motionEvent.getActionMasked() == MotionEvent.ACTION_MOVE;
        });

        binding.countryField.setOnClickListener(view -> {
            navigate(countryTag);
        });

        binding.categoryField.setOnClickListener(view -> {
            navigate(categoryTag);
        });

        binding.arrowBackImageView.setOnClickListener(view -> requireActivity().onBackPressed());
    }

    private void navigate(String tag) {
        Fragment fragment;
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        if (tag.equals(sourceFragmentTag)) {
            if (fragmentManager.findFragmentByTag(tag) == null) {
                fragment = (SourcesFragment) SourcesFragment.newInstance();
                fragmentManager.beginTransaction()
                        .replace(R.id.settings_fragment_container, fragment, tag)
                        .addToBackStack(settingsFragmentTag)
                        .commit();
            } else {
                fragment = (SourcesFragment)
                        fragmentManager.findFragmentByTag(tag);
                assert fragment != null;
                fragmentManager.beginTransaction().attach(fragment);
            }
        }
        if (tag.equals(countryTag) || tag.equals(categoryTag)) {
            Bundle bundle = new Bundle();
            if (tag.equals(countryTag)) {
                bundle.putString("title", "Country");
            } else {
                bundle.putString("title", "Category");
            }
            if (fragmentManager.findFragmentByTag(tag) == null) {
                fragment = (ChooseCountryOrCategoryFragment) ChooseCountryOrCategoryFragment.newInstance();
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.settings_fragment_container, fragment, tag)
                        .addToBackStack(settingsFragmentTag)
                        .commit();
            } else {
                fragment = (ChooseCountryOrCategoryFragment)
                        fragmentManager.findFragmentByTag(tag);
                assert fragment != null;
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().attach(fragment);
            }
        }

    }


}