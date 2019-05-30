package com.example.emre.hastanerandevu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emre.hastanerandevu.Activity.FavorilerActivity;
import com.example.emre.hastanerandevu.Activity.GirisActivity;
import com.example.emre.hastanerandevu.Activity.HastaRandevuListActivity;
import com.example.emre.hastanerandevu.Activity.HastaneActivity;
import com.example.emre.hastanerandevu.Activity.KullaniciActivity;
import com.example.emre.hastanerandevu.Activity.ProfilActivity;
import com.example.emre.hastanerandevu.Activity.RandevuActivity;

public class SlideAdapter extends PagerAdapter {
    Context context;
    LayoutInflater inflater;
    public int[] mImageIds=new int[]{R.drawable.agenda,R.drawable.appointment,R.drawable.favourite,R.drawable.settings};
    public String[] baslik={"Randevu Al","Randevularım","Favoriler","Profili Düzenle"};
    public String[] altYazi={"Randevu sisteminden \n Randevu almak için resime tıklayın. \nFarklı seçenekler için ekranı kaydırın.","Randevu listenizi görmek için\n resime tıklayın. \nFarklı seçenekler için Ekranı kaydırın. ","Randevuları görmek için resime tıklayın","Profilinizi düzenlemek için\n resime tıklayın. \nFarklı seçenekler için ekranı kaydırın."};
    public int[] lst_Background={Color.rgb(1,188,212) ,
            Color.rgb(55,55,55),
            Color.rgb(255,192,0),
            Color.rgb(239,85,85)};

    public SlideAdapter(Context context)
    {
        this.context=context;
    }
    @Override
    public int getCount() {
        return baslik.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==(LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.slide,container,false);
        LinearLayout layautalide=view.findViewById(R.id.sliderLinerLayout);
        ImageView imageLide= view.findViewById(R.id.kullanıcıImgId);
        TextView textLide=view.findViewById(R.id.kullanıcıTxtId);
        TextView txt2Lide =view.findViewById(R.id.kullanıcıtxt2Id);
        layautalide.setBackgroundColor(lst_Background[position]);
        imageLide.setImageResource(mImageIds[position]);
        textLide.setText(baslik[position]);
        txt2Lide.setText(altYazi[position]);
        imageLide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position==0)
                    view.getContext().startActivity(new Intent(context, RandevuActivity.class));
                else if (position==1)
                    view.getContext().startActivity(new Intent(context, HastaRandevuListActivity.class));
                else if (position==2)
                    view.getContext().startActivity(new Intent(context, FavorilerActivity.class));
                else if (position==3)
                    view.getContext().startActivity(new Intent(context, ProfilActivity.class));
            }
        });

        container.addView(view);

        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }
}
