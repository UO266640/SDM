package es.uniovi.eii.bestfood;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import es.uniovi.eii.bestfood.modelo.Comida;

public class BarcodeScanner extends AppCompatActivity {

    public static final String COMIDA_SELE = "comida";
    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    String intentData = "";
    private Comida comida;
    private boolean finish;
    private AlertDialog ad;


    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);


    }


    private void initialiseDetectorsAndSources() {

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(BarcodeScanner.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(BarcodeScanner.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    txtBarcodeValue.post(() -> {

                        if (barcodes.valueAt(0).email != null) {
                            txtBarcodeValue.removeCallbacks(null);
                            intentData = barcodes.valueAt(0).email.address;
                            txtBarcodeValue.setText(intentData);
                        } else {
                            intentData = barcodes.valueAt(0).displayValue;
                            txtBarcodeValue.setText(intentData);
                        }

                        cameraSource.stop();

                        readProperties(intentData);
                        try {
                            if (ActivityCompat.checkSelfPermission(BarcodeScanner.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                cameraSource.start(surfaceView.getHolder());
                            } else {
                                ActivityCompat.requestPermissions(BarcodeScanner.this, new
                                        String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    private void readProperties(String intentData) {
        new JsonTask().execute("https://world.openfoodfacts.org/api/v2/product/" + intentData + ".json");

    }


    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();


    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();

    }

    private class JsonTask extends AsyncTask<String, String, String> {


        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");

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

            String jsonString = result;
            JSONObject obj = null;
            try {

                if (!finish && result != null) {
                    obj = new JSONObject(jsonString);
                    JSONObject jsonResponse = obj.getJSONObject("product");

                    String scoreLetter;
                    if (jsonResponse.has("nutriscore_grade")) {
                        scoreLetter = jsonResponse.getString("nutriscore_grade");
                    } else {
                        scoreLetter = "Sin datos";
                    }

                    jsonResponse = obj.getJSONObject("product").getJSONObject("nutriments");

                    String salt;
                    if (jsonResponse.has("salt_100g")) {
                        salt = jsonResponse.getString("salt_100g");
                    } else {
                        salt = "Sin datos";
                    }
                    String carbohydrates;
                    if (jsonResponse.has("carbohydrates_100g")) {
                        carbohydrates = jsonResponse.getString("carbohydrates_100g");
                    } else {
                        carbohydrates = "Sin datos";
                    }
                    String energy;

                    if (jsonResponse.has("energy-kcal_100g")) {
                        energy = jsonResponse.getString("energy-kcal_100g");
                    } else {
                        energy = "Sin datos";
                    }
                    String proteins;
                    if (jsonResponse.has("proteins_100g")) {
                        proteins = jsonResponse.getString("proteins_100g");
                    } else {
                        proteins = "Sin datos";
                    }

                    String saturated;
                    if (jsonResponse.has("saturated-fat_100g")) {
                        saturated = jsonResponse.getString("saturated-fat_100g");
                    } else {
                        saturated = "Sin datos";
                    }


                    jsonResponse = obj.getJSONObject("product");

                    String nombre;
                    if (jsonResponse.has("product_name")) {
                        nombre = jsonResponse.getString("product_name");
                    } else {
                        nombre = "Sin datos";
                    }

                    String marca;
                    if (jsonResponse.has("brands")) {
                        marca = jsonResponse.getString("brands");
                    } else {
                        marca = "Sin datos";
                    }


                    String imagen;
                    if (jsonResponse.has("image_front_url")) {
                        imagen = jsonResponse.getString("image_front_url");
                    } else {
                        imagen = "https://uh.edu/pharmacy/_images/directory-staff/no-image-available.jpg";
                    }

                    String idd = jsonResponse.getString("_id");


                    comida = new Comida(idd, nombre, salt, carbohydrates, energy, proteins, saturated, scoreLetter, marca, imagen);


                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    String id = FirebaseAuth.getInstance().getUid();

                    mDatabase.child("users").child(id).child("barcode").child(idd).child("nombre").setValue(nombre);
                    mDatabase.child("users").child(id).child("barcode").child(idd).child("imagen").setValue(imagen);
                    mDatabase.child("users").child(id).child("barcode").child(idd).child("scoreLetter").setValue(scoreLetter);
                    mDatabase.child("users").child(id).child("barcode").child(idd).child("salt").setValue(salt);
                    mDatabase.child("users").child(id).child("barcode").child(idd).child("proteins").setValue(proteins);
                    mDatabase.child("users").child(id).child("barcode").child(idd).child("carbohydrates").setValue(carbohydrates);
                    mDatabase.child("users").child(id).child("barcode").child(idd).child("energy").setValue(energy);
                    mDatabase.child("users").child(id).child("barcode").child(idd).child("saturated").setValue(saturated);
                    mDatabase.child("users").child(id).child("barcode").child(idd).child("marca").setValue(marca);

                    Intent intent = new Intent(BarcodeScanner.this, PropiedadesActivity.class);
                    intent.putExtra(COMIDA_SELE, comida);
                    startActivity(intent);
                    finish = true;

                    finish();
                } else if (!finish) {
                    cameraSource.stop();

                    ad = new AlertDialog.Builder(BarcodeScanner.this)
                            .setTitle("BestFood")
                            .setMessage("Producto no encontrado")
                            .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                        dialog.dismiss();
                                        finish();
                                    }
                            )
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
            }
        }
    }

}