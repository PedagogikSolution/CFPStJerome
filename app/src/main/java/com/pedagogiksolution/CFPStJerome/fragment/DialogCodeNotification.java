package com.pedagogiksolution.CFPStJerome.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DialogCodeNotification extends DialogFragment {

    public DialogCodeNotification() {

    }

    public interface MonDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    public MonDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());

        builder.setTitle("Abonnement notification");

        builder.setMessage("Pour vous abonner a ce segment, vous devez entrer le code d'accès reçu de votre école ou enseignant");
        final EditText input = new EditText(getContext());
        input.setHint("Cliquez ici pour entrer le code");
        

        builder.setView(input);

        builder.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String code = input.getText().toString();
                SharedPreferences sharedPref;
                sharedPref = getContext().getSharedPreferences("code", 0);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("code", code);
                editor.apply();
                listener.onDialogPositiveClick(DialogCodeNotification.this);

            }
        });

        builder .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,	int which) {
                listener.onDialogNegativeClick(DialogCodeNotification.this);
            }
        });

        return builder.create();
    }


}
