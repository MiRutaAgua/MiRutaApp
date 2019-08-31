package com.example.luisreyes.proyecto_aguas;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by luis.reyes on 30/08/2019.
 */

public class Dialog extends AppCompatDialogFragment {

    EditText editText_observaciones;
    private DialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"Debes implementar DialogListener");
        }
    }

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);
        builder.setView(view)
                .setTitle("Observaciones")
                .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                })
                .setPositiveButton("aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String observaciones_string = editText_observaciones.getText().toString();
                        listener.pasarTexto(observaciones_string);
                    }
                });

        editText_observaciones = view.findViewById(R.id.edit_usename_layout_dialog);
        return  builder.create();
    }

    public interface DialogListener{

        void pasarTexto(String observaciones);
    }
}
