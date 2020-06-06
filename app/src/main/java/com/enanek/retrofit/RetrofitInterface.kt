package com.techymarvel.retrofit

import com.enanek.model.response.PhotosResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitInterface {

    @GET("services/rest/")
    fun getPhotosResponse(
        @Query("method") method: String
        , @Query("api_key") api_key: String
        , @Query("gallery_id") gallery_id: String
        , @Query("format") format: String
        , @Query("nojsoncallback") nojsoncallback: Int
        , @Query("page") page: Int
        , @Query("per_page") per_page: Int
    ): Single<Response<PhotosResponse>>

}