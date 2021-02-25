package com.dupat.layouttest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Calculator extends AppCompatActivity implements View.OnClickListener {

    private CardView btnBagi,btnKali,btnDelete,btnAc,btnSatu,btnDua,btnTiga,btnEmpat,btnLima,btnEnam,btnTujuh,btnDelapan,btnSembilan,btnNol,btnTambah,btnKurang,btnTitik,btnSamadengan;
    private EditText etSatu,etDua;
    private double numOne = 0.0;
    private double numTwo = 0.0;
    private boolean isRes = false;
    private DecimalFormat withComa,withoutComa;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        withComa = new DecimalFormat("#.#");
        withoutComa = new DecimalFormat("###");
        btnBagi = findViewById(R.id.btnBagi);
        btnBagi.setOnClickListener(this);
        btnKali = findViewById(R.id.btnKali);
        btnKali.setOnClickListener(this);
        btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(this);
        btnAc = findViewById(R.id.btnAc);
        btnAc.setOnClickListener(this);
        btnSatu = findViewById(R.id.btnSatu);
        btnSatu.setOnClickListener(this);
        btnDua = findViewById(R.id.btnDua);
        btnDua.setOnClickListener(this);
        btnTiga = findViewById(R.id.btnTiga);
        btnTiga.setOnClickListener(this);
        btnEmpat = findViewById(R.id.btnEmpat);
        btnEmpat.setOnClickListener(this);
        btnLima = findViewById(R.id.btnLima);
        btnLima.setOnClickListener(this);
        btnEnam = findViewById(R.id.btnEnam);
        btnEnam.setOnClickListener(this);
        btnTujuh = findViewById(R.id.btnTujuh);
        btnTujuh.setOnClickListener(this);
        btnDelapan = findViewById(R.id.btnDelapan);
        btnDelapan.setOnClickListener(this);
        btnSembilan = findViewById(R.id.btnSembilan);
        btnSembilan.setOnClickListener(this);
        btnNol = findViewById(R.id.btnNol);
        btnNol.setOnClickListener(this);
        btnTambah = findViewById(R.id.btnTambah);
        btnTambah.setOnClickListener(this);
        btnKurang = findViewById(R.id.btnKurang);
        btnKurang.setOnClickListener(this);
        btnTitik = findViewById(R.id.btnTitik);
        btnTitik.setOnClickListener(this);
        btnSamadengan = findViewById(R.id.btnSamadengan);
        btnSamadengan.setOnClickListener(this);
        etSatu = findViewById(R.id.etSatu);
        etDua = findViewById(R.id.etDua);
        etDua.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private String getResult(String val)
    {
        boolean isNotDigit = false;
        if(!val.contains("÷") && !val.contains("×") && !val.contains("+") && !val.contains("-"))
        {
            return val;
        }
        else
        {
            if(!Character.isDigit(val.charAt(val.length()-1)))
            {
                val = val.substring(0,val.length()-1);
                isNotDigit = true;
//                return "="+val;
            }

            List<String> operation = new ArrayList<>();

            for(int i = 0;i<val.length();i++)
            {
                if(String.valueOf(val.charAt(i)).equals("÷") || String.valueOf(val.charAt(i)).equals("×") || String.valueOf(val.charAt(i)).equals("+") || String.valueOf(val.charAt(i)).equals("-"))
                {
                    operation.add(String.valueOf(val.charAt(i)));
                }
            }

            numOne = 0.0;
            val = val.replaceAll("[^\\d.]", ",");
            String[] numArr = val.split(",");
            if(isNotDigit && operation.size() <= 1){
                return "="+val;
            }

            for(int i = 0;i<numArr.length;i++)
            {
                if(i != numArr.length-1)
                {
                    if(operation.get(i).equals("÷"))
                    {
                        if(i == 0)
                        {
                            numOne+=(Double.parseDouble(numArr[i]) / Double.parseDouble(numArr[i+1]));
                        }
                        else
                        {
                            numOne/=Double.parseDouble(numArr[i+1]);
                        }
                    }
                    else if(operation.get(i).equals("×"))
                    {
                        if(i == 0)
                        {
                            numOne+=(Double.parseDouble(numArr[i]) * Double.parseDouble(numArr[i+1]));
                        }
                        else
                        {
                            numOne*= Double.parseDouble(numArr[i+1]);
                        }
                    }
                    else if(operation.get(i).equals("-"))
                    {
                        if(i == 0)
                        {
                            numOne+=(Double.parseDouble(numArr[i]) - Double.parseDouble(numArr[i+1]));
                        }
                        else
                        {
                            numOne-=Double.parseDouble(numArr[i+1]);
                        }
                    }
                    else if(operation.get(i).equals("+"))
                    {
                        if(i == 0)
                        {
                            numOne+=(Double.parseDouble(numArr[i]) + Double.parseDouble(numArr[i+1]));
                        }
                        else
                        {
                            numOne+= Double.parseDouble(numArr[i+1]);
                        }
                    }
                }
            }

            return "="+(numOne%1==0?String.valueOf(withoutComa.format(numOne)):String.valueOf(withComa.format(numOne)));
        }

    }

    public void deleteNum()
    {
        String defText = etDua.getText().toString();
        if(defText.length()>1)
        {
            etDua.setText(defText.substring(0,defText.length()-1));
        }
        else
        {
            etDua.setText("");
            etDua.setHint("0");
        }
    }

    private void deleteAllNum()
    {
        etSatu.setText("");
        etDua.setText("");
        etSatu.setHint("0");
        etDua.setHint("0");
        etSatu.setTextSize(27);
        etSatu.setTextColor(Color.parseColor("#A3A3A3"));
        etDua.setTextSize(35);
        etDua.setTextColor(Color.parseColor("#000000"));
    }

    public void inputNum(String num)
    {
        if(isRes)
        {
            deleteAllNum();
            isRes = false;
            etSatu.setTextSize(27);
            etSatu.setTextColor(Color.parseColor("#A3A3A3"));
            etDua.setTextSize(35);
            etDua.setTextColor(Color.parseColor("#000000"));
        }
        etDua.setText(etDua.getText()+num);
        etSatu.setText(getResult(etDua.getText().toString()));
        playSound();
    }

    private void playSound()
    {
        MediaPlayer mPlayer = MediaPlayer.create(this, R.raw.click);
        if(!mPlayer.isPlaying())
        {
            mPlayer.start();
        }
        else
        {
            mPlayer.release();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnBack:
            {
                onBackPressed();
                break;
            }
            case R.id.btnTitik:
            {
                if(etDua.getText().length() > 0)
                    inputNum(".");
                break;
            }
            case R.id.btnKali:
            {
                if(etDua.getText().length() > 0)
                    inputNum("×");
                break;
            }
            case R.id.btnBagi:
            {
                if(etDua.getText().length() > 0)
                    inputNum("÷");
                break;
            }
            case R.id.btnTambah:
            {
                if(etDua.getText().length() > 0)
                    inputNum("+");
                break;
            }
            case R.id.btnKurang:
            {
                if(etDua.getText().length() > 0)
                    inputNum("-");
                break;
            }
            case R.id.btnSamadengan:
            {
                isRes = true;
                etSatu.setTextSize(35);
                etSatu.setTextColor(Color.parseColor("#000000"));
                etDua.setTextSize(27);
                etDua.setTextColor(Color.parseColor("#A3A3A3"));
                playSound();
                break;
            }
            case R.id.btnDelete:
            {
                deleteNum();
                inputNum("");
                break;
            }
            case R.id.btnAc:
            {
                playSound();
                deleteAllNum();
                break;
            }
            case R.id.btnSatu:
            {
                inputNum("1");
                break;
            }
            case R.id.btnDua:
            {
                inputNum("2");
                break;
            }
            case R.id.btnTiga:
            {
                inputNum("3");
                break;
            }
            case R.id.btnEmpat:
            {
                inputNum("4");
                break;
            }
            case R.id.btnLima:
            {
                inputNum("5");
                break;
            }
            case R.id.btnEnam:
            {
                inputNum("6");
                break;
            }
            case R.id.btnTujuh:
            {
                inputNum("7");
                break;
            }
            case R.id.btnDelapan:
            {
                inputNum("8");
                break;
            }
            case R.id.btnSembilan:
            {
                inputNum("9");
                break;
            }
            case R.id.btnNol:
            {
                inputNum("0");
                break;
            }
        }
    }
}