package com.dupat.layouttest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dupat.layouttest.adapter.AdapterPortfolio;
import com.dupat.layouttest.fragment.LayoutProfile;
import com.dupat.layouttest.model.ModelPortfolio;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity implements View.OnClickListener {

    private CardView btnShowPortfolio,btnCalculator;
    private ImageView pictProfile;
    private FrameLayout frameProfile;
    private SlideUp slideUpProfile;
    private View dim;
    RecyclerView recyclerPortfolio;
    RecyclerView.Adapter adapter;
    List<ModelPortfolio> listPortfolio;
    RelativeLayout slideView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnCalculator = findViewById(R.id.btnCalculator);
        btnCalculator.setOnClickListener(this);
        slideView = findViewById(R.id.slideView);
        dim = findViewById(R.id.dim);
        frameProfile = findViewById(R.id.frameProfile);
        slideUpProfile = new SlideUpBuilder(slideView)
                .withListeners(new SlideUp.Listener.Events() {
                    @Override
                    public void onSlide(float percent) {
                        dim.setAlpha(1 - (percent / 100));
                    }

                    @Override
                    public void onVisibilityChanged(int visibility) {

                    }
                })
                .withStartGravity(Gravity.BOTTOM)
                .withLoggingEnabled(true)
                .withHideSoftInputWhenDisplayed(true)
                .withGesturesEnabled(true)
                .withStartState(SlideUp.State.HIDDEN)
                .build();

        slideView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slideUpProfile.hide();
            }
        });

        frameProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnShowPortfolio = findViewById(R.id.btnToPortfolio);
        btnShowPortfolio.setOnClickListener(this);
        pictProfile = findViewById(R.id.pictProfile);
        pictProfile.setOnClickListener(this);
        recyclerPortfolio = findViewById(R.id.recyclerPortfolio);
        recyclerPortfolio.setHasFixedSize(false);
        recyclerPortfolio.setLayoutManager(new LinearLayoutManager(this));

        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameProfile, new LayoutProfile());
        transaction.addToBackStack(null);
        transaction.commit();

        String[] appsName = {"Dupat Smart School","Mesjidku","Covid-19 Apps","Dupat Chat","Security Patrol","Dupat LSP","ChatKuy"};
        String[] appsDesc = {"Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged.","Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged.","Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged.","Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged.","Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged.","Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged.","Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged."};
        int[] appsIcon = new int[]{R.drawable.ic_android,R.drawable.ic_mesjidku,R.drawable.ic_android,R.drawable.ic_android,R.drawable.ic_android,R.drawable.ic_android,R.drawable.ic_android};
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

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnToPortfolio:
            {
//                startActivity(new Intent(this,Portfolio.class));
                slideUpProfile.show();
                break;
            }

            case R.id.pictProfile:
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                View layoutView = getLayoutInflater().inflate(R.layout.layout_pup_up_picture,null);
                builder.setView(layoutView);
                builder.setCancelable(true);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
            }

            case R.id.btnCalculator:
            {
                startActivity(new Intent(this,Calculator.class));
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(slideUpProfile.isVisible()){
            slideUpProfile.hide();
        }
        else
        {
            super.onBackPressed();
        }
    }
}