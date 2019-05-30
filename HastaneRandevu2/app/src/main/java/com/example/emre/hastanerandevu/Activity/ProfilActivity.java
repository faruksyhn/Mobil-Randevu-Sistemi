package com.example.emre.hastanerandevu.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emre.hastanerandevu.Class.Kullanıcı;
import com.example.emre.hastanerandevu.Class.Randevu;
import com.example.emre.hastanerandevu.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ProfilActivity extends AppCompatActivity {


    FirebaseDatabase db= FirebaseDatabase.getInstance();
    DatabaseReference myRef=db.getReference();
    DatabaseReference oku;
    private TextView KullaniciAdi,KullaniciTc;
    private GirisActivity Giren;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
         dialog=new Dialog(this);
         init();
         TextDoldur();
    }

    public void init()
    {
        KullaniciAdi=findViewById(R.id.txtKullaniciAdi);
        KullaniciTc=findViewById(R.id.txtKullaniciTC);
    }

    public void TextDoldur()
    {

        oku=FirebaseDatabase.getInstance().getReference().child("Kullanıcılar").child(Giren.GirenKullanici);
        ValueEventListener data=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Kullanıcı k =dataSnapshot.getValue(Kullanıcı.class);
                KullaniciAdi.setText(k.ad+" "+k.soyad);
                KullaniciTc.setText(k.tc);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };oku.addValueEventListener(data);
    }

    public void sifreSecim(View view)
    {
        final EditText txtEskiSifre,txtYeniSifre1,txtYeniSifre2;
        Button btnSifreGuncelle;
        dialog.setContentView(R.layout.profilsifrepopup);
        btnSifreGuncelle=dialog.findViewById(R.id.btnSifreGuncelle);
        txtEskiSifre=dialog.findViewById(R.id.txtEskiSifre);
        txtYeniSifre1=dialog.findViewById(R.id.txtYeni1);
        txtYeniSifre2=dialog.findViewById(R.id.txtYeni2);

        btnSifreGuncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                oku=FirebaseDatabase.getInstance().getReference().child("Kullanıcılar").child(KullaniciTc.getText().toString());
                ValueEventListener data=new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Kullanıcı k=dataSnapshot.getValue(Kullanıcı.class);
                        if (k.sifre.equalsIgnoreCase(txtEskiSifre.getText().toString()) && txtYeniSifre1.getText().toString().equalsIgnoreCase(txtYeniSifre2.getText().toString())
                        && txtYeniSifre1.getText().length()==6)
                        {
                            DatabaseReference userRef = myRef.child("Kullanıcılar").child(KullaniciTc.getText().toString());
                            Map newUserData = new HashMap();
                            newUserData.put("sifre",txtYeniSifre1.getText().toString());
                            userRef.updateChildren(newUserData);
                            Toast.makeText(ProfilActivity.this, "Şifre Güncellendi", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                        else Toast.makeText(ProfilActivity.this, "Şifre Hatalı", Toast.LENGTH_SHORT).show();
                        oku.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };oku.addValueEventListener(data);
            }
        });
        dialog.show();
    }

    public void profilCikis(View view) {
        Intent i=new Intent(ProfilActivity.this,AnaMenuActivity.class);
        startActivity(i);
        finish();
    }
}
