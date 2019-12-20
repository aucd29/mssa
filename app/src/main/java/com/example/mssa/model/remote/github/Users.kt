package com.example.mssa.model.remote.github

import brigitte.IRecyclerDiff

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-12-20 <p/>
 */

data class Users(
    val total_count: Int,
    val incomplete_results: Boolean,
    val items: List<User>
)

data class User(
    val id: Int,
    val login: String,
    val node_id: String?,
    val avatar_url: String?,
    val gravatar_id: String?,
    val url: String,
    val html_url: String,
    val followers_url: String?,
    val following_url: String?,
    val gists_url: String,
    val starred_url:String,
    val subscriptions_url:String?,
    val organizations_url:String?,
    val repos_url: String,
    val events_url:String,
    val received_events_url:String,
    val type:String,
    val site_admin:String,
    val score: Float
): IRecyclerDiff {
    override fun itemSame(item: IRecyclerDiff) =
        id == (item as User).id

    override fun contentsSame(item: IRecyclerDiff) =
        login == (item as User).login && url == item.url

    fun scoreToString() = score.toString()
}