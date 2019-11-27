package com.example.luisreyes.proyecto_aguas;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;

/**
 * Created by Administrador on 6/10/2019.
 */

public class MessageDialog extends AppCompatDialogFragment {

    public TextView textView_mensaje;
    private Dialog.DialogListener listener;
    AlertDialog.Builder builder;

    private static String hint="";
    private static String title="";

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.message_dialog, null);
        builder.setView(view)
                .setTitle(title)
                .setIcon(getResources().getDrawable(R.drawable.ic_info_blue_black_24dp))
                .setPositiveButton("aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        textView_mensaje = view.findViewById(R.id.textView_layout_dialog);
        textView_mensaje.setHint(hint);
        return  builder.create();
    }

    public void setTitleAndHint(String title_tag, String hint_tag){
        hint = hint_tag;
        title = title_tag;
    }
    public static String getTitle(){
        return title;
    }
}
