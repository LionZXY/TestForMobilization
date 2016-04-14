package ru.lionzxy.yandexmusic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import ru.lionzxy.yandexmusic.lists.author.AuthorObject;
import ru.lionzxy.yandexmusic.lists.author.AuthorRecyclerAdapter;

public class MusicList extends AppCompatActivity {

    private AuthorRecyclerAdapter mAdapter;
    public static AuthorObject unknowObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);
        unknowObject = new AuthorObject("Неизвестный автор", "Если вы видете это сообщение, значит произошла ошибка в программе. Пожалуйста сообщите об этом разработчику",  R.drawable.notfoundmusic);

        RecyclerView mRecyclerView;
        RecyclerView.LayoutManager mLayoutManager;


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new AuthorRecyclerAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        //Test
        mAdapter.addItem(new AuthorObject("Dove Lo", "шведская певица и автор песен. Она привлекла к себе внимание в 2013 году с выпуском сингла «Habits», но настоящего успеха добилась с ремиксом хип-хоп продюсера Hippie Sabotage на эту песню, который получил название «Stay High». 4 марта 2014 года вышел её дебютный мини-альбом Truth Serum, а 24 сентября этого же года дебютный студийный альбом Queen of the Clouds. Туве Лу является автором песен таких артистов, как Icona Pop, Girls Aloud и Шер Ллойд.", R.drawable.testcover));
        mAdapter.addItem(new AuthorObject("Ne-Yo", "обладатель трёх премии Грэмми, американский певец, автор песен, продюсер, актёр, филантроп. В 2009 году журнал Billboard поставил Ни-Йо на 57 место в рейтинге «Артисты десятилетия».", R.drawable.notfoundmusic));
    }
}
