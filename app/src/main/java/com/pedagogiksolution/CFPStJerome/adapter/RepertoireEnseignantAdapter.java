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
        case "Adjoint Administratif":
            mDrawableId= R.drawable.ic_adjoint_adminitratif;
            break;
        case "Dessin Industriel":
            mDrawableId= R.drawable.ic_dessin_technique;
            break;
        case "Représentation":
            mDrawableId= R.drawable.ic_representation;
            break;
        case "Vente-Conseil":
            mDrawableId= R.drawable.ic_vente_conseil;
            break;
        case "Électricité":
            mDrawableId= R.drawable.ic_electricite;
            break;
         case "Carrosserie":
             mDrawableId= R.drawable.ic_carrosserie;
             break;
         case "Électromécanique de systèmes automatisés":
             mDrawableId= R.drawable.ic_electromecanique;
             break;
         case "Soudage-Montage":
             mDrawableId= R.drawable.ic_soudage;
             break;
         case "Esthétique automobile":
             mDrawableId= R.drawable.ic_esthetique_auto;
             break;
         case "Mécanique automobile":
             mDrawableId= R.drawable.ic_mecanique_auto;
             break;
         case "Secrétariat":
             mDrawableId= R.drawable.ic_secretariat;
             break;
         case "Conseil et vente de pièces d'équipement motorisé":
             mDrawableId= R.drawable.ic_conseil_vente_auto;
             break;
         case "Comptabilité":
             mDrawableId= R.drawable.ic_comptabilite;
             break;
         case "Lancement d'une entreprise":
             mDrawableId= R.drawable.ic_entreprise;
             break;
         case "Technique d'usinage":
             mDrawableId= R.drawable.ic_technique_usinage;
             break;
         case "Tôlerie de précision":
             mDrawableId= R.drawable.ic_tolerie;
             break;
         case "Ferblanterie":
             mDrawableId= R.drawable.ic_ferblantier;
             break;

         default: mDrawableId=R.drawable.horaire_icone_adapt;

    }

    return  mDrawableId;
    }


}