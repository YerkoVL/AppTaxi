package devep.veralezama.app.apptaxi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import devep.veralezama.app.apptaxi.Modelos.Usuarios;

import static android.widget.Toast.*;
import static devep.veralezama.app.apptaxi.Utilitarios.Genericos.GENERAL_ESTADO_HABILITADO;
import static devep.veralezama.app.apptaxi.Utilitarios.Genericos.GENERAL_PERFIL_EMPLEADOS;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth autorizacion;
    FirebaseDatabase db;
    DatabaseReference usuarios;
    Context ctx;
    LocationManager adminLocalizacion;

    @BindView(R.id.regEdtCorreo)
    EditText correo;
    @BindView(R.id.regEdtNombres)
    EditText nombres;
    @BindView(R.id.regEdtApellidos)
    EditText apellidos;
    @BindView(R.id.regEdtCelular)
    EditText celular;
    @BindView(R.id.regEdtPass)
    EditText password;
    @BindView(R.id.regEdtRepPass)
    EditText repPassword;
    @BindView(R.id.regEdtBtnRegistrar)
    TextView registrar;

    String valCorreo, valNombres, valApellidos, valCelular, valPassword, valRepPassword;
    double latitud = 0, longitud= 0;
    boolean bandera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        adminLocalizacion = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerTextos();
                if(validarTextos()){
                    if (verificarUbicacion()) {
                        adminLocalizacion.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2 * 20 * 1000, 10, escuchadorLocalizacion);
                        autorizacion.createUserWithEmailAndPassword(valCorreo, valPassword)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult resultadoAutorizacion) {
                                        Usuarios usuario = new Usuarios();
                                        usuario.setNombres(valNombres);
                                        usuario.setApellidos(valApellidos);
                                        usuario.setCelular(valCelular);
                                        usuario.setPassword(valPassword);
                                        usuario.setLatitud(String.valueOf(latitud));
                                        usuario.setLongitud(String.valueOf(longitud));
                                        usuario.setIdEstado(String.valueOf(GENERAL_ESTADO_HABILITADO));
                                        usuario.setIdPerfil(String.valueOf(GENERAL_PERFIL_EMPLEADOS));

                                        usuarios.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                makeText(ctx, getResources().getString(R.string.regCompleto), LENGTH_SHORT).show();
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                makeText(ctx, getResources().getString(R.string.error) + e.getMessage(), LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        makeText(ctx, getResources().getString(R.string.errorInicio) + e.getMessage(), LENGTH_SHORT).show();
                                    }
                                });
                    }else{
                        Toast.makeText(ctx,"Debe habilitar su GPS",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        inicializar();
    }

    private void inicializar() {
        ctx = this;
        autorizacion = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        usuarios = db.getReference("Usuarios");
    }

    private void obtenerTextos() {
        valCorreo = correo.getText().toString();
        valNombres = nombres.getText().toString();
        valApellidos = apellidos.getText().toString();
        valPassword = password.getText().toString();
        valRepPassword = repPassword.getText().toString();
        valCelular = celular.getText().toString();
    }

    private boolean validarTextos() {
        if (TextUtils.isEmpty(valCorreo)) {
            makeText(ctx,R.string.regFaltaCorreo, LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(valNombres)) {
            makeText(ctx,R.string.regFaltaNombres, LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(valApellidos)) {
            makeText(ctx,R.string.regFaltaApellidos, LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(valCelular)) {
            makeText(ctx,R.string.regFaltaCelular, LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(valPassword)) {
            makeText(ctx,R.string.regFaltaContrasena, LENGTH_SHORT).show();
            return false;
        }
        if (valPassword.equals(valRepPassword)) {
            return true;
        }else{
            makeText(ctx,R.string.regNoCoincidenContrasena, LENGTH_SHORT).show();
            return false;
        }
    }

    private final LocationListener escuchadorLocalizacion = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            longitud = location.getLongitude();
            latitud = location.getLatitude();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private boolean verificarUbicacion() {
        if (!estaHabilitadaLocalizacion())
            mostrarAlerta();
            return estaHabilitadaLocalizacion();
    }

    private boolean estaHabilitadaLocalizacion() {
        return adminLocalizacion.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                adminLocalizacion.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void mostrarAlerta() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.regHabilitarGPS)
                .setMessage("Su ubicación esta desactivada.\npor favor active su ubicación " +
                        "usa esta app")
                .setPositiveButton("Configuración de ubicación", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }
}
