package com.dupat.layouttest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;

import com.dupat.layouttest.adapter.ContactAdapter;
import com.dupat.layouttest.helper.DBHelper;
import com.dupat.layouttest.model.ContactModel;

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "MESSAGE";
    private ListView obj;
    DBHelper mydb;
    List<ContactModel> array_list;
    ContactAdapter arrayAdapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        array_list = new ArrayList();
        arrayAdapter = new ContactAdapter(array_list,this);

        obj = (ListView)findViewById(R.id.listView1);
        mydb = new DBHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        array_list = mydb.getAllCotacts();
        arrayAdapter=new ContactAdapter(array_list, this);
        obj.setAdapter(arrayAdapter);
    }

    public void addContact(View view) {
        Bundle dataBundle = new Bundle();
        dataBundle.putInt("id", 0);

        Intent intent = new Intent(getApplicationContext(),DisplayContactActivity.class);
        intent.putExtras(dataBundle);

        startActivity(intent);
    }
}