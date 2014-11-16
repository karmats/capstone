package org.coursera.capstone.android.task;

import android.os.AsyncTask;

import org.coursera.capstone.android.fragment.PatientSettingsFragment;
import org.coursera.capstone.android.http.api.SymptomManagementApi;
import org.coursera.capstone.android.http.api.SymptomManagementApiBuilder;
import org.coursera.capstone.android.parcelable.Question;

import java.util.List;

/**
 * Fetches the patient questions
 */
public class FetchQuestionsTask extends AsyncTask<Void, Void, List<Question>> {

    private String mAccessToken;
    private QuestionsCallback mCallback;

    public FetchQuestionsTask(String accessToken, QuestionsCallback callback) {
        this.mAccessToken = accessToken;
        this.mCallback = callback;
    }

    @Override
    protected void onPostExecute(List<Question> questions) {
        if (questions.size() <= 0) {
            mCallback.onQuestionsFailure("Failed to fetch questions");
        } else {
            mCallback.onQuestionSuccess(questions);
        }
    }

    @Override
    protected List<Question> doInBackground(Void... params) {
        SymptomManagementApi api = SymptomManagementApiBuilder.newInstance(mAccessToken);
        return api.getQuestions();
    }


    public interface QuestionsCallback {
        void onQuestionSuccess(List<Question> questions);

        void onQuestionsFailure(String error);
    }
}
