package com.dupat.layouttest

import android.Manifest
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dupat.layouttest.databinding.ActivityFaceRecognitionBinding
import com.dupat.layouttest.db.SecurityDatabase
import com.dupat.layouttest.repositories.SecurityDatabaseRepository
import com.dupat.layouttest.session.SharedPrefManager
import com.dupat.layouttest.utils.snackbar
import com.dupat.layouttest.utils.toast
import com.dupat.layouttest.viewmodel.DataSetSecurityViewModel
import com.dupat.layouttest.viewmodel.factory.DataSetSecurityViewModelFactory
import com.dupat.layouttest.viewmodel.state.ViewState
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_face_recognition.*

class FaceRecognitionActivity : AppCompatActivity(), View.OnClickListener, PermissionListener {
    private lateinit var binding: ActivityFaceRecognitionBinding
    private lateinit var viewModel: DataSetSecurityViewModel
    private lateinit var uriBmp: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_face_recognition)
        val dao = SecurityDatabase.getInstance(application).dataSetSecurityDao
        val repository = SecurityDatabaseRepository(dao)
        val factory = DataSetSecurityViewModelFactory(repository)
        viewModel = ViewModelProvider(this,factory).get(DataSetSecurityViewModel::class.java)
        binding.viewmodel = viewModel

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = ""

        btnChooseImage.setOnClickListener(this)
        btnLoadImage.setOnClickListener(this)
        handleViewState()
        requestStoragePermission()
        toast("Not all device support this fiture")
    }

    private fun requestStoragePermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(this).check()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnChooseImage -> {
                if(etName.text.toString().isNullOrEmpty()){
                    containerSetDataSet.snackbar("Enter your name first!")
                }
                else{
                    val intent = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )

                    intent.type = "image/*"
                    startActivityForResult(intent, 10)
                }
            }
        }
    }

    private fun getRealpath(uri: Uri): String {
        var realPath: String? = null
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = contentResolver.query(uri, filePathColumn, null, null, null)
        if (cursor!!.moveToFirst()) {
            val columnIndex: Int = cursor.getColumnIndex(filePathColumn[0])
            realPath = cursor.getString(columnIndex)
        }
        cursor.close()
        return realPath!!
    }

    fun handleViewState(){
        viewModel.getState().observer(this, Observer {
            when(it){
                is ViewState.IsLoading -> {
                    containerSetDataSet.snackbar("Loading...")
                }

                is ViewState.Error -> {
                    containerSetDataSet.snackbar("Error: ${it.err!!}")
                }

                is ViewState.IsSuccess -> {
                    when(it.what){
                        0 -> {
                            val intent = Intent(this,DetectorActivity::class.java)
                            intent.putExtra("isFirst","")
                            intent.putExtra("bmpUri",SharedPrefManager.getString(this,"uriDataSet").toUri())
                            intent.putExtra("securityName",SharedPrefManager.getString(this,"name"))
                            startActivity(intent)
                        }

                        1 -> {
//                            containerMain.snackbar("Success insert data")
//                            startActivity(Intent(this,DetectorActivity::class.java))
                        }

                        2 -> {

                        }
                    }
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            // When an Image is picked
            if (requestCode == 10 && resultCode == RESULT_OK) {
                if (data?.data != null) {
                    val mImageUri: Uri = data.data!!
                    uriBmp = mImageUri
                    SharedPrefManager.setString(this,"uriDataSet",mImageUri.toString())
                    SharedPrefManager.setString(this,"name",etName.text.toString())
                    val realPath: String = getRealpath(mImageUri)
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, mImageUri)
//                    if(bitmap.height > 320){
//                        bitmap = resizedBitmap(bitmap,320)
//                    }
                    ivPerson.setImageBitmap(bitmap)
                    viewModel.bmpImage = bitmap
                    viewModel.validateImage()
                    Log.d(
                        "TAG",
                        "onActivityResult: " + realPath + " uri: " + mImageUri + " string: " + mImageUri.toString()
                    )
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Something went wrong: ${e.message}", Toast.LENGTH_LONG)
                .show()
        }
    }

    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {

    }

    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
        requestStoragePermission()
    }

    override fun onPermissionRationaleShouldBeShown(p0: PermissionRequest?, p1: PermissionToken?) {
        p1!!.continuePermissionRequest()
    }
}