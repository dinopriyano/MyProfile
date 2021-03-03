package com.dupat.layouttest

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dupat.layouttest.adapter.FriendsAdapter
import com.dupat.layouttest.model.FriendsModel
import com.dupat.layouttest.network.*
import com.dupat.layouttest.utils.disable
import com.dupat.layouttest.utils.enable
import com.dupat.layouttest.utils.snackbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONObject

class FriendsActivity : AppCompatActivity(), View.OnClickListener, TextWatcher, FriendsAdapter.OnMenuClickCallback {
    lateinit var btnAdd: FloatingActionButton
    lateinit var recyclerContact: RecyclerView
    lateinit var addDialog: AlertDialog
    lateinit var progressAdd: ProgressBar
    lateinit var btnSubmit: Button
    lateinit var btnClose: Button
    lateinit var txtAction: TextView
    lateinit var txtName: TextInputEditText
    lateinit var txtEmail: TextInputEditText
    lateinit var txtPhone: TextInputEditText
    lateinit var acComment: AutoCompleteTextView
    lateinit var tilComment: TextInputLayout
    lateinit var containerMain: RelativeLayout
    var itemFriendsModel: FriendsModel? = null
    val CODE_POST_REQUEST: Int = 1
    val CODE_GET_REQUEST: Int = 2
    val CODE_UPDATE_REQUEST: Int = 3
    val CODE_DELETE_REQUEST: Int = 4
    var toolbar: Toolbar? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = ""
        containerMain = findViewById(R.id.containerMain)
        btnAdd = findViewById(R.id.btnAdd)
        btnAdd.setOnClickListener(this)
        recyclerContact = findViewById(R.id.recyclerContact)
        getData()
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btnAdd -> {
                showActionDialog("Insert")
            }
            R.id.btnCloseDialog -> {
                addDialog.dismiss()
            }
            R.id.btnSubmit -> {
                when ((v as Button).text) {
                    "Insert" -> {
                        insertData()
                    }
                    "Update" -> {
                        updateData()
                    }
                    "Delete" -> {
                        deleteData()
                    }
                }
            }
        }
    }

    private fun showActionDialog(action: String) {
        val factory = layoutInflater
        val addDialogView: View = factory.inflate(R.layout.add_dialog, null)
        addDialog = AlertDialog.Builder(this).create()
        addDialog.setCancelable(true)
        addDialog.setView(addDialogView)
        addDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        addDialog.show()
        btnClose = addDialogView.findViewById(R.id.btnCloseDialog)
        progressAdd = addDialogView.findViewById(R.id.progressAdd)
        btnSubmit = addDialogView.findViewById(R.id.btnSubmit)
        txtName = addDialogView.findViewById(R.id.txtName)
        txtEmail = addDialogView.findViewById(R.id.txtEmail)
        txtPhone = addDialogView.findViewById(R.id.txtPhone)
        txtAction = addDialogView.findViewById(R.id.txtAction)
        acComment = addDialogView.findViewById(R.id.acComment)
        tilComment = addDialogView.findViewById(R.id.tilComment)

        if(action == "Update" || action == "Delete"){

            txtPhone.setText(itemFriendsModel!!.phone)
            txtEmail.setText(itemFriendsModel!!.email)
            txtName.setText(itemFriendsModel!!.name)
            acComment.setText(itemFriendsModel!!.comment)

            if(action == "Update"){
                enableInputLayout()
            }
            else if(action == "Delete"){
                disableInputLayout()
                btnSubmit.enable()
            }
        }
        else if(action == "Insert"){
            enableInputLayout()
            txtPhone.setText("")
            txtEmail.setText("")
            txtName.setText("")
            acComment.setText("")
        }

        btnSubmit.text = action
        txtAction.text = "$action Data"
        txtName.addTextChangedListener(this)
        txtPhone.addTextChangedListener(this)
        txtEmail.addTextChangedListener(this)
        acComment.addTextChangedListener(this)
        btnSubmit.setOnClickListener(this)
        btnClose.setOnClickListener(this)
        setCommentData()
    }

    private fun disableInputLayout(){
        txtName.disable()
        txtEmail.disable()
        txtPhone.disable()
        acComment.disable()
    }

    private fun enableInputLayout(){
        txtName.enable()
        txtEmail.enable()
        txtPhone.enable()
        acComment.enable()
    }

    private fun setCommentData(){
        val itemsComment = listOf(*resources.getStringArray(R.array.comments))
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, itemsComment)
        acComment.setAdapter(adapter)
    }

    private fun insertData(){
        var params: HashMap<String, String> = HashMap<String, String>()
        params["name"] = txtName.text.toString()
        params["email"] = txtEmail.text.toString()
        params["phone"] = txtPhone.text.toString()
        params["comment"] = acComment.text.toString()

        val request = PerformNetworkRequest(this, URL_CREATE, CODE_POST_REQUEST, params)
        request.execute()
    }

    private fun updateData(){
        var params: HashMap<String, String> = HashMap<String, String>()
        params["id"] = itemFriendsModel!!.id.toString()
        params["name"] = txtName.text.toString()
        params["email"] = txtEmail.text.toString()
        params["phone"] = txtPhone.text.toString()
        params["comment"] = acComment.text.toString()

        val request = PerformNetworkRequest(this, URL_UPDATE, CODE_UPDATE_REQUEST, params)
        request.execute()
    }

    private fun deleteData(){
        val request = PerformNetworkRequest(this, "$URL_DELETE&id=${itemFriendsModel!!.id}", CODE_DELETE_REQUEST)
        request.execute()
    }

    private fun getData(){
        val request = PerformNetworkRequest(this, URL_READ, CODE_GET_REQUEST)
        request.execute()
    }

    private fun clearForm(){
        txtName.setText("")
        txtEmail.setText("")
        txtPhone.setText("")
        acComment.setText("")
    }

    private class PerformNetworkRequest(val activity: FriendsActivity, val url: String, val requestCode: Int, val params: HashMap<String, String>? = null) : AsyncTask<Void, Void, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            if(requestCode == activity.CODE_POST_REQUEST || requestCode == activity.CODE_UPDATE_REQUEST || requestCode == activity.CODE_DELETE_REQUEST){
                activity.btnSubmit.text = ""
                activity.progressAdd.visibility = View.VISIBLE
            }
            else if(requestCode == activity.CODE_GET_REQUEST){
                activity.containerMain.snackbar("Loading...")
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            if(requestCode == activity.CODE_POST_REQUEST){
                activity.btnSubmit.text = "Insert"
                activity.progressAdd.visibility = View.GONE
                try {
                    var jsonObject = JSONObject(result)
                    if(!jsonObject.getBoolean("error")){
                        activity.containerMain.snackbar(jsonObject.getString("message"))
                        activity.clearForm()
                        activity.addDialog.dismiss()
                        activity.getData()
                    }
                }
                catch (e: Exception){
                    e.printStackTrace()
                }
            }

            else if(requestCode == activity.CODE_UPDATE_REQUEST){
                activity.btnSubmit.text = "Update"
                activity.progressAdd.visibility = View.GONE
                try {
                    var jsonObject = JSONObject(result)
                    if(!jsonObject.getBoolean("error")){
                        activity.containerMain.snackbar(jsonObject.getString("message"))
                        activity.clearForm()
                        activity.addDialog.dismiss()
                        activity.getData()
                    }
                }
                catch (e: Exception){
                    e.printStackTrace()
                }
            }

            else if(requestCode == activity.CODE_DELETE_REQUEST){
                activity.btnSubmit.text = "Delete"
                activity.progressAdd.visibility = View.GONE
                try {
                    var jsonObject = JSONObject(result)
                    if(!jsonObject.getBoolean("error")){
                        activity.containerMain.snackbar(jsonObject.getString("message"))
                        activity.clearForm()
                        activity.addDialog.dismiss()
                        activity.getData()
                    }
                }
                catch (e: Exception){
                    e.printStackTrace()
                }
            }

            else if(requestCode == activity.CODE_GET_REQUEST){
                try {
                    Log.d("Broo", "res bro: ${result!!}")
                    var jsonObject = JSONObject(result)
                    if(!jsonObject.getBoolean("error")){
                        var jsonArr = jsonObject.getJSONArray("data")

                        var modelList: MutableList<FriendsModel> = arrayListOf()
                        for(i in 0 until jsonArr.length()){
                            val jObj: JSONObject = jsonArr.getJSONObject(i)
                            val model = FriendsModel(jObj.getString("comment"), jObj.getString("email"), jObj.getInt("id"), jObj.getString("name"), jObj.getString("phone"))
                            modelList.add(model)
                        }

                        val rvLayoutManager = LinearLayoutManager(activity)
                        rvLayoutManager.reverseLayout = true
                        activity.recyclerContact.apply {
                            layoutManager = rvLayoutManager
                            adapter = FriendsAdapter(modelList, activity, activity)
                        }

                    }
                }
                catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }

        override fun doInBackground(vararg v: Void?): String {
            return when(requestCode){
                activity.CODE_POST_REQUEST -> {
                    APIHandler.sendPostRequest(url, params)!!
                }
                activity.CODE_UPDATE_REQUEST -> {
                    APIHandler.sendPostRequest(url, params)!!
                }
                activity.CODE_GET_REQUEST -> {
                    APIHandler.sendGetRequest(url)!!
                }
                activity.CODE_DELETE_REQUEST -> {
                    APIHandler.sendGetRequest(url)!!
                }
                else -> {
                    ""
                }
            }
        }

    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        btnSubmit.isEnabled = !(txtName.text.isNullOrEmpty() || txtEmail.text.isNullOrEmpty() || txtPhone.text.isNullOrEmpty() || acComment.text.isNullOrEmpty())
    }

    override fun onMenuClicked(model: FriendsModel, action: String) {
        itemFriendsModel = model
        when(action){
            "edit" -> {
                showActionDialog("Update")
            }
            "delete" -> {
                showActionDialog("Delete")
            }
        }
    }
}