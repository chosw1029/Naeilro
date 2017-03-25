package nextus.naeilro.model;

import com.google.firebase.database.IgnoreExtraProperties;

// [START blog_user_class]
@IgnoreExtraProperties
public class User {

    public String username;
    public String email;
    public String userImg;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, String userImg) {
        this.username = username;
        this.email = email;
        this.userImg = userImg;
    }

}
// [END blog_user_class]
