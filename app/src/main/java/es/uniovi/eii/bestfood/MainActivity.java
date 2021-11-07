package es.uniovi.eii.bestfood;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends AppCompatActivity {
    ProgressDialog pd;
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
        new JsonTask().execute("https://world.openfoodfacts.org/api/v2/product/8410320111751.json");


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

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Porfavor espere");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL("https://world.openfoodfacts.org/api/v2/product/8410320111751.json");
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                    Log.d("Response: ", "> " + line);

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()) {
                pd.dismiss();
            }
            String jsonString = result;
            JSONObject obj = null;
            try {
                obj = new JSONObject(jsonString);
                JSONObject jsonResponse = obj.getJSONObject("product").getJSONObject("nutriments");

                String salt = jsonResponse.getString("salt_100g");
                String carbohydrates = jsonResponse.getString("carbohydrates_100g");
                String energy = jsonResponse.getString("energy-kcal_100g");
                String proteins = jsonResponse.getString("proteins_100g");
                String saturated = jsonResponse.getString("saturated-fat_100g");


                jsonResponse = obj.getJSONObject("product").getJSONObject("nutriscore_data");
                String scoreLetter = jsonResponse.getString("grade");
                String scoreNumber = jsonResponse.getString("score");


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}