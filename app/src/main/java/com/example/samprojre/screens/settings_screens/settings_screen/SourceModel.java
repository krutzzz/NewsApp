package com.example.samprojre.screens.settings_screens.settings_screen;

import com.example.samprojre.base_adapter.ListAdapterItem;
import com.example.samprojre.utils.diffutil.DiffUtilEquality;

public class SourceModel implements ListAdapterItem, DiffUtilEquality {

    private String sourceName;

    private boolean enabled;

    public SourceModel(String source, boolean enabled) {
        this.sourceName = source;
        this.enabled = enabled;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public boolean realEquals(Object toCompare) {
        if (this == toCompare) return true;
        if (toCompare == null || getClass() != toCompare.getClass()) return false;
        SourceModel item = (SourceModel) toCompare;
        return sourceName.equals(item.getSourceName()) && enabled == item.isEnabled();

    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
