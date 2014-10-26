package therisingthumbs.banking;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class LogIn extends Activity {

    public final static String EXTRA_MESSAGE = "therisingthumbs.banking.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.log_in, menu);
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
            View rootView = inflater.inflate(R.layout.fragment_log_in, container, false);
            return rootView;
        }
    }

    /**
     * Activity to respond to log in button click
     * attempt to access user data on parse and log in
     */
    public void LogInAttempt(View view)
    {
        final Intent intent = new Intent(this, HomePage.class);
        final Bundle b = new Bundle();
        final EditText email = (EditText) findViewById(R.id.email);
        final EditText pass = (EditText) findViewById(R.id.pass);
        final TextView errMsg = (TextView) findViewById(R.id.errMsg);

        if(!isEmpty(email) && !isEmpty(pass))
        {
            System.err.println("LOG IN ATTEMPT!");
            //check parse data base for email and password
            ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
            query.whereEqualTo("email", email.getText().toString());
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject parseObject, ParseException e) {
                    if (e == null) {
                        //user exists, access home page
                        errMsg.setText("");
                        String passWord = parseObject.getString ("pass");
                        if (passWord.equals(pass.getText().toString())) {
                            String message = email.getText().toString();
                            b.putString("email", message);
                            intent.putExtra("homePage", b);
                            startActivity(intent);

                        } else {
                            errMsg.setText("Invalid password!");
                            errMsg.setTextColor(Color.RED);
                        }
                    } else {
                        errMsg.setText("Email does not exist");
                        errMsg.setTextColor(Color.RED);
                    }
                }
            });
        }
        else
        {
            //TODO handle error
        }



    }

    protected boolean isEmpty(EditText t)
    {
        if(t.getText().toString().trim().length() == 0)
            return true;
        return false;

    }


}


