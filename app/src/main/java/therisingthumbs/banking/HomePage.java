package therisingthumbs.banking;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class HomePage extends Activity {

    static String userEmail,
                  userFirstName,
                  userLastName;
    static User u;
    static int screenWidth, screenHeight; //vars to hold the size of the screen
    static boolean isPortrait; //bool used to check if in portrait or landscape

    static List<ParseObject> accounts = new ArrayList<ParseObject>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        u = ((myApplication) this.getApplication()).getUser();
        System.err.println("HomePage: " + u);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        if (screenWidth <= screenHeight)
        {
            isPortrait = true;
        }
        else
            isPortrait = false;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            System.err.println("SETTINGS CLICKED");
            return true;
        }

        if (id == R.id.create) {
            Intent intent = new Intent(this, CreateAccount.class);
            startActivity(intent);
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
            final View rootView = inflater.inflate(R.layout.fragment_home_page, container, false);
            /**
             * Initialize global (file scope) user information
             **/

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

            TextView text = (TextView) rootView.findViewById(R.id.userEmail);
            text.setText("Welcome, " + u.firstName);
            text.setTextSize(20);
            text.setPadding(0, screenHeight/8, 0, 0);

            //clear accounts list to prevent duplicate display of accounts
            accounts.clear();

            //System.err.println("Accounts empty:"+ accounts.isEmpty());

            /*Find accounts*/
            ParseQuery<ParseObject> acc = ParseQuery.getQuery("Account");
            acc.whereEqualTo("email",u.email);
            acc.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> accList, ParseException e) {
                    //account(s) found
                    if (e == null)
                    {
                        //add all the accounts found
                        accounts.addAll(accList);
                        //System.err.println("Found "+accounts.size());

                    } else
                    {
                        System.err.println("ERROR Finding Accounts");
                    }
                    System.err.println("Accounts size "+accounts.size());

                    //this is used to populate the ListView
                    ListView accView = (ListView)rootView.findViewById(R.id.accountList);
                    accountAdapter accAdapter = new accountAdapter( getActivity(), accounts);
                    accView.setAdapter(accAdapter);
                    ///////////////////

                    //set click function for the list view
                    accView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            Intent intent = new Intent( getActivity(),ManageAccount.class);

                            ParseObject acc = (ParseObject)adapterView.getItemAtPosition(i);

                            String type = acc.getString("type");
                            String title = acc.getString("title");

                            u.accountType = type;
                            u.accountTitle = title;

                            startActivity(intent);

                         }
                    });

                }
            });


            return rootView;
        }
    }

}
