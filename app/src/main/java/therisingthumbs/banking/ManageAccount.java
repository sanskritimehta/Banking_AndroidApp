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
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class ManageAccount extends Activity {

    static User u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);
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
        getMenuInflater().inflate(R.menu.manage_account, menu);
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
            final View rootView = inflater.inflate(R.layout.fragment_manage_account, container, false);

            TextView title = (TextView) rootView.findViewById(R.id.account_title);
            title.setText("Account Title: " +u.accountTitle);

            TextView type = (TextView) rootView.findViewById(R.id.account_type);
            type.setText("Account Type: " + u.accountType);



            ParseQuery<ParseObject> query = ParseQuery.getQuery("Account");
            query.whereEqualTo("email", u.email);
            query.whereEqualTo("type", u.accountType);
            query.whereEqualTo("title", u.accountTitle);
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject parseObject, ParseException e) {
                    if (e == null) {
                        // object exists
                        TextView balance = (TextView) rootView.findViewById(R.id.balance);
                        balance.setText("Current balance: $" + parseObject.get("balance"));
                    } else {
                        // does not exists, create
                        System.err.println("FUCK");
                    }
                }
            });

            return rootView;
        }
    }

    public void deposit(View view) {
        EditText e = (EditText) findViewById(R.id.enter_amount);
        if (!isEmpty(e)) {
            u.deposit(Double.parseDouble(e.getText().toString()), this);
        }
    }

    public void withdraw(View view) {
        EditText e = (EditText) findViewById(R.id.enter_amount);
        if(!isEmpty(e)) {
            u.withdraw(Double.parseDouble(e.getText().toString()), this);
        }
    }

    protected boolean isEmpty(EditText t)
    {
        if(t.getText().toString().trim().length() == 0)
            return true;
        return false;

    }
}
