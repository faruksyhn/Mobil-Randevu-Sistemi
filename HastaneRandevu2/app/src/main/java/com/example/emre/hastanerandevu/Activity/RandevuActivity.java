package com.example.emre.hastanerandevu.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emre.hastanerandevu.Class.Bolum;
import com.example.emre.hastanerandevu.Class.Doktor;
import com.example.emre.hastanerandevu.Class.Hastane;
import com.example.emre.hastanerandevu.Class.Randevu;
import com.example.emre.hastanerandevu.Class.Saat;
import com.example.emre.hastanerandevu.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class RandevuActivity extends AppCompatActivity {


    private static String RandevuAlinanHastane,RandevuAlinanBolum,RandevuAlinanDoktor,Doktortc;
    private Randevu HastaRandevular;
    private GirisActivity Giren;
    private ListView listRandevuSil;
    private Boolean[] DoluSaatler=new Boolean[12];
    private Button BtnSaatGetir;
    private static int i=0;
    private LinearLayout BtnLayout1, BtnLayout2, BtnLayout3;
    private TextView SecilenTarih;
    private DatePickerDialog.OnDateSetListener DateSetListener;
    private String[] SaatDizisi={"09:00","09:30","10:00","10:30","11:00","11:30","12:00",
            "13:00","13:30","14:00","14:30","15:00"};
    private ArrayList<String> Saatlist=new ArrayList<String>();
    private String[] BosSaatDizisi;
    private Spinner SpnrHastane,SpnrBolum,SpnrDoktor;
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
    private ArrayList<String> Saatler = new ArrayList<>();
    private List<Randevu> Randevulist = new ArrayList<>();
    private List<Randevu> HastaRandevuList = new ArrayList<>();
    Date SimdikiTarih = Calendar.getInstance().getTime();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_randevu);
        ActionBar actionBar=getSupportActionBar();
        if(Giren.GirenKullanici.equalsIgnoreCase("11111111111")) actionBar.setTitle("Randevu Kısıtla");
        else actionBar.setTitle("Randevu Al");
        init();
        HastaneGetir();
        TarihSec();

    }






    private void init() {
        SpnrHastane=findViewById(R.id.SpnrHastanelerR);
        SpnrBolum=findViewById(R.id.SpnrBolumlerR);
        SpnrDoktor=findViewById(R.id.SpnrDoktorlarR);
        SecilenTarih=findViewById(R.id.Tarih);
        BtnLayout1=findViewById(R.id.ButtonLinerLayout1);
        BtnLayout2=findViewById(R.id.ButtonLinerLayout2);
        BtnLayout3=findViewById(R.id.ButtonLinerLayout3);
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
                dataAdapterForHastane = new ArrayAdapter<String>(RandevuActivity.this, android.R.layout.simple_spinner_item, Hastaneler);
                dataAdapterForHastane.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpnrHastane.setAdapter(dataAdapterForHastane);
                SpnrHastane.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        HastaneId=Hastanelist.get(i).id;
                        RandevuAlinanHastane=SpnrHastane.getSelectedItem().toString();
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
                Toast.makeText(RandevuActivity.this, "Hatalı Giriş Yaptınız", Toast.LENGTH_SHORT).show();
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
                dataAdapterForBolum = new ArrayAdapter<String>(RandevuActivity.this, android.R.layout.simple_spinner_item, Bolumler);
                dataAdapterForBolum.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpnrBolum.setAdapter(dataAdapterForBolum);
                SpnrBolum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        BolmuId="";
                        BolmuId=Bolumlist.get(i).id;
                        RandevuAlinanBolum=SpnrBolum.getSelectedItem().toString();
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
                Toast.makeText(RandevuActivity.this, "Hatalı Giriş Yaptınız", Toast.LENGTH_SHORT).show();
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
                dataAdapterForDoktor = new ArrayAdapter<String>(RandevuActivity.this, android.R.layout.simple_spinner_item, Doktorlar);
                dataAdapterForDoktor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpnrDoktor.setAdapter(dataAdapterForDoktor);
                SpnrDoktor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        DoktorId="";
                        DoktorId=Doktorlist.get(i).id;
                        Doktortc=Doktorlist.get(i).tc;
                        HastaRandevuList.clear();
                        RandevuAlinanDoktor=SpnrDoktor.getSelectedItem().toString();
                        TarihSec();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(RandevuActivity.this, "Hatalı Giriş Yaptınız", Toast.LENGTH_SHORT).show();
            }

        };
        oku.addValueEventListener(data);
    }
    public void HastaRandevuOku()
    {
        oku= FirebaseDatabase.getInstance().getReference().child("Randevular");
        ValueEventListener data=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot post:dataSnapshot.getChildren()){
                        Randevu r=post.getValue(Randevu.class);
                        if ( r.hastaid.equalsIgnoreCase(Giren.GirenKullanici) && r.doktorid.equalsIgnoreCase(DoktorId)  ){
                            HastaRandevuList.add(r);
                        }
                    }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
oku.addValueEventListener(data);
    }
    public void TarihSec()
    {
        SecilenTarih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar cal=Calendar.getInstance();
                int yil = cal.get(Calendar.YEAR);
                int ay = cal.get(Calendar.MONTH);
                int gun = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(RandevuActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                i=0;
                                month += 1;

                                SecilenTarih.setText(dayOfMonth + "/" + month + "/" + year);
                                RandevuGetir(dayOfMonth + "/" + month + "/" + year);
                                HastaRandevuOku();

                            }
                        }, yil, ay, gun);

                dpd.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                dpd.getDatePicker().setMaxDate(System.currentTimeMillis()+1000000000);

                dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, "Seç", dpd);
                dpd.show();
            }
        });
    }

    public void RandevuGetir(final String tarih)
    {
        Randevulist.clear();
        oku=FirebaseDatabase.getInstance().getReference().child("Randevular");
        oku.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot post:dataSnapshot.getChildren())
                {
                    Randevu r=post.getValue(Randevu.class);
                    r.randevuid=post.getKey().toString();
                    if(r.doktorid.equalsIgnoreCase(DoktorId) && r.tarih.equalsIgnoreCase(tarih)){
                        Randevulist.add(r);
                    }
                    if (!Giren.GirenKullanici.equalsIgnoreCase("11111111111")) oku.removeEventListener(this);
                }
                SaatGetir(tarih);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @SuppressLint("ResourceAsColor")
    public void SaatGetir(final String tarih)
    {
        BtnLayout1.removeAllViews();
        BtnLayout2.removeAllViews();
        BtnLayout3.removeAllViews();
        Saatlist.clear();
        for(String saat:SaatDizisi)
        {
            Saatlist.add(saat);
        }
        for(Randevu saat:Randevulist)
        {
            Saatlist.remove(saat.saat);
        }
        int j=0;
        for(final String Saatler : SaatDizisi)
        {
            DoluSaatler[j]=true;
            for(Randevu randevu : Randevulist)
            {
                if (randevu.saat.equalsIgnoreCase(Saatler))
                {
                    DoluSaatler[j]=false;
                }
            }
            j++;

        }

        for(final String Saatler : SaatDizisi)
        {
            BtnSaatGetir=new Button(this);
            if (i<4) BtnLayout1.addView(BtnSaatGetir);
            else if (i<8) BtnLayout2.addView(BtnSaatGetir);
            else BtnLayout3.addView(BtnSaatGetir);


            if (DoluSaatler[i]==false)
            {
                BtnSaatGetir.setEnabled(false);
                BtnSaatGetir.setTextColor(Color.RED);

            }
            //ADMİN GİRİŞ YAPARSA
            if(Giren.GirenKullanici.equalsIgnoreCase("11111111111"))
            {
                BtnSaatGetir.setText(Saatler);
                BtnSaatGetir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RandevuActivity.this);
                        builder.setTitle("Randevu Kısıtla");
                        builder.setMessage("Hastahane Adı: "+RandevuAlinanHastane+"\n\nBölüm Adı: "+RandevuAlinanBolum+"\n\nDoktor Adı: "+RandevuAlinanDoktor+"\n\nKısıtlanan Tarih: "+tarih+"\n\nKısıtlanan Saat: "+Saatler);
                        builder.setNegativeButton("İPTAL", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id) {

                                //İptal butonuna basılınca yapılacaklar.Sadece kapanması isteniyorsa boş bırakılacak
                            }
                        });
                        builder.setPositiveButton("KISITLA", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String uniqueID = UUID.randomUUID().toString();
                                Randevu r=new Randevu();
                                r.saat=Saatler;
                                r.tarih=tarih;
                                r.hastaneid=HastaneId;
                                r.bolumid=BolmuId;
                                r.doktorid=DoktorId;
                                r.hastaneadi=RandevuAlinanHastane;
                                r.bolumadi=RandevuAlinanBolum;
                                r.doktoradi=RandevuAlinanDoktor;
                                r.hastaid=Giren.GirenKullanici;
                                r.doktortc=Doktortc;
                                myRef.child("Randevular").child(uniqueID).setValue(r);
                                Toast.makeText(RandevuActivity.this, "Saat Kısıtlanmıştır", Toast.LENGTH_SHORT).show();
                                i=0;

                            }
                        });
                        builder.show();
                    }
                });
                i++;
            }
            //KULLANICI GİRİŞ YAPARSA
            else
            {
                BtnSaatGetir.setText(Saatler);
                BtnSaatGetir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String Tarih="";
                        String Saat="";
                        for(Randevu randevu:HastaRandevuList){
                            if (randevu.tarih.equalsIgnoreCase(tarih))
                            {
                                Tarih =randevu.tarih;


                            }
                        }
                        if(Tarih!="")
                        {
                            Toast.makeText(RandevuActivity.this, "Randevunuz var", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(RandevuActivity.this);
                            builder.setTitle("Randevu Al");
                            builder.setMessage("Hastahane Adı: "+RandevuAlinanHastane+"\n\nBölüm Adı: "+RandevuAlinanBolum+"\n\nDoktor Adı: "+RandevuAlinanDoktor+"\n\nRandevu Tarihi: "+tarih+"\n\nRandevu Saati: "+Saatler);
                            builder.setNegativeButton("İPTAL", new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int id) {

                                    //İptal butonuna basılınca yapılacaklar.Sadece kapanması isteniyorsa boş bırakılacak
                                }
                            });
                            builder.setPositiveButton("ONAYLA", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    String uniqueID = UUID.randomUUID().toString();
                                    Randevu r=new Randevu();
                                    r.saat=Saatler;
                                    r.tarih=tarih;
                                    r.hastaneid=HastaneId;
                                    r.bolumid=BolmuId;
                                    r.doktorid=DoktorId;
                                    r.hastaneadi=RandevuAlinanHastane;
                                    r.bolumadi=RandevuAlinanBolum;
                                    r.doktoradi=RandevuAlinanDoktor;
                                    r.hastaid=Giren.GirenKullanici;
                                    r.doktortc=Doktortc;

                                    myRef.child("Randevular").child(uniqueID).setValue(r);

                                    Intent intent =new Intent(RandevuActivity.this,KullaniciActivity.class);
                                    startActivity(intent);

                                    Toast.makeText(RandevuActivity.this, "Randevunuz oluşmuştur", Toast.LENGTH_SHORT).show();
                                    i=0;

                                }
                            });
                            builder.show();
                        }
                    }
                });
                i++;
            }


        }
    }
}


