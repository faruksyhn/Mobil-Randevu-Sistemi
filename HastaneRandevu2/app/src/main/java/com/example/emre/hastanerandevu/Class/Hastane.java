package com.example.emre.hastanerandevu.Class;

public class Hastane {


    public String id,HastaneAdi,Sehir,Adres;
    public String BolumAdi;
    public Hastane(){

    }
    /*public Hastane(String id,String HastaneAdi,String Sehir,String Adres){
        this.id=id;
        this.HastaneAdi=HastaneAdi;
        this.Sehir=Sehir;
        this.Adres=Adres;
    }*/
    public Hastane(String HastaneAdi){
        this.HastaneAdi=HastaneAdi;
    }
}

