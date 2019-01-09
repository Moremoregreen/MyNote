package com.moremoregreen.mynote.Activity.main;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.moremoregreen.mynote.Activity.editor.EditorActivity;
import com.moremoregreen.mynote.Model.Note;
import com.moremoregreen.mynote.R;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainView {
    private static final int INTENT_ADD = 100;
    private static final int INTENT_EDIT = 200;
    FloatingActionButton fab;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefresh;
    MainPresenter presenter;
    MainAdapter adapter;
    MainAdapter.ItemClickListener itemClickListener;

    List<Note> note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefresh = findViewById(R.id.swipe_refresh);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fab = findViewById(R.id.add);
        //(view -> {}) add lamba support
        fab.setOnClickListener(view -> {
            startActivityForResult(new Intent(MainActivity.this, EditorActivity.class),
                    INTENT_ADD);
        });

        presenter = new MainPresenter(this);
        presenter.getData();

        swipeRefresh.setOnRefreshListener(
                () -> presenter.getData()
        );

        itemClickListener = ((view, position) -> {
            int id = note.get(position).getId();
            String title = note.get(position).getTitle();
            String notes = note.get(position).getNote();
            int color  = note.get(position).getColor();
            Intent intent = new Intent(this,EditorActivity.class);
            intent.putExtra("id",id);
            intent.putExtra("title",title);
            intent.putExtra("note",notes);
            intent.putExtra("color",color);
            startActivityForResult(intent,INTENT_EDIT);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == INTENT_ADD  && resultCode ==RESULT_OK){
            presenter.getData(); //Reload Data
        }else if(requestCode == INTENT_EDIT && resultCode ==RESULT_OK){
            presenter.getData(); //Reload Data
        }
//        else if (requestCode == INTENT_EDIT && resultCode ==RESULT_OK){
//            presenter.getData();
//        }
    }

    @Override
    public void showLoading() {
        swipeRefresh.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void onGetResult(List<Note> notes) {
        adapter = new MainAdapter(this,notes,itemClickListener);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        note = notes;
    }

    @Override
    public void onErrorLoading(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
