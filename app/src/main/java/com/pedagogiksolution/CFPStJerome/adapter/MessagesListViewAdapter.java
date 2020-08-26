package com.pedagogiksolution.CFPStJerome.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.pedagogiksolution.CFPStJerome.R;


public class MessagesListViewAdapter extends CursorAdapter {


    public MessagesListViewAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor c, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return inflater.inflate(R.layout.mes_messages_list_item, parent, false);
    }

    @Override
    public void bindView(View v, Context context, Cursor cursor) {

        TextView messageTitre = v.findViewById(R.id.titreMessages);
        TextView messageDate = v.findViewById(R.id.dateMessages);
        TextView messageDesc = v.findViewById(R.id.bodyMessages);
        TextView messageUrl = v.findViewById(R.id.urlMessages);



        messageTitre.setText(cursor.getString(cursor.getColumnIndex(cursor
                .getColumnName(1))));
        messageDate.setText(cursor.getString(cursor.getColumnIndex(cursor
                .getColumnName(2))));
        messageDesc.setText(cursor.getString(cursor.getColumnIndex(cursor
                .getColumnName(3))));

        String mUrl = cursor.getString(cursor.getColumnIndex(cursor
                .getColumnName(4)));
        if(mUrl!=null) {
            messageUrl.setText(mUrl);
            messageUrl.setVisibility(TextView.VISIBLE);
        }


    }


}
