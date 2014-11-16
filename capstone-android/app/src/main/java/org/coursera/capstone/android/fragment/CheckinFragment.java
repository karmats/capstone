package org.coursera.capstone.android.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.coursera.capstone.android.R;
import org.coursera.capstone.android.constant.CapstoneConstants;
import org.coursera.capstone.android.parcelable.Patient;
import org.coursera.capstone.android.parcelable.Question;

import java.util.ArrayList;
import java.util.Stack;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link org.coursera.capstone.android.fragment.CheckInFragment.OnQuestionsAnsweredListener} interface
 * to handle interaction events.
 * Use the {@link CheckInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckInFragment extends Fragment implements View.OnClickListener {
    private static final String QUESTIONS_PARAM = "questions_param";
    private static final String PATIENT_PARAM = "patient_param";

    private Stack<Question> mQuestions;
    private Patient mPatient;
    private Question mCurrentQuestion;

    // Question text and answer buttons
    private TextView mQuestionText;
    private Button mAnswer1Btn;
    private Button mAnswer2Btn;
    private Button mAnswer3Btn;

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
            ArrayList<Question> questions = getArguments().getParcelableArrayList(QUESTIONS_PARAM);
            mQuestions = new Stack<Question>();
            mQuestions.addAll(questions);
            mPatient = getArguments().getParcelable(PATIENT_PARAM);
        } else {
            throw new IllegalArgumentException("Questions and Patient is required");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_checkin, container, false);
        // Question and answer text and buttons
        mQuestionText = (TextView) v.findViewById(R.id.checking_question_txt);
        mAnswer1Btn = (Button) v.findViewById(R.id.checking_answer_1);
        mAnswer1Btn.setOnClickListener(this);
        mAnswer2Btn = (Button) v.findViewById(R.id.checking_answer_2);
        mAnswer2Btn.setOnClickListener(this);
        mAnswer3Btn = (Button) v.findViewById(R.id.checking_answer_3);
        mAnswer3Btn.setOnClickListener(this);
        mCurrentQuestion = mQuestions.pop();
        setupQuestion(mCurrentQuestion);
        return v;
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

    // Setup a question with answers
    private void setupQuestion(Question q) {
        mQuestionText.setText(q.getText());
        // There is always two answers, sometimes three
        mAnswer1Btn.setText(q.getAnswers().get(0).getText());
        mAnswer2Btn.setText(q.getAnswers().get(1).getText());
        if (q.getAnswers().size() > 2) {
            mAnswer3Btn.setText(q.getAnswers().get(2).getText());
        } else {
            // Hide button if not an answer connected to it
            mAnswer3Btn.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Answer click listener
     *
     * @param v The button the user clicked
     */
    @Override
    public void onClick(View v) {
        Button b = (Button) v;
        switch (b.getId()) {
            case R.id.checking_answer_1:
                Log.i(CapstoneConstants.LOG_TAG, "You answered" + mCurrentQuestion.getAnswers().get(0).getText());
                break;
            case R.id.checking_answer_2:
                Log.i(CapstoneConstants.LOG_TAG, "You answered" + mCurrentQuestion.getAnswers().get(1).getText());
                break;
            case R.id.checking_answer_3:
                Log.i(CapstoneConstants.LOG_TAG, "You answered" + mCurrentQuestion.getAnswers().get(2).getText());
                break;
        }
        if (mQuestions.empty()) {
            mListener.onAllQuestionsAnswered();
        } else {
            mCurrentQuestion = mQuestions.pop();
            setupQuestion(mCurrentQuestion);
        }
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
