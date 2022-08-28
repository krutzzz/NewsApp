package com.example.samprojre.screens.settings_screens.addsource_screen;

import android.view.View;

import androidx.annotation.NonNull;

import com.example.samprojre.R;
import com.example.samprojre.base_adapter.BaseAdapter;
import com.example.samprojre.base_adapter.OnItemClickListener;
import com.example.samprojre.data.model.newsapi.Source;
import com.example.samprojre.databinding.ItemSourcesBinding;

import java.util.ArrayList;
import java.util.List;

public class AdapterSources extends BaseAdapter<ItemSourcesBinding, Source> {

    private ArrayList<String> listOfSourcesInList;

    public AdapterSources(@NonNull List<? extends Source> data, @NonNull OnItemClickListener onItemClickListener, List<String> listOfSourcesInList) {
        super(data, onItemClickListener);
        this.listOfSourcesInList = new ArrayList<>(listOfSourcesInList);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_sources;
    }

    @Override
    public void bind(@NonNull ItemSourcesBinding binding, @NonNull Source item, @NonNull OnItemClickListener onItemClickListener, int position) {
        binding.setItem(item);
        if(listOfSourcesInList.contains(item.getName())){
            binding.bookmarkImageView.setVisibility(View.VISIBLE);
        }
        else{
            binding.bookmarkImageView.setVisibility(View.GONE);
        }
        binding.itemView.setOnClickListener(view -> {
            if (binding.bookmarkImageView.getVisibility() != View.VISIBLE) {
                binding.bookmarkImageView.setVisibility(View.VISIBLE);
                onItemClickListener.onItemClick(item, position);
                listOfSourcesInList.add(item.getName());
            }
        });
        binding.executePendingBindings();
    }
}
