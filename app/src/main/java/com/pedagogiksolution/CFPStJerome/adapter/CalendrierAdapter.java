package com.pedagogiksolution.CFPStJerome.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class CalendrierAdapter extends FragmentStatePagerAdapter {

	private List<Fragment> fragments;

	  public CalendrierAdapter(FragmentManager fm, List<Fragment> fragments) {

	    super(fm);

	    this.fragments = fragments;

	  }

	  @Override

	  public Fragment getItem(int position) {

	    return this.fragments.get(position);

	  }

	  @Override

	  public int getCount() {

	    return this.fragments.size();

	  }


}
