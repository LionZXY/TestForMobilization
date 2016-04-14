package ru.lionzxy.yandexmusic.lists.author;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.view.View;
import android.view.ViewGroup;

import ru.lionzxy.yandexmusic.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LionZXY on 09.04.16.
 * YandexMusic
 */
public class AuthorRecyclerAdapter extends RecyclerView.Adapter<AuthorRecyclerViewHolder> {
    private List<AuthorObject> mData = new ArrayList<>();
    private Context context;

    public AuthorRecyclerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public AuthorRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.authorcard, parent, false);
        return new AuthorRecyclerViewHolder((LinearLayout) itemView, context);
    }

    @Override
    public void onBindViewHolder(AuthorRecyclerViewHolder holder, int position) {
        holder.setItem(mData.get(position));
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
