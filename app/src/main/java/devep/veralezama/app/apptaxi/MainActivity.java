package devep.veralezama.app.apptaxi;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuAdapter;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;
import nl.psdcompany.duonavigationdrawer.views.DuoMenuView;
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle;

public class MainActivity extends AppCompatActivity implements DuoMenuView.OnMenuClickListener{

    @BindView(R.id.menuNavegacion) DuoDrawerLayout menuNavegador;
    @BindView(R.id.menuOpcion) DuoMenuView menuNavegacionOpcion;
    @BindView(R.id.barraNavegacion) Toolbar barraNavegador;

    private ArrayList<String> mlistaOpciones = new ArrayList<>();
    private MenuAdaptador mMenuAdaptador = null;
    private Context mContexto = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        inicializar();

        mlistaOpciones = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.menuOpciones)));

        // Handle menu actions
        encargadoMenu();

        // Handle drawer actions
        encargadoDrawer();

        // Show main fragment in container
        irFragmento(new MapFragment(), false);
        mMenuAdaptador.setVistaSeleccionada(0, true);
        setTitle(mlistaOpciones.get(0));
    }

    private void inicializar(){
        mContexto = this;
    }

    private void encargadoDrawer() {
        DuoDrawerToggle duoDrawerToggle = new DuoDrawerToggle(this,
                menuNavegador,
                barraNavegador,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        menuNavegador.setDrawerListener(duoDrawerToggle);
        duoDrawerToggle.syncState();

    }

    private void encargadoMenu() {
        mMenuAdaptador = new MenuAdaptador(mlistaOpciones);
        menuNavegacionOpcion.setOnMenuClickListener(this);
        menuNavegacionOpcion.setAdapter(mMenuAdaptador);
    }

    @Override
    public void onFooterClicked() {

    }

    @Override
    public void onHeaderClicked() {

    }

    private void irFragmento(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.add(R.id.contenedor, fragment).commit();
    }

    @Override
    public void onOptionClicked(int posicion, Object objectClicked) {
        // Set the toolbar title
        setTitle(mlistaOpciones.get(posicion));

        // Set the right options selected
        mMenuAdaptador.setVistaSeleccionada(posicion, true);

        // Navigate to the right fragment
        switch (posicion) {
            default:
                irFragmento(new MapFragment(), false);
                break;
        }

        // Close the drawer
        menuNavegador.closeDrawer();
    }
}
