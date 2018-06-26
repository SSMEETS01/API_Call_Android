package com.multiscreen.siams.project6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnClassmate, btnPersonal, btnPets, btnGrades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnClassmate = (Button)findViewById(R.id.btnClassmates);
        btnPersonal = (Button)findViewById(R.id.btnMyself);
        btnPets = (Button)findViewById(R.id.btnPet);
        btnGrades = (Button)findViewById(R.id.btnGrade);

        btnClassmate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ClassmatesActivity.class);
                startActivity(intent);
            }
        });
        btnPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PersonalActivity.class);
                startActivity(intent);
            }
        });
        btnPets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PetActivity.class);
                startActivity(intent);
            }
        });
        btnGrades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                startActivity(intent);
            }
        });
    }
}
