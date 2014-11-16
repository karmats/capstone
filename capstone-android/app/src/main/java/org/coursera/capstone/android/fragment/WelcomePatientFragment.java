package org.coursera.capstone.android.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.coursera.capstone.android.R;
import org.coursera.capstone.android.constant.CapstoneConstants;
import org.coursera.capstone.android.parcelable.Patient;
import org.coursera.capstone.android.parcelable.Question;
import org.coursera.capstone.android.parcelable.User;
import org.coursera.capstone.android.task.FetchQuestionsTask;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link WelcomePatientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WelcomePatientFragment extends Fragment implements FetchQuestionsTask.QuestionsCallback {
    private static final String PATIENT_PARAM = "patient_arg";

    private Patient mPatient;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param patient The patient to welcome.
     * @return A new instance of fragment WelcomePatientFragment.
     */
    public static WelcomePatientFragment newInstance(Patient patient) {
        WelcomePatientFragment fragment = new WelcomePatientFragment();
        Bundle args = new Bundle();
        args.putParcelable(PATIENT_PARAM, patient);
        fragment.setArguments(args);
        return fragment;
    }

    public WelcomePatientFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPatient = getArguments().getParcelable(PATIENT_PARAM);
        } else {
            throw new IllegalArgumentException("Patient argument is required");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_welcome_patient, container, false);
        Button nextBtn = (Button) v.findViewById(R.id.patient_next_btn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fetch the questions
                User user = User.fromJsonString(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(CapstoneConstants.PREFERENCES_USER, ""));
                new FetchQuestionsTask(user.getAccessToken(), WelcomePatientFragment.this).execute();
            }
        });
        return v;
    }

    // Fetch questions callback
    @Override
    public void onQuestionSuccess(List<Question> questions) {
        Log.i(CapstoneConstants.LOG_TAG, "Got " + questions.size() + " questions");
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, CheckInFragment.newInstance(new ArrayList<Question>(questions), mPatient))
                .commit();
    }

    @Override
    public void onQuestionsFailure(String error) {
        Log.e(CapstoneConstants.LOG_TAG, error);
    }

}
