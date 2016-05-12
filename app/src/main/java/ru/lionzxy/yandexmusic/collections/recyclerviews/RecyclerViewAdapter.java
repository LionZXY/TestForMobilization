package ru.lionzxy.yandexmusic.collections.recyclerviews;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.lionzxy.yandexmusic.interfaces.IListElement;

/**
 * Created by LionZXY on 19.04.2016.
 * YandexMusic
 */
public class RecyclerViewAdapter<T extends IListElement> extends RecyclerView.Adapter<RecyclerViewHolder> {
    private List<T> mData = new ArrayList<>();
    private int layoutId;
    private Activity activity;

    public RecyclerViewAdapter(@Nullable Activity activity, int layoutId) {
        super();
        this.layoutId = layoutId;
        this.activity = activity;
    }

    public RecyclerViewAdapter(@Nullable Activity activity, List<T> mData, int layoutId) {
        this(activity, layoutId);
        this.mData = mData;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(layoutId, parent, false);
        return new RecyclerViewHolder(activity, itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.setItem(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addItem(int position, T data) {
        mData.add(position, data);
        notifyItemInserted(position);
    }

    public void addItem(T data) {
        addItem(getItemCount(), data);
    }

    public void removeItem(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }
}
