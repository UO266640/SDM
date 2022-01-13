package es.uniovi.eii.bestfood;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


    public void register(String user, String pass) {
        mAuth.createUserWithEmailAndPassword(user, pass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Intent myIntent = new Intent(Login.this, MainActivity.class);
                        Login.this.startActivity(myIntent);
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast toast = Toast.makeText(getApplicationContext(), R.string.correoya, Toast.LENGTH_SHORT);
                        toast.show();

                    }

                });
    }

    public void login(String user, String pass) {
        mAuth.signInWithEmailAndPassword(user, pass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Intent myIntent = new Intent(Login.this, MainActivity.class);
                        Login.this.startActivity(myIntent);
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast toast = Toast.makeText(getApplicationContext(), R.string.usinc, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final Button login = findViewById(R.id.login);
        final Button register = findViewById(R.id.register);
        final EditText user = findViewById(R.id.username);
        final EditText pass = findViewById(R.id.password);


        if (!getConexion()) {
            mostrarNoConexion();
        }

        int result = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this, 213655000);

        if (result != ConnectionResult.SUCCESS) {
            if (GoogleApiAvailability.getInstance().isUserResolvableError(result)) {
                GoogleApiAvailability.getInstance().getErrorDialog(this, result, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
        }

        ActivityCompat.requestPermissions(Login.this, new
                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(v -> {

            if (!getConexion()) {
                mostrarNoConexion();
            }
            int result2 = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this, 213655000);

            if (result2 != ConnectionResult.SUCCESS) {
                if (GoogleApiAvailability.getInstance().isUserResolvableError(result2)) {
                    GoogleApiAvailability.getInstance().getErrorDialog(this, result2, PLAY_SERVICES_RESOLUTION_REQUEST).show();
                }
            } else {
                if (comprobarDatos(user, pass))
                    login(user.getText().toString(), pass.getText().toString());
            }
        });

        register.setOnClickListener(v -> {
            int result3 = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this, 213655000);

            if (!getConexion()) {
                mostrarNoConexion();
            }
            if (result3 != ConnectionResult.SUCCESS) {
                if (GoogleApiAvailability.getInstance().isUserResolvableError(result3)) {
                    GoogleApiAvailability.getInstance().getErrorDialog(this, result3, PLAY_SERVICES_RESOLUTION_REQUEST).show();
                }
            } else {
                if (comprobarDatos(user, pass))

                    register(user.getText().toString(), pass.getText().toString());
            }
        });


    }

    public boolean getConexion() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        } else {
            return false;
        }
    }

    public void mostrarNoConexion() {
        new AlertDialog.Builder(Login.this)
                .setTitle("BestFood")
                .setMessage("Es necesario que tengas conexión a internet para que la aplicación funcione")
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            dialog.dismiss();
                        }
                )
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public boolean comprobarDatos(EditText user, EditText pass) {
        if (user.getText().length() == 0) {
            Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
            user.startAnimation(shake);
            user.setError(getString(R.string.correo));
            return false;
        }

        Pattern pattern = Patterns.EMAIL_ADDRESS;

        if (!pattern.matcher(user.getText()).matches()) {
            Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
            user.startAnimation(shake);
            user.setError(getString(R.string.correonovalido));
            return false;
        }

        if (pass.getText().length() == 0) {
            Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
            pass.startAnimation(shake);
            pass.setError(getString(R.string.passvacia));
            return false;

        }

        if (pass.getText().length() < 6) {
            Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
            pass.startAnimation(shake);
            pass.setError(getString(R.string.passmin6));
            return false;

        }

        return true;
    }

}
