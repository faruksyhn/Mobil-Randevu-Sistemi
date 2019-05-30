package com.example.emre.hastanerandevu.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.emre.hastanerandevu.Class.Hastane;
import com.example.emre.hastanerandevu.Class.Kullanıcı;
import com.example.emre.hastanerandevu.Class.Randevu;
import com.example.emre.hastanerandevu.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GirisActivity extends AppCompatActivity {
    public static String GirenKullanici;
    private EditText tc, sifre;
    private Button girisYap;
    private Context context = this;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference myRef = db.getReference();
    DatabaseReference oku;
    private ArrayList<String> Kullanicilar = new ArrayList<>();
    private List<Kullanıcı> Kullanicilist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);

        init();
        if (tc.getText().toString()!="")
        {
            girisYap();
        }
        else Toast.makeText(context, "Hatalı kullanıcı adı veya şifre", Toast.LENGTH_SHORT).show();
       // KullaniciKontrol();
    }

    public void init() {
        tc = findViewById(R.id.txtTc);
        sifre = findViewById(R.id.txtPass);
        girisYap = findViewById(R.id.btnLogin);
    }

    public void girisYap() {
        girisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tc.getText().toString().trim().equals("") || !sifre.getText().toString().trim().equals("") )
                {
                    oku = FirebaseDatabase.getInstance().getReference().child("Kullanıcılar").child(tc.getText().toString());
                    ValueEventListener data = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Kullanıcı k =new Kullanıcı();
                            k = dataSnapshot.getValue(Kullanıcı.class);
                            Kullanicilist.add(k);
                            if (k!=null && k.rol.equalsIgnoreCase("admin") && k.sifre.equalsIgnoreCase(sifre.getText().toString())) {
                                GirenKullanici=k.tc;
                                Intent i = new Intent(GirisActivity.this, AdminActivity.class);
                                startActivity(i);
                                finish();
                            }

                            else if (k!=null && k.rol.equalsIgnoreCase("kullanıcı")&& k.sifre.equalsIgnoreCase(sifre.getText().toString()))
                            {
                                GirenKullanici=k.tc;
                                Intent i = new Intent(GirisActivity.this,KullaniciActivity.class);
                                startActivity(i);
                                Toast.makeText(context, "Hoş Geldiniz " + k.ad + " " + k.soyad, Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            else if (k!=null && k.rol.equalsIgnoreCase("doktor")&& k.sifre.equalsIgnoreCase(sifre.getText().toString()))
                            {
                                GirenKullanici=k.tc;
                                AlertDialog.Builder builder = new AlertDialog.Builder(GirisActivity.this);
                                builder.setTitle("              Hoş Geldiniz");
                                builder.setMessage(""+k.ad+" "+k.soyad+"\nDoktor olarak mı, Hasta olarak mı giriş yapmak istersiniz?");
                                builder.setNegativeButton("HASTA", new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog, int id) {

                                        Intent i = new Intent(GirisActivity.this,KullaniciActivity.class);
                                        startActivity(i);

                                    }
                                });


                                builder.setPositiveButton("DOKTOR", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent i = new Intent(GirisActivity.this,DoktorAnaSayfaActivity
                                                .class);
                                        startActivity(i);

                                    }
                                });


                                builder.show();

                            }
                            else
                                Toast.makeText(context, "Hatalı Tc veya şifre", Toast.LENGTH_SHORT).show();
                            oku.removeEventListener(this);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(GirisActivity.this, "Hatalı Giriş Yaptınız", Toast.LENGTH_SHORT).show();
                        }


                    };
                    oku.addValueEventListener(data);
                }else
                Toast.makeText(context, "Tc veya şifre boş geçilemez", Toast.LENGTH_SHORT).show();
            }
        });
    }

}


