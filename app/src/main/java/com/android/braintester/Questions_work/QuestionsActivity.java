package com.android.braintester.Questions_work;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.braintester.R;
import com.android.braintester.ScorePage.Score_Activity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class QuestionsActivity extends AppCompatActivity {

    public static final String FILE_NAME = "QUIZZER";
    public static final String KEY_NAME = "QUESTIONS";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    private TextView question,numberindicator;
    private FloatingActionButton bookmark;
    private LinearLayout optionContainer;
    private Button share,next;
    private Integer count = 0;
    private List<QuestionModel> list;
    private Integer position = 0;
    private Integer score = 0;
    private String category;
    private int setNo;
    private Dialog loadingdialog;
    private List<QuestionModel> bookmarkList;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private int matchedQuestionPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        Toolbar toolbar = findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);
        question = findViewById(R.id.question);
        bookmark = findViewById(R.id.bookmark);
        numberindicator = findViewById(R.id.numberindicator);
        optionContainer = findViewById(R.id.optionsContainer);
        share = findViewById(R.id.share);
        next = findViewById(R.id.next);

        preferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        gson = new Gson();

        getBookmark();

        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (modelMatch()){
                    bookmarkList.remove(matchedQuestionPosition);
                    bookmark.setImageDrawable(getDrawable(R.drawable.ic_bookmark));
                }
                else
                {
                    bookmarkList.add(list.get(position));
                    bookmark.setImageDrawable(getDrawable(R.drawable.ic_selectedbook));

                }
            }
        });


        category = getIntent().getStringExtra("category");
        setNo = getIntent().getIntExtra("setNo",1);


        loadingdialog = new Dialog(this);
        loadingdialog.setContentView(R.layout.loading);
        loadingdialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingdialog.setCancelable(false);

        list = new ArrayList<>();

        loadingdialog.show();
        myRef.child("SETS").child(category).child("questions").orderByChild("setNo").equalTo(setNo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    list.add(snapshot1.getValue(QuestionModel.class));
                }
                if (list.size()>0){

                    for (int i=0;i<4;i++){
                        optionContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                checkAnswer((Button)view);
                            }
                        });
                    }
                    playanim(question,0,list.get(position).getQuestion());
                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            next.setEnabled(false);
                            next.setAlpha(0.7f);
                            enableOption(true);
                            position++;
                            if (position == list.size()){
                                Intent scoreIntent = new Intent(QuestionsActivity.this, Score_Activity.class);
                                scoreIntent.putExtra("score",score);
                                scoreIntent.putExtra("total",list.size());
                                startActivity(scoreIntent);
                                finish();
                                return;
                            }
                            count=0;

                            playanim(question,0,list.get(position).getQuestion());

                        }
                    });

                    share.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String body = list.get(position).getQuestion() +"\n"+ list.get(position).getOptionA()+ "\n"+ list.get(position).getOptionB()+ "\n"+ list.get(position).getOptionC()+ "\n"+ list.get(position).getOptionD();
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Brain test challenge");
                            shareIntent.putExtra(Intent.EXTRA_TEXT,body);
                            startActivity(Intent.createChooser(shareIntent,"share via"));
                        }
                    });
                }
                else {
                    finish();
                }
                loadingdialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadingdialog.dismiss();
                finish();
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        storeBookmarks();
    }

    private void playanim(final View view, final int value, final String data){
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100).setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        if (value == 0 && count < 4){
                            String option="";
                            if (count==0){
                                option = list.get(position).getOptionA();

                            }else if (count==1){
                                option = list.get(position).getOptionB();

                            }else if (count==2){
                                option = list.get(position).getOptionC();

                            }else if (count==3){
                                option = list.get(position).getOptionD();

                            }
                            playanim(optionContainer.getChildAt(count),0,option);
                            count++;
                        }

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        if (value == 0){
                            try {
                                ((TextView)view).setText(data);
                                numberindicator.setText(position+1+"/"+list.size());
                                if (modelMatch()){
                                    bookmark.setImageDrawable(getDrawable(R.drawable.ic_selectedbook));
                                }
                                else
                                {
                                    bookmark.setImageDrawable(getDrawable(R.drawable.ic_bookmark));

                                }
                            }catch (ClassCastException ex){
                                ((Button)view).setText(data);
                            }
                            view.setTag(data);
                            playanim(view,1,data);
                        }

                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
    }
    private void checkAnswer(Button selectedOption){
        enableOption(false);
        next.setEnabled(true);
        next.setAlpha(1);
        if (selectedOption.getText().toString().equals(list.get(position).getCorrectAnswer())){
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#66bb6a")));
            score++;
        }else{
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff0000")));
            Button correctanswer = (Button) optionContainer.findViewWithTag(list.get(position).getCorrectAnswer());
            correctanswer.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#66bb6a")));

        }

    }
    private void enableOption(boolean enable){
        for (int i=0;i<4;i++){
            optionContainer.getChildAt(i).setEnabled(enable);
            if (enable == true){
                optionContainer.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
            }


        }




    }
    private void getBookmark(){
        String json = preferences.getString(KEY_NAME,"");

        Type type = new TypeToken<List<QuestionModel>>(){}.getType();

        bookmarkList = gson.fromJson(json,type);

        if (bookmarkList == null){
            bookmarkList = new ArrayList<>();

        }
    }


    private boolean modelMatch(){
        boolean matched = false;
        int i =0;
        for (QuestionModel model : bookmarkList){

            if (model.getQuestion().equals(list.get(position).getQuestion()) && model.getCorrectAnswer().equals(list.get(position).getCorrectAnswer()) && model.getSetNo() == list.get(position).getSetNo()){
                matched = true;
                matchedQuestionPosition = i;
            }
            i++;
        }
        return  matched;
    }

    private void storeBookmarks(){
        String json = gson.toJson(bookmarkList);

        editor.putString(KEY_NAME,json);
        editor.commit();

    }
}