package org.coursera.capstone.android.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.coursera.capstone.android.R;
import org.coursera.capstone.android.constant.CapstoneConstants;
import org.coursera.capstone.android.parcelable.Patient;
import org.coursera.capstone.android.parcelable.Question;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link org.coursera.capstone.android.fragment.CheckInFragment.OnQuestionsAnsweredListener} interface
 * to handle interaction events.
 * Use the {@link CheckInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckInFragment extends Fragment {
    private static final String QUESTIONS_PARAM = "questions_param";
    private static final String PATIENT_PARAM = "patient_param";

    private ArrayList<Question> mQuestions;
    private Patient mPatient;

    private OnQuestionsAnsweredListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param questions The questions to be answerd.
     * @param patient   The patient answering the questions.
     * @return A new instance of fragment CheckinFragment.
     */
    public static CheckInFragment newInstance(ArrayList<Question> questions, Patient patient) {
        CheckInFragment fragment = new CheckInFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(QUESTIONS_PARAM, questions);
        args.putParcelable(PATIENT_PARAM, patient);
        fragment.setArguments(args);
        return fragment;
    }

    public CheckInFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mQuestions = getArguments().getParcelableArrayList(QUESTIONS_PARAM);
            mPatient = getArguments().getParcelable(PATIENT_PARAM);
            Log.i(CapstoneConstants.LOG_TAG, "Successfully started checkin fragment for patient " + mPatient.getUsername() +
                    " with " + mQuestions.size() + " questions");
        } else {
            throw new IllegalArgumentException("Questions and Patient is required");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_checkin, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnQuestionsAnsweredListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     */
    public interface OnQuestionsAnsweredListener {
        public void onAllQuestionsAnswered();
    }

}
