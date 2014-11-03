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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class HomePage extends Activity {

    static String userEmail,
                  userFirstName,
                  userLastName;
    static User u;
    static int screenWidth, screenHeight; //vars to hold the size of the screen
    static boolean isPortrait; //bool used to check if in portrait or landscape

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
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_home_page, container, false);
            /**
             * Initialize global (file scope) user information
             */
            ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
            query.whereEqualTo("email", userEmail);
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject parseObject, ParseException e) {
                    if (e == null) {
                        userFirstName = parseObject.getString("first_name");
                        userLastName = parseObject.getString("last_name");

                        TextView text = (TextView) rootView.findViewById(R.id.userEmail);
                        text.setText("Welcome, " + userFirstName);

                    } else {
                        // error reading parse database!
                    }
                }
            });

            TextView text = (TextView) rootView.findViewById(R.id.userEmail);
            text.setText("Welcome, " + u.firstName);
            text.setTextSize(20);
            text.setPadding(0, screenHeight/8, 0, 0);

            LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.linearLayout);
            TextView newText = new TextView(rootView.getContext());

            newText.setText("TESTING");

            layout.addView(newText);

            return rootView;
        }
    }
}
