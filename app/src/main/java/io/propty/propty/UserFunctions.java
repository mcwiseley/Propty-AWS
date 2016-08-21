package io.propty.propty;

/**
 * Created by micheal on 11/18/15.
 **/
import android.content.Context;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class UserFunctions {

    private JSONParser jsonParser;

    //URL of the PHP API

    /**
     * UserFunctions Constructor
     **/
    public UserFunctions() { jsonParser = new JSONParser(); }

    /**
     * Function to Login
     **/
    public JSONObject loginUser(String email, String password) {
        // TODO: assign values to login_tag and loginURL
        String login_tag = "";
        String loginURL = "";
        // Building Parameters
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        return jsonParser.getJSONFromUrl(loginURL, params);
    }

    /**
     * Function to change password
     **/
    public JSONObject chgPass(String newpas, String email) {
        // TODO: assign values to chgpass_tag and chgpassURL
        String chgpass_tag = "";
        String chgpassURL = "";
        // Building Parameters
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("tag", chgpass_tag));
        params.add(new BasicNameValuePair("newpas", newpas));
        params.add(new BasicNameValuePair("email", email));
        return jsonParser.getJSONFromUrl(chgpassURL, params);
    }

    /**
     * Function to reset the password
     **/
    public JSONObject forPass(String forgotpassword) {
        // TODO: assign values to forpass_tag and forpassURL
        String forpass_tag = "";
        String forpassURL = "";
        // Building Parameters
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("tag", forpass_tag));
        params.add(new BasicNameValuePair("forgotpassword", forgotpassword));
        return jsonParser.getJSONFromUrl(forpassURL, params);
    }

    /**
     * Function to  Register
     **/
    public JSONObject registerUser(String fname, String lname, String email, String uname, String password) {
        // TODO: assign values to register_tag and registerURL
        String register_tag = "";
        String registerURL = "";
        // Building Parameters
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("fname", fname));
        params.add(new BasicNameValuePair("lname", lname));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("uname", uname));
        params.add(new BasicNameValuePair("password", password));
        return jsonParser.getJSONFromUrl(registerURL, params);
    }

    /**
     * Function to logout user
     * Resets the temporary data stored in SQLite Database
     */
    public boolean logoutUser(Context context) {
        UserDatabaseHandler db = new UserDatabaseHandler(context);
        db.resetTables();
        return true;
    }
}
