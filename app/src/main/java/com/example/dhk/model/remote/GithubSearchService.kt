package com.example.dhk.model.remote

import com.example.dhk.model.remote.github.Users
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by <a href="mailto:aucd29@gmail.com">Burke Choi</a> on 2019-12-20 <p/>
 */

// https://developer.github.com/v3/search/#search-users
// curl https://api.github.com/search/users?q=tom+repos:%3E42+followers:%3E1000

interface GithubSearchService {
    @GET("/search/users")
    fun users(@Query("q") query: String,
              @Query("page") page: String = "1",
              @Query("per_page") perPage: String = "30"): Observable<Users>
}