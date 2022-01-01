package es.uniovi.eii.bestfood;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Iterator;

public class PropiedadesActivity extends AppCompatActivity {

    private TextView titulo;
    private TextView proteinas;
    private TextView sal;
    private TextView punLetra;
    private TextView marca;
    private TextView carbohydrates;
    private TextView saturadas;
    private TextView energia;
    private ImageView caratulaimg;
    private Button botonBorrar;
    private DatabaseReference mDatabase;
    private String id;
    private String barcode;
    Comida comida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propiedades);
        Intent intent = getIntent();
        if (intent.getParcelableExtra(MainActivity.COMIDA_SELE) != null) {
            comida = intent.getParcelableExtra(MainActivity.COMIDA_SELE);

        } else if (intent.getParcelableExtra(BarcodeScanner.COMIDA_SELE) != null) {
            comida = intent.getParcelableExtra(BarcodeScanner.COMIDA_SELE);
        }
        titulo = findViewById(R.id.NombreComida);
        proteinas = findViewById(R.id.proteinas);
        sal = findViewById(R.id.sal);
        punLetra = findViewById(R.id.puntLetra);
        marca = findViewById(R.id.marca);
        carbohydrates = findViewById(R.id.hidratos);
        saturadas = findViewById(R.id.saturadas);
        energia = findViewById(R.id.energia);
        caratulaimg = findViewById(R.id.imagen);
        botonBorrar = findViewById(R.id.btBorrar);

        if (comida != null) {
            barcode = comida.getId();
            titulo.setText(comida.getNombre());
            sal.setText("Sal: " + comida.getSalt() + " g");
            proteinas.setText("Proteinas: " + comida.getProteins() + " g");
            punLetra.setText("Puntuación Nutriscore: " + comida.getScoreLetter());
            marca.setText("Marca: " + comida.getMarca());
            carbohydrates.setText("Carbohidratos: " + comida.getCarbohydrates() + " g");
            energia.setText("Energia: " + comida.getEnergy() + "kcal");
            saturadas.setText("Grasas saturadas: " + comida.getSaturated() + " g");

            Picasso.get().load(comida.getImagen()).into(caratulaimg);

        }
        botonBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (comida != null) {
                    mDatabase = FirebaseDatabase.getInstance().getReference("");
                    id = FirebaseAuth.getInstance().getUid();

                    mDatabase.child("users").child(id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Iterator<DataSnapshot> dataSnapshotsChat = dataSnapshot.child("barcode").getChildren().iterator();

                            while (dataSnapshotsChat.hasNext()) {
                                DataSnapshot dataSnapshotChild = dataSnapshotsChat.next();

                                if (dataSnapshotChild.getKey().equals(barcode)) {
                                    dataSnapshotChild.getRef().removeValue();
                                    cambiarPantalla();
                                    break;

                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }


                    });
                }
            }
        });
    }

    private void cambiarPantalla() {


        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}