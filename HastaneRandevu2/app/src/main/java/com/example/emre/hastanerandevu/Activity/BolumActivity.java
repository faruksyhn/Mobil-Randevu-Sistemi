package com.example.emre.hastanerandevu.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emre.hastanerandevu.Class.Bolum;
import com.example.emre.hastanerandevu.Class.Hastane;
import com.example.emre.hastanerandevu.Class.Kullanıcı;
import com.example.emre.hastanerandevu.Class.Saat;
import com.example.emre.hastanerandevu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BolumActivity extends AppCompatActivity {
    private Spinner SpnrHastaneAdi,SpnrBolumler;
    private ArrayList<String> Hastaneler = new ArrayList<>();
    private List<Hastane> Hastanelist = new ArrayList<>();
    private ArrayList<String> Bolumler = new ArrayList<>();
    private List<Bolum> Bolumlist = new ArrayList<>();
    private EditText TxtBolumAdi;
    private TextView TxtHgiris,TxtBgiris;
    public Button BtnBolumEkle,BtnGuncelle,BtnSil;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference myRef = db.getReference();
    DatabaseReference oku;
    private ArrayAdapter<String> dataAdapterForHastane,dataAdapterForBolum;
    private String HastaneId="",BolmuId="";
    private RadioButton RdBolumEkle,RdBolumGuncelleSil;
    private RadioGroup Rdgrup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bolum);
        HastaneId="";
        BolmuId="";
        init();
        OmEkle();
        Hastanegetir();

    }
    public void init()
    {

        BtnBolumEkle=findViewById(R.id.BolumEkle);
        TxtBolumAdi=findViewById(R.id.BolumAdi);
        SpnrHastaneAdi=new Spinner(this);
        SpnrHastaneAdi=findViewById(R.id.SpnrHastaneler);
        SpnrBolumler=findViewById(R.id.SpnrBolumler);

        TxtHgiris=findViewById(R.id.Hastaneadinigir);
        TxtBgiris=findViewById(R.id.Bolumadinigir);
        BtnGuncelle=findViewById(R.id.BtnBolumGuncelle);
        BtnSil=findViewById(R.id.BtnBolumSil);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bolum,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.ObEkle:{ OmEkle(); break;}
            case R.id.ObGuncelle: {OmGuncelle();break;}
            case R.id.ObSil:{OmSil();break;}
        }
        return true;
    }

    public void OmEkle(){
        SpnrBolumler.setVisibility(View.INVISIBLE);
        SpnrHastaneAdi.setVisibility(View.VISIBLE);
        BtnGuncelle.setVisibility(View.INVISIBLE);
        BtnSil.setVisibility(View.INVISIBLE);
        BtnBolumEkle.setVisibility(View.VISIBLE);
        TxtBgiris.setVisibility(View.INVISIBLE);
        TxtBolumAdi.setText("");
        BolumKaydet();
    }
    public void OmGuncelle(){
        Hastanegetir();
        BolumGuncelle();
        BolumSil();
        HastaneId="";
        BolmuId="";
        TxtBgiris.setVisibility(View.VISIBLE);
        SpnrBolumler.setVisibility(View.VISIBLE);
        BtnGuncelle.setVisibility(View.VISIBLE);
        BtnSil.setVisibility(View.INVISIBLE);
        BtnBolumEkle.setVisibility(View.INVISIBLE);
        TxtBolumAdi.setVisibility(View.VISIBLE);
        TxtBolumAdi.setText("");
    }
    public void OmSil(){
        Hastanegetir();
        BolumGuncelle();
        BolumSil();
        HastaneId="";
        BolmuId="";
        TxtBgiris.setVisibility(View.VISIBLE);
        SpnrBolumler.setVisibility(View.VISIBLE);
        BtnGuncelle.setVisibility(View.INVISIBLE);
        BtnSil.setVisibility(View.VISIBLE);
        BtnBolumEkle.setVisibility(View.INVISIBLE);
        TxtBolumAdi.setVisibility(View.INVISIBLE);
    }


    public void BolumSil()
    {
        BtnSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference Sil=FirebaseDatabase.getInstance().getReference("Hastaneler").child(HastaneId).child("Bolumler").child(BolmuId);
                Sil.removeValue();
                Toast.makeText(BolumActivity.this, ""+SpnrBolumler.getSelectedItem().toString()+" Hastanesi Silindi", Toast.LENGTH_SHORT).show();
                Intent i =new Intent(BolumActivity.this,AdminActivity.class);
                startActivity(i);
            }
        });
    }

    public void BolumGuncelle(){
        SpnrBolumler.setAdapter(null);
        BtnGuncelle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                oku= FirebaseDatabase.getInstance().getReference().child("Hastaneler").child(HastaneId).child("Bolumler").child(BolmuId);
                ValueEventListener data = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DatabaseReference userRef = myRef.child("Hastaneler").child(HastaneId).child("Bolumler").child(BolmuId);
                        Map newUserData = new HashMap();
                        newUserData.put("BolumAdi",TxtBolumAdi.getText().toString());
                        userRef.updateChildren(newUserData);

                        Hastanelist.clear();
                        oku.removeEventListener(this);Toast.makeText(BolumActivity.this, " Bölüm Güncellendi", Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(BolumActivity.this,AdminActivity.class);
                        startActivity(i);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(BolumActivity.this, "Hatalı Giriş Yaptınız", Toast.LENGTH_SHORT).show();
                    }

                };

                oku.addValueEventListener(data);
            }

        });HastaneId="";
    }

    private void BolumKaydet() {
        BtnBolumEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BolumKaydetDB(TxtBolumAdi.getText().toString());
                Toast.makeText(BolumActivity.this, "Bolüm Eklendi.", Toast.LENGTH_SHORT).show();
                Intent i =new Intent(BolumActivity.this,AdminActivity.class);
                startActivity(i);
            }
        });
    }

    private void BolumKaydetDB(String BolumAdi) {
        Bolum b  = new Bolum();
        b.BolumAdi=BolumAdi;
        String uniqueID = UUID.randomUUID().toString();
        myRef.child("Hastaneler").child(HastaneId).child("Bolumler").child(uniqueID).setValue(b);
    }

    public void Hastanegetir()
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
                dataAdapterForHastane = new ArrayAdapter<String>(BolumActivity.this, android.R.layout.simple_spinner_item, Hastaneler);
                dataAdapterForHastane.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpnrHastaneAdi.setAdapter(dataAdapterForHastane);
                SpnrHastaneAdi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        HastaneId=Hastanelist.get(i).id;
                        Bolumgetir();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                oku.removeEventListener(this);
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(BolumActivity.this, "Hatalı Giriş Yaptınız", Toast.LENGTH_SHORT).show();
            }

        };
        oku.addValueEventListener(data);
    }

    public void Bolumgetir()
    {
        SpnrBolumler.setAdapter(null);
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
                dataAdapterForBolum = new ArrayAdapter<String>(BolumActivity.this, android.R.layout.simple_spinner_item, Bolumler);
                dataAdapterForBolum.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpnrBolumler.setAdapter(dataAdapterForBolum);
                SpnrBolumler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                Toast.makeText(BolumActivity.this, "Hatalı Giriş Yaptınız", Toast.LENGTH_SHORT).show();
            }

        };
        oku.addValueEventListener(data);
    }
}

