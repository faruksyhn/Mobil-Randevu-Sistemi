package com.example.emre.hastanerandevu.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.emre.hastanerandevu.Class.Favori;
import com.example.emre.hastanerandevu.Class.Randevu;
import com.example.emre.hastanerandevu.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavorilerActivity extends AppCompatActivity {

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference myRef = db.getReference();
    DatabaseReference oku;
    private GirisActivity Giren;
    private ArrayList<Favori> Favorilerim=new ArrayList<>();
    private ArrayList<String> Favorilerilist=new ArrayList<>();
    private ListView Favorilerlistview;
    private ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoriler);
        Favorilerilist.clear();
        Favorilerim.clear();
        init();
        FavorileriListele();
        BilgiGetir();
    }

    private void init() {
        Favorilerlistview=findViewById(R.id.FavorilerListView);
    }

    public void FavorileriListele()
    {
        Favorilerlistview.deferNotifyDataSetChanged();
        oku=FirebaseDatabase.getInstance().getReference().child("Favoriler");
        ValueEventListener data =new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot post:dataSnapshot.getChildren())
                {
                    Favori f=post.getValue(Favori.class);
                    f.favoriid=post.getKey();
                    if (f.hastaid.equalsIgnoreCase(Giren.GirenKullanici))
                    {
                        Favorilerilist.add(f.doktoradi);
                        Favorilerim.add(f);
                        arrayAdapter=new ArrayAdapter(FavorilerActivity.this,android.R.layout.simple_list_item_1,Favorilerilist);
                        Favorilerlistview.setAdapter(arrayAdapter);
                    }
                    //oku.removeEventListener(this);
                }
                if (Favorilerim.size()==0)
                {
                    Toast.makeText(FavorilerActivity.this, "Favori Doktorunuz Bulunmamaktadır", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FavorilerActivity.this,KullaniciActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        Favorilerlistview.deferNotifyDataSetChanged();
        oku.addValueEventListener(data);
    }

    public void BilgiGetir(){
        Favorilerlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                Toast.makeText(FavorilerActivity.this, ""+Favorilerim.size(), Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(FavorilerActivity.this);
                builder.setTitle("              Doktor Bilgileri");
                builder.setMessage("Hastane Adi: "+Favorilerim.get(i).hastaneadi+"\n\nBolüm Adı: "+Favorilerim.get(i).bolumadi+"\n\nDoktor Adı: "+Favorilerim.get(i).doktoradi);
                builder.setNegativeButton("FAVORİLERDEN KALDIR", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {
                        oku=FirebaseDatabase.getInstance().getReference("Favoriler");
                        ValueEventListener data=new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot post:dataSnapshot.getChildren())
                                {
                                    Favori f=post.getValue(Favori.class);
                                    f.favoriid=post.getKey();
                                    if (f.doktorid.equalsIgnoreCase(Favorilerim.get(i).doktorid) && f.hastaid.equalsIgnoreCase(Favorilerim.get(i).hastaid))
                                    {
                                        oku=FirebaseDatabase.getInstance().getReference("Favoriler").child(f.favoriid);
                                        oku.removeValue();
                                        /*Intent intent = new Intent(FavorilerActivity.this,KullaniciActivity.class);
                                        startActivity(intent);*/
                                    }
                                   oku.removeEventListener(this);
                                }
                                Favorilerilist.clear();
                                Favorilerim.clear();
                                Favorilerlistview.deferNotifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };oku.addValueEventListener(data);
                    }
                });
                builder.show();
            }
        });
    }
}
