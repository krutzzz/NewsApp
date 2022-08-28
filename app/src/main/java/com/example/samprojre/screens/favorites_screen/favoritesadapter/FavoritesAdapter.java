package com.example.samprojre.screens.favorites_screen.favoritesadapter;

import androidx.annotation.NonNull;

import com.example.samprojre.R;
import com.example.samprojre.base_adapter.BaseAdapter;
import com.example.samprojre.base_adapter.OnArticleItemClickListener;
import com.example.samprojre.data.model.FavoritesModel;
import com.example.samprojre.base_adapter.OnItemClickListener;
import com.example.samprojre.databinding.ItemFavoritesBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FavoritesAdapter extends BaseAdapter<ItemFavoritesBinding, FavoritesModel> {

    public FavoritesAdapter(@NonNull List<? extends FavoritesModel> data, OnItemClickListener onItemClickListener) {
        super(data,onItemClickListener);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_favorites;
    }

    @Override
    public void bind(@NotNull ItemFavoritesBinding binding, @NotNull FavoritesModel item, @NotNull OnItemClickListener onItemClickListener, int position) {
        binding.setItem(item);
        binding.setItemListener((OnArticleItemClickListener) onItemClickListener);
        binding.setPosition(position);
        binding.executePendingBindings();
    }
}
