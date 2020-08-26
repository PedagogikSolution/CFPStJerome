package com.pedagogiksolution.CFPStJerome.fragment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.ListFragment;

import com.pedagogiksolution.CFPStJerome.R;

public class PreferencesFragment extends ListFragment {
    SharedPreferences sharedPref;
    ProgressDialog progress;
    String[] preferences_items;
    ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater
                .inflate(R.layout.preferences_listview, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        progress = new ProgressDialog(getActivity());
        getListView().setDividerHeight(4);
        preferences_items = getResources().getStringArray(
                R.array.preferences_items_arrays);

        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, preferences_items);
        setListAdapter(adapter);

    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

        if (progress != null) {
            progress.dismiss();
            progress = null;

        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        switch (position) {

            case 0:
                Fragment fragment5 = new SolutionPedagogikFragment();
                FragmentManager fragmentManager5 = getFragmentManager();
                fragmentManager5.beginTransaction()
                        .replace(R.id.content_frame, fragment5).commit();
                break;

        }

    }


}
