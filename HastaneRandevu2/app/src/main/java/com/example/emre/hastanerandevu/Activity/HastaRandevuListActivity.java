package com.example.emre.hastanerandevu.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emre.hastanerandevu.Class.Favori;
import com.example.emre.hastanerandevu.Class.Hastane;
import com.example.emre.hastanerandevu.Class.Kullanıcı;
import com.example.emre.hastanerandevu.Class.Randevu;
import com.example.emre.hastanerandevu.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class HastaRandevuListActivity extends AppCompatActivity {

    private String Tarih;
    public static boolean FavorideMi=false;
    private Button BtnGuncelRandevular,BtnGecmisRandevular;
    public TextView TarihiGecmis,TarihiGecmemis;
    Date SimdikiTarih = Calendar.getInstance().getTime();
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference myRef = db.getReference();
    DatabaseReference oku;
    private static ListView RandevuListView,RandevuListView2;
    private ArrayList<String> Randevular=new ArrayList<>();
    private ArrayList<String> Randevular2=new ArrayList<>();
    private static ArrayList<String> RandevuID=new ArrayList<>();
    private static ArrayList<Randevu> DoktorID=new ArrayList<>();
    private static ArrayList<String> RandevuID2=new ArrayList<>();
    private GirisActivity Giren;
    private String RandevuAlinanDoktor;
    private ArrayAdapter<String> arrayAdapter,arrayAdapter2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasta_randevu_list);
        init();
        Favori fav=new Favori();
        fav.doktorid="1";
        fav.hastaid="1";
        myRef.child("Favoriler").child("1").setValue(fav);
        RandevuListView2.setVisibility(View.INVISIBLE);
        BtnGuncelRandevular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RandevuListView.setVisibility(View.VISIBLE);
                RandevuListView2.setVisibility(View.INVISIBLE);
            }
        });
        BtnGecmisRandevular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RandevuListView.setVisibility(View.INVISIBLE);
                RandevuListView2.setVisibility(View.VISIBLE);
            }
        });
        GecmemisRandevuGetir();
        GecmisRandevuGetir();
        RandevuSil();
        FavorilereEkle();
    }

    private void init()
    {
       RandevuListView = findViewById(R.id.HastaRandevuListView);
       RandevuListView2 = findViewById(R.id.HastaRandevuListView2);
       BtnGuncelRandevular=findViewById(R.id.BtnGuncelRandevular);
       BtnGecmisRandevular=findViewById(R.id.BtnGecmisRandevular);
    }

    private void GecmemisRandevuGetir()
    {
        final int Ay=SimdikiTarih.getMonth()+1;
        oku=FirebaseDatabase.getInstance().getReference().child("Randevular");
        ValueEventListener data=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot post:dataSnapshot.getChildren()){
                    final Randevu r=post.getValue(Randevu.class);
                    RandevuListView.setAdapter(arrayAdapter);
                    if (r.tarih.length()==8) Tarih="0"+r.tarih;
                    else Tarih=r.tarih;
                    if(r.hastaid.equalsIgnoreCase(Giren.GirenKullanici))
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
                    arrayAdapter=new ArrayAdapter(HastaRandevuListActivity.this,android.R.layout.simple_list_item_1,Randevular);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        oku.addValueEventListener(data);
    }

    public void GecmisRandevuGetir()
    {
        final int Ay=SimdikiTarih.getMonth()+1;
        oku=FirebaseDatabase.getInstance().getReference().child("Randevular");
        ValueEventListener data=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot post:dataSnapshot.getChildren()){
                    Randevu r=post.getValue(Randevu.class);
                    if (r.tarih.length()==8) Tarih="0"+r.tarih;
                    else Tarih=r.tarih;
                    if(r.hastaid.equalsIgnoreCase(Giren.GirenKullanici))
                    {
                        if (Integer.parseInt(Tarih.substring(0,2))<SimdikiTarih.getDate() && Integer.parseInt(Tarih.substring(3,4))==Ay)
                        {
                            Randevular2.add("Doktor Adı: "+r.doktoradi+"\nRandevu Tarihi: "+r.tarih+"\nRandevu Saati: "+r.saat);
                            DoktorID.add(r);
                            RandevuID2.add(r.randevuid);
                        }
                        else if(Integer.parseInt(Tarih.substring(3,4))<Ay)
                        {
                            Randevular2.add("Doktor Adı: "+r.doktoradi+"\nRandevu Tarihi: "+r.tarih+"\nRandevu Saati: "+r.saat);
                            DoktorID.add(r);
                            RandevuID2.add(r.randevuid);
                        }
                        else if(Integer.parseInt(Tarih.substring(3,4))==Ay && Integer.parseInt(Tarih.substring(0,2))==SimdikiTarih.getDate() && Integer.parseInt(r.saat.substring(0,2))*10<SimdikiTarih.getHours()*10)
                        {
                            Randevular2.add("Doktor Adı: "+r.doktoradi+"\nRandevu Tarihi: "+r.tarih+"\nRandevu Saati: "+r.saat);
                            DoktorID.add(r);
                            RandevuID2.add(r.randevuid);
                        }
                    }
                    arrayAdapter2=new ArrayAdapter(HastaRandevuListActivity.this,android.R.layout.simple_list_item_1,Randevular2);
                    RandevuListView2.setAdapter(arrayAdapter2);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        oku.addValueEventListener(data);
    }

    public void RandevuSil()
    {
        RandevuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                oku=FirebaseDatabase.getInstance().getReference().child("Randevular").child(RandevuID.get(i));
                ValueEventListener data=new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Randevu r=dataSnapshot.getValue(Randevu.class);
                        AlertDialog.Builder builder = new AlertDialog.Builder(HastaRandevuListActivity.this);
                        builder.setTitle("             Randevu Bilgileri");
                        builder.setMessage("Hastane Adı:"+r.hastaneadi+"\n\nBölüm:"+r.bolumadi+"\n\nDoktor Adı: "+r.doktoradi+"\n\nRandevu Tarihi: "+r.tarih+"\n\nRandevu Saati: "+r.saat);
                        builder.setNegativeButton("İPTAL ET", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id) {
                                Randevular.clear();
                                RandevuListView.deferNotifyDataSetChanged();
                                Randevular2.clear();
                                RandevuListView2.deferNotifyDataSetChanged();
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
                };
                oku.addValueEventListener(data);
            }
        });
    }

    public void FavorilereEkle()
    {
        RandevuListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                oku=FirebaseDatabase.getInstance().getReference().child("Favoriler");
                ValueEventListener data=new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot post:dataSnapshot.getChildren())
                        {
                            Favori f=post.getValue(Favori.class);
                            f.favoriid=post.getKey().toString();
                            if(f.doktorid.equalsIgnoreCase(DoktorID.get(i).doktorid) && f.hastaid.equalsIgnoreCase(Giren.GirenKullanici))
                            {
                                FavorideMi=true;
                                AlertDialog.Builder builder = new AlertDialog.Builder(HastaRandevuListActivity.this);
                                builder.setMessage("Doktor Favorilerde");
                                builder.setNegativeButton("Favorilerime git", new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(HastaRandevuListActivity.this,FavorilerActivity.class);
                                        startActivity(intent);
                                    }
                                });


                                builder.show();
                                break;
                            }
                            else FavorideMi=false;
                            oku.removeEventListener(this);

                        }
                        if (FavorideMi==false)
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(HastaRandevuListActivity.this);
                            builder.setMessage("Doktor Favorilere Eklensin Mi?");
                            builder.setNegativeButton("HAYIR", new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });


                            builder.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Favori fav=new Favori();
                                    fav.doktorid=DoktorID.get(i).doktorid;
                                    fav.hastaid=Giren.GirenKullanici;
                                    fav.hastaneadi=DoktorID.get(i).hastaneadi;
                                    fav.bolumadi=DoktorID.get(i).bolumadi;
                                    fav.doktoradi = DoktorID.get(i).doktoradi;
                                    String uniqueID = UUID.randomUUID().toString();
                                    myRef.child("Favoriler").child(uniqueID).setValue(fav);

                                }
                            });


                            builder.show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                };
                oku.addValueEventListener(data);
            }
        });
























       /* RandevuListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HastaRandevuListActivity.this);
                builder.setMessage("Doktor Favorilere Eklensin Mi?");
                builder.setNegativeButton("HAYIR", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

                builder.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Favori fav=new Favori();
                        fav.doktorid=DoktorID.get(i);
                        fav.hastaid=Giren.GirenKullanici;
                        String uniqueID = UUID.randomUUID().toString();
                        myRef.child("Favoriler").child(uniqueID).setValue(fav);
                    }
                });
                builder.show();
            }
        });*/
    }
}

