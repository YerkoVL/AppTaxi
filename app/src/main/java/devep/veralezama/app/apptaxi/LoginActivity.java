package devep.veralezama.app.apptaxi;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth autorizacion;
    FirebaseDatabase db;
    DatabaseReference usuarios;
    Context ctx;

    @BindView(R.id.logEdtEmail) EditText correo;
    @BindView(R.id.logEdtPassword)
    EditText password;
    @BindView(R.id.logBtnIngresar)
    TextView ingresar;
    @BindView(R.id.logBtnRegistrar)
    TextView registrar;

    private String valCorreo, valPassword;

    public static final int MY_PERMISSIONS_REQUEST_UBICACION = 1;
    public static final int MY_PERMISSIONS_REQUEST_ALMACENAMIENTO = 2;
    public static final int MY_PERMISSIONS_REQUEST_LLAMADA = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        inicializar();

        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerTextos();
                validarTextos();
                autorizacion.signInWithEmailAndPassword(valCorreo,valPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ctx, getResources().getString(R.string.error) + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
    }

    private void inicializar(){
        ctx = this;
        autorizacion = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        usuarios = db.getReference("Usuarios");

        validarPermisosUbicacion();
    }

    private void obtenerTextos() {
        valCorreo = correo.getText().toString();
        valPassword = password.getText().toString();
    }

    private void validarTextos() {
        if (TextUtils.isEmpty(valCorreo)) {
            Toast.makeText(ctx, "Ingrese correo", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(valPassword)) {
            Toast.makeText(ctx, "Ingrese contraseña", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void validarPermisosUbicacion(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_UBICACION);
            }
        }
    }
    public void validarPermisosAlmacenamiento(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_ALMACENAMIENTO);

            }
        }
    }
    public void validarPermisosLlamada(){

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CALL_PHONE)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_LLAMADA);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ALMACENAMIENTO: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }

                validarPermisosLlamada();
                return;
            }
            case MY_PERMISSIONS_REQUEST_LLAMADA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }

                return;
            }
            case MY_PERMISSIONS_REQUEST_UBICACION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                validarPermisosAlmacenamiento();
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
