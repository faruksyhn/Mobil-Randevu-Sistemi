package com.example.emre.hastanerandevu.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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

public class DoktorRandevulariActivity extends AppCompatActivity {

    private String Tarih;
    private ListView RandevuListView;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference myRef = db.getReference();
    DatabaseReference oku;
    private ArrayAdapter<String> arrayAdapter;
    private static ArrayList<String> RandevuID=new ArrayList<>();
    private GirisActivity Giren;
    private ArrayList<String> Randevular=new ArrayList<>();
    Date SimdikiTarih = Calendar.getInstance().getTime();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doktor_randevulari);
        init();
        GecmemisRandevuGetir();
        ListviewClick();
    }

    public void init()
    {
        RandevuListView=findViewById(R.id.DoktorRandevular);
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
                    if(r.doktortc.equalsIgnoreCase(Giren.GirenKullanici)&&!r.hastaid.equalsIgnoreCase("11111111111") )
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
                    arrayAdapter=new ArrayAdapter(DoktorRandevulariActivity.this,android.R.layout.simple_list_item_1,Randevular);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        oku.addValueEventListener(data);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(DoktorRandevulariActivity.this);
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
