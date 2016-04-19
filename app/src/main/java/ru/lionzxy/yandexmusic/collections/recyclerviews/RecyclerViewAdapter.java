package ru.lionzxy.yandexmusic.collections.recyclerviews;

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
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    private List<IListElement> mData = new ArrayList<>();
    private int layoutId;
    public RecyclerViewAdapter(int layoutId) {
        super();
        this.layoutId = layoutId;
    }

    public RecyclerViewAdapter(List<IListElement> mData, int layoutId) {
        this(layoutId);
        this.mData = mData;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(layoutId, parent, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.setItem(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addItem(int position, IListElement data) {
        mData.add(position, data);
        notifyItemInserted(position);
    }

    public void addItem(IListElement data) {

        addItem(getItemCount(), data);
    }

    public void removeItem(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }
}
