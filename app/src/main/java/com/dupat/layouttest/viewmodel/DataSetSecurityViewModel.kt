package com.dupat.layouttest.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dupat.layouttest.db.entity.DataSetSecurity
import com.dupat.layouttest.repositories.SecurityDatabaseRepository
import com.dupat.layouttest.utils.APIExceptions
import com.dupat.layouttest.utils.Corountines
import com.dupat.layouttest.utils.Function.bitmapToByteArray
import com.dupat.layouttest.utils.SingleLiveEvent
import com.dupat.layouttest.viewmodel.state.ViewState
import com.google.android.gms.tasks.OnSuccessListener
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*


class DataSetSecurityViewModel(val repository: SecurityDatabaseRepository): ViewModel() {

    private var state: SingleLiveEvent<ViewState> = SingleLiveEvent()
    var bmpImage: Bitmap? = null
    var securityName: String? = null
    var imageUrl: String? = null
    private var dataSet = repository.dataSetSecurity
    private var numValidDetect = MutableLiveData<Int>()
    var latitude: String? = null
    var longitude: String? = null
    var note: String? = null
    var photo: String? = null
    var status: String? = null
    var user_id: String? = null
    var zone_id: String? = null

    fun validateImage(){
        state.value = ViewState.IsLoading(true)

        if(bmpImage!!.height < bmpImage!!.width){
            state.value = ViewState.IsLoading(false)
            state.value = ViewState.Error("Image height must be more than width")
        }
        else{
            val options = FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .setContourMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
                .build()
            val detector = FaceDetection.getClient(options)
            val faceDetector: FaceDetector = detector

            val inputImage = InputImage.fromBitmap(bmpImage!!, 0)
            faceDetector
                .process(inputImage)
                .addOnSuccessListener(OnSuccessListener { faces ->
                    if (faces.size == 0) {
                        state.value = ViewState.IsLoading(false)
                        state.value = ViewState.Error("Face not found")
                        Log.d("TAG", "Face not found")
                        return@OnSuccessListener
                    } else if (faces.size > 1) {
                        state.value = ViewState.IsLoading(false)
                        state.value = ViewState.Error("Face more than one")
                        Log.d("TAG", "Face more than one")
                        return@OnSuccessListener
                    } else {
                        state.value = ViewState.IsLoading(false)
                        state.value = ViewState.IsSuccess(0)
                        Log.d("TAG", "Face count: " + faces.size)
                    }
                })
        }
    }

    fun addProgress(num: Int){
        numValidDetect.postValue(num)
    }

    fun insertDataSet(){
        if(bmpImage == null){
            state.value = ViewState.Error("Choose image first")
        }
        else{
            state.value = ViewState.IsLoading(true)
            val dataSetSecurity: DataSetSecurity = DataSetSecurity(
                    1,
                    securityName!!,
                    imageUrl!!,
                    bitmapToByteArray(bmpImage!!)
            )

            Corountines.main {
                state.value = ViewState.IsLoading(true)
                repository.insert(dataSetSecurity)
                state.value = ViewState.IsLoading(false)
                state.value = ViewState.IsSuccess(1)
            }
        }
    }

    fun getDatSet() = dataSet
    fun getState() = state
    fun getNumValid() = numValidDetect
}