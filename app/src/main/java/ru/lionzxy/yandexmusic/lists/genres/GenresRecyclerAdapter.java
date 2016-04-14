package ru.lionzxy.yandexmusic.lists.genres;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import ru.lionzxy.yandexmusic.R;


public class GenresRecyclerAdapter extends RecyclerView.Adapter<GenresRecyclerViewHolder> {
    private List<GenresObject> mData = new ArrayList<>();
    private Context context;

    public GenresRecyclerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public GenresRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.genrelist, parent, false);
        return new GenresRecyclerViewHolder((LinearLayout) itemView, context);
    }

    @Override
    public void onBindViewHolder(GenresRecyclerViewHolder holder, int position) {
        holder.setItem(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addItem(int position, GenresObject data) {
        mData.add(position, data);
        notifyItemInserted(position);
    }

    public void addItem(GenresObject data) {
        addItem(getItemCount(), data);
    }

    public void removeItem(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }
}
