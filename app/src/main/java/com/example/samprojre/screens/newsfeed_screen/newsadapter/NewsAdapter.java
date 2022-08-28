package com.example.samprojre.screens.newsfeed_screen.newsadapter;

import androidx.annotation.NonNull;


import com.example.samprojre.R;
import com.example.samprojre.base_adapter.OnArticleItemClickListener;
import com.example.samprojre.base_adapter.OnItemClickListener;
import com.example.samprojre.base_adapter.BaseAdapter;
import com.example.samprojre.data.model.newsapi.Article;
import com.example.samprojre.databinding.ItemNewsfeedBinding;


import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NewsAdapter extends BaseAdapter<ItemNewsfeedBinding, Article> {
//

    public NewsAdapter(@NonNull List<? extends Article> data, OnItemClickListener onItemClickListener) {
        super(data,onItemClickListener);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_newsfeed;
    }

    @Override
    public void bind(@NotNull ItemNewsfeedBinding binding, @NotNull Article item, @NotNull OnItemClickListener onItemClickListener, int position) {
        binding.setItem(item);
        binding.setItemListener((OnArticleItemClickListener) onItemClickListener);
        binding.setPosition(position);
        binding.executePendingBindings();
    }
}
