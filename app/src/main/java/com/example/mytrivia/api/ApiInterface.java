package com.example.mytrivia.api;

import com.example.mytrivia.model.Question;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("/api/questions")
    Call<List<Question>> doGetListResources();
}
