package es.uniovi.eii.bestfood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

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

        if (comida != null) {
            titulo.setText(comida.getNombre());
            sal.setText("Sal: " + comida.getSalt());
            proteinas.setText("Proteinas: " + comida.getProteins());
            puntuacion.setText("Puntuación: " + comida.getPuntuacion());
            punLetra.setText("Puntuación Nutriscore: " + comida.getScoreLetter());
            marca.setText(comida.getMarca());
            carbohydrates.setText("Carbohidratos: " + comida.getCarbohydrates());
            energia.setText("Energia: " + comida.getEnergy());
            saturadas.setText("Grasas saturadas: " + comida.getSaturated());


        }
    }
}