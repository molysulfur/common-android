package com.awonar.android.shared.db.room.instrument

import androidx.room.*
import com.awonar.android.model.market.Instrument

@Dao
interface InstrumentDao {
    @Query("SELECT * FROM instruments")
    fun getAll(): List<Instrument>

    @Query("SELECT * FROM instruments WHERE id IN (:id)")
    fun loadAllByIds(id: IntArray): List<Instrument>

    @Query("SELECT * FROM instruments WHERE id = :id")
    fun loadById(id: Int): Instrument?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(tradingDataList: List<Instrument>)

    @Delete
    fun delete(instrument: Instrument)
}