package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.AreaDao
import com.example.data.local.dao.BusRouteDao
import com.example.data.local.dao.BusStopDao
import com.example.data.local.dao.DistrictDao
import com.example.data.local.dao.LiveBusDao
import com.example.data.local.dao.StateDao
import com.example.data.local.dao.TicketDao
import com.example.data.local.entities.AreaEntity
import com.example.data.local.entities.BusRouteEntity
import com.example.data.local.entities.BusStopEntity
import com.example.data.local.entities.DistrictEntity
import com.example.data.local.entities.LiveBusEntity
import com.example.data.local.entities.StateEntity
import com.example.data.local.entities.TicketEntity

@Database(
    entities = [
        StateEntity::class,
        DistrictEntity::class,
        AreaEntity::class,
        BusRouteEntity::class,
        BusStopEntity::class,
        LiveBusEntity::class,
        TicketEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class PayanMitraDatabase : RoomDatabase() {
    abstract fun stateDao(): StateDao
    abstract fun districtDao(): DistrictDao
    abstract fun areaDao(): AreaDao
    abstract fun busRouteDao(): BusRouteDao
    abstract fun busStopDao(): BusStopDao
    abstract fun liveBusDao(): LiveBusDao
    abstract fun ticketDao(): TicketDao

    companion object {
        @Volatile
        private var INSTANCE: PayanMitraDatabase? = null

        fun getDatabase(context: Context): PayanMitraDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PayanMitraDatabase::class.java,
                    "payanmitra_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
