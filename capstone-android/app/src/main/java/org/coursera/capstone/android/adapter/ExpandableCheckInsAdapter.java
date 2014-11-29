package org.coursera.capstone.android.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.coursera.capstone.android.R;
import org.coursera.capstone.android.parcelable.CheckInResponse;
import org.coursera.capstone.android.util.UIUtils;

import java.util.Date;
import java.util.List;

/**
 * Used for displaying check-in items
 */
public class ExpandableCheckInsAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<CheckInResponse> mCheckIns;

    public ExpandableCheckInsAdapter(Context context, List<CheckInResponse> checkIns) {
        this.mContext = context;
        this.mCheckIns = checkIns;
    }

    @Override
    public int getGroupCount() {
        return mCheckIns.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mCheckIns.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mCheckIns.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder viewHolder;
        if (null == convertView) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.view_check_in_title, parent, false);
            viewHolder = new GroupViewHolder();
            viewHolder.titleText = (TextView) convertView.findViewById(R.id.view_check_in_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GroupViewHolder) convertView.getTag();
        }
        CheckInResponse checkIn = mCheckIns.get(groupPosition);
        // Create a relative time from now
        String relativeTimeString = DateUtils.getRelativeDateTimeString(mContext, checkIn.getWhen(),
                DateUtils.MINUTE_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, 0).toString();
        viewHolder.titleText.setText(relativeTimeString);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder viewHolder;
        if (null == convertView) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.view_check_in_data, parent, false);
            viewHolder = new ChildViewHolder();
            viewHolder.questionAnswers = (LinearLayout) convertView.findViewById(R.id.view_check_in_data_questions);
            viewHolder.medicationsTaken = (LinearLayout) convertView.findViewById(R.id.view_check_in_data_medications);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ChildViewHolder) convertView.getTag();
        }
        CheckInResponse checkIn = mCheckIns.get(groupPosition);
        viewHolder.questionAnswers.removeAllViews();
        for (CheckInResponse.QuestionAnswerResponse qa : checkIn.getQuestionAnswers()) {
            LinearLayout qaView = UIUtils.createTwoColumnText(mContext, qa.getQuestionText(), qa.getAnswerText());
            viewHolder.questionAnswers.addView(qaView);
        }
        viewHolder.medicationsTaken.removeAllViews();
        for (CheckInResponse.MedicationTaken mt : checkIn.getMedicationsTaken()) {
            String mtWhenText = mContext.getString(R.string.patient_answer_no);
            if (mt.getWhen() != null) {
                mtWhenText = DateFormat.getTimeFormat(mContext).format(new Date(mt.getWhen()));
            }
            LinearLayout mtView = UIUtils.createTwoColumnText(mContext, mt.getMedicationName(), mtWhenText);
            viewHolder.medicationsTaken.addView(mtView);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    static class GroupViewHolder {
        private TextView titleText;
    }

    static class ChildViewHolder {
        private LinearLayout questionAnswers;
        private LinearLayout medicationsTaken;
    }
}
