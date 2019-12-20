package com.example.mssa.model.remote

import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-12-20 <p/>
 */

interface GithubService {
    // https://raw.githubusercontent.com/aucd29/clone-daum/merge_search/dumy/popular-keyword.json
    @GET("/aucd29/clone-daum/merge_search/dumy/popular-keyword.json")
    fun popularKeywordList(): Observable<List<String>>
}