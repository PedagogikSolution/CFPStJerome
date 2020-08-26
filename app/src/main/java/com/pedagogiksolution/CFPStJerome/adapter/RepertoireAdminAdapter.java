package com.pedagogiksolution.CFPStJerome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pedagogiksolution.CFPStJerome.R;
import com.pedagogiksolution.CFPStJerome.beans.RepertoireAdminBean;

import java.util.ArrayList;

public class RepertoireAdminAdapter extends ArrayAdapter<RepertoireAdminBean> {

        Context mContext;

private static class ViewHolder {
    TextView titre, nom,courriel,poste;

}
 public RepertoireAdminAdapter(ArrayList<RepertoireAdminBean> data, Context context) {
    super(context, R.layout.repertoire_admin_list_item,data);

    this.mContext=context;
 }
    public View getView(int position, View convertView, ViewGroup parent) {

        RepertoireAdminBean mBean = getItem(position);

        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.repertoire_admin_list_item, parent, false);
            viewHolder.titre = convertView.findViewById(R.id.titre);
            viewHolder.nom = convertView.findViewById(R.id.nom);
            viewHolder.courriel = convertView.findViewById(R.id.courriel);
            viewHolder.poste = convertView.findViewById(R.id.poste);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        viewHolder.titre.setText( mBean.getFonction());
        viewHolder.nom.setText(mBean.getNom()+", "+mBean.getPrenom());
        viewHolder.courriel.setText( mBean.getCourriel());
        viewHolder.poste.setText( mBean.getPoste());

        return convertView;

    }


}