package com.example.mytrivia.repository;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mytrivia.api.ApiClient;
import com.example.mytrivia.api.ApiInterface;
import com.example.mytrivia.model.Question;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repo {

    private final MutableLiveData<ArrayList<Question>> data = new MutableLiveData<>();
    private final ArrayList<Question> questionArrayList = new ArrayList<>();


    private static Repo instance;
    private final ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);


    public static Repo getInstance(){
        if(instance ==null){
            instance = new Repo();
        }
        return instance;
    }

    public LiveData<ArrayList<Question>> getData(){
        loadData();
        return data;
    }


    private void loadData(){

        Call<List<Question>> call = apiInterface.doGetListResources();
        call.enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if(response.isSuccessful()){
                  List<Question> responseList = response.body();
                    for (int i = 0; i < responseList.size(); i++) {
                        responseList.get(i).getAllAnswers().add(responseList.get(i).getCorrectAnswer());
                        for(String answer: responseList.get(i).getIncorrectAnswers()){
                            responseList.get(i).getAllAnswers().add(answer);
                        }
                    }
                  questionArrayList.addAll(responseList);
                  data.postValue(questionArrayList);
                 }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                call.cancel();
            }
        });

    }

    private void loadScore(){

    }

    private void saveScore(){

    }




}
