package com.example.samprojre.screens.downloaded_screen;

import androidx.annotation.NonNull;

import com.example.samprojre.R;
import com.example.samprojre.base_adapter.BaseAdapter;
import com.example.samprojre.base_adapter.OnArticleItemClickListener;
import com.example.samprojre.base_adapter.OnItemClickListener;
import com.example.samprojre.data.model.DownloadsModel;
import com.example.samprojre.databinding.ItemDownloadsBinding;
import com.example.samprojre.databinding.ItemNewsfeedBinding;

import java.util.List;

public class DownloadsAdapter extends BaseAdapter<ItemDownloadsBinding, DownloadsModel> {

    public DownloadsAdapter(@NonNull List<? extends DownloadsModel> data, @NonNull OnItemClickListener onItemClickListener) {
        super(data, onItemClickListener);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_downloads;
    }

    @Override
    public void bind(@NonNull ItemDownloadsBinding binding, @NonNull DownloadsModel item, @NonNull OnItemClickListener onItemClickListener, int position) {
        binding.setItem(item);
        binding.setItemClickListener(onItemClickListener);
        binding.setPosition(position);
        binding.executePendingBindings();
    }
}
