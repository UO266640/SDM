package es.uniovi.eii.bestfood;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    List<Comida> listaComida;
    Comida comida;
    RecyclerView listaComidaView;
    private FloatingActionButton añadir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rellenarLista();
        listaComidaView = findViewById(R.id.recyclerView);
        listaComidaView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        listaComidaView.setLayoutManager(layoutManager);
        ListaComidasAdapter lpAdapter = new ListaComidasAdapter(listaComida, this::clickonItem);
        listaComidaView.setAdapter(lpAdapter);

        añadir = findViewById(R.id.floatingActionButton);

        añadir.setOnClickListener(view -> {
             Intent intent = new Intent(MainActivity.this, BarcodeScanner.class);
              startActivity(intent);
        });
    }


    private void rellenarLista() {
        listaComida = new ArrayList<Comida>();
        Comida comida = new Comida("Cocacola", 0);
        Comida comida2 = new Comida("Cereales", 50);

        listaComida.add(comida);
        listaComida.add(comida2);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void clickonItem(Comida comida) {

        Intent intent = new Intent(MainActivity.this, PropiedadesActivity.class);
        //intent.putExtra(COMIDA_SELECIONADA, peli);
        startActivity(intent);
    }

}