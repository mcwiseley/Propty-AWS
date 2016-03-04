package io.propty.propty;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.util.HashMap;

public class MainActivity extends Activity {

    Button btnChangePass;
    Button btnLogout;
    Button btnLogin;
    Button btnSwipeCard;
    Resources res;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnChangePass = (Button) findViewById(R.id.btchangepass);
        btnLogout = (Button) findViewById(R.id.btlogout);
        btnLogin = (Button) findViewById(R.id.btlogin);
        btnSwipeCard = (Button) findViewById(R.id.btswipecard);
        res = getResources();

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        // Hashmap to load data from the Sqlite database
        HashMap<String, String> user = db.getUserDetails();

        /**
         * Change Password Activity Started
         **/
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chgpass = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                startActivity(chgpass);
            }
        });

        /**
         *Logout from the User Panel which clears the data in Sqlite database
         **/
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserFunctions logout = new UserFunctions();
                logout.logoutUser(getApplicationContext());
                Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(login);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(login);
            }
        });

        btnSwipeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent swipecard = new Intent(getApplicationContext(), SwipeCardActivity.class);
                startActivity(swipecard);
            }
        });

//        // Set welcome message for a logged-in user.
//        final TextView welcome = (TextView) findViewById(R.id.textwelcome);
//        String fname = user.get("fname").toString();
//        String welcomeText = String.format(res.getString(R.string.welcome_text), fname);
//        welcome.setText(welcomeText);
//        final TextView lname = (TextView) findViewById(R.id.lname);
//        lname.setText(user.get("lname").toString());
    }

}
