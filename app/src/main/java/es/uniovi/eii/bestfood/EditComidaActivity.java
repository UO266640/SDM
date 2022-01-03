package es.uniovi.eii.bestfood;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditComidaActivity extends AppCompatActivity {
    Comida comida;
    EditText sal, proteinas, carbohidratos, energia, saturadas;
    private DatabaseReference mDatabase;
    private Button botonAceptar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_comida);

        sal = findViewById(R.id.editTextSal);
        proteinas = findViewById(R.id.editTextProteinas);
        carbohidratos = findViewById(R.id.editTextCarbohidratos);
        energia = findViewById(R.id.editTextEnergia);
        saturadas = findViewById(R.id.editTextSaturadas);
        botonAceptar = findViewById(R.id.btnOK);

        Intent intent = getIntent();
        if (intent.getParcelableExtra("editar") != null) {
            comida = intent.getParcelableExtra("editar");

            sal.setText(comida.getSalt());
            proteinas.setText(comida.getProteins());
            carbohidratos.setText(comida.getCarbohydrates());
            energia.setText(comida.getEnergy());
            saturadas.setText(comida.getSaturated());
        }

        botonAceptar.setOnClickListener(v -> {
            if (comida != null) {
                mDatabase = FirebaseDatabase.getInstance().getReference();
                String id = FirebaseAuth.getInstance().getUid();
                String idd = comida.getId();
                mDatabase.child("users").child(id).child("barcode").child(idd).child("salt").setValue(sal.getText());
                mDatabase.child("users").child(id).child("barcode").child(idd).child("proteins").setValue(proteinas.getText());
                mDatabase.child("users").child(id).child("barcode").child(idd).child("carbohydrates").setValue(carbohidratos.getText());
                mDatabase.child("users").child(id).child("barcode").child(idd).child("energy").setValue(energia.getText());
                mDatabase.child("users").child(id).child("barcode").child(idd).child("saturated").setValue(saturadas.getText());

                Intent intent2 = new Intent(this, MainActivity.class);
                startActivity(intent2);

                finish();
            }
        });

    }
}