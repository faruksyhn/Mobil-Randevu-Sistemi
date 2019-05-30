package com.example.emre.hastanerandevu.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.emre.hastanerandevu.Class.Randevu;
import com.example.emre.hastanerandevu.R;

public class AdminActivity extends AppCompatActivity {

    private Button BtnHastaneEkle,BtnBolumEkle,BtnDoktorEkle,BtnRandevuDuzenle;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        init();
        HastaneEkle();
        BolumEkle();
        dialog=new Dialog(this);
        DoktorEkle();
        //RandevuKısıtla();
    }

    public void init()
    {
        BtnHastaneEkle=findViewById(R.id.HastaneEkle);
        BtnBolumEkle=findViewById(R.id.BolumEkle);
        BtnDoktorEkle=findViewById(R.id.DoktorEkle);
        BtnRandevuDuzenle=findViewById(R.id.RandevuDuzenle);
    }

    private void BolumEkle() {
        BtnBolumEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(AdminActivity.this,BolumActivity.class);
                startActivity(i);
            }
        });
    }

    public void HastaneEkle()
    {
        BtnHastaneEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(AdminActivity.this, HastaneActivity.class);
                startActivity(i);
            }
        });
    }

    public void DoktorEkle()
    {
        BtnDoktorEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(AdminActivity.this, DoktorActivity.class);
                startActivity(i);
            }
        });
    }

   /* public void RandevuKısıtla()
    {
        BtnRandevuDuzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
dialog.show();
secim();
            }
        });
    }*/
    public void secim(View view){
        ImageButton imageRndvu1,imageRndevu2;
        dialog.setContentView(R.layout.randevusecim);
        imageRndvu1=dialog.findViewById(R.id.btnRndevuKısıt);
        imageRndevu2=dialog.findViewById(R.id.btnRndevuSil);
        imageRndvu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(AdminActivity.this, RandevuActivity.class);
                dialog.dismiss();
                startActivity(i);
            }
        });
        imageRndevu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(AdminActivity.this, RandevuSilActivity.class);
                dialog.dismiss();
                startActivity(i);
            }
        });

        dialog.show();
    }

}
