package com.multiscreen.siams.project6;

public class Myself {

    public String Voornaam, Achternaam, Adres, Postcode, WoonplaatsMijzelf, Geboorttedatum, GeboortePlaats, Klas, Crebo, Opleiding, Studentnr, geslacht;

    public Myself(String voornaam, String achternaam, String adres, String postcode, String woonplaatsMijzelf, String geboorttedatum, String geboortePlaats, String klas, String crebo, String opleiding, String studentnr, String Geslacht) {
        Voornaam = voornaam;
        Achternaam = achternaam;
        Adres = adres;
        Postcode = postcode;
        WoonplaatsMijzelf = woonplaatsMijzelf;
        Geboorttedatum = geboorttedatum;
        GeboortePlaats = geboortePlaats;
        Klas = klas;
        Crebo = crebo;
        Opleiding = opleiding;
        Studentnr = studentnr;
        geslacht = Geslacht;
    }

    public String getVoornaam() {
        return Voornaam;
    }

    public String getAchternaam() {
        return Achternaam;
    }

    public String getAdres() {
        return Adres;
    }

    public String getPostcode() {
        return Postcode;
    }

    public String getWoonplaatsMijzelf() {
        return WoonplaatsMijzelf;
    }

    public String getGeboorttedatum() {
        return Geboorttedatum;
    }

    public String getGeboortePlaats() {
        return GeboortePlaats;
    }

    public String getKlas() {
        return Klas;
    }

    public String getCrebo() {
        return Crebo;
    }

    public String getOpleiding() {
        return Opleiding;
    }

    public String getStudentnr() {
        return Studentnr;
    }

    public String getGeslacht() {
        return geslacht;
    }
}
