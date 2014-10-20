package therisingthumbs.banking;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.EditText;

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
        System.err.println("SIGN UP ATTEMPT!");

        EditText first_name = (EditText) findViewById(R.id.first_name);
        EditText last_name = (EditText) findViewById(R.id.last_name);
        EditText email = (EditText) findViewById(R.id.email);
        EditText pass = (EditText) findViewById(R.id.pass);
        EditText pass_confirm = (EditText) findViewById(R.id.pass_confirm);

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

                }
                else
                {
                    //TODO error passwords don't match
                }

            }
            else
            {
                //TODO not valid email format
            }

        }//end empty string check
        else
        {
            //TODO empty field(s)
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
