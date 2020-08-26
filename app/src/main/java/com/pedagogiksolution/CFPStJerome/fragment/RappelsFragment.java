package com.pedagogiksolution.CFPStJerome.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.ListFragment;

import com.pedagogiksolution.CFPStJerome.R;
import com.pedagogiksolution.CFPStJerome.adapter.RappelListViewAdapter;
import com.pedagogiksolution.CFPStJerome.utils.DatabaseHelper;


public class RappelsFragment extends ListFragment implements OnItemClickListener, OnItemLongClickListener {

    View v;
    RappelListViewAdapter mAdapter;
    DatabaseHelper mDatabase;
    int mId;
    String mUrl;
    Cursor map;
    AlertDialog.Builder builder;
    AlertDialog builder2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.rappel_listview, container, false);
        mDatabase = new DatabaseHelper(getActivity());
        mDatabase.open();
        setHasOptionsMenu(true);
        return v;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getListView().setDivider(this.getResources().getDrawable(R.drawable.transperent_color));
        getListView().setDividerHeight(5);

        new Handler().post(new Runnable() {

            @Override
            public void run() {

                mAdapter = new RappelListViewAdapter(getActivity(), mDatabase.getListeRappel());

                setListAdapter(mAdapter);
            }
        });

        getListView().setOnItemClickListener(this);
        getListView().setOnItemLongClickListener(this);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.main, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.ajouter_rappel :
                Fragment fragment = new RappelsAjoutFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                break;
            case R.id.delete_rappel :
                builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("INFORMATION");
                builder.setMessage("Pour détruire un rappel, vous n'avez qu'à appuyer longuement sur la fiche de celui-ci");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                builder2=builder.create();
                builder2.show();

                break;
        }
        return true;
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

        if (builder2 != null) {
            builder2.dismiss();
            builder2 = null;
        }
    }





    @Override
    public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {

        map = (Cursor) adapter.getAdapter().getItem(position);
        mUrl = map.getString(map.getColumnIndex(map.getColumnName(4)));

        // TODO on envoie vers le url dans une webview

    }


    @Override
    public boolean onItemLongClick(AdapterView<?> adapter, View v, int position,
                                   long id) {
        map = (Cursor) adapter.getAdapter().getItem(position);
        mId = map.getInt(map.getColumnIndex(map.getColumnName(0)));
        mDatabase = new DatabaseHelper(getActivity());
        mDatabase.open();
        mDatabase.deleteMesRappels(mId);
        mDatabase.close();

        Fragment fragment = new RappelsFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        return true;
    }


}
