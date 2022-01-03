package es.uniovi.eii.bestfood;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

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
    private Button botonBorrar, botonEditar;
    private DatabaseReference mDatabase;
    private String id;
    private String barcode;
    Comida comida;
    public static final String COMIDA_SELE = "";

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
        botonEditar = findViewById(R.id.btnEditar);


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
        botonBorrar.setOnClickListener(v -> {
            if (comida != null) {
                mDatabase = FirebaseDatabase.getInstance().getReference();
                id = FirebaseAuth.getInstance().getUid();
                mDatabase.child("users").child(id).child("barcode").child(barcode).removeValue();
                finish();
            }
        });

        botonEditar.setOnClickListener(v -> {
            if (comida != null) {
                Intent intent2 = new Intent(this, EditComidaActivity.class);
                intent2.putExtra("editar", comida);

                startActivity(intent2);

                finish();
            }
        });
    }

}