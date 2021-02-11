package com.example.cryptomoney.service

import com.example.cryptomoney.model.CryptoModel
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET

interface CryptoAPI {

    //GET,POST,UPDATE,DELETE

    @GET("prices?key=da4c273526ac0f2931fffe9b0e731eb1")
    //fun getData(): Call<List<CryptoModel>>

    fun getData(): Observable<List<CryptoModel>>

}