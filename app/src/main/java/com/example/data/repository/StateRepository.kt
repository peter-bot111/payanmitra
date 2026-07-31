package com.example.data.repository

import com.example.data.local.dao.AreaDao
import com.example.data.local.dao.DistrictDao
import com.example.data.local.dao.StateDao
import com.example.data.local.entities.AreaEntity
import com.example.data.local.entities.DistrictEntity
import com.example.data.local.entities.StateEntity
import kotlinx.coroutines.flow.Flow

class StateRepository(
    private val stateDao: StateDao,
    private val districtDao: DistrictDao,
    private val areaDao: AreaDao
) {
    fun getAllStates(): Flow<List<StateEntity>> = stateDao.getAllStates()

    suspend fun getStateByCode(code: String): StateEntity? = stateDao.getStateByCode(code)

    fun getDistrictsForState(stateCode: String): Flow<List<DistrictEntity>> =
        districtDao.getDistrictsByState(stateCode)

    suspend fun getDistrictByCode(code: String): DistrictEntity? = districtDao.getDistrictByCode(code)

    fun getAreasForDistrict(districtCode: String): Flow<List<AreaEntity>> =
        areaDao.getAreasByDistrict(districtCode)

    fun getAllAreas(): Flow<List<AreaEntity>> = areaDao.getAllAreas()

    suspend fun getAreaByName(name: String): AreaEntity? = areaDao.getAreaByName(name)

    suspend fun getAreaByCode(areaCode: String): AreaEntity? = areaDao.getAreaByCode(areaCode)

    fun searchAreas(query: String): Flow<List<AreaEntity>> = areaDao.searchAreas(query)
}
