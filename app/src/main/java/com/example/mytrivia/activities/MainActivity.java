package com.example.mytrivia.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;

import com.example.mytrivia.R;
import com.example.mytrivia.databinding.ActivityMainBinding;
import com.example.mytrivia.databinding.DialogFinishTriviaBinding;
import com.example.mytrivia.listeners.DialogClickListeners;
import com.example.mytrivia.model.Question;
import com.example.mytrivia.repository.PrefRepository;
import com.example.mytrivia.viewModel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "MainActivity";
    private MainActivityViewModel mainActivityViewModel;
    private ActivityMainBinding binding;
    private int questionCounter =0;
    private ArrayList<Question> questionArrayList;
    ArrayList<Button> allButtons;
    private int correctButtonId;
    private PrefRepository prefRepository;
    private int highestScore;
    private int currentScore =0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        mainActivityViewModel.init();

        checkHighestScore();
        updateHighestScore();
        updateCurrentScore();

        binding.setIsProgressBarVisible(true);
        questionArrayList = new ArrayList<>();

        allButtons = new ArrayList<>();
        allButtons.add(binding.answerAButton);
        allButtons.add(binding.answerBButton);
        allButtons.add(binding.answerCButton);
        allButtons.add(binding.answerDButton);

        mainActivityViewModel.getTasks().observe(this, questionsObserver);
        binding.answerAButton.setOnClickListener(this);
        binding.answerBButton.setOnClickListener(this);
        binding.answerCButton.setOnClickListener(this);
        binding.answerDButton.setOnClickListener(this);

    }

    Observer<ArrayList<Question>> questionsObserver = new Observer<ArrayList<Question>>() {
        @Override
        public void onChanged(ArrayList<Question> questions) {
            questionArrayList.clear();
            if(questions!=null){
                questionArrayList.addAll(questions);
                binding.setIsProgressBarVisible(false);
                setQuestion();
            }
        }
    };

    public void setQuestion(){
        binding.questionCounterTextView.setText(String.format("Question %d/%s", questionCounter+1, questionArrayList.size()));
        binding.setMyQuestion(questionArrayList.get(questionCounter));
        ArrayList<String> allAnswers = questionArrayList.get(questionCounter).getAllAnswers();
        Collections.shuffle(allAnswers);
        String correctAnswer = questionArrayList.get(questionCounter).getCorrectAnswer();
        binding.answerAButton.setText(allAnswers.get(0));
        binding.answerBButton.setText(allAnswers.get(1));
        binding.answerCButton.setText(allAnswers.get(2));
        binding.answerDButton.setText(allAnswers.get(3));

        for(Button button : allButtons){
            if(button.getText() == correctAnswer){
                correctButtonId = button.getId();
            }
        }
        setCardColor();
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == correctButtonId){
            currentScore += 10;
            fadeAnimation(view,true);

        }
        else{
            if(currentScore>0){
                currentScore -= 10;
            }
            fadeAnimation(view,false);

        }

    }

    public void goToTheNextQuestion(){
        if(questionCounter<questionArrayList.size()-1){
            questionCounter++;
            setQuestion();
        }
        else{
            showFinishDialog();
            saveHighestScore();
        }
    }

    public void updateHighestScore(){
        String highest = "Highest: "+ highestScore;
        binding.highestScoreTextView.setText(highest);
    }

    public void saveHighestScore(){
        if(currentScore>highestScore){
            prefRepository.saveHighestScore(currentScore);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveHighestScore();
    }

    public void updateCurrentScore(){
        String current = "Current: " +currentScore;
        binding.currentScoreTextView.setText(current);
    }

    public void setCardColor(){
        Question currentQuestion = questionArrayList.get(questionCounter);
        if(Objects.equals(currentQuestion.getDifficulty(), "hard")){
            binding.difficultyTextView.setBackgroundColor(Color.parseColor("#E91E63"));
        }
        else if(Objects.equals(currentQuestion.getDifficulty(), "medium")){
            binding.difficultyTextView.setBackgroundColor(Color.parseColor("#FF9800"));
        }
        else{
            binding.difficultyTextView.setBackgroundColor(Color.parseColor("#1C8120"));
        }
    }

    public void showFinishDialog(){
        final Dialog dialog = new Dialog(this);
        DialogFinishTriviaBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getApplicationContext()),
                R.layout.dialog_finish_trivia,null, false);

        binding.setListener(new DialogClickListeners() {
            @Override
            public void onPlayAgainClicked() {
                currentScore = 0;
                questionCounter = 0;
                setQuestion();
                updateCurrentScore();
                checkHighestScore();
                updateHighestScore();
                dialog.dismiss();
            }

            @Override
            public void onShareScoreClicked() {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "My score is: "+currentScore);
                intent.putExtra(Intent.EXTRA_SUBJECT, "I am playing Trivia");
                startActivity(intent);

            }
        });
        binding.scoreTextView.setText(String.valueOf(currentScore));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(binding.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    public void checkHighestScore(){
        prefRepository = new PrefRepository(MainActivity.this);
        highestScore = prefRepository.getHighestScore();
    }

    public void fadeAnimation(View view, boolean isTrue){

        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setDuration(300);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        view.startAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if(isTrue){
                    view.setBackgroundColor(Color.GREEN);
                }
                else{
                    view.setBackgroundColor(Color.RED);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setBackgroundColor(Color.parseColor("#673AB7"));
                updateCurrentScore();
                goToTheNextQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }








}