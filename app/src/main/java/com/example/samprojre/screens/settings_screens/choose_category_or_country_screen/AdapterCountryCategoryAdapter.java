package com.example.samprojre.screens.settings_screens.choose_category_or_country_screen;

import android.view.View;

import androidx.annotation.NonNull;

import com.example.samprojre.R;
import com.example.samprojre.base_adapter.BaseAdapter;
import com.example.samprojre.base_adapter.OnItemClickListener;
import com.example.samprojre.databinding.ItemCategoryCountryBinding;

import java.util.List;

public class AdapterCountryCategoryAdapter extends BaseAdapter<ItemCategoryCountryBinding, CountryCategoryModel> {

    private View previouslyClicked;
    private CountryCategoryModel prevChosenItem;

    public AdapterCountryCategoryAdapter(@NonNull List<? extends CountryCategoryModel> data, @NonNull OnItemClickListener onItemClickListener) {
        super(data, onItemClickListener);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_category_country;
    }

    @Override
    public void bind(@NonNull ItemCategoryCountryBinding binding, @NonNull CountryCategoryModel item, @NonNull OnItemClickListener onItemClickListener, int position) {
        binding.itemRow.setOnClickListener(view -> {

            //Kind of complex code but didn't find any other way around the flickering effect
            //when changing the "setChosen" state. So this code is basically for handling it
            if (binding.checkImageView.getVisibility() != View.VISIBLE) {
                onItemClickListener.onItemClick(item, position);
                binding.checkImageView.setVisibility(View.VISIBLE);
                if (previouslyClicked != null) {
                    previouslyClicked.setVisibility(View.INVISIBLE);
                    previouslyClicked = binding.checkImageView;
                }
                if (prevChosenItem != null) {
                    prevChosenItem.setChosen(false);
                }
                item.setChosen(true);
                prevChosenItem = item;
            }
        });

        //Code for handling chosenUiState for checkImageView
        if (item.isChosen()) {
            if (prevChosenItem == null) {
                binding.checkImageView.setVisibility(View.VISIBLE);
                previouslyClicked = binding.checkImageView;
                prevChosenItem = item;
            }
            else if(item.realEquals(prevChosenItem)){
                binding.checkImageView.setVisibility(View.VISIBLE);
            }
            else{
                item.setChosen(false);
                binding.checkImageView.setVisibility(View.INVISIBLE);
            }
        } else {
            binding.checkImageView.setVisibility(View.INVISIBLE);
        }

        binding.setAdapter(this);
        binding.setPosition(position);
        binding.setClickListener(onItemClickListener);
        binding.setItem(item);
        binding.executePendingBindings();
    }




}
