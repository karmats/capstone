package org.coursera.capstone.android.fragment;

import android.app.Activity;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.coursera.capstone.android.R;

import java.util.Date;

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
            questionsView.addView(createTwoColumnText(qa.getQuestion().getText(), qa.getAnswer().getText()));
        }
        // Pain medications
        LinearLayout medicationView = (LinearLayout) v.findViewById(R.id.patient_check_in_summary_medications);
        medicationView.removeAllViews();
        for (CheckIn.MedicationTaken mt : mCheckIn.getMedicationsTaken()) {
            String medicationTakenText = getString(R.string.patient_answer_no);
            if (mt.getWhen() != null) {
                medicationTakenText = DateFormat.getTimeFormat(getActivity()).format(new Date(mt.getWhen()));
            }
            medicationView.addView(createTwoColumnText(mt.getMedication().getName(), medicationTakenText));
        }
    }

    private LinearLayout createTwoColumnText(String text1, String text2) {
        LinearLayout holderView = new LinearLayout(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        holderView.setLayoutParams(params);
        holderView.setOrientation(LinearLayout.VERTICAL);
        int padding = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
        holderView.setPadding(0, padding, 0, 0);

        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView text1View = new TextView(getActivity());
        text1View.setText(text1);
        text1View.setTypeface(null, Typeface.BOLD);
        text1View.setLayoutParams(textViewParams);
        holderView.addView(text1View);

        TextView text2View = new TextView(getActivity());
        text2View.setText(text2);
        text2View.setLayoutParams(textViewParams);
        holderView.addView(text2View);
        return holderView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onCheckInSubmit();
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
