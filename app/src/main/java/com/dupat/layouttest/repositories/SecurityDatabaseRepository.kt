package com.dupat.layouttest.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dupat.layouttest.db.dao.DataSetSecurityDao
import com.dupat.layouttest.db.entity.DataSetSecurity
import okhttp3.MultipartBody
import okhttp3.RequestBody

class SecurityDatabaseRepository(private val dao: DataSetSecurityDao) {

    val dataSetSecurity: LiveData<List<DataSetSecurity>> = dao.getData(1)

    suspend fun insert(dataSetSecurity: DataSetSecurity){
        dao.insertData(dataSetSecurity)
    }

    suspend fun delete(dataSetSecurity: DataSetSecurity){
        dao.deleteData(dataSetSecurity)
    }


}