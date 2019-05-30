package com.example.emre.hastanerandevu.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.emre.hastanerandevu.Class.Bolum;
import com.example.emre.hastanerandevu.Class.Doktor;
import com.example.emre.hastanerandevu.Class.Hastane;
import com.example.emre.hastanerandevu.Class.Randevu;
import com.example.emre.hastanerandevu.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RandevuSilActivity extends AppCompatActivity {

    private String Tarih;
    private Spinner SpnrHastane,SpnrBolum,SpnrDoktor;
    private ArrayList<String> Randevular=new ArrayList<>();
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference myRef = db.getReference();
    DatabaseReference oku;
    private ArrayAdapter<String> dataAdapterForHastane;
    private ArrayAdapter<String> dataAdapterForBolum;
    private ArrayAdapter<String> dataAdapterForDoktor;
    private static String HastaneId="",BolmuId="",DoktorId="";
    private ArrayList<String> Hastaneler = new ArrayList<>();
    private List<Hastane> Hastanelist = new ArrayList<>();
    private ArrayList<String> Bolumler = new ArrayList<>();
    private List<Bolum> Bolumlist = new ArrayList<>();
    private ArrayList<String> Doktorlar = new ArrayList<>();
    private List<Doktor> Doktorlist = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private static ArrayList<String> RandevuID=new ArrayList<>();
    private static ListView RandevuListView;
    Date SimdikiTarih = Calendar.getInstance().getTime();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_randevu_sil);
        init();
        HastaneGetir();
        ListviewClick();
    }

    public void init()
    {
        SpnrHastane=findViewById(R.id.SpnrHastanelerAdmin);
        SpnrBolum=findViewById(R.id.SpnrBolumlerAdmin);
        SpnrDoktor=findViewById(R.id.SpnrDoktorlarAdmin);
        RandevuListView=findViewById(R.id.listWievAdminRandevu);
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
                    oku.removeEventListener(this);
                }
                dataAdapterForHastane = new ArrayAdapter<String>(RandevuSilActivity.this, android.R.layout.simple_spinner_item, Hastaneler);
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
                Toast.makeText(RandevuSilActivity.this, "Hatalı Giriş Yaptınız", Toast.LENGTH_SHORT).show();
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
                    oku.removeEventListener(this);
                }
                dataAdapterForBolum = new ArrayAdapter<String>(RandevuSilActivity.this, android.R.layout.simple_spinner_item, Bolumler);
                dataAdapterForBolum.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpnrBolum.setAdapter(dataAdapterForBolum);
                SpnrBolum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        BolmuId="";
                        BolmuId=Bolumlist.get(i).id;
                        DoktorGetir();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                oku.removeEventListener(this);
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(RandevuSilActivity.this, "Hatalı Giriş Yaptınız", Toast.LENGTH_SHORT).show();
            }

        };
        oku.addValueEventListener(data);
    }
    public void DoktorGetir()
    {
        SpnrDoktor.setAdapter(null);
        Doktorlar.clear();
        Doktorlist.clear();
        oku= FirebaseDatabase.getInstance().getReference().child("Hastaneler").child(HastaneId).child("Bolumler").child(BolmuId).child("Doktorlar");
        ValueEventListener data = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot post : dataSnapshot.getChildren())
                {
                    Doktor Doktor2=post.getValue(Doktor.class);
                    Doktor2.id=post.getKey().toString();
                    Doktorlar.add(Doktor2.ad);
                    Doktorlist.add(Doktor2);
                    oku.removeEventListener(this);
                }
                dataAdapterForDoktor = new ArrayAdapter<String>(RandevuSilActivity.this, android.R.layout.simple_spinner_item, Doktorlar);
                dataAdapterForDoktor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpnrDoktor.setAdapter(dataAdapterForDoktor);
                SpnrDoktor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        DoktorId="";
                        DoktorId=Doktorlist.get(i).id;
                        Randevular.clear();
                        RandevuListView.deferNotifyDataSetChanged();
                        RandevulariGetir();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(RandevuSilActivity.this, "Hatalı Giriş Yaptınız", Toast.LENGTH_SHORT).show();
            }

        };
        oku.addValueEventListener(data);
    }

    public void RandevulariGetir()
    {
        final int Ay=SimdikiTarih.getMonth()+1;
        oku=FirebaseDatabase.getInstance().getReference().child("Randevular");
        ValueEventListener data=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot post:dataSnapshot.getChildren())
                {
                    final Randevu r=post.getValue(Randevu.class);
                    RandevuListView.setAdapter(arrayAdapter);
                    if (r.tarih.length()==8) Tarih="0"+r.tarih;
                    else Tarih=r.tarih;
                    if (r.doktorid.equalsIgnoreCase(DoktorId) && !r.hastaid.equalsIgnoreCase("11111111111"))
                    {
                        if (Integer.parseInt(Tarih.substring(0,2))>SimdikiTarih.getDate() && Integer.parseInt(Tarih.substring(3,4))==Ay)
                        {
                            Randevular.add("Doktor Adı: "+r.doktoradi+"\nRandevu Tarihi: "+r.tarih+"\nRandevu Saati: "+r.saat);
                            RandevuID.add(post.getKey());
                        }
                        else if (Integer.parseInt(Tarih.substring(3,4))>Ay)
                        {
                            Randevular.add("Doktor Adı: "+r.doktoradi+"\nRandevu Tarihi: "+r.tarih+"\nRandevu Saati: "+r.saat);
                            RandevuID.add(post.getKey());
                        }
                        else if(Integer.parseInt(Tarih.substring(3,4))==Ay && Integer.parseInt(Tarih.substring(0,2))==SimdikiTarih.getDate() && Integer.parseInt(r.saat.substring(0,2))*10>SimdikiTarih.getHours()*10)
                        {
                            Randevular.add("Doktor Adı: "+r.doktoradi+"\nRandevu Tarihi: "+r.tarih+"\nRandevu Saati: "+r.saat);
                            RandevuID.add(post.getKey());
                        }
                    }
                }
                arrayAdapter=new ArrayAdapter(RandevuSilActivity.this,android.R.layout.simple_list_item_1,Randevular);
                RandevuListView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };oku.addValueEventListener(data);
    }

    public void ListviewClick()
    {
        RandevuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                oku=FirebaseDatabase.getInstance().getReference().child("Randevular").child(RandevuID.get(i));
                ValueEventListener data=new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Randevu r=dataSnapshot.getValue(Randevu.class);
                        AlertDialog.Builder builder = new AlertDialog.Builder(RandevuSilActivity.this);
                        builder.setTitle("             Randevu Bilgileri");
                        builder.setMessage("Hastane Adı:"+r.hastaneadi+"\n\nBölüm:"+r.bolumadi+"\n\nDoktor Adı: "+r.doktoradi+"\n\nRandevu Tarihi: "+r.tarih+"\n\nRandevu Saati: "+r.saat);
                        builder.setNegativeButton("İPTAL ET", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id) {
                                Randevular.clear();
                                RandevuListView.deferNotifyDataSetChanged();
                                DatabaseReference Sil=FirebaseDatabase.getInstance().getReference().child("Randevular").child(RandevuID.get(i));
                                Sil.removeValue();
                            }
                        });
                        builder.show();
                        oku.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };oku.addValueEventListener(data);
            }
        });
    }
}
