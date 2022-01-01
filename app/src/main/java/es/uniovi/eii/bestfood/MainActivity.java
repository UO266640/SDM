package es.uniovi.eii.bestfood;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import javax.net.ssl.HttpsURLConnection;


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
                    String idd = dataSnapshotChild.child("_id").getValue(String.class);

                    comida = new Comida(idd, nombre, salt, proteins, carbohydrates, energy, saturated, scoreLetter, marca, imagen);

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