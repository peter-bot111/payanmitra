package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entities.StateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StateDao {
    @Query("SELECT * FROM states ORDER BY stateName ASC")
    fun getAllStates(): Flow<List<StateEntity>>

    @Query("SELECT * FROM states WHERE stateCode = :code")
    suspend fun getStateByCode(code: String): StateEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStates(states: List<StateEntity>)
}
