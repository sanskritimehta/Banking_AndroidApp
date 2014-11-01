package therisingthumbs.banking;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.SyncStateContract;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignUp extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        // connect to parse
        Parse.initialize(this, "fVmX21jyCA3B7ffHgU8RCJQJCls6x9wJBSdx5KHY",
                                "RxrZt3ldrgG0xilRZHrIZe5ViQQqC1OcxBl33DlK");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sign_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);
            return rootView;
        }
    }

    /**
     * Activity to respond to sign up button click
     * attempt to access user data on parse and log in
     */
    public void SignUpAttempt(View view)
    {
        final Intent intent = new Intent(this, HomePage.class);
        final Bundle b = new Bundle();
        System.err.println("SIGN UP ATTEMPT!");

        final EditText first_name = (EditText) findViewById(R.id.first_name);
        final EditText last_name = (EditText) findViewById(R.id.last_name);
        final EditText email = (EditText) findViewById(R.id.email);
        final EditText pass = (EditText) findViewById(R.id.pass);
        final EditText pass_confirm = (EditText) findViewById(R.id.pass_confirm);
        final TextView err = (TextView) findViewById(R.id.errMsg);



        //check if all fields are non-empty
        if (!isEmpty(first_name) &&
            !isEmpty(last_name) &&
            !isEmpty(email) &&
            !isEmpty(pass) &&
            !isEmpty(pass_confirm))
        {
            System.err.println("All values non empty");

            //check for valid email
            if (emailValidator(email.getText().toString())) {
                //check passwords match
                if (pass.getText().toString().equals(pass_confirm.getText().toString())) {
                    //passwords match
                    System.err.println("pass match!\nCheck Parse for user");

                    // Check parse database to see if user exists (by email)
                    // if does not, create new object. otherwise show error message
                    User u = new User( first_name.getText().toString(),
                                       last_name.getText().toString(),
                                       email.getText().toString(),
                                       pass.getText().toString() );
                    u.printData();

                    System.err.println("User in SignUp: " + u);

                    String e = u.addToDatabase();
                    if (e == null) {
                        //Add successful! start new activity with user object
                        ((myApplication) this.getApplication()).setUser(u);
                        System.err.println("Add Successful!");
                        b.putParcelable("user_object", u);
                        intent.putExtras(b);

                        startActivity(intent);
                    }
                    else {
                        err.setText(e);
                    }

                }
                else
                {
                    //error passwords don't match
                    err.setText("Passwords do not match!");
                    err.setTextColor(Color.RED);
                }

            }
            else
            {
                //not valid email format
                err.setText("Email not in valid format (example@example.com)");
                err.setTextColor(Color.RED);
            }

        }//end empty string check
        else
        {
            // empty field(s)
            err.setText("Missing information. ALL fields are required");
            err.setTextColor(Color.RED);
        }
    }

    protected boolean isEmpty(EditText t)
    {
        if(t.getText().toString().trim().length() == 0)
            return true;
        return false;

    }
    /**
     * validate your email address format. Ex-akhi@mani.com
     */
    public boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\." +
                "                      [A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
