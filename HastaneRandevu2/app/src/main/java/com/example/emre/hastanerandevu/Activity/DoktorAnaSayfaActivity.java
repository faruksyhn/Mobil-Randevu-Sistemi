package com.example.emre.hastanerandevu.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.emre.hastanerandevu.R;

public class DoktorAnaSayfaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doktor_ana_sayfa);
    }

    public void dktrProfil(View view) {
        Intent i=new Intent(DoktorAnaSayfaActivity.this,ProfilActivity.class);
        startActivity(i);
    }

    public void rndevuProfilGecis(View view) {
        Intent i=new Intent(DoktorAnaSayfaActivity.this,DoktorRandevulariActivity.class);
        startActivity(i);
    }
}
