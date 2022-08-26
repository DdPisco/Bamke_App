package com.example.bamke_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    /*Se declaran todas las variables que se usaran en este Layout*/
    TextView bienvenidolabel,continuarlabel,nuevoUsuario;
    ImageView singUpImageView;
    TextInputLayout usuarioSingUpTextField,contrasenaTextField;
    MaterialButton inicioSesion;
    EditText emailEditText,passwordEditText,confirmarPasswordEditText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        /*Se instancian todas las variables a usarse dentro de este Layout*/
        singUpImageView = findViewById(R.id.signUpImageView);
        bienvenidolabel = findViewById(R.id.bienvenidoLabel);
        continuarlabel = findViewById(R.id.continuarLabel);
        nuevoUsuario = findViewById(R.id.nuevoUsuario);
        usuarioSingUpTextField = findViewById(R.id.usuarioTextField);
        contrasenaTextField = findViewById(R.id.contraseñaTextField);
        inicioSesion = findViewById(R.id.inicioSesion);
        emailEditText = findViewById(R.id.EmailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmarPasswordEditText = findViewById(R.id.confirmarPasswordEditText);

        /*
        Este metodo regresa al Layout principal del Login
         */
        /*Este metodo se da uso cuando un nuevo usuario desea registrar sus datos en la App*/
        nuevoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transitionBack();
            }
        });
        /*
        Este metodo es unicamente cuando ya se encuentran del usuario luego de
        que se ingresaron.
         */
        inicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validar();
            }
        });
        mAuth =FirebaseAuth.getInstance();
    }


    public void validar() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmarPassword = confirmarPasswordEditText.getText().toString().trim();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Correo Invalido");
            return;
        } else {
            emailEditText.setError(null);
        }
        if (password.isEmpty() || password.length() < 8) {
            passwordEditText.setError("Se necesitan mas de 8 caracteres");
            return;
        } else if (!Pattern.compile("[0-9]").matcher(password).find()) {
            passwordEditText.setError("Al menos 1 numero");
            return;
        } else {
            passwordEditText.setError(null);
        }
        if (!confirmarPassword.equals(password)) {
            confirmarPasswordEditText.setError("Las contraseñas deben ser iguales");
        } else {
            registrar(email, password);
        }
    }
    @Override
    public void onBackPressed(){
        transitionBack();
    }

    public void transitionBack() {
        Intent intent = new Intent(SignUpActivity.this, MainActivity2.class);
        Pair[] pairs = new Pair[7];
        pairs[0] = new Pair<View, String>(singUpImageView, "logoImageTrans");
        pairs[1] = new Pair<View, String>(bienvenidolabel, "textTrans");
        pairs[2] = new Pair<View, String>(continuarlabel, "iniciaSesionTextTrans");
        pairs[3] = new Pair<View, String>(nuevoUsuario, "newUserTrans");
        pairs[4] = new Pair<View, String>(usuarioSingUpTextField, "emailInputTextTrans");
        pairs[5] = new Pair<View, String>(contrasenaTextField, "passwordInputTextTrans");
        pairs[6] = new Pair<View, String>(inicioSesion, "buttonSignInTrans");

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUpActivity.this,pairs);
            startActivity(intent,options.toBundle());
        }else {
            startActivity(intent);
            finish();
        }



    }

    public void registrar(String email, String password){
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(SignUpActivity.this,UserActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(SignUpActivity.this,"Fallo al registrarse",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}