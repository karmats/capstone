package org.coursera.capstone.android.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
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
public class QuestionFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private static final String QUESTION_PARAM = "question_param";
    private static final String PAIN_MEDICATION_PARAM = "pain_medication_param";
    private static final String HAS_ONE_PAIN_MEDICATION = "has_one_pain_medication";

    private Question mQuestion;
    private PainMedication mPainMedication;
    private boolean mHasOnePainMedication;

    // Question text and answer buttons
    private TextView mQuestionText;
    private Button mAnswer1Btn;
    private Button mAnswer2Btn;
    private Button mAnswer3Btn;
    private DatePickerDialog mDatePickerDlg;
    private TimePickerDialog mTimePickerDlg;
    private Calendar mCalendar;

    private OnQuestionAnsweredListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param question             The question to be answerd.
     * @param painMedication       if this is a medication question
     * @param hasOnePainMedication True if there patient only takes one pain medication
     * @return A new instance of fragment QuestionFragment.
     */
    public static QuestionFragment newInstance(Question question, PainMedication painMedication, boolean hasOnePainMedication) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putParcelable(QUESTION_PARAM, question);
        args.putParcelable(PAIN_MEDICATION_PARAM, painMedication);
        args.putBoolean(HAS_ONE_PAIN_MEDICATION, hasOnePainMedication);
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
            mHasOnePainMedication = getArguments().getBoolean(HAS_ONE_PAIN_MEDICATION);
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
        // Setup time and date picker
        Calendar c = Calendar.getInstance();
        mTimePickerDlg = new TimePickerDialog(getActivity(), this, c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE), DateFormat.is24HourFormat(getActivity()));
        mDatePickerDlg = new DatePickerDialog(getActivity(), this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        mCalendar = Calendar.getInstance();

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
                // Show date picker dialog if this is a medical question and the user responded yes
                if (mQuestion != null && !mHasOnePainMedication) {
                    // Don't show if patient has more than one pain medication
                    mListener.onMedicationsTaken(null);
                } else {
                    mDatePickerDlg.show();
                }
            } else {
                // Patient choose no. If both question and pain medication has a value, this is the
                // "Did you take your pain medication" question, and we choose no for all
                if (mQuestion != null) {
                    mListener.onMedicalNoMedicationsTaken();
                } else {
                    mListener.onMedicalQuestionAnswered(mPainMedication, null);
                }
            }
        } else {
            mListener.onQuestionAnswered(mQuestion, mQuestion.getAnswers().get(answerId));
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (view.isShown()) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            Log.i(CapstoneConstants.LOG_TAG, "Adding check in time " + hourOfDay + ":" + minute);
            // Notify listener with date and medication
            if (mPainMedication != null && mQuestion != null) {
                // If pain medication and question have values this is the
                // "Did you take your pain medication" question.
                mListener.onMedicationsTaken(calendar.getTime());
            } else {
                mListener.onMedicalQuestionAnswered(mPainMedication, calendar.getTime());
            }
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if (view.isShown()) {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, monthOfYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            mTimePickerDlg.show();
        }
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

        /**
         * Patient didn't take any pain medications
         */
        public void onMedicalNoMedicationsTaken();

        /**
         * Patient choose yes on the "Did you take your pain medication" question
         *
         * @param when If there is only one medication, the date is used.
         */
        public void onMedicationsTaken(Date when);

    }

}
