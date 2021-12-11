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
                    // String id = dataSnapshotChild.child("id").getValue(String.class);

                    listaComida.add(new Comida(nombre, imagen));

                }
                ListaComidasAdapter lpAdapter = new ListaComidasAdapter(listaComida, this::clickonItem);
                listaComidaView.setAdapter(lpAdapter);
            }

            private void clickonItem(Comida comida) {

                Intent intent = new Intent(MainActivity.this, PropiedadesActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {/*Do Nothing*/}
        });


        añadir = findViewById(R.id.floatingActionButton);

        añadir.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, BarcodeScanner.class);
            startActivity(intent);
        });


    }

}