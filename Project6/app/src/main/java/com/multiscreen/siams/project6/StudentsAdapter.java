package com.multiscreen.siams.project6;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class StudentsAdapter extends ArrayAdapter<Students> {

    public StudentsAdapter(@NonNull Context context, ArrayList<Students> getallen) {
        super(context, 0, getallen);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.standaardlayout, parent, false
            );
        }

        Students currentStudent = getItem(position);
        String voornaam = currentStudent.getFirstName();
        String achternaam = currentStudent.getLastName();

        char[] vnArray = voornaam.toCharArray();
        char[] anArray = achternaam.toCharArray();

        String Voorletters = "" + vnArray[0] + anArray[0];

        TextView tvVoorletter = (TextView)convertView.findViewById(R.id.voorletters);
        GradientDrawable CityColor = (GradientDrawable) tvVoorletter.getBackground();
        int CityColorID = getCityColor(currentStudent.getResidence());
        CityColor.setColor(CityColorID);
        tvVoorletter.setText(Voorletters);

        TextView tvNaam = (TextView)convertView.findViewById(R.id.Name);
        if(currentStudent.getMiddleName() == " "){
            String naam = "" + currentStudent.getFirstName() + " " + currentStudent.getLastName();
            tvNaam.setText(naam);
        }
        else{
            String naam = "" + currentStudent.getFirstName() + " " + currentStudent.getMiddleName() + " " + currentStudent.getLastName();
            tvNaam.setText(naam);
        }

        TextView tvGeslacht = (TextView)convertView.findViewById(R.id.Geslacht);
        tvGeslacht.setText(currentStudent.getGender());

        TextView tvWoonplaats = (TextView)convertView.findViewById(R.id.Woonplaats);
        tvWoonplaats.setText(currentStudent.getResidence());

        TextView tvOpleiding = (TextView)convertView.findViewById(R.id.opleiding);
        tvOpleiding.setText(currentStudent.getEducation());
        return convertView;
    }


    private int getCityColor(String city){
        int CityColorID;
        switch (city)
        {
            case "ENSCHEDE":
                CityColorID = R.color.Enschede;
                break;
            case "HENGELO OV":
                CityColorID = R.color.Hengelo;
                break;
            case "HAAKSBERGEN":
                CityColorID = R.color.Haaksbergen;
                break;
                default:
                CityColorID = R.color.Overig;
                break;
        }
        return ContextCompat.getColor(getContext(), CityColorID);
    }
}
