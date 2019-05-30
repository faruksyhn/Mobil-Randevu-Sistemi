package com.example.emre.hastanerandevu.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.Switch;
import android.widget.Toast;

import com.example.emre.hastanerandevu.Class.Bolum;
import com.example.emre.hastanerandevu.Class.Hastane;
import com.example.emre.hastanerandevu.Class.Kullanıcı;
import com.example.emre.hastanerandevu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HastaneActivity extends AppCompatActivity {

    private RadioGroup Rdgrup;
    private Button BtnKaydet,BtnGuncelle,BtnSil;
    private EditText TxtHastaneAdi,TxtAdres;
    private Spinner SpnrHastane2,SpnrSehirler;
    private RadioButton RdHastaneEkle,RdHastaneGuncelleSil;
    FirebaseDatabase db= FirebaseDatabase.getInstance();
    DatabaseReference myRef=db.getReference();
    DatabaseReference oku,oku2;
    private ArrayList<String> Hastaneler = new ArrayList<>();
    private List<Hastane> Hastanelist = new ArrayList<>();
    private ArrayAdapter<String> dataAdapterForIller;
    private ArrayAdapter<String> dataAdapterForHastane;
    private String HastaneId="";

    private String[] iller={"Adana", "Adıyaman", "Afyon", "Ağrı", "Amasya", "Ankara", "Antalya", "Artvin",
            "Aydın", "Balıkesir", "Bilecik", "Bingöl", "Bitlis", "Bolu", "Burdur", "Bursa", "Çanakkale",
            "Çankırı", "Çorum", "Denizli", "Diyarbakır", "Edirne", "Elazığ", "Erzincan", "Erzurum", "Eskişehir",
            "Gaziantep", "Giresun", "Gümüşhane", "Hakkari", "Hatay", "Isparta", "Mersin", "İstanbul", "İzmir",
            "Kars", "Kastamonu", "Kayseri", "Kırklareli", "Kırşehir", "Kocaeli", "Konya", "Kütahya", "Malatya",
            "Manisa", "Kahramanmaraş", "Mardin", "Muğla", "Muş", "Nevşehir", "Niğde", "Ordu", "Rize", "Sakarya",
            "Samsun", "Siirt", "Sinop", "Sivas", "Tekirdağ", "Tokat", "Trabzon", "Tunceli", "Şanlıurfa", "Uşak",
            "Van", "Yozgat", "Zonguldak", "Aksaray", "Bayburt", "Karaman", "Kırıkkale", "Batman", "Şırnak",
            "Bartın", "Ardahan", "Iğdır", "Yalova", "Karabük", "Kilis", "Osmaniye", "Düzce"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hastane);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Hastane Ekle");
        Arrays.sort(iller);
        HastaneId="";
        init();
OmEkle();

    }

    public void init()
    {
        dataAdapterForIller = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, iller);
        dataAdapterForIller.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpnrSehirler=findViewById(R.id.SpnrSehirler);
        SpnrSehirler.setAdapter(dataAdapterForIller);
        SpnrHastane2=new Spinner(this);
        SpnrHastane2=findViewById(R.id.SpnrHastane2);
        BtnKaydet=findViewById(R.id.BtnHastaneKaydet);
        BtnGuncelle=findViewById(R.id.BtnHastaneGuncelle);
        BtnSil=findViewById(R.id.BtnHastaneSil);
        TxtAdres=findViewById(R.id.HastaneAdres);
        TxtHastaneAdi=findViewById(R.id.HastaneAdi);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_hastane,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId())
       {
           case R.id.OhEkle:
                   { OmEkle();break;}
           case R.id.OhGuncelle:
           {OmGuncelle();break;}
           case R.id.OhSil:{
               OmSil();break;
             }
       }
       return true;
    }
public void OmEkle(){SpnrHastane2.setVisibility(View.INVISIBLE);
    SpnrSehirler.setVisibility(View.VISIBLE);
    TxtAdres.setVisibility(View.VISIBLE);
    TxtHastaneAdi.setHint("Hastane Adını giriniz...");
    BtnGuncelle.setVisibility(View.INVISIBLE);
    BtnSil.setVisibility(View.INVISIBLE);
    BtnKaydet.setVisibility(View.VISIBLE);
    HastaneKaydet();}
public void OmGuncelle(){ Hastanegetir();
    HastaneGuncelle();
    TxtHastaneAdi.setHint("Yeni Hastane Adını giriniz...");
    SpnrHastane2.setVisibility(View.VISIBLE);
    TxtAdres.setVisibility(View.INVISIBLE);
    SpnrSehirler.setVisibility(View.INVISIBLE);
    BtnGuncelle.setVisibility(View.VISIBLE);
    BtnSil.setVisibility(View.INVISIBLE);
    BtnKaydet.setVisibility(View.INVISIBLE);}
public void OmSil(){  Hastanegetir();
    HastaneSil();
    TxtHastaneAdi.setVisibility(View.INVISIBLE);
    SpnrHastane2.setVisibility(View.VISIBLE);
    TxtAdres.setVisibility(View.INVISIBLE);
    SpnrSehirler.setVisibility(View.INVISIBLE);
    BtnGuncelle.setVisibility(View.INVISIBLE);
    BtnSil.setVisibility(View.VISIBLE);
    BtnKaydet.setVisibility(View.INVISIBLE);}

    public void HastaneGuncelle()
    {
        BtnGuncelle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                oku= FirebaseDatabase.getInstance().getReference().child("Hastaneler").child(HastaneId);
                ValueEventListener data = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DatabaseReference userRef = myRef.child("Hastaneler").child(HastaneId);
                        Map newUserData = new HashMap();
                        newUserData.put("HastaneAdi",TxtHastaneAdi.getText().toString());
                        userRef.updateChildren(newUserData);

                        Hastanelist.clear();
                        oku.removeEventListener(this);Toast.makeText(HastaneActivity.this, " Hastane Güncellendi", Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(HastaneActivity.this,AdminActivity.class);
                        startActivity(i);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(HastaneActivity.this, "Hatalı Giriş Yaptınız", Toast.LENGTH_SHORT).show();
                    }

                };

                oku.addValueEventListener(data);
            }

        });HastaneId="";

    }

    private void HastaneSil()
    {
        BtnSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(HastaneActivity.this);
                builder.setTitle("              UYARI!");
                builder.setMessage("Hastane içerisindeki bölüm ve doktorlar da silinecek!\nSilmek istediğinize emin misiniz?");
                builder.setNegativeButton("İPTAL", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });


                builder.setPositiveButton("SİL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DatabaseReference Sil=FirebaseDatabase.getInstance().getReference("Hastaneler").child(HastaneId);
                        Sil.removeValue();
                        Toast.makeText(HastaneActivity.this, ""+SpnrHastane2.getSelectedItem().toString()+" Hastanesi Silindi", Toast.LENGTH_SHORT).show();
                        Intent i =new Intent(HastaneActivity.this,AdminActivity.class);
                        startActivity(i);

                    }
                });
                builder.show();
            }
        });
    }

    public void HastaneKaydet()
    {
        BtnKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TxtHastaneAdi.length()<2||TxtHastaneAdi.length()>50)
                {
                    Toast.makeText(HastaneActivity.this, "Geçersiz Hastane adı", Toast.LENGTH_SHORT).show();
                }
                else {
                    HastaneKaydetDB(TxtHastaneAdi.getText().toString(),SpnrSehirler.getSelectedItem().toString(),TxtAdres.getText().toString());
                    Intent i =new Intent(HastaneActivity.this,AdminActivity.class);
                    startActivity(i);
                }
            }
        });
    }
    private void HastaneKaydetDB(String HastaneAdi,String Sehir,String Adres)
    {
        Hastane h = new Hastane();
        h.HastaneAdi=HastaneAdi;
        h.Sehir=Sehir;
        h.Adres=Adres;
        String uniqueID = UUID.randomUUID().toString();
        myRef.child("Hastaneler").child(uniqueID).setValue(h);
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
                dataAdapterForHastane = new ArrayAdapter<String>(HastaneActivity.this, android.R.layout.simple_spinner_item, Hastaneler);
                dataAdapterForHastane.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpnrHastane2.setAdapter(dataAdapterForHastane);
                SpnrHastane2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        HastaneId="";
                        HastaneId=Hastanelist.get(i).id;

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                oku.removeEventListener(this);
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HastaneActivity.this, "Hatalı Giriş Yaptınız", Toast.LENGTH_SHORT).show();
            }

        };
        oku.addValueEventListener(data);
    }
}
