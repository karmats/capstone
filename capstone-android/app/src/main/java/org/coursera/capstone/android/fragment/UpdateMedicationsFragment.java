package org.coursera.capstone.android.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import org.coursera.capstone.android.R;
import org.coursera.capstone.android.constant.CapstoneConstants;
import org.coursera.capstone.android.parcelable.PainMedication;
import org.coursera.capstone.android.parcelable.Patient;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnUpdateMedicationsListener} interface
 * to handle interaction events.
 * Use the {@link UpdateMedicationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateMedicationsFragment extends Fragment {
    private static final String PATIENT_PARAM = "patient_param";
    private static final String PAIN_MEDICATIONS_PARAM = "pain_medications_param";

    private Patient mPatient;
    private ArrayList<PainMedication> mAvailableMedications;

    private OnUpdateMedicationsListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param patient         The patient to update the medications for.
     * @param painMedications All available pain medications
     * @return A new instance of fragment UpdateMedicationsFragment.
     */
    public static UpdateMedicationsFragment newInstance(Patient patient, ArrayList<PainMedication> painMedications) {
        UpdateMedicationsFragment fragment = new UpdateMedicationsFragment();
        Bundle args = new Bundle();
        args.putParcelable(PATIENT_PARAM, patient);
        args.putParcelableArrayList(PAIN_MEDICATIONS_PARAM, painMedications);
        fragment.setArguments(args);
        return fragment;
    }

    public UpdateMedicationsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPatient = getArguments().getParcelable(PATIENT_PARAM);
            mAvailableMedications = getArguments().getParcelableArrayList(PAIN_MEDICATIONS_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_update_medications, container, false);
        LinearLayout checkboxContainer = (LinearLayout) v.findViewById(R.id.update_medication_checkbox_container);
        Log.i(CapstoneConstants.LOG_TAG, "Adding " + mPatient.getMedications().size());
        for (PainMedication pm : mAvailableMedications) {
            CheckBox cb = new CheckBox(getActivity());
            cb.setText(pm.getName());
            // Set to checked if the patient takes this medication
            cb.setChecked(mPatient.getMedications().contains(pm));
            checkboxContainer.addView(cb);
        }
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnUpdateMedicationsListener) activity;
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
    public interface OnUpdateMedicationsListener {
        public void onMedicationUpdate(Patient patient);
    }

}
