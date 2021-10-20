package com.awonar.android.shared.db.room.conversionrate

import androidx.room.*
import com.awonar.android.model.Conversion
import com.awonar.android.model.tradingdata.TradingData


@Dao
interface ConversionRateDao {
    @Query("SELECT * FROM conversions")
    fun getAll(): List<Conversion>

    @Query("SELECT * FROM conversions WHERE id = :id LIMIT 1")
    fun getById(id: Int): Conversion

    @Query("SELECT * FROM conversions WHERE id IN (:id)")
    fun loadAllByIds(id: IntArray): List<Conversion>

    @Query("SELECT * FROM conversions WHERE id = :id")
    fun loadById(id: Int): Conversion

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(data: List<Conversion>)

    @Delete
    fun delete(user: Conversion)
}