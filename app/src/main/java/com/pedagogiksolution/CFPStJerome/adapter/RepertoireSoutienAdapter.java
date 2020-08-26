package com.pedagogiksolution.CFPStJerome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pedagogiksolution.CFPStJerome.R;
import com.pedagogiksolution.CFPStJerome.beans.RepertoireSoutienBean;

import java.util.ArrayList;

public class RepertoireSoutienAdapter extends ArrayAdapter<RepertoireSoutienBean> {
    private ArrayList<RepertoireSoutienBean> dataSet;
        Context mContext;

    /*private view holder class*/
private static class ViewHolder {
    TextView nom, fonction,courriel,poste;
    ImageView image;

}
 public RepertoireSoutienAdapter(ArrayList<RepertoireSoutienBean> data, Context context) {
    super(context, R.layout.repertoire_soutien_list_item,data);
    this.dataSet=data;
    this.mContext=context;
 }
    public View getView(int position, View convertView, ViewGroup parent) {

        RepertoireSoutienBean mBean = getItem(position);

        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.repertoire_soutien_list_item, parent, false);
            viewHolder.nom = convertView.findViewById(R.id.nomSoutien);
            viewHolder.fonction =  convertView.findViewById(R.id.fonctionSoutien);
            viewHolder.courriel = convertView.findViewById(R.id.courrielSoutien);
            viewHolder.poste = convertView.findViewById(R.id.posteSoutien);
            viewHolder.image = convertView.findViewById(R.id.imgSoutien);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        viewHolder.nom.setText( mBean.getNom()+", "+mBean.getPrenom());
        viewHolder.fonction.setText(mBean.getFonction());
        viewHolder.courriel.setText(mBean.getCourriel());
        viewHolder.poste.setText(mBean.getPoste());

        viewHolder.image.setImageResource(R.drawable.horaire_icone_adapt);


        return convertView;

    }




}