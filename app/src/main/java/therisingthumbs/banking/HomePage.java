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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class HomePage extends Activity {

    static String userEmail,
                  userFirstName,
                  userLastName;
    static User u;

    static String[] names ={"David","Wendy","David","Adam","Blair", "Sanskriti","Julio","Vyom"};

    //static ListAdapter accountAdapter = new accountAdapter(this,names);

    //static ListView accList = (ListView)findViewById(R.id.accountList);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        /*if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }*/

        u = ((myApplication) this.getApplication()).getUser();
        System.err.println("HomePage: " + u);

        List<ParseObject> accounts;

        ParseQuery<ParseObject> account = ParseQuery.getQuery("Account");
        account.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> accList, ParseException e) {
                //account(s) found
                if (e == null)
                {
                    System.out.println("Found "+accList.size());

                } else
                {
                    System.out.println("ERROR Finding Accounts");
                }
            }
        });

        /*String[] names ={"David","Wendy","David","Adam","Blair", "Sanskriti","Julio","Vyom"};

        ListAdapter accountAdapter = new accountAdapter(this,names);

        ListView accList = (ListView)findViewById(R.id.accountList);*/

        //accList.setAdapter(accountAdapter);

        /*Intent intent = getIntent();
        User user = (User) intent.getParcelableExtra("user_object");
        if (user == null)
           System.err.println("YA FUCKED UP");
        else
            System.err.println("well, maybe not");

        userEmail = user.email;
        userFirstName = user.firstName;
        userLastName = user.lastName;

        System.err.println("email passed: " + user);

        user.printData();*/



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
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
    public class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_home_page, container, false);
            /**
             * Initialize global (file scope) user information
             **/
            accountAdapter accAdapter = new accountAdapter( getActivity(), names);

            ListView accList = (ListView)rootView.findViewById(R.id.accountList);

            accList.setAdapter(accAdapter);

            ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
            query.whereEqualTo("email", userEmail);
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject parseObject, ParseException e) {
                    if (e == null) {
                        userFirstName = parseObject.getString("first_name");
                        userLastName = parseObject.getString("last_name");

                        //TextView text = (TextView) rootView.findViewById(R.id.userEmail);
                        //text.setText("Welcome, " + userFirstName);

                    } else {
                        // error reading parse database!
                    }
                }
            });

            /*TextView text = (TextView) rootView.findViewById(R.id.userEmail);
            text.setText("Welcome, " + u.firstName);*/

            return rootView;
        }
    }
}
