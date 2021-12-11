package es.uniovi.eii.bestfood;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

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


    private DatabaseReference mDatabase;
    String id;

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
                    JSONObject jsonResponse = obj.getJSONObject("product").getJSONObject("nutriments");

                    String salt = jsonResponse.getString("salt_100g");
                    String carbohydrates = jsonResponse.getString("carbohydrates_100g");
                    String energy = jsonResponse.getString("energy-kcal_100g");
                    String proteins = jsonResponse.getString("proteins_100g");
                    String saturated = jsonResponse.getString("saturated-fat_100g");


                    jsonResponse = obj.getJSONObject("product").getJSONObject("nutriscore_data");
                    String scoreLetter = jsonResponse.getString("grade");
                    String scoreNumber = jsonResponse.getString("score");

                    jsonResponse = obj.getJSONObject("product");
                    String nombre = jsonResponse.getString("product_name");


                    String marca = jsonResponse.getString("brands");

                    String imagen = jsonResponse.getString("image_front_url");

                    String id = jsonResponse.getString("_id");
                    comida = new Comida(id, nombre, salt, carbohydrates, energy, proteins, saturated, scoreLetter, marca, imagen);


                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    id = FirebaseAuth.getInstance().getUid();

                    String idd = jsonResponse.getString("_id");
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
                } else {
                    cameraSource.stop();

                    new AlertDialog.Builder(BarcodeScanner.this)
                            .setTitle("BestFood")
                            .setMessage("Producto no encontrado")
                            .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                dialog.dismiss(); finish();})
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}