package com.example.luisreyes.proyecto_aguas;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;

/**
 * Created by luis.reyes on 30/08/2019.
 */

public class Dialog_Email extends AppCompatDialogFragment {

    public EditText editText_from, editText_to, editText_subject, editText_body, edit_password_layout_dialog;
    private DialogEmailListener listener;
    AlertDialog.Builder builder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DialogEmailListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"Debes implementar DialogEmailListener");
        }
    }

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_email, null);
        builder.setView(view)
                .setTitle("Enviar Email")
                .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton("aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String from = "", pass = "", to = "", sub ="", body ="";
                        if(Screen_Login_Activity.checkStringVariable(editText_from.getText().toString())){
                            from = editText_from.getText().toString();
                        }
                        if(Screen_Login_Activity.checkStringVariable(edit_password_layout_dialog.getText().toString())){
                            pass = edit_password_layout_dialog.getText().toString();
                        }
                        if(Screen_Login_Activity.checkStringVariable(editText_to.getText().toString())){
                            to = editText_to.getText().toString();
                        }
                        if(Screen_Login_Activity.checkStringVariable(editText_subject.getText().toString())){
                            sub = editText_subject.getText().toString();
                        }
                        if(Screen_Login_Activity.checkStringVariable(editText_body.getText().toString())){
                            body = editText_body.getText().toString();
                        }
                        try {
                            listener.pasarInfoEmail(from, pass, to, sub, body);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        editText_from = view.findViewById(R.id.edit_myemail_layout_dialog);
        editText_to = view.findViewById(R.id.edit_toemail_layout_dialog);
        editText_subject = view.findViewById(R.id.edit_subject_layout_dialog);
        editText_body = view.findViewById(R.id.edit_body_layout_dialog);
        edit_password_layout_dialog =  view.findViewById(R.id.edit_password_layout_dialog);
        return  builder.create();
    }

    public interface DialogEmailListener{
        void pasarInfoEmail(String from, String password, String to, String subject, String body) throws JSONException;
    }
}
