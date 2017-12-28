package devep.veralezama.app.apptaxi;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth autorizacion;
    FirebaseDatabase db;
    DatabaseReference usuarios;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inicializar();
    }

    private void inicializar(){
        ctx = this;
        autorizacion = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        usuarios = db.getReference("Usuarios");
    }
}
