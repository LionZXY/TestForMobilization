package ru.lionzxy.yandexmusic.dialogs;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.lionzxy.yandexmusic.LoadingActivity;
import ru.lionzxy.yandexmusic.R;
import ru.lionzxy.yandexmusic.collections.recyclerviews.RecyclerViewAdapter;
import ru.lionzxy.yandexmusic.model.GenresCheck;
import ru.lionzxy.yandexmusic.model.GenresObject;

/**
 * Created by LionZXY on 09.05.2016.
 */
public class GenresDialog extends DialogFragment {
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter adapter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.list_dialog, null);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new RecyclerViewAdapter<GenresCheck>(null, R.layout.listelement_genres_checkbox);
        for (GenresObject genre : LoadingActivity.genresObjects)
            adapter.addItem(new GenresCheck(genre));

        mRecyclerView.setAdapter(adapter);

        builder.setTitle(R.string.drawer_sorting_custom_genres);
        builder.setView(v);
        return builder.create();
    }
}
