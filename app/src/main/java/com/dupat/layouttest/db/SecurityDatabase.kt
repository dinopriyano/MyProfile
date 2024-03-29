package com.dupat.layouttest.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dupat.layouttest.db.dao.DataSetSecurityDao
import com.dupat.layouttest.db.entity.DataSetSecurity

@Database(entities = [DataSetSecurity::class],version = 1)
abstract class SecurityDatabase: RoomDatabase() {

    abstract val dataSetSecurityDao: DataSetSecurityDao

    companion object{
        @Volatile
        private var INSTANCE: SecurityDatabase? = null
        fun getInstance(context: Context): SecurityDatabase{
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SecurityDatabase::class.java,
                        "security_database"
                    ).build()
                }

                return instance
            }
        }
    }

}