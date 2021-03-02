package com.dupat.layouttest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.dupat.layouttest.adapter.AdapterPortfolio;
import com.dupat.layouttest.model.ModelPortfolio;

import java.util.ArrayList;
import java.util.List;

public class PortfolioActivity extends AppCompatActivity {

    RecyclerView recyclerPortfolio;
    RecyclerView.Adapter adapter;
    List<ModelPortfolio> listPortfolio;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Portfolio");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerPortfolio = findViewById(R.id.recyclerPortfolio);
        recyclerPortfolio.setHasFixedSize(false);
        recyclerPortfolio.setLayoutManager(new LinearLayoutManager(this));

        String[] appsName = {"Dupat Smart School","Mesjidku","Dupat Chat"};
        String[] appsDesc = {"Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged.","Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged.","Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged."};
        int[] appsIcon = new int[]{R.drawable.ic_android,R.drawable.ic_mesjidku,R.drawable.ic_android};
        int[] appsSS = new int[]{1,2,3,4,5,6,7};

        listPortfolio = new ArrayList<>();
        for(int i = 0;i<appsName.length;i++)
        {
            ModelPortfolio model = new ModelPortfolio(appsName[i],appsDesc[i],appsIcon[i],appsSS);
            listPortfolio.add(model);
        }

        loadRecycler();

    }

    public void loadRecycler()
    {
        adapter = new AdapterPortfolio(listPortfolio,this);
        recyclerPortfolio.setAdapter(adapter);
    }
}