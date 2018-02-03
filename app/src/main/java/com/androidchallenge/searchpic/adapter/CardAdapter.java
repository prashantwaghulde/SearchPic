package com.androidchallenge.searchpic.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidchallenge.searchpic.R;
import com.androidchallenge.searchpic.activities.ImageDetail;
import com.androidchallenge.searchpic.interfaces.ItemClickListener;
import com.androidchallenge.searchpic.interfaces.LoadMore;
import com.androidchallenge.searchpic.model.CardItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.androidchallenge.searchpic.activities.MainActivity.emptyTextView;
import static com.androidchallenge.searchpic.activities.MainActivity.getListObservable;
import static com.androidchallenge.searchpic.activities.MainActivity.isOnLoadMore;
import static com.androidchallenge.searchpic.activities.MainActivity.recyclerView;


public class CardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final int VIEW_TYPE_ITEM = 0,VIEW_TYPE_LOADING = 1;
    LoadMore loadMore;
    boolean isLoading;
    Activity activity;
    List<CardItem> items;
    int visibleThresholdItem =2;
    int lastVisibleItem,totalItemCount;

    public CardAdapter(RecyclerView recyclerView, Activity activity, List<CardItem> items ){
        this.activity = activity;
        this.items = items;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem+visibleThresholdItem)){
                    if (loadMore !=null)
                        loadMore.onLoadMore();
                    isLoading = true;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) == null)
            return VIEW_TYPE_LOADING;
        else
            return VIEW_TYPE_ITEM;
    }

    public void setLoadMore(LoadMore loadMore){
        this.loadMore = loadMore;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {

            View view = LayoutInflater.from(activity).inflate(R.layout.layout_carditem,parent,false);
            return new ItemViewHolder(view);
        }else
            if (viewType == VIEW_TYPE_LOADING){
            View view = LayoutInflater.from(activity).inflate(R.layout.layout_progressbar,parent,false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemViewHolder){
            CardItem item = items.get(position);
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.cardItemTitle.setText(item.getCardItemTitle());
            final Context context = itemViewHolder.cardImageView.getContext();

            Picasso.with(context).load(item.getCardImageUrl()).resize(600,400).centerCrop().into(itemViewHolder.cardImageView);

            itemViewHolder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Intent intent = new Intent(context, ImageDetail.class);
                    Bundle bundle = ActivityOptions.
                            makeSceneTransitionAnimation(
                                    activity,
                                    itemViewHolder.cardImageView,
                                    itemViewHolder.cardImageView.getTransitionName())
                            .toBundle();
                    intent.putExtra("image", items.get(position).getCardImageUrl());
                    intent.putExtra("title", items.get(position).getCardItemTitle());

                    context.startActivity(intent,bundle);
                }
            });

        }else
            if (holder instanceof LoadingViewHolder){
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setIndeterminate(true);
            }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setLoaded(){
        isLoading = false;
    }

    public void setData(ArrayList<CardItem> items){
        this.items= (items);
        notifyDataSetChanged();

    }

    public void removeItems() {
        items.clear();
        notifyDataSetChanged();
    }

    public void setRxData(String searchText,int pageNo){

        getListObservable(searchText,pageNo)
                .debounce(250, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<CardItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Prash",e.getMessage());
                    }

                    @Override
                    public void onNext(ArrayList<CardItem> cardItems) {
                        if(!cardItems.isEmpty()){
                            items = cardItems;
                            notifyDataSetChanged();
                            setLoaded();
                            recyclerView.setVisibility(View.VISIBLE);
                            emptyTextView.setVisibility(View.GONE);
                            emptyTextView.setText("Type query to begin search");
                        }
                        else{
                            recyclerView.setVisibility(View.GONE);
                            emptyTextView.setVisibility(View.VISIBLE);

                            if(isOnLoadMore == true){
                                emptyTextView.setText("Type query to begin search");
                            }else{
                                emptyTextView.setText("No Image Found");
                            }
                        }
                    }
                });
    }

}
