package com.example.mssa.model.local.table

import androidx.room.*
import brigitte.IRecyclerDiff
import io.reactivex.Completable
import io.reactivex.Observable

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2019-12-20 <p/>
 */

@Dao
interface DibsDao {
    @Query("SELECT * FROM dibs ORDER BY _id DESC LIMIT :page, 10")
    fun select(page: Int): Observable<List<Dibs>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: Dibs): Completable

    @Update
    fun update(data: Dibs): Completable

    @Delete
    fun delete(data: Dibs): Completable
}

@Entity(tableName = "dibs")
data class Dibs (
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
        login == (item as Dibs).login && avatar_url == item.avatar_url &&
                score == item.score && starred_url == item.starred_url
}