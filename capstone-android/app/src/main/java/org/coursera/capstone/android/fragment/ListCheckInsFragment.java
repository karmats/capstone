package org.coursera.capstone.android.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import org.coursera.capstone.android.R;
import org.coursera.capstone.android.adapter.ExpandableCheckInsAdapter;
import org.coursera.capstone.android.parcelable.CheckInResponse;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnCheckInSelectedListener}
 * interface.
 */
public class ListCheckInsFragment extends Fragment implements AbsListView.OnItemClickListener {

    private static final String CHECK_INS_PARAM = "check_ins_param";

    private ArrayList<CheckInResponse> mCheckIns;

    private OnCheckInSelectedListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private ExpandableListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ExpandableListAdapter mAdapter;

    public static ListCheckInsFragment newInstance(ArrayList<CheckInResponse> checkIns) {
        ListCheckInsFragment fragment = new ListCheckInsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(CHECK_INS_PARAM, checkIns);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ListCheckInsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mCheckIns = getArguments().getParcelableArrayList(CHECK_INS_PARAM);
        }

        mAdapter = new ExpandableCheckInsAdapter(getActivity(), mCheckIns);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkin, container, false);

        // Set the adapter
        mListView = (ExpandableListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(view.findViewById(android.R.id.empty));

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);
        // Set empty text if there are no check-ins
        if (mCheckIns.size() <= 0) {
            setEmptyText(getString(R.string.doctor_patient_no_check_ins));
        }
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnCheckInSelectedListener) activity;
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onCheckInSelected(mCheckIns.get(position));
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();
        ((TextView) emptyView).setText(emptyText);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnCheckInSelectedListener {
        public void onCheckInSelected(CheckInResponse checkIn);
    }

}
