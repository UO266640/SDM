package es.uniovi.eii.bestfood;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propiedades);
        Intent intent = getIntent();
        Comida comida;
        if (intent.getParcelableExtra(MainActivity.COMIDA_SELE) != null) {
            comida = intent.getParcelableExtra(MainActivity.COMIDA_SELE);

        } else {
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

        if (comida != null) {
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
    }
}