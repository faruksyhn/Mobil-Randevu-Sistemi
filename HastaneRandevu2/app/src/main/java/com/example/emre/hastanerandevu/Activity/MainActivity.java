package com.example.emre.hastanerandevu.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emre.hastanerandevu.Class.Hastane;
import com.example.emre.hastanerandevu.Class.Kullanıcı;
import com.example.emre.hastanerandevu.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private Button btnKaydol;
    private  EditText tc,sifre,ad,soyad;
    private TextView giris;
    private Context context=this;
    private Boolean VarMi=false;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference myRef = db.getReference();
    DatabaseReference oku;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        KayıtKontrol();
        GiriseYonlendir();
    }

    public void init() {
        btnKaydol=findViewById(R.id.btnKayıtOnay);
        tc=findViewById(R.id.kytTcNo);
        sifre=findViewById(R.id.kytPassword);
        ad=findViewById(R.id.kytAd);
        soyad=findViewById(R.id.kytSoyad);
        giris=findViewById(R.id.txtGiris);
    }

    public void KayıtKontrol()
    {
        btnKaydol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!tc.getText().toString().trim().equals("") || !sifre.getText().toString().trim().equals(""))
                {
                    oku= FirebaseDatabase.getInstance().getReference().child("Kullanıcılar");
                    ValueEventListener data=new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot post:dataSnapshot.getChildren())
                            {
                                Kullanıcı k= post.getValue(Kullanıcı.class);
                                if (VarMi==false && k.tc.equalsIgnoreCase(tc.getText().toString()))
                                {
                                    Toast.makeText(context, "Bu Tc kimlik numarası kullanılmaktadır.", Toast.LENGTH_SHORT).show();
                                    VarMi=true;
                                }
                                oku.removeEventListener(this);
                            }
                            if (VarMi==false)
                            {
                                ValidasyonKontrol();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };
                    oku.addValueEventListener(data);
                }
                else
                    Toast.makeText(context, "Tc veya şifre boş geçilemez", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ValidasyonKontrol()
    {
        if(tc.getText().toString().length()<11 || sifre.getText().toString().length()<6 ||
                ad.getText().toString().length()<2 || ad.getText().toString().trim().equals("") ||
                soyad.getText().toString().trim().equals(""))
        {
            Toast.makeText(context, "Hata! Girilen değerleri kontrol ediniz", Toast.LENGTH_SHORT).show();
        }
        else
        {KayıtOl();}
    }

    public void KayıtOl()
    {
        KayıtOlDB(tc.getText().toString(),sifre.getText().toString(),ad.getText().toString(),soyad.getText().toString());
        Toast.makeText(context, "Kaydınız Gerçekleştirilmiştir.", Toast.LENGTH_SHORT).show();
        Intent i=new Intent(MainActivity.this,GirisActivity.class);
        startActivity(i);
    }

    private void KayıtOlDB( String tc, String sifre,String ad,String soyad) {
        Kullanıcı kullanıcı= new Kullanıcı();
        kullanıcı.tc=tc;
        kullanıcı.sifre=sifre;
        kullanıcı.ad=ad;
        kullanıcı.soyad=soyad;
        kullanıcı.rol="kullanıcı";
        myRef.child("Kullanıcılar").child(tc).setValue(kullanıcı);

    }

    public void GiriseYonlendir()
    {
        giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,GirisActivity.class);
                startActivity(i);
            }
        });
    }






 /*   public void Oku() {
        btnOku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValueEventListener data = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Kullanıcı k = new Kullanıcı();
                        k = dataSnapshot.getValue(Kullanıcı.class);
                        Toast.makeText(MainActivity.this, "" + k.username, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                oku.addValueEventListener(data);
            }
        });
    }
*/

}


