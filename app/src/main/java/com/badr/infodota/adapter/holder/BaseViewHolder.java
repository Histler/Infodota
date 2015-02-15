package com.badr.infodota.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.badr.infodota.adapter.OnItemClickListener;

/**
 * Created by ABadretdinov
 * 17.12.2014
 * 18:06
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private OnItemClickListener listener;

    public BaseViewHolder(View itemView, OnItemClickListener listener) {
        super(itemView);
        this.listener = listener;
        itemView.setOnClickListener(this);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onItemClick(v, getPosition());
        }
    }
}
