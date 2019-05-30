package com.example.emre.hastanerandevu.Activity;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.transition.Slide;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emre.hastanerandevu.R;
import com.example.emre.hastanerandevu.SlideAdapter;

public class KullaniciActivity extends AppCompatActivity {

    ImageView imgKullanici;

    private ViewPager viewPager;
 private SlideAdapter myAdapter;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kullanici);
        ViewPager viewPager=findViewById(R.id.pager1);
        myAdapter=new SlideAdapter(this);
        viewPager.setAdapter(myAdapter);
    }



}
