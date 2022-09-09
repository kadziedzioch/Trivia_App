package com.example.mytrivia.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mytrivia.model.Question;
import com.example.mytrivia.repository.Repo;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivityViewModel extends ViewModel {

    private LiveData<ArrayList<Question>> questionsList;
    private Repo repository;


    public void init(){
        if(questionsList!=null){
            return;
        }
        repository = Repo.getInstance();
        questionsList = repository.getData();

    }

    public LiveData<ArrayList<Question>> getTasks(){
        return questionsList;
    }






}
