package com.pedagogiksolution.CFPStJerome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pedagogiksolution.CFPStJerome.R;
import com.pedagogiksolution.CFPStJerome.beans.RepertoireEnseignantBean;

import java.util.ArrayList;

public class RepertoireEnseignantAdapter extends ArrayAdapter<RepertoireEnseignantBean> {
    private ArrayList<RepertoireEnseignantBean> dataSet;
        Context mContext;

    /*private view holder class*/
private static class ViewHolder {
    TextView nom, matiere,courriel,poste;
    ImageView image;

}
 public RepertoireEnseignantAdapter(ArrayList<RepertoireEnseignantBean> data, Context context) {
    super(context, R.layout.repertoire_prof_list_item,data);
    this.dataSet=data;
    this.mContext=context;
 }
    public View getView(int position, View convertView, ViewGroup parent) {

        RepertoireEnseignantBean mBean = getItem(position);

        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.repertoire_prof_list_item, parent, false);
            viewHolder.nom =  convertView.findViewById(R.id.nomProf);
            viewHolder.matiere = convertView.findViewById(R.id.matiereProf);
            viewHolder.courriel = convertView.findViewById(R.id.courrielProf);
            viewHolder.poste = convertView.findViewById(R.id.posteProf);
            viewHolder.image = convertView.findViewById(R.id.imgProf);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        viewHolder.nom.setText(mBean.getNom()+", "+mBean.getPrenom());
        viewHolder.matiere.setText(mBean.getMatiere());
        viewHolder.courriel.setText(mBean.getCourriel());
        viewHolder.poste.setText(mBean.getPoste());


        int mDrawable = getDrawableId(mBean.getMatiere());
        viewHolder.image.setImageResource(mDrawable);


        return convertView;

    }

    private int getDrawableId(String matiere) {

    int mDrawableId;

     switch(matiere){
        case "SASI/APED":
            mDrawableId= R.drawable.ic_sasi_aped;
            break;
        case "Secrétariat/Comptabilité":
            mDrawableId= R.drawable.ic_secretariat_comptabilite;
            break;
        case "Assistance dentaire":
            mDrawableId= R.drawable.ic_assistance_dentaire;
            break;
        case "MICE/ESA":
            mDrawableId= R.drawable.ic_mice_esa;
            break;
        case "Assistance technique en pharmacie":
            mDrawableId= R.drawable.ic_assistance_pharmacie;
            break;

         default: mDrawableId=R.drawable.horaire_icone_adapt;

    }

    return  mDrawableId;
    }


}