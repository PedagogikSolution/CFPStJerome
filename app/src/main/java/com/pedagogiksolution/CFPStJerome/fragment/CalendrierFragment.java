package com.pedagogiksolution.CFPStJerome.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.pedagogiksolution.CFPStJerome.R;
import com.pedagogiksolution.CFPStJerome.adapter.CalendrierAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CalendrierFragment extends Fragment {

    String aout,septembre, octobre, novembre, decembre, janvier, fevrier, mars, avril, mai, juin,juillet;
    CalendrierAdapter mAdapter;
    int month;
    String line;
    StringBuilder sb_aout,sb_septembre,sb_octobre,sb_novembre,sb_decembre,sb_janvier,sb_fevrier,sb_mars,sb_avril,sb_mai,sb_juin,sb_juillet;
    FirebaseFirestore db;
    List<Fragment> fragments;
    ViewPager pager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Date date = new Date();
        String dateFormat = "MM";

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.CANADA_FRENCH);
        String month_x = sdf.format(date);
        int month_y = Integer.parseInt(month_x);

        month = 0;
        if (month_y == 8) {
            month = 0;
        }
        if (month_y == 9) {
            month = 1;
        }
        if (month_y == 10) {
            month = 2;
        }
        if (month_y == 11) {
            month = 3;
        }
        if (month_y == 12) {
            month = 4;
        }
        if (month_y == 1) {
            month = 5;
        }
        if (month_y == 2) {
            month = 6;
        }
        if (month_y == 3) {
            month = 7;
        }
        if (month_y == 4) {
            month = 8;
        }
        if (month_y == 5) {
            month = 9;
        }
        if (month_y == 6) {
            month = 10;
        }
        if (month_y == 7) {
            month = 11;
        }



        View v = inflater.inflate(R.layout.calendrier_view_pager, container, false);

        pager = v.findViewById(R.id.viewpager);

        fragments = new ArrayList<>();

        db = FirebaseFirestore.getInstance();



        db.collection("descriptionCalendrierPedagogique").orderBy("date")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            sb_aout = new StringBuilder();
                            sb_septembre = new StringBuilder();
                            sb_octobre = new StringBuilder();
                            sb_novembre = new StringBuilder();
                            sb_decembre = new StringBuilder();
                            sb_janvier = new StringBuilder();
                            sb_fevrier = new StringBuilder();
                            sb_mars = new StringBuilder();
                            sb_avril = new StringBuilder();
                            sb_mai = new StringBuilder();
                            sb_juin = new StringBuilder();
                            sb_juillet = new StringBuilder();

                            for (DocumentSnapshot document : task.getResult()) {


                                Map mData = document.getData();
                                String date = (String) mData.get("date");
                                String description = (String) mData.get("description");
                                String mois = (String) mData.get("mois");
                                line = date + " : " + description + System.getProperty("line.separator");

                                switch (mois){
                                    case "aout":
                                        sb_aout.append(line);
                                        break;
                                    case "septembre":
                                        sb_septembre.append(line);
                                        break;
                                    case "octobre":
                                        sb_octobre.append(line);
                                        break;
                                    case "novembre":
                                        sb_novembre.append(line);
                                        break;
                                    case "decembre":
                                        sb_decembre.append(line);
                                        break;
                                    case "janvier":
                                        sb_janvier.append(line);
                                        break;
                                    case "fevrier":
                                        sb_fevrier.append(line);
                                        break;
                                    case "mars":
                                        sb_mars.append(line);
                                        break;
                                    case "avril":
                                        sb_avril.append(line);
                                        break;
                                    case "mai":
                                        sb_mai.append(line);
                                        break;
                                    case "juin":
                                        sb_juin.append(line);
                                        break;
                                    case "juillet":
                                        sb_juillet.append(line);
                                        break;

                                }


                            }

                            aout = sb_aout.toString();
                            septembre = sb_septembre.toString();
                            octobre = sb_octobre.toString();
                            novembre = sb_novembre.toString();
                            decembre = sb_decembre.toString();
                            janvier = sb_janvier.toString();
                            fevrier = sb_fevrier.toString();
                            mars = sb_mars.toString();
                            avril = sb_avril.toString();
                            mai = sb_mai.toString();
                            juin = sb_juin.toString();
                            juillet = sb_juillet.toString();


                            fragments.add(CalendrierMonthFragment.newInstance(8, aout));
                            fragments.add(CalendrierMonthFragment.newInstance(9, septembre));
                            fragments.add(CalendrierMonthFragment.newInstance(10, octobre));
                            fragments.add(CalendrierMonthFragment.newInstance(11, novembre));
                            fragments.add(CalendrierMonthFragment.newInstance(12, decembre));
                            fragments.add(CalendrierMonthFragment.newInstance(1, janvier));
                            fragments.add(CalendrierMonthFragment.newInstance(2, fevrier));
                            fragments.add(CalendrierMonthFragment.newInstance(3, mars));
                            fragments.add(CalendrierMonthFragment.newInstance(4, avril));
                            fragments.add(CalendrierMonthFragment.newInstance(5, mai));
                            fragments.add(CalendrierMonthFragment.newInstance(6, juin));
                            fragments.add(CalendrierMonthFragment.newInstance(7, juillet));

                            mAdapter = new CalendrierAdapter(getChildFragmentManager(), fragments);

                            pager.setAdapter(mAdapter);
                            pager.setCurrentItem(month);

                        } else {
                            // on affiche erreur dialogue
                        }
                    }
                });



        return v;
    }


}


