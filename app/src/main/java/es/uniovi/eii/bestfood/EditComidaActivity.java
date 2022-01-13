package es.uniovi.eii.bestfood;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import es.uniovi.eii.bestfood.modelo.Comida;

public class EditComidaActivity extends AppCompatActivity {
    Comida comida;
    EditText sal, proteinas, carbohidratos, energia, saturadas;
    private DatabaseReference mDatabase;
    private Button botonAceptar;
    private ImageView caratulaimg;
    private TextView titulo, punLetra;
    private TextView marca;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_comida);
        titulo = findViewById(R.id.NombreComidaedit);
        sal = findViewById(R.id.editTextSal);
        proteinas = findViewById(R.id.editTextProteinas);
        carbohidratos = findViewById(R.id.editTextCarbohidratos);
        energia = findViewById(R.id.editTextEnergia);
        saturadas = findViewById(R.id.editTextSaturadas);
        botonAceptar = findViewById(R.id.btnOK);
        punLetra = findViewById(R.id.puntLetra);
        caratulaimg = findViewById(R.id.imagen);
        marca = findViewById(R.id.marca);

        Intent intent = getIntent();
        if (intent.getParcelableExtra("editar") != null) {
            comida = intent.getParcelableExtra("editar");
            marca.setText("Marca: " + comida.getMarca());
            sal.setText(comida.getSalt());
            proteinas.setText(comida.getProteins());
            carbohidratos.setText(comida.getCarbohydrates());
            energia.setText(comida.getEnergy());
            saturadas.setText(comida.getSaturated());
            Picasso.get().load(comida.getImagen()).into(caratulaimg);
            titulo.setText(comida.getNombre());
            punLetra.setText("Puntuación Nutriscore: " + comida.getScoreLetter());

        }

        botonAceptar.setOnClickListener(v -> {
            if (comida != null) {
                mDatabase = FirebaseDatabase.getInstance().getReference();
                String id = FirebaseAuth.getInstance().getUid();
                String idd = comida.getId();
                mDatabase.child("users").child(id).child("barcode").child(idd).child("salt").setValue(sal.getText().toString());
                mDatabase.child("users").child(id).child("barcode").child(idd).child("proteins").setValue(proteinas.getText().toString());
                mDatabase.child("users").child(id).child("barcode").child(idd).child("carbohydrates").setValue(carbohidratos.getText().toString());
                mDatabase.child("users").child(id).child("barcode").child(idd).child("energy").setValue(energia.getText().toString());
                mDatabase.child("users").child(id).child("barcode").child(idd).child("saturated").setValue(saturadas.getText().toString());

                Intent intent2 = new Intent(this, MainActivity.class);
                startActivity(intent2);

                finish();
            }
        });

    }
}