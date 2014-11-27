package org.coursera.capstone.android.fragment;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import org.coursera.capstone.android.R;
import org.coursera.capstone.android.constant.CapstoneConstants;
import org.coursera.capstone.android.parcelable.Answer;
import org.coursera.capstone.android.parcelable.PainMedication;
import org.coursera.capstone.android.parcelable.Question;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link org.coursera.capstone.android.fragment.QuestionFragment.OnQuestionAnsweredListener} interface
 * to handle interaction events.
 * Use the {@link QuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionFragment extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {
    private static final String QUESTION_PARAM = "question_param";
    private static final String PAIN_MEDICATION_PARAM = "pain_medication_param";

    private Question mQuestion;
    private PainMedication mPainMedication;

    // Question text and answer buttons
    private TextView mQuestionText;
    private Button mAnswer1Btn;
    private Button mAnswer2Btn;
    private Button mAnswer3Btn;
    private TimePickerDialog mTimePickerDlg;

    private OnQuestionAnsweredListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param question       The question to be answerd.
     * @param painMedication if this is a medication question
     * @return A new instance of fragment QuestionFragment.
     */
    public static QuestionFragment newInstance(Question question, PainMedication painMedication) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putParcelable(QUESTION_PARAM, question);
        args.putParcelable(PAIN_MEDICATION_PARAM, painMedication);
        fragment.setArguments(args);
        return fragment;
    }

    public QuestionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mQuestion = getArguments().getParcelable(QUESTION_PARAM);
            mPainMedication = getArguments().getParcelable(PAIN_MEDICATION_PARAM);
        } else {
            throw new IllegalArgumentException("Questions and Patient is required");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_patient_question, container, false);
        // Question and answer text and buttons
        mQuestionText = (TextView) v.findViewById(R.id.checking_question_txt);
        mAnswer1Btn = (Button) v.findViewById(R.id.checking_answer_1);
        mAnswer1Btn.setOnClickListener(this);
        mAnswer2Btn = (Button) v.findViewById(R.id.checking_answer_2);
        mAnswer2Btn.setOnClickListener(this);
        mAnswer3Btn = (Button) v.findViewById(R.id.checking_answer_3);
        mAnswer3Btn.setOnClickListener(this);
        // Setup time picker
        Calendar c = Calendar.getInstance();
        mTimePickerDlg = new TimePickerDialog(getActivity(), this, c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE), DateFormat.is24HourFormat(getActivity()));

        // Either the question or the pain medication is nullœ
        if (mQuestion != null) {
            setupQuestion(mQuestion);
        } else {
            setupQuestion(createMedicationQuestion(mPainMedication));
        }
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnQuestionAnsweredListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnQuestionsAnsweredListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Answer click listener
     *
     * @param v The button the user clicked
     */
    @Override
    public void onClick(View v) {
        int answerId = 0;
        switch (v.getId()) {
            case R.id.checking_answer_1:
                answerId = 0;
                break;
            case R.id.checking_answer_2:
                answerId = 1;
                break;
            case R.id.checking_answer_3:
                answerId = 2;
                break;
        }
        // Add the answer to the check in
        if (mPainMedication != null) {
            if (answerId == 0) {
                // Show time picker dialog if this is a medical question and the user responded yes
                mTimePickerDlg.show();
            } else {
                // Patient choose no
                mListener.onMedicalQuestionAnswered(mPainMedication, null);
            }
        } else {
            mListener.onQuestionAnswered(mQuestion, mQuestion.getAnswers().get(answerId));
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        Log.i(CapstoneConstants.LOG_TAG, "Adding check in time " + hourOfDay + ":" + minute);
        // Notify listener with date and medication
        mListener.onMedicalQuestionAnswered(mPainMedication, calendar.getTime());
    }

    private Question createMedicationQuestion(PainMedication painMedication) {
        ArrayList<Answer> yesNo = new ArrayList<Answer>();
        yesNo.add(new Answer(getString(R.string.patient_answer_yes)));
        yesNo.add(new Answer(getString(R.string.patient_answer_no)));
        String question = getString(R.string.patient_question_medication, painMedication.getName());
        return new Question(question, yesNo);
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
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     */
    public interface OnQuestionAnsweredListener {
        /**
         * @param question Question a patient has answered
         */
        public void onQuestionAnswered(Question question, Answer answer);

        /**
         * @param painMedication The pain medication the patient has answered to
         * @param when           When patient took it, null if patient didn't take it
         */
        public void onMedicalQuestionAnswered(PainMedication painMedication, Date when);

    }

}
