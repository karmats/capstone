package org.coursera.capstone.android.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.coursera.capstone.android.R;
import org.coursera.capstone.android.animation.ScalePageTransformer;
import org.coursera.capstone.android.parcelable.Patient;
import org.coursera.capstone.android.parcelable.Question;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientCheckInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientCheckInFragment extends Fragment {
    public static final String TAG = "PATIENT_CHECK_IN_TAG";

    private static final String PATIENT_PARAM = "patient_param";
    private static final String QUESTIONS_PARAM = "questions_param";

    private Patient mPatient;
    private ArrayList<Question> mQuestions;
    private CheckIn mCheckIn;

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param patient   The patient making the check-in.
     * @param questions The questions to answer.
     * @return A new instance of fragment PatientCheckInFragment.
     */
    public static PatientCheckInFragment newInstance(Patient patient, ArrayList<Question> questions) {
        PatientCheckInFragment fragment = new PatientCheckInFragment();
        Bundle args = new Bundle();
        args.putParcelable(PATIENT_PARAM, patient);
        args.putParcelableArrayList(QUESTIONS_PARAM, questions);
        fragment.setArguments(args);
        return fragment;
    }

    public PatientCheckInFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPatient = getArguments().getParcelable(PATIENT_PARAM);
            mQuestions = getArguments().getParcelableArrayList(QUESTIONS_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_patient_check_in, container, false);

        // Create the adapter that will return a question fragment
        mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) v.findViewById(R.id.checkin_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setPageTransformer(false, new ScalePageTransformer());
        return v;
    }

    /**
     * Called from activity
     */
    public void nextQuestion(CheckIn checkIn) {
        this.mCheckIn = checkIn;
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
        if (mViewPager.getCurrentItem() == mViewPager.getAdapter().getCount() - 1) {
            // Update the check in in summary
            SectionsPagerAdapter adapter = (SectionsPagerAdapter) mViewPager.getAdapter();
            ((CheckInSummaryFragment) adapter.getItem(mViewPager.getCurrentItem())).updateCheckIn(mCheckIn);
        }
    }

    /**
     * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        // The check in summary fragment needs to be cached since it needs to be updated
        // when all questions are answered
        private CheckInSummaryFragment summaryFragment;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            if (mQuestions.size() > position) {
                return QuestionFragment.newInstance(mQuestions.get(position), null);
            } else if ((mQuestions.size() + mPatient.getMedications().size()) > position) {
                return QuestionFragment.newInstance(null, mPatient.getMedications().get(position - mQuestions.size()));
            } else {
                // All questions answered show summary view
                if (summaryFragment == null) {
                    summaryFragment = CheckInSummaryFragment.newInstance(mCheckIn);
                }
                return summaryFragment;
            }
        }

        @Override
        public int getCount() {
            // Patient needs to answer questions about the questions and his/hers medications
            // The +1 is for summary
            return mQuestions.size() + mPatient.getMedications().size() + 1;
        }
    }

}
