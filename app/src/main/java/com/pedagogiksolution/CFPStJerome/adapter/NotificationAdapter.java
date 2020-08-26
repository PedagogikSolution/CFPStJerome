package com.pedagogiksolution.CFPStJerome.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.pedagogiksolution.CFPStJerome.R;
import com.pedagogiksolution.CFPStJerome.beans.NotificationsBean;

import java.util.ArrayList;

public class NotificationAdapter extends ArrayAdapter<NotificationsBean> {
    ArrayList<NotificationsBean> dataModels;
    Context mContext;
    NotificationsBean mBean;

    private static class ViewHolder {
        public Switch toggleSwitch;
        public TextView nomCategorie;

    }

    public NotificationAdapter(ArrayList<NotificationsBean> data, Context context) {
        super(context, R.layout.notification_list_item, data);
        this.mContext = context;
        this.dataModels = data;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        LayoutInflater inflater;
        mBean = dataModels.get(position);

        if (convertView == null) {
            viewHolder = new ViewHolder();
            inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.notification_list_item, parent, false);

           viewHolder.nomCategorie = convertView.findViewById(R.id.nomCategorie);
            viewHolder.toggleSwitch = convertView.findViewById(R.id.switchSegment);
            convertView.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }

        viewHolder.toggleSwitch.setVisibility(View.VISIBLE);
        viewHolder.nomCategorie.setVisibility(View.VISIBLE);

            if (mBean.getSection()) {

                viewHolder.nomCategorie.setText(mBean.getCategorie());
                viewHolder.toggleSwitch.setOnClickListener(null);
                viewHolder.toggleSwitch.setVisibility(View.GONE);

            } else {
                viewHolder.nomCategorie.setVisibility(View.GONE);
                viewHolder.toggleSwitch.setText(mBean.getSegment());
                boolean checkAbonnement = checkAbonnement(mBean.getDocumentId());
                viewHolder.toggleSwitch.setChecked(checkAbonnement);

           }

        return convertView;

    }


    private Boolean checkAbonnement(String documentId) {

        SharedPreferences mSharedRef = getContext().getSharedPreferences(documentId, 0);

        return mSharedRef.getBoolean("Abonnement", false);
    }

    @Override
    public int getViewTypeCount() {
        if (getCount()!=0){
            return getCount();
        }
        return 1;
    }
}