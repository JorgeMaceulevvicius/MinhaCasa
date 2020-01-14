package br.com.livroandroid.minhacasa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    TextView txtUmi, txtTemp;
    Switch switchSom, switchLuz;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtUmi = (TextView) findViewById(R.id.txtUmi);
        txtTemp  = (TextView) findViewById(R.id.txtTemp);
        switchSom = (Switch)findViewById(R.id.switchSom);
        switchLuz = (Switch)findViewById(R.id.switchLuz);

        inicializaFirebase();
        eventoDataBaseClima();
        eventoDataBaseAcionamentos();



        switchLuz.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if(isChecked){

                    databaseReference.child("acionamentos").child("luzQuarto").setValue(1);

                }else {
                    databaseReference.child("acionamentos").child("luzQuarto").setValue(0);

                }
            }
        });

        switchSom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){

                    databaseReference.child("acionamentos").child("caixaSom").setValue(1);

                }else {
                    databaseReference.child("acionamentos").child("caixaSom").setValue(0);

                }
            }
        });

    }

    private void inicializaFirebase(){

        FirebaseApp.initializeApp(MainActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();

    }

    private void eventoDataBaseClima() {

        final DecimalFormat df = new DecimalFormat("##.##");

        databaseReference.child("clima").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    clima c = dataSnapshot.getValue(clima.class);
                    txtTemp.setText(df.format(c.getTemperatura()) + " °C");
                    txtUmi.setText(c.getUmidade()+" %");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void eventoDataBaseAcionamentos() {

        final DecimalFormat df = new DecimalFormat("##.##");

        databaseReference.child("acionamentos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                acionamentos ac = dataSnapshot.getValue(acionamentos.class);
                if(ac.getCaixaSom() == 1){
                    switchSom.setChecked(true);
                }
                if(ac.getLuzQuarto() == 1){
                    switchLuz.setChecked(true);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
