package com.example.samprojre.utils.diffutil;

import androidx.recyclerview.widget.DiffUtil;

import com.example.samprojre.utils.diffutil.DiffUtilEquality;

import java.util.List;

public class DiffCallback<T> extends DiffUtil.Callback {

    private final List<T> oldList;
    private final List<T> newList;

    public DiffCallback(List<T> oldList, List<T> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }


    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {

        T oldItem = oldList.get(oldItemPosition);
        T newItem = newList.get(newItemPosition);
        if(oldItem instanceof DiffUtilEquality){
            return ((DiffUtilEquality) oldItem).realEquals(newItem);
        }
        return oldItem.equals(newItem);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        T oldItem = oldList.get(oldItemPosition);
        T newItem = newList.get(newItemPosition);
        if(oldItem instanceof DiffUtilEquality){
            return ((DiffUtilEquality) oldItem).realEquals(newItem);
        }
        return oldItem.equals(newItem);
    }
}
