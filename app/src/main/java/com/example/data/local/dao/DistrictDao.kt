package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entities.DistrictEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DistrictDao {
    @Query("SELECT * FROM districts WHERE stateCode = :stateCode ORDER BY districtName ASC")
    fun getDistrictsByState(stateCode: String): Flow<List<DistrictEntity>>

    @Query("SELECT * FROM districts WHERE districtCode = :code")
    suspend fun getDistrictByCode(code: String): DistrictEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDistricts(districts: List<DistrictEntity>)
}
