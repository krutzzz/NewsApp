package com.example.samprojre.screens.settings_screens.choose_category_or_country_screen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.samprojre.R;
import com.example.samprojre.databinding.FragmentChooseCountryOrCategoryBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ChooseCountryOrCategoryFragment extends Fragment {


    private ChooseCountryOrCategoryViewModel viewModel;
    private FragmentChooseCountryOrCategoryBinding binding;
    private AdapterCountryCategoryAdapter adapter;


    public ChooseCountryOrCategoryFragment() {
        // Required empty public constructor
    }

    public static ChooseCountryOrCategoryFragment newInstance() {
        ChooseCountryOrCategoryFragment fragment = new ChooseCountryOrCategoryFragment();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewModel = new ViewModelProvider(this).get(ChooseCountryOrCategoryViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_choose_country_or_category, container, false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.settingsLoaded.observe(getViewLifecycleOwner(), loaded->{
            if(loaded){
                if(viewModel.title.getValue().equals("Country")){
                    String[] countryNames = getResources().getStringArray(R.array.countries_1);
                    String[] countryCodes = getResources().getStringArray(R.array.countries_2);
                    viewModel.setCountries(countryNames,countryCodes);

                }
                else{
                    String[] categories = getResources().getStringArray(R.array.categories);
                    viewModel.setCategories(categories);

                }
            }

        });

        viewModel.list.observe(getViewLifecycleOwner(), list->{
            if(adapter==null){
                int position = 0;
                for (int i=0; i<list.size();i++){
                    if(list.get(i).isChosen())
                        position=i;
                }
                adapter=new AdapterCountryCategoryAdapter(list, viewModel);
                binding.setAdapter(adapter);
            }

        });



    }
}