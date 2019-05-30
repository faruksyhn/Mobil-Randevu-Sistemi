package com.example.emre.hastanerandevu.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.emre.hastanerandevu.Class.Hastane;
import com.example.emre.hastanerandevu.R;

public class AnaMenuActivity extends AppCompatActivity {
Button giris,kaydol;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana_menu);
        init();
        GiriseYonlendir();
        KayıtaYonlendir();
    }
    public void init()
    {
        giris=findViewById(R.id.btnGiris);
        kaydol=findViewById(R.id.btnKaydol);
    }
    public void GiriseYonlendir()
    {
        giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(AnaMenuActivity.this, GirisActivity.class);
                startActivity(i);
            }
        });

    }
    public void KayıtaYonlendir()
    {
        kaydol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(AnaMenuActivity.this,MainActivity.class);
                startActivity(i);
            }
        });
    }
    @Override    public void onBackPressed() {
        System.runFinalizersOnExit(true);
    }
}
