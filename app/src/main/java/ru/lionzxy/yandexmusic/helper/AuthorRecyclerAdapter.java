package ru.lionzxy.yandexmusic.helper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ru.lionzxy.yandexmusic.R;
import ru.lionzxy.yandexmusic.elements.AuthorObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by LionZXY on 09.04.16.
 * YandexMusic
 */
public class AuthorRecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    private List<AuthorObject> mData = new ArrayList<>();

    public AuthorRecyclerAdapter() {
        // Pass context or other static stuff that will be needed.
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.authorcard, parent, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.title.setText(mData.get(position).name);
        holder.description.setText(mData.get(position).description);
        holder.icon.setImageBitmap(mData.get(position).image);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addItem(int position, AuthorObject data) {
        mData.add(position, data);
        notifyItemInserted(position);
    }

    public void addItem(AuthorObject data) {
        addItem(getItemCount(), data);
    }

    public void removeItem(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }



}
