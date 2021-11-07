package es.uniovi.eii.bestfood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PropiedadesActivity extends AppCompatActivity {

    private TextView titulo;
    private TextView puntuacion;
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

        Comida comida = intent.getParcelableExtra(BarcodeScanner.COMIDA_SELE);
        titulo = (TextView) findViewById(R.id.NombreComida);
        puntuacion = (TextView) findViewById(R.id.valor);
        proteinas = (TextView) findViewById(R.id.proteinas);
        sal = (TextView) findViewById(R.id.sal);
        punLetra = (TextView) findViewById(R.id.puntLetra);
        marca = (TextView) findViewById(R.id.marca);
        carbohydrates = (TextView) findViewById(R.id.hidratos);
        saturadas = (TextView) findViewById(R.id.saturadas);
        energia = (TextView) findViewById(R.id.energia);
        caratulaimg = (ImageView) findViewById(R.id.imagen);

        if (comida != null) {
            titulo.setText(comida.getNombre());
            sal.setText("Sal: " + comida.getSalt()+" g");
            proteinas.setText("Proteinas: " + comida.getProteins()+" g");
            puntuacion.setText("Puntuación: " + comida.getPuntuacion());
            punLetra.setText("Puntuación Nutriscore: " + comida.getScoreLetter());
            marca.setText(comida.getMarca());
            carbohydrates.setText("Carbohidratos: " + comida.getCarbohydrates()+" g");
            energia.setText("Energia: " + comida.getEnergy()+"kcal");
            saturadas.setText("Grasas saturadas: " + comida.getSaturated()+" g");

            Picasso.get().load(comida.getImagen()).into(caratulaimg);

        }
    }
}