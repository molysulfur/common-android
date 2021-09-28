package com.awonar.android.shared.db.room

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE


@Dao
interface TradingDataDao {
    @Query("SELECT * FROM trading_data")
    fun getAll(): List<TradingData>

    @Query("SELECT * FROM trading_data WHERE instrumentId IN (:id)")
    fun loadAllByIds(id: IntArray): List<TradingData>

    @Query("SELECT * FROM trading_data WHERE instrumentId = :id")
    fun loadById(id: Int): TradingData

    @Insert(onConflict = REPLACE)
    fun insertAll(tradingDataList: List<TradingData>)

    @Delete
    fun delete(user: TradingData)
}