package com.example.samprojre.base_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.samprojre.utils.diffutil.DiffCallback

abstract class BaseAdapter<BINDING : ViewDataBinding, T : ListAdapterItem>(
    var data: List<T>, val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<BaseViewHolder<BINDING>>() {

    @get:LayoutRes
    abstract val layoutId: Int

    abstract fun bind(
        binding: BINDING,
        item: T,
        onItemClickListener: OnItemClickListener,
        position: Int
    )

    fun updateData(list: List<T>) {
        val diffCallback =
            DiffCallback(this.data, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.data.toMutableList().clear();
        this.data = list
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<BINDING> {
        val binder = DataBindingUtil.inflate<BINDING>(
            LayoutInflater.from(parent.context),
            layoutId,
            parent,
            false
        )

        return BaseViewHolder(binder)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<BINDING>, position: Int) {
        bind(holder.binder, data[position], onItemClickListener,position)
    }

    override fun getItemCount(): Int = data.size
}