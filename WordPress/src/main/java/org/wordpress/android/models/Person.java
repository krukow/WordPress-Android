package org.wordpress.android.models;

import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;
import org.wordpress.android.util.AppLog;
import org.wordpress.android.util.StringUtils;

public class Person {
    private long personID;
    private String blogId;
    private int localTableBlogId;

    private String username;
    private String firstName;
    private String lastName;
    private String displayName;
    private String avatarUrl;
    private String role;
    private boolean isFollower;
    private boolean isEmailFollower;

    public Person(long personID, String blogId, int localTableBlogId) {
        this.personID = personID;
        this.blogId = blogId;
        this.localTableBlogId = localTableBlogId;
    }

    @Nullable
    public static Person fromJSON(JSONObject json, String blogId, int localTableBlogId, boolean isFollower) throws JSONException {
        if (json == null) {
            return null;
        }

        /**
         * We'll parse the user information for both the user & follows point here:
         * https://developer.wordpress.com/docs/api/1.1/get/sites/%24site/users/%24user_id/
         * https://developer.wordpress.com/docs/api/1.1/get/sites/%24site/follows/
         */
        // Response parameters are in:
        try {
            long personID = Long.parseLong(json.getString("ID"));
            Person person = new Person(personID, blogId, localTableBlogId);
            person.setUsername(json.optString("login"));
            person.setFirstName(json.optString("first_name"));
            person.setLastName(json.optString("last_name"));
            person.setDisplayName(json.optString("name"));
            person.setAvatarUrl(json.optString("avatar_URL"));
            // We don't support multiple roles, so the first role is picked just as it's in Calypso
            String role = json.getJSONArray("roles").optString(0);
            person.setRole(role);

            /**
             * If we made a request to the follows point we know that this is a follower and not a user.
             * Please note that the `email` parameter for these endpoints return different values. For users endpoint,
             * it will return the user's email address, whereas for follows endpoint it'll return if it's an email
             * follower. We don't use user's email address for now, so it's ignored for users endpoint.
             */
            if (isFollower) {
                person.setFollower(true);
                person.setEmailFollower(json.optBoolean("email"));
            }

            return person;
        } catch (NumberFormatException e) {
            AppLog.e(AppLog.T.PEOPLE, "The ID parsed from the JSON couldn't be converted to long: " + e);
        }

        return null;
    }

    public long getPersonID() {
        return personID;
    }

    public String getBlogId() {
        return StringUtils.notNullStr(blogId);
    }

    public int getLocalTableBlogId() {
        return localTableBlogId;
    }

    public String getUsername() {
        return StringUtils.notNullStr(username);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return StringUtils.notNullStr(firstName);
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return StringUtils.notNullStr(lastName);
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDisplayName() {
        return StringUtils.notNullStr(displayName);
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getRole() {
        return StringUtils.notNullStr(role);
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAvatarUrl() {
        return StringUtils.notNullStr(avatarUrl);
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public boolean isFollower() {
        return isFollower;
    }

    public void setFollower(boolean follower) {
        isFollower = follower;
    }

    public boolean isEmailFollower() {
        return isEmailFollower;
    }

    public void setEmailFollower(boolean emailFollower) {
        isEmailFollower = emailFollower;
    }
}
