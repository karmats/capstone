package org.coursera.capstone.android.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import org.coursera.capstone.android.R;
import org.coursera.capstone.android.parcelable.CheckIn;
import org.coursera.capstone.android.util.UIUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CheckInSummaryFragment.OnCheckInSubmitListener} interface
 * to handle interaction events.
 * Use the {@link CheckInSummaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckInSummaryFragment extends Fragment {
    // The check-in to summarize.
    private static final String CHECK_IN_PARAM = "check_in_param";

    private CheckIn mCheckIn;

    private OnCheckInSubmitListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param checkIn The check-in to summarize.
     * @return A new instance of fragment CheckInSummaryFragment.
     */
    public static CheckInSummaryFragment newInstance(CheckIn checkIn) {
        CheckInSummaryFragment fragment = new CheckInSummaryFragment();
        Bundle args = new Bundle();
        args.putParcelable(CHECK_IN_PARAM, checkIn);
        fragment.setArguments(args);
        return fragment;
    }

    public CheckInSummaryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCheckIn = getArguments().getParcelable(CHECK_IN_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_check_in_summary, container, false);
        // Creates summary list
        createSummary(v);
        // Button for submission
        Button b = (Button) v.findViewById(R.id.patient_check_in_summary_submit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCheckInSubmit();
            }
        });
        return v;
    }

    public void updateCheckIn(CheckIn checkIn) {
        this.mCheckIn = checkIn;
        createSummary(getView());
    }

    private void createSummary(View v) {
        // Questions
        LinearLayout questionsView = (LinearLayout) v.findViewById(R.id.patient_check_in_summary_questions);
        questionsView.removeAllViews();
        for (CheckIn.QuestionAnswer qa : mCheckIn.getQuestionAnswers()) {
            questionsView.addView(UIUtils.createTwoColumnText(getActivity(), qa.getQuestion().getText(),
                    qa.getAnswer().getText(), getResources().getDimension(R.dimen.text_small)));
        }
        // Pain medications
        LinearLayout medicationView = (LinearLayout) v.findViewById(R.id.patient_check_in_summary_medications);
        medicationView.removeAllViews();
        for (CheckIn.MedicationTaken mt : mCheckIn.getMedicationsTaken()) {
            String medicationTakenText = getString(R.string.patient_answer_no);
            if (mt.getWhen() != null) {
                medicationTakenText = DateUtils.getRelativeDateTimeString(getActivity(), mt.getWhen(),
                        DateUtils.MINUTE_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, 0).toString();
            }
            medicationView.addView(UIUtils.createTwoColumnText(getActivity(), mt.getMedication().getName(),
                    medicationTakenText, getResources().getDimension(R.dimen.text_small)));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnCheckInSubmitListener) activity;
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
     */
    public interface OnCheckInSubmitListener {
        void onCheckInSubmit();
    }

}
