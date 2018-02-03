package com.androidchallenge.searchpic.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidchallenge.searchpic.R;
import com.androidchallenge.searchpic.interfaces.ItemClickListener;

/**
 * Created by waghup on 1/30/18.
 */
class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView cardItemTitle;
    public ImageView cardImageView;

    private ItemClickListener itemClickListener;

    public ItemViewHolder(View itemView) {
        super(itemView);
        cardItemTitle = (TextView)itemView.findViewById(R.id.tv_card_view_item);
        cardImageView = (ImageView)itemView.findViewById(R.id.iv_card_image);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition());
    }
}
