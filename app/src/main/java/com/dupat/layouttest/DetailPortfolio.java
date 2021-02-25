package com.dupat.layouttest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.dupat.layouttest.adapter.AdapterSS;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailPortfolio extends AppCompatActivity {

    RecyclerView recyclerSS;
    RecyclerView.Adapter adapter;
    ImageView iconApps;
    TextView nameApps,descApps;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_portfolio);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerSS = findViewById(R.id.recyclerSS);
        recyclerSS.setHasFixedSize(false);
        recyclerSS.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        iconApps = findViewById(R.id.iconApps);
        nameApps = findViewById(R.id.nameApps);
        descApps = findViewById(R.id.descApps);

        loadExtras();
    }

    public void loadExtras()
    {
        String name = getIntent().getStringExtra("name");
        int icon = getIntent().getIntExtra("icon",R.drawable.bg_gradient_profile);
        String desc = getIntent().getStringExtra("desc");
        int[] ss = getIntent().getIntArrayExtra("ss");

        if(icon == R.drawable.ic_android)
            iconApps.setPadding(20,20,20,20);
        Picasso.get().load(icon).into(iconApps);
        nameApps.setText(name);
        descApps.setText(desc);
        adapter = new AdapterSS(this,ss);
        recyclerSS.setAdapter(adapter);
    }
}