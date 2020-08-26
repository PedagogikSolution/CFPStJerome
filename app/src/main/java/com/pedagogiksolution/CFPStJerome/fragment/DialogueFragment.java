package com.pedagogiksolution.CFPStJerome.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.pedagogiksolution.CFPStJerome.R;
import com.pedagogiksolution.CFPStJerome.utils.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DialogueFragment extends DialogFragment {




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        final Bundle mType = getArguments();

        int mDialogNumber = mType.getInt("dialogType");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        switch(mDialogNumber){

            case 1 : // Dialogue qui provient de la premiere visite quand redirigé dans section notification avec succes

                builder.setTitle("Message de bienvenue").setMessage("Bienvenue dans l'application du Centre de formation professionnel Performance Plus de Lachute. Afin de recevoir les notifications envoyées par l'école tel que les fermetures lors des journées de tempête, vous pouvez en tout temps vous inscrire ou désinscrire aux notifications personnalisées dans cette section en activant les boutons correspondants à vos choix")
                        .setPositiveButton("J'ai compris", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                // on change preference premiere visite a 1
                                SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.premiere_visite), Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putInt(getString(R.string.premiere_visite),1  );
                                editor.commit();


                                Fragment newFragment = new NotificationFragment();
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.content_frame, newFragment);
                                transaction.commit();
                            }
                        });



                break;

            case 2:
                builder.setTitle("Avertissement").setMessage("Vous devez être connecté pour accéder à cette section")
                        .setPositiveButton("J'ai compris", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                Fragment newFragment = new AccueilFragment();
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.content_frame, newFragment);
                                transaction.commit();
                            }
                        });



                break;

            case 4:
                builder.setTitle(mType.getString("title"));
                builder.setMessage(mType.getString("body"));
                builder.setPositiveButton("Sauvegarder", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        //on persiste dans bdd le message

                        DatabaseHelper mDbHelper = new DatabaseHelper(getContext());
                        mDbHelper.open();


                            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                            mDbHelper.insertMesMessages(mType.getString("title"), date, mType.getString("body"), mType.getString("url"));


                        mDbHelper.close();
                        Fragment newFragment = new MessagesFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.content_frame, newFragment);
                        transaction.commit();
                    }
                });
                builder.setNegativeButton("Détruire", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        Fragment newFragment = new MessagesFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.content_frame, newFragment);
                        transaction.commit();
                    }
                });

                break;

            default : //aucune alerte

        }

        // Create the AlertDialog object and return it
        return builder.create();

    }


}
