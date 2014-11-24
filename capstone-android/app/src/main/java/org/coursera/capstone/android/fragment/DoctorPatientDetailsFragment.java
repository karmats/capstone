package org.coursera.capstone.android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.coursera.capstone.android.R;
import org.coursera.capstone.android.constant.CapstoneConstants;
import org.coursera.capstone.android.parcelable.CheckInResponse;
import org.coursera.capstone.android.parcelable.PainMedication;
import org.coursera.capstone.android.parcelable.Patient;

import java.util.ArrayList;
import java.util.Locale;

public class DoctorPatientDetailsFragment extends Fragment {
    private static String PATIENT_PARAM = "patient_param";
    private static String PAIN_MEDICATIONS_PARAM = "pain_medications_param";
    private static String CHECK_INS_PARAM = "check_ins_param";

    private Patient mPatient;
    private ArrayList<PainMedication> mAllPainMedications;
    private ArrayList<CheckInResponse> mCheckIns;

    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    public static DoctorPatientDetailsFragment newInstance(Patient patient, ArrayList<PainMedication> painMedications, ArrayList<CheckInResponse> checkIns) {
        DoctorPatientDetailsFragment fragment = new DoctorPatientDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(PATIENT_PARAM, patient);
        args.putParcelableArrayList(PAIN_MEDICATIONS_PARAM, painMedications);
        args.putParcelableArrayList(CHECK_INS_PARAM, checkIns);
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
            mAllPainMedications = getArguments().getParcelableArrayList(PAIN_MEDICATIONS_PARAM);
            mCheckIns = getArguments().getParcelableArrayList(CHECK_INS_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.activity_doctor_patient_details_fragment, container, false);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) v.findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        PagerTabStrip pagerTabStrip = (PagerTabStrip) v.findViewById(R.id.pager_title_strip);
        pagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.pager_tab_indicator));

        TextView patientName = (TextView) v.findViewById(R.id.doctor_patient_details_name);
        patientName.setText(mPatient.getFirstName() + " " + mPatient.getLastName());

        return v;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        private static final int TOTAL_PAGES = 2;

        private static final int CHECK_IN_PAGE = 0;
        private static final int MEDICATIONS_PAGE = 1;

        private ListCheckInsFragment mCheckInsFragment;
        private UpdateMedicationsFragment mMedicationsFragment;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case CHECK_IN_PAGE:
                    if (mCheckInsFragment == null) {
                        mCheckInsFragment = ListCheckInsFragment.newInstance(mCheckIns);
                    }
                    return mCheckInsFragment;
                case MEDICATIONS_PAGE:
                    if (mMedicationsFragment == null) {
                        mMedicationsFragment = UpdateMedicationsFragment.newInstance(mPatient, mAllPainMedications);
                    }
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
                case CHECK_IN_PAGE:
                    return getString(R.string.doctor_patient_check_ins_title).toUpperCase(l);
                case MEDICATIONS_PAGE:
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
