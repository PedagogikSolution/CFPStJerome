package com.pedagogiksolution.CFPStJerome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pedagogiksolution.CFPStJerome.R;
import com.pedagogiksolution.CFPStJerome.beans.NouvellesBean;

import java.util.ArrayList;


public class NouvelleListViewAdapter extends ArrayAdapter<NouvellesBean> {

    Context mContext;

    private static class ViewHolder {
        TextView titre, body,date,url;

    }
    public NouvelleListViewAdapter(ArrayList<NouvellesBean> data, Context context) {
        super(context, R.layout.nouvelle_list_item,data);

        this.mContext=context;
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        NouvellesBean mBean = getItem(position);

        ViewHolder viewHolder;




        if (convertView == null) {

            viewHolder = new NouvelleListViewAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.nouvelle_list_item, parent, false);
            viewHolder.titre= convertView.findViewById(R.id.titreNews);
            viewHolder.body = convertView.findViewById(R.id.bodyNews);
            viewHolder.date = convertView.findViewById(R.id.dateNews);
            viewHolder.url = convertView.findViewById(R.id.urlNews);



            convertView.setTag(viewHolder);
        } else {
            viewHolder = (NouvelleListViewAdapter.ViewHolder) convertView.getTag();

        }

        if(mBean.getUrl()!=null) {
            viewHolder.titre.setText(mBean.getTitre());
            viewHolder.body.setText(mBean.getBody());
            viewHolder.date.setText(mBean.getDate());
            viewHolder.url.setVisibility(View.VISIBLE);
        } else {
            viewHolder.titre.setText(mBean.getTitre());
            viewHolder.body.setText(mBean.getBody());
            viewHolder.date.setText(mBean.getDate());
        }

        return convertView;

    }




}