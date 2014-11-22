package org.coursera.capstone.android.fragment;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.coursera.capstone.android.R;
import org.coursera.capstone.android.constant.CapstoneConstants;
import org.coursera.capstone.android.parcelable.Patient;

import java.util.Locale;

public class DoctorPatientDetailsFragment extends Fragment implements ActionBar.TabListener {
    private static String PATIENT_PARAM = "patient_param";

    private Patient mPatient;

    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    public static DoctorPatientDetailsFragment newInstance(Patient patient) {
        DoctorPatientDetailsFragment fragment = new DoctorPatientDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(PATIENT_PARAM, patient);
        fragment.setArguments(args);
        return fragment;
    }

    public DoctorPatientDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mPatient = getArguments().getParcelable(PATIENT_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.activity_doctor_patient_details_fragment, container, false);

        // Set up the action bar.
        final ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) v.findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

        return v;
    }

    @Override
    public void onDestroyView() {
        // Reset the action bar.
        final ActionBar actionBar = getActivity().getActionBar();
        actionBar.removeAllTabs();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        super.onDestroyView();
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.);
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        private static final int TOTAL_PAGES = 3;

        private static final int ALERTS_PAGE = 0;
        private static final int CHECK_IN_PAGE = 1;
        private static final int MEDICATIONS_PAGE = 2;

        private PlaceholderFragment mAlertsFragment;
        private PlaceholderFragment mCheckInsFragment;
        private PlaceholderFragment mMedicationsFragment;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case ALERTS_PAGE:
                    if (mAlertsFragment == null) {
                        mAlertsFragment = PlaceholderFragment.newInstance(
                                getString(R.string.doctor_patient_alerts_title));
                    }
                    Log.i(CapstoneConstants.LOG_TAG, "Returning alerts");
                    return mAlertsFragment;
                case CHECK_IN_PAGE:
                    if (mCheckInsFragment == null) {
                        mCheckInsFragment = PlaceholderFragment.newInstance(
                                getString(R.string.doctor_patient_check_ins_title));
                    }
                    Log.i(CapstoneConstants.LOG_TAG, "Returning check ins");
                    return mCheckInsFragment;
                case MEDICATIONS_PAGE:
                    if (mMedicationsFragment == null) {
                        mMedicationsFragment = PlaceholderFragment.newInstance(
                                getString(R.string.doctor_patient_medications_title));
                    }
                    Log.i(CapstoneConstants.LOG_TAG, "Returning medications");
                    return mMedicationsFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return TOTAL_PAGES;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.doctor_patient_alerts_title).toUpperCase(l);
                case 1:
                    return getString(R.string.doctor_patient_check_ins_title).toUpperCase(l);
                case 2:
                    return getString(R.string.doctor_patient_medications_title).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String SECTION_TEXT_PARAM = "section_text_param";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(String sectionText) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putString(SECTION_TEXT_PARAM, sectionText);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_doctor_patient_details, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText("Section: " + getArguments().getString(SECTION_TEXT_PARAM));
            return rootView;
        }
    }

}
