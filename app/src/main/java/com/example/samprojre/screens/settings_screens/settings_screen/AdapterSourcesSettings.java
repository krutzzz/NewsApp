package com.example.samprojre.screens.settings_screens.settings_screen;

import androidx.annotation.NonNull;

import com.example.samprojre.R;
import com.example.samprojre.base_adapter.BaseAdapter;
import com.example.samprojre.base_adapter.OnItemClickListener;
import com.example.samprojre.databinding.ItemSourceBinding;

import java.util.List;

public class AdapterSourcesSettings extends BaseAdapter<ItemSourceBinding, SourceModel> {

    private boolean enabled;

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public AdapterSourcesSettings(@NonNull List<? extends SourceModel> data, @NonNull OnItemClickListener onItemClickListener, boolean enabled) {
        super(data, onItemClickListener);
        this.enabled=enabled;
    }



    @Override
    public int getLayoutId() {
        return R.layout.item_source;
    }

    @Override
    public void bind(@NonNull ItemSourceBinding binding, @NonNull SourceModel item, @NonNull OnItemClickListener onItemClickListener, int position) {
        binding.setItem(item);
        binding.setPosition(position);
        binding.setItemEnabled(enabled);
        binding.setClickListener(onItemClickListener);
        binding.executePendingBindings();
    }


}
