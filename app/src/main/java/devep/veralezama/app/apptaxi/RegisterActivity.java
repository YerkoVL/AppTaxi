package devep.veralezama.app.apptaxi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import devep.veralezama.app.apptaxi.Model.Usuarios;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth autorizacion;
    FirebaseDatabase db;
    DatabaseReference usuarios;
    Context ctx;

    @BindView(R.id.edtLogEmail) EditText email;
    @BindView(R.id.edtLogNombres) EditText nombres;
    @BindView(R.id.edtLogApellidos) EditText apellidos;
    @BindView(R.id.edtLogCelular) EditText celular;
    @BindView(R.id.edtLogPass) EditText password;

    String valEmail, valNombres, valApellidos, valCelular, valPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        inicializar();
        obtenerTextos();
        validarTextos();
    }

    private void inicializar(){
        ctx = this;
        autorizacion = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        usuarios = db.getReference("Usuarios");
    }

    private void obtenerTextos(){
        valEmail = email.getText().toString();
        valNombres = nombres.getText().toString();
        valApellidos = apellidos.getText().toString();
        valPassword = password.getText().toString();
        valCelular = celular.getText().toString();
    }

    private void validarTextos(){
        if(TextUtils.isEmpty(valEmail)){
            Toast.makeText(ctx,"Ingrese correo",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(valNombres)){
            Toast.makeText(ctx,"Ingrese nombres",Toast.LENGTH_SHORT).show();
            return;
        }if(TextUtils.isEmpty(valApellidos)){
            Toast.makeText(ctx,"Ingrese apellidos",Toast.LENGTH_SHORT).show();
            return;
        }if(TextUtils.isEmpty(valCelular)){
            Toast.makeText(ctx,"Ingrese celular",Toast.LENGTH_SHORT).show();
            return;
        }if(TextUtils.isEmpty(valPassword)){
            Toast.makeText(ctx,"Ingrese contraseña",Toast.LENGTH_SHORT).show();
            return;
        }
        autorizacion.createUserWithEmailAndPassword(valEmail,valPassword)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult resultadoAutorizacion) {
                Usuarios usuario = new Usuarios();
                usuario.setNombres(valNombres);
                usuario.setApellidos(valApellidos);
                usuario.setCelular(valCelular);
                usuario.setPassword(valPassword);

                usuarios.child(usuario.getEmail()).setValue(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ctx,"Registro completo",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ctx,"Error: " + e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ctx,"Error inicio: "+ e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
