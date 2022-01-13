package es.uniovi.eii.bestfood;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import es.uniovi.eii.bestfood.modelo.Comida;


public class MainActivity extends AppCompatActivity {

    List<Comida> listaComida;
    RecyclerView listaComidaView;
    private FloatingActionButton añadir;
    private DatabaseReference mDatabase;
    String id;
    private Comida comida;
    public static final String COMIDA_SELE = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        listaComidaView = findViewById(R.id.recyclerView);
        listaComidaView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        listaComidaView.setLayoutManager(layoutManager);

        listaComida = new ArrayList<>();


        mDatabase = FirebaseDatabase.getInstance().getReference("");
        id = FirebaseAuth.getInstance().getUid();


        mDatabase.child("users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshotsChat = dataSnapshot.child("barcode").getChildren().iterator();

                listaComida = new ArrayList<>();

                while (dataSnapshotsChat.hasNext()) {
                    DataSnapshot dataSnapshotChild = dataSnapshotsChat.next();
                    String nombre = dataSnapshotChild.child("nombre").getValue(String.class);
                    String imagen = dataSnapshotChild.child("imagen").getValue(String.class);
                    String salt = dataSnapshotChild.child("salt").getValue(String.class);
                    String carbohydrates = dataSnapshotChild.child("carbohydrates").getValue(String.class);
                    String energy = dataSnapshotChild.child("energy").getValue(String.class);
                    String proteins = dataSnapshotChild.child("proteins").getValue(String.class);
                    String saturated = dataSnapshotChild.child("saturated").getValue(String.class);


                    String scoreLetter = dataSnapshotChild.child("scoreLetter").getValue(String.class);
                    String marca = dataSnapshotChild.child("marca").getValue(String.class);

                    comida = new Comida(dataSnapshotChild.getKey(), nombre, salt, proteins, carbohydrates, energy, saturated, scoreLetter, marca, imagen);

                    listaComida.add(comida);

                }
                ListaComidasAdapter lpAdapter = new ListaComidasAdapter(listaComida, this::clickonItem);
                listaComidaView.setAdapter(lpAdapter);
            }

            private void clickonItem(Comida comida) {

                Intent intent = new Intent(MainActivity.this, PropiedadesActivity.class);
                intent.putExtra(COMIDA_SELE, comida);
                startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {/*Do Nothing*/}
        });


        añadir = findViewById(R.id.floatingActionButton);

        añadir.setOnClickListener(view -> {
            Intent intent = new Intent(this, BarcodeScanner.class);
            startActivity(intent);
        });


    }

}