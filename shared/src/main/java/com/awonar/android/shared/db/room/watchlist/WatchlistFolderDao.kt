package com.awonar.android.shared.db.room.watchlist

import androidx.room.*
import com.awonar.android.model.watchlist.Folder

@Dao
interface WatchlistFolderDao {

    @Query("SELECT * FROM watchlist_folders")
    fun getAll(): List<Folder>

    @Query("SELECT * FROM watchlist_folders WHERE id IN (:id)")
    fun loadAllByIds(id: IntArray): List<Folder>

    @Query("SELECT * FROM watchlist_folders WHERE id = :id")
    fun loadById(id: Int): Folder

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(tradingDataList: List<Folder>)

    @Delete
    fun delete(user: Folder)
}