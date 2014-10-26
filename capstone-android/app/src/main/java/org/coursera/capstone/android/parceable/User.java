package org.coursera.capstone.android.parceable;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.coursera.capstone.android.constant.CapstoneConstants;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * User object
 */
public class User implements Parcelable {

    private String username;
    private String firstName;
    private String lastName;
    private String accessToken;

    public User(String userName, String firstName, String lastName, String accessToken) {
        this.username = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.accessToken = accessToken;
    }

    public User(Parcel source) {
        Bundle data = source.readBundle(User.class.getClassLoader());
        username = data.getString("username");
        firstName = data.getString("firstName");
        lastName = data.getString("lastName");
        accessToken = data.getString("accessToken");
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle data = new Bundle();
        data.putString("username", username);
        data.putString("firstName", firstName);
        data.putString("lastName", lastName);
        data.putString("accessToken", accessToken);
        dest.writeBundle(data);
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel data) {
            return new User(data);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * Converts this user object to a json string
     *
     * @return Json string
     */
    public String toJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("username", username);
            json.put("firstName", firstName);
            json.put("lastName", lastName);
            json.put("accessToken", accessToken);
        } catch (JSONException e) {
            Log.w(CapstoneConstants.LOG_TAG, "Failed to create user json string");
        }
        return json.toString();
    }

    /**
     * Converts a json string to a user object.
     * The string must been created with the toJson() method
     *
     * @param json The json string
     * @return A User object
     */
    public static User fromJsonString(String json) {
        User result = null;
        try {
            JSONObject obj = new JSONObject(json);
            result = new User(obj.getString("username"), obj.getString("firstName"), obj.getString("lastName"),
                    obj.getString("accessToken"));
        } catch (JSONException e) {
            Log.w(CapstoneConstants.LOG_TAG, "Failed to convert string " + json + " to user");
        }
        return result;
    }
}
