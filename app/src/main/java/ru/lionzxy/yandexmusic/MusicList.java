package ru.lionzxy.yandexmusic;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuIcon;
import com.balysv.materialmenu.MaterialMenuView;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.Drawer.OnDrawerListener;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.MiniDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import ru.lionzxy.yandexmusic.animations.ResizeAnimation;
import ru.lionzxy.yandexmusic.collections.recyclerviews.LockableRecyclerView;
import ru.lionzxy.yandexmusic.collections.recyclerviews.RecyclerViewAdapter;
import ru.lionzxy.yandexmusic.exceptions.ContextDialogException;
import ru.lionzxy.yandexmusic.helper.PixelHelper;
import ru.lionzxy.yandexmusic.interfaces.IListElement;
import ru.lionzxy.yandexmusic.io.ImageResource;

public class MusicList extends AppCompatActivity implements View.OnClickListener, OnDrawerListener {
    public boolean ready = false;
    private MaterialMenuView materialMenu;
    private TextView textView;
    private SearchView searchView;
    private byte actionBarMenuState = 0;
    private Drawer navigationDrawer;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageResource.imageLoader.init(ImageLoaderConfiguration.createDefault(MusicList.this));
        setContentView(R.layout.activity_music_list);

        try {
            materialMenu = (MaterialMenuView) findViewById(R.id.action_bar_menu);
            materialMenu.setOnClickListener(this);

            textView = (TextView) findViewById(R.id.action_bar_text);
            textView.setText(getResources().getString(R.string.load_load1));

            searchView = (SearchView) findViewById(R.id.search_actionbar);
            searchView.setOnSearchClickListener(this);
            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    //Visible animation for textview
                    Animation visibleAnimation = AnimationUtils.loadAnimation(MusicList.this, R.anim.alphavisible);
                    visibleAnimation.setFillAfter(true);
                    textView.startAnimation(visibleAnimation);

                    //Return searchview to normal size
                    Animation resizeAnimation = new ResizeAnimation(searchView, -1, (int) PixelHelper.pixelFromDP(getResources(), 50));
                    resizeAnimation.setDuration(1000);
                    searchView.startAnimation(resizeAnimation);

                    //Material menu animation
                    actionBarMenuState = 0;
                    materialMenu.animatePressedState(MaterialMenuDrawable.IconState.BURGER);

                    Toast.makeText(MusicList.this, "close", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

            navigationDrawer = new DrawerBuilder().withActivity(this)
                    .withActionBarDrawerToggle(true)
                    .addDrawerItems(
                            new SectionDrawerItem()
                                    .withName(R.string.drawer_sorting_title),
                            new PrimaryDrawerItem() //По имени
                                    .withName(R.string.drawer_sorting_by_name)
                                    .withIcon(R.drawable.ic_sort_by_alpha_black_24dp),
                            new PrimaryDrawerItem() //По количеству альбомов
                                    .withName(R.string.drawer_sorting_by_albums)
                                    .withIcon(R.drawable.ic_library_music_black_24dp),
                            new PrimaryDrawerItem() //По количеству треков
                                    .withName(R.string.drawer_sorting_by_tracks)
                                    .withIcon(R.drawable.ic_queue_music_black_24dp),
                            new PrimaryDrawerItem() //В порядке загрузки из БД и из сервера
                                    .withName(R.string.drawer_sorting_by_random)
                                    .withIcon(R.drawable.ic_autorenew_black_24dp),
                            new SwitchDrawerItem() //Только те, у которых есть сайт
                                    .withName(R.string.drawer_sorting_only_website)
                                    .withIcon(R.drawable.internetlogo),
                            new SwitchDrawerItem() //Только те, у которых есть большое изображение
                                    .withName(R.string.drawer_sorting_only_big_picture)
                                    .withIcon(R.drawable.ic_crop_original_black_24dp),
                            new PrimaryDrawerItem() //По нажатию на кнопку открывается выбор жанров
                                    .withName(R.string.drawer_sorting_custom_genres)
                                    .withIcon(R.drawable.ic_recent_actors_black_24dp)
                    )
                    .withOnDrawerListener(this)
                    .withHeader(R.layout.drawer_header).build();
        } catch (Exception e) {
            new ContextDialogException(MusicList.this, e);
        }

        if (LoadingActivity.authorObjects == null) {
            Intent intent = new Intent(this, LoadingActivity.class);
            startActivityForResult(intent, 1);
        } else loadList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            loadList();
        else new ContextDialogException(MusicList.this, new Exception());
    }

    public void loadList() {//Set up recyclerlist

        LockableRecyclerView mRecyclerView;
        try {
            mRecyclerView = (LockableRecyclerView) findViewById(R.id.recyclerview);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setAdapter(new AlphaInAnimationAdapter(new RecyclerViewAdapter(MusicList.this, new ArrayList<IListElement>(LoadingActivity.authorObjects), R.layout.authorcard)));

            checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.INTERNET");

            ready = true;
        } catch (Exception e) {
            new ContextDialogException(this, e);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode != KeyEvent.KEYCODE_BACK && super.onKeyDown(keyCode, event);
    }

    public void checkPermission(String... perm) {
        List<String> requestPerm = new ArrayList<String>();
        for (String p : perm)
            if (ContextCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED && !ActivityCompat.shouldShowRequestPermissionRationale(this, p))
                requestPerm.add(p);
        if (requestPerm.size() > 0)
            ActivityCompat.requestPermissions(this, requestPerm.toArray(new String[requestPerm.size()]), 1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_bar_menu: {
                switch (actionBarMenuState) {
                    case 0: {
                        actionBarMenuState = 1;
                        materialMenu.animatePressedState(MaterialMenuDrawable.IconState.ARROW);
                        navigationDrawer.openDrawer();
                        break;
                    }
                    case 1: {
                        actionBarMenuState = 0;
                        materialMenu.animatePressedState(MaterialMenuDrawable.IconState.BURGER);
                        navigationDrawer.closeDrawer();
                        break;
                    }
                    case 2: {
                        searchView.setIconified(true);
                    }
                }
                return;
            }
            case R.id.search_actionbar: {
                if (!searchView.isIconified()) {

                    //Text unvisible animation
                    Animation unvisibleAnimation = AnimationUtils.loadAnimation(MusicList.this, R.anim.alphaunvisible);
                    unvisibleAnimation.setFillAfter(true);
                    textView.startAnimation(unvisibleAnimation);

                    //Resize animation on full toolbar
                    Animation resizeAnimation = new ResizeAnimation(searchView, -1,
                            findViewById(R.id.toolbar).getWidth() - materialMenu.getWidth() - (int) PixelHelper.pixelFromDP(getResources(), 10));
                    resizeAnimation.setDuration(1000);
                    searchView.startAnimation(resizeAnimation);

                    //Burger button animated
                    actionBarMenuState = 2;
                    materialMenu.animatePressedState(MaterialMenuDrawable.IconState.ARROW);

                    Toast.makeText(MusicList.this, "click", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        if (actionBarMenuState == 0) {
            actionBarMenuState = 1;
            materialMenu.animatePressedState(MaterialMenuDrawable.IconState.ARROW);
        }
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        if (actionBarMenuState == 1) {
            actionBarMenuState = 0;
            materialMenu.animatePressedState(MaterialMenuDrawable.IconState.BURGER);
        }
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        if (actionBarMenuState == 0)
            materialMenu.setTransformationOffset(MaterialMenuDrawable.AnimationState.BURGER_ARROW, slideOffset);
    }
}
