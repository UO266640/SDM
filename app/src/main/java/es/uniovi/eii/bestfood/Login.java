package es.uniovi.eii.bestfood;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;


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

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(v -> {
            if (comprobarDatos(user, pass))

                login(user.getText().toString(), pass.getText().toString());

        });

        register.setOnClickListener(v -> {
            if (comprobarDatos(user, pass))

                register(user.getText().toString(), pass.getText().toString());
        });


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
