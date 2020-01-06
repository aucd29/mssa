package com.example.dhk.model.local.room

import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.room.*
import brigitte.IRecyclerDiff
import brigitte.bindingadapter.ToLargeAlphaAnimParams
import com.example.dhk.R
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by <a href="mailto:aucd29@gmail.com">Burke Choi</a> on 2019-12-20 <p/>
 */

@Dao
interface DibsDao {
    @Query("SELECT * FROM dibs") //  LIMIT :limit OFFSET :offset
        fun select(): Flowable<List<Dibs>>  // offset: Int, limit: Int = 10

    @Query("SELECT * FROM dibs")
    fun selectAll(): Flowable<List<Dibs>>

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
    @Ignore
    var dibs = ObservableInt(R.drawable.ic_star_yellow_24dp)
    @Ignore
    var anim = ObservableField<ToLargeAlphaAnimParams?>()

    fun toggleDibs() {
        dibs.set(if (dibs.get() == R.drawable.ic_star_border_yellow_24dp) {
            R.drawable.ic_star_yellow_24dp
        } else {
            R.drawable.ic_star_border_yellow_24dp
        })
    }

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