package ru.lionzxy.yandexmusic.lists.author;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import ru.lionzxy.yandexmusic.R;
import ru.lionzxy.yandexmusic.lists.LockableRecyclerView;

/**
 * Created by LionZXY on 09.04.16.
 * YandexMusic
 */
public class AuthorRecyclerAdapter extends RecyclerView.Adapter<AuthorRecyclerViewHolder> {
    private List<AuthorObject> mData = new ArrayList<>();
    private Activity activity;
    private LockableRecyclerView recyclerView;

    public AuthorRecyclerAdapter(Activity activity, LockableRecyclerView recyclerView) {
        this.activity = activity;
        this.recyclerView = recyclerView;
    }

    public AuthorRecyclerAdapter(Activity activity, List<AuthorObject> authorObjects, LockableRecyclerView recyclerView) {
        this.activity = activity;
        this.mData = authorObjects;
        this.recyclerView = recyclerView;
    }

    @Override
    public AuthorRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.authorcard, parent, false);
        return new AuthorRecyclerViewHolder((LinearLayout) itemView, activity, recyclerView);
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
