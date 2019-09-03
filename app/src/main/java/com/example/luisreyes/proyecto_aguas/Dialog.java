package com.example.luisreyes.proyecto_aguas;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;

/**
 * Created by luis.reyes on 30/08/2019.
 */

public class Dialog extends AppCompatDialogFragment {

    public EditText editText_observaciones;
    private DialogListener listener;
    AlertDialog.Builder builder;

    private static String hint="";
    private static String title="";
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
        builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);
        builder.setView(view)
                .setTitle(title)
                .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                })
                .setPositiveButton("aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String observaciones_string = editText_observaciones.getText().toString();
                        try {
                            listener.pasarTexto(observaciones_string);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        editText_observaciones = view.findViewById(R.id.edit_usename_layout_dialog);
        editText_observaciones.setHint(hint);
        return  builder.create();
    }

    public interface DialogListener{

        void pasarTexto(String observaciones) throws JSONException;
    }

    public void setTitleAndHint(String title_tag, String hint_tag){
        hint = hint_tag;
        title = title_tag;
    }
}
