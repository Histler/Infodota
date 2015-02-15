package com.badr.infodota.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.badr.infodota.R;
import com.badr.infodota.adapter.OnItemClickListener;

/**
 * Created by Badr on 27.12.2014.
 */
public class PlayerHolder extends BaseViewHolder {
    public ImageView icon;
    public TextView name;
    public TextView group;
    public ImageView add;
    public ImageView delete;

    public PlayerHolder(View itemView, OnItemClickListener listener) {
        super(itemView, listener);
        icon = (ImageView) itemView.findViewById(R.id.player_image);
        name = (TextView) itemView.findViewById(R.id.player_name);
        add = (ImageView) itemView.findViewById(R.id.add_btn);
        delete = (ImageView) itemView.findViewById(R.id.delete_btn);
        group = (TextView) itemView.findViewById(R.id.group_id);

    }
}
