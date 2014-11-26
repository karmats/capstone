package org.coursera.capstone.android.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.coursera.capstone.android.R;
import org.coursera.capstone.android.constant.CapstoneConstants;
import org.coursera.capstone.android.parcelable.Patient;
import org.coursera.capstone.android.parcelable.User;
import org.coursera.capstone.android.task.SearchPatientsByNameTask;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link org.coursera.capstone.android.fragment.SearchPatientsFragment.OnSearchPatientSelectedListener}
 * interface.
 */
public class SearchPatientsFragment extends ListFragment implements SearchPatientsByNameTask.OnSearchPatientsByNameCallback {
    private static final String SEARCH_QUERY_PARAM = "search_query_param";

    // The result from the search
    private List<Patient> mPatientsResult;

    private OnSearchPatientSelectedListener mListener;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ArrayAdapter<Patient> mAdapter;

    public static SearchPatientsFragment newInstance(String searchQuery) {
        SearchPatientsFragment fragment = new SearchPatientsFragment();
        Bundle args = new Bundle();
        args.putString(SEARCH_QUERY_PARAM, searchQuery);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SearchPatientsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String searchQuery = getArguments().getString(SEARCH_QUERY_PARAM);
        Log.i(CapstoneConstants.LOG_TAG, "Searching for patients with name " + searchQuery);

        // Get the user information from shared preferences
        String userJsonString = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString(CapstoneConstants.PREFERENCES_USER, "");
        User user = User.fromJsonString(userJsonString);

        mPatientsResult = new ArrayList<Patient>();
        mAdapter = new ArrayAdapter<Patient>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, mPatientsResult);

        // Execute the search
        new SearchPatientsByNameTask(user.getAccessToken(), this).execute(searchQuery);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_searchpatient, container, false);

        // Set the adapter
        setListAdapter(mAdapter);

        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.i(CapstoneConstants.LOG_TAG, position + " clicked");
        mListener.onSearchedPatientSelected(mPatientsResult.get(position));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnSearchPatientSelectedListener) activity;
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
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = getView().findViewById(android.R.id.empty);
        ((TextView) emptyView).setText(emptyText);
    }

    @Override
    public void onSearchPatientsByNameSuccess(List<Patient> patients) {
        mPatientsResult.clear();
        if (patients.isEmpty()) {
            setEmptyText(getString(R.string.doctor_search_patients_no_results));
        } else {
            mPatientsResult.addAll(patients);
        }
        // Notify adapter that list view has changed
        mAdapter.notifyDataSetChanged();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnSearchPatientSelectedListener {
        public void onSearchedPatientSelected(Patient patient);
    }

}
