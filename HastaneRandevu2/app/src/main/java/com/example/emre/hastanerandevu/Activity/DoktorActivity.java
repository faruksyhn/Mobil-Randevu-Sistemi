package com.example.emre.hastanerandevu.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.emre.hastanerandevu.Class.Bolum;
import com.example.emre.hastanerandevu.Class.Doktor;
import com.example.emre.hastanerandevu.Class.Hastane;
import com.example.emre.hastanerandevu.Class.Kullanıcı;
import com.example.emre.hastanerandevu.Class.Randevu;
import com.example.emre.hastanerandevu.Class.Saat;
import com.example.emre.hastanerandevu.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DoktorActivity extends AppCompatActivity {

    private Spinner SpnrHastane,SpnrBolum;
    private EditText TxtAd,TxtSoyad,TxtTc,TxtSifre;
    public Button BtnEkle;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference myRef = db.getReference();
    DatabaseReference oku;
    private ArrayAdapter<String> dataAdapterForHastane;
    private ArrayAdapter<String> dataAdapterForBolum;
    private String HastaneId="",BolmuId="";
    private ArrayList<String> Hastaneler = new ArrayList<>();
    private List<Hastane> Hastanelist = new ArrayList<>();
    private ArrayList<String> Bolumler = new ArrayList<>();
    private List<Bolum> Bolumlist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doktor);
        init();
        HastaneGetir();
        ValidasyonKontrol();
    }
    private void init()
    {
        SpnrHastane=findViewById(R.id.SpnrHastaneler);
        SpnrBolum=findViewById(R.id.SpnrBolumler);
        TxtAd=findViewById(R.id.txtDoktorAd);
        TxtSoyad=findViewById(R.id.txtDoktorSoyad);
        TxtTc=findViewById(R.id.txtDoktorTc);
        TxtSifre=findViewById(R.id.txtDoktorSifre);
        BtnEkle=findViewById(R.id.BtnDoktorEkle);
    }
    public void HastaneGetir()
    {
        Hastaneler.clear();

        oku= FirebaseDatabase.getInstance().getReference().child("Hastaneler");
        ValueEventListener data = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot post : dataSnapshot.getChildren())
                {
                    Hastane Hastane2=post.getValue(Hastane.class);
                    Hastane2.id=post.getKey().toString();
                    Hastaneler.add(Hastane2.HastaneAdi);
                    Hastanelist.add(Hastane2);

                }
                dataAdapterForHastane = new ArrayAdapter<String>(DoktorActivity.this, android.R.layout.simple_spinner_item, Hastaneler);
                dataAdapterForHastane.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpnrHastane.setAdapter(dataAdapterForHastane);
                SpnrHastane.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        HastaneId=Hastanelist.get(i).id;
                        BolumGetir();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                oku.removeEventListener(this);
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DoktorActivity.this, "Hatalı Giriş Yaptınız", Toast.LENGTH_SHORT).show();
            }

        };
        oku.addValueEventListener(data);
    }
    public void BolumGetir()
    {
        SpnrBolum.setAdapter(null);
        Bolumler.clear();
        Bolumlist.clear();
        oku= FirebaseDatabase.getInstance().getReference().child("Hastaneler").child(HastaneId).child("Bolumler");
        ValueEventListener data = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot post : dataSnapshot.getChildren())
                {
                    Bolum Bolum2=post.getValue(Bolum.class);
                    Bolum2.id=post.getKey().toString();
                    Bolumler.add(Bolum2.BolumAdi);
                    Bolumlist.add(Bolum2);

                }
                dataAdapterForBolum = new ArrayAdapter<String>(DoktorActivity.this, android.R.layout.simple_spinner_item, Bolumler);
                dataAdapterForBolum.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpnrBolum.setAdapter(dataAdapterForBolum);
                SpnrBolum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        BolmuId="";
                        BolmuId=Bolumlist.get(i).id;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                oku.removeEventListener(this);
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DoktorActivity.this, "Hatalı Giriş Yaptınız", Toast.LENGTH_SHORT).show();
            }

        };
        oku.addValueEventListener(data);
    }

    public void ValidasyonKontrol()
    {BtnEkle.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (TxtTc.getText().toString().length() < 11 || TxtSifre.getText().toString().length() < 6 ||
                    TxtAd.getText().toString().length() < 2 || TxtAd.getText().toString().trim().equals("") ||
                    TxtSoyad.getText().toString().trim().equals("")) {
                Toast.makeText(DoktorActivity.this, "Hata! Girilen değerleri kontrol ediniz", Toast.LENGTH_SHORT).show();
            } else {
                DoktorEkle();
            }
        }
    });
    }
    private void DoktorEkle()
    {
        DoktorEkleDB(TxtAd.getText().toString(),TxtSoyad.getText().toString(),TxtTc.getText().toString(),TxtSifre.getText().toString());
        Toast.makeText(DoktorActivity.this, "Doktor Kaydı Gerçekleştirilmiştir.", Toast.LENGTH_SHORT).show();
        Intent i=new Intent(DoktorActivity.this,AdminActivity.class);
        startActivity(i);

    }
    private void DoktorEkleDB(String Ad,String Soyad,String Tc,String Sifre)
    {
        Kullanıcı d=new Kullanıcı();
        d.ad=Ad;
        d.soyad=Soyad;
        d.tc=Tc;
        d.sifre=Sifre;
        d.rol="doktor";
        String uniqueID = UUID.randomUUID().toString();
        myRef.child("Hastaneler").child(HastaneId).child("Bolumler").child(BolmuId).child("Doktorlar").child(uniqueID).setValue(d);
        myRef.child("Kullanıcılar").child(Tc).setValue(d);
    }
}
