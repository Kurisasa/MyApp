package com.example.zapper.repository.api

import com.example.zapper.model.PersonDetail
import com.example.zapper.model.PersonResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiMethods {

    @GET("/persons")
    fun getData() : Observable<PersonResponse>

    @POST("/persons/{id}")
    fun getPersonDetail(@Path("id") personId: Int?, param: Any): Observable<PersonDetail>
}