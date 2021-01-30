package com.android.braintester.bookmark_work;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.android.braintester.Questions_work.QuestionModel;
import com.android.braintester.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.android.braintester.Questions_work.QuestionsActivity.FILE_NAME;
import static com.android.braintester.Questions_work.QuestionsActivity.KEY_NAME;

public class Bookmark_Activity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private List<QuestionModel> bookmarkList;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private int matchedQuestionPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark_);
        Toolbar toolbar = findViewById(R.id.toolbarBook);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Bookmarks");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.rvBook);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        preferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        gson = new Gson();

        getBookmark();
        List<QuestionModel> list = new ArrayList<>();

        BookmarkAdapter adapter = new BookmarkAdapter(bookmarkList);
        recyclerView.setAdapter(adapter);

    }
    @Override
    protected void onPause() {
        super.onPause();
        storeBookmarks();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void getBookmark(){
        String json = preferences.getString(KEY_NAME,"");

        Type type = new TypeToken<List<QuestionModel>>(){}.getType();

        bookmarkList = gson.fromJson(json,type);

        if (bookmarkList == null){
            bookmarkList = new ArrayList<>();

        }
    }


        private void storeBookmarks(){
        String json = gson.toJson(bookmarkList);

        editor.putString(KEY_NAME,json);
        editor.commit();

    }
}