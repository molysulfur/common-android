package com.awonar.android.shared.db.room.watchlist

import androidx.room.*
import com.awonar.android.model.watchlist.Folder

@Dao
interface WatchlistFolderDao {

    @Update
    fun updateFolders(folders: List<Folder>)

    @Query("SELECT * FROM watchlist_folders")
    fun getAll(): List<Folder>

    @Query("SELECT * FROM watchlist_folders WHERE id IN (:id)")
    fun loadAllByIds(id: ArrayList<String>): List<Folder>

    @Query("SELECT * FROM watchlist_folders WHERE id = :id")
    fun loadById(id: String): Folder

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertAll(folder: List<Folder>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(folder: Folder)

    @Update
    fun update(folder: Folder)

    @Delete
    fun delete(folder: Folder)

    @Query("DELETE from watchlist_folders")
    fun clear()

}