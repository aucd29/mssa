package com.example.mssa.model.local.room

import androidx.room.*
import brigitte.IRecyclerDiff
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-12-20 <p/>
 */

@Dao
interface DibsDao {
    @Query("SELECT * FROM dibs LIMIT :limit OFFSET :offset")
    fun select(offset: Int, limit: Int = 10): Observable<List<Dibs>>

    @Query("SELECT * FROM dibs")
    fun selectAll(): Single<List<Dibs>>

    @Query("SELECT COUNT(*) FROM dibs")
    fun count(): Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: Dibs): Completable

    @Query("DELETE FROM dibs WHERE sid=:sid")
    fun delete(sid: Int): Completable
}

@Entity(tableName = "dibs")
data class Dibs (
    val sid: Int,
    val login: String,
    val avatar_url: String,
    val score: Float,
    val starred_url: String,
    @PrimaryKey(autoGenerate = true)
    val _id: Int = 0
): IRecyclerDiff {
    override fun itemSame(item: IRecyclerDiff) =
        _id == (item as Dibs)._id

    override fun contentsSame(item: IRecyclerDiff) =
        login == (item as Dibs).login &&
            avatar_url == item.avatar_url &&
            score == item.score &&
            starred_url == item.starred_url &&
            sid == item.sid

    fun scoreToString() = score.toString()
}