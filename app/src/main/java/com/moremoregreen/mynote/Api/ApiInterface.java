package com.moremoregreen.mynote.Api;

import com.moremoregreen.mynote.Model.Note;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("save.php")
    Call<Note> saveNote(
            @Field("title") String title,
            @Field("note") String note,
            @Field("color") int color
    );

    @GET("notes.php")
    Call<List<Note>> getNotes();
}
