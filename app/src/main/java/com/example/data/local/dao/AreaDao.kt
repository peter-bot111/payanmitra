package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entities.AreaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AreaDao {
    @Query("SELECT * FROM areas WHERE districtCode = :districtCode ORDER BY areaName ASC")
    fun getAreasByDistrict(districtCode: String): Flow<List<AreaEntity>>

    @Query("SELECT * FROM areas ORDER BY areaName ASC")
    fun getAllAreas(): Flow<List<AreaEntity>>

    @Query("SELECT * FROM areas WHERE areaName LIKE '%' || :name || '%' LIMIT 1")
    suspend fun getAreaByName(name: String): AreaEntity?

    @Query("SELECT * FROM areas WHERE areaCode = :areaCode")
    suspend fun getAreaByCode(areaCode: String): AreaEntity?

    @Query("SELECT * FROM areas WHERE areaName LIKE '%' || :query || '%' OR areaNameTamil LIKE '%' || :query || '%'")
    fun searchAreas(query: String): Flow<List<AreaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAreas(areas: List<AreaEntity>)
}
