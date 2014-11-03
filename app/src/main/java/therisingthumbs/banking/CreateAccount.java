package therisingthumbs.banking;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.parse.*;



public class CreateAccount extends Activity {

    static Spinner spinner;
    static User u; //current user


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        u = ((myApplication) this.getApplication()).getUser();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_account, menu);
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
            View rootView = inflater.inflate(R.layout.fragment_create_account, container, false);

            /**
             * Create our cool spinner that doesn't spin
             */
            spinner = (Spinner) rootView.findViewById(R.id.spinner);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    rootView.getContext(),
                    R.array.create_spinner,
                    android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            return rootView;
        }
    }

    /**
     * Respond to create button being clicked
     */
    public void createAccount(View view) {

        final Intent intent = new Intent(this, ManageAccount.class);

        System.err.println("Create button clicked");

        System.err.println("Spinner: " + spinner.getSelectedItem().toString());

        final EditText  title = (EditText) findViewById(R.id.title);
        final String type = spinner.getSelectedItem().toString();

        /**
         * Make sure edittext field are not empty
         */
        if(!isEmpty(title)) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Account");
            query.whereEqualTo("email", u.email);
            query.whereEqualTo("type", type);
            query.whereEqualTo("title", title.getText().toString());
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject parseObject, ParseException e) {
                    if (e == null) {
                      // object exists
                        System.err.println("Account exists");
                    } else {
                      // does not exists, create
                        System.err.println("Creating account");
                        ParseObject account = new ParseObject("Account");
                        account.put("email", u.email);
                        account.put("title", title.getText().toString());
                        account.put("type", type);
                        account.put("balance", 0);
                        account.saveInBackground();

                        u.accountType = type;
                        u.accountTitle = title.getText().toString();

                        startActivity(intent);
                    }
                }
            });
        }
        else {
            // TODO
            // edit text field is empty!
        }


    }

    protected boolean isEmpty(EditText t)
    {
        if(t.getText().toString().trim().length() == 0)
            return true;
        return false;

    }
}
