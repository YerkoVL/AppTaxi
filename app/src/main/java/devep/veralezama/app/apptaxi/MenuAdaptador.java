package devep.veralezama.app.apptaxi;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import nl.psdcompany.duonavigationdrawer.views.DuoOptionView;

class MenuAdaptador extends BaseAdapter{

    private ArrayList<String> mOpciones = new ArrayList<>();
    private ArrayList<DuoOptionView> mVistaOpciones = new ArrayList<>();

    MenuAdaptador(ArrayList<String> opciones) {
        mOpciones = opciones;
    }

    @Override
    public int getCount() {
        return mOpciones.size();
    }

    @Override
    public Object getItem(int posicion) {
        return mOpciones.get(posicion);
    }

    void setVistaSeleccionada(int posicion, boolean seleccionado) {

        // Looping through the options in the menu
        // Selecting the chosen option
        for (int i = 0; i < mVistaOpciones.size(); i++) {
            if (i == posicion) {
                mVistaOpciones.get(i).setSelected(seleccionado);
            } else {
                mVistaOpciones.get(i).setSelected(!seleccionado);
            }
        }
    }

    @Override
    public long getItemId(int posicion) {
        return posicion;
    }

    @Override
    public View getView(int posicion, View vistaConvertida, ViewGroup padre) {
        final String option = mOpciones.get(posicion);

        // Using the DuoOptionView to easily recreate the demo
        final DuoOptionView optionView;
        if (vistaConvertida == null) {
            optionView = new DuoOptionView(padre.getContext());
        } else {
            optionView = (DuoOptionView) vistaConvertida;
        }

        // Using the DuoOptionView's default selectors
        optionView.bind(option, null, null);

        // Adding the views to an array list to handle view selection
        mVistaOpciones.add(optionView);

        return optionView;
    }
}
