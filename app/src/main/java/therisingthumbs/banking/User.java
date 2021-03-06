package therisingthumbs.banking;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by dhart_000 on 11/1/2014.
 */
@ParseClassName("User")
public class User extends ParseObject implements Parcelable {
    private int mData;
    /**
     * User class data fields
     */
    public String firstName,
           lastName,
           email,
           pass,
           accountTitle,
           accountType,
           retString;


    /* default constructor */
    public User() {}

    /* copy constructor */
    public User (User u)
    {
        this.firstName = u.firstName;
        this.lastName = u.lastName;
        this.email = u.email;
        this.pass = u.pass;
        this.accountTitle = u.accountTitle;
        this.accountType = u.accountType;
    }

    /* Other constructor */
    public User (String first, String last, String email, String pass) {
        this.firstName = first;
        this.lastName = last;
        this.email = email;
        this.pass = pass;
    }

    /**
     * Parse Query methods
     * OUR STANDARD:
     *      upon error, these methods return an error message to be displayed
     *      on the calling screen (as a toast and/or message on screen). To
     *      determine if the calling method was successful, our methods here
     *      will return NULL
     *      Example from a calling method
     *
     *      if ( !String e = user.addToDatabase() ) {
     *          // call was successful
     *      }
     *      else {
     *          // call failed, use String e to set our error message
     *      }
     *
     */

    public void setRetString(String string) {
        this.retString = string;
    }

    /**
     * Method used to deposit into an account in the database
     */
    public void deposit(double a, Activity act) {

        final Activity activity = act;
        final double amount = a;

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Account");
        query.whereEqualTo("email", this.email);
        query.whereEqualTo("type", this.accountType);
        query.whereEqualTo("title", this.accountTitle);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    // object exists
                    double balance = Double.parseDouble(parseObject.get("balance").toString());
                    double newAmount = balance + amount;
                    parseObject.put("balance", newAmount);
                    parseObject.saveInBackground();

                    Intent intent = new Intent(activity, ManageAccount.class);
                    activity.startActivity(intent);

                } else {
                    // YOU FUCKED UP
                }
            }
        });
    }

    /**
     * Method used to withdraw money from account
     */
    public void withdraw(double a, Activity act) {

        final Activity activity = act;
        final double amount = a;

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Account");
        query.whereEqualTo("email", this.email);
        query.whereEqualTo("type", this.accountType);
        query.whereEqualTo("title", this.accountTitle);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    // object exists
                    double balance = Double.parseDouble(parseObject.get("balance").toString());
                    if (amount > balance) {
                        //GO FUCK YOURSELF
                    }
                    else {
                        // We're good :)
                        double newAmount = balance - amount;
                        parseObject.put("balance", newAmount);
                        parseObject.saveInBackground();

                        Intent intent = new Intent(activity, ManageAccount.class);
                        activity.startActivity(intent);
                    }
                } else {
                    // YOU FUCKED UP HARD

                }
            }
        });
    }

    public String addToDatabase() {

        final User u = this;

        System.err.println("User is User class: " + this);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        query.whereEqualTo("email", this.email);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject parseObject, ParseException e) {

                if (e == null) {
                    u.setRetString( u.email + " already exists!");
                    System.err.println("USER.JAVA USER EXISTS");

                } else {
                    //User does not exists, create account
                    u.put("email", u.email);
                    u.put("first_name", u.firstName);
                    u.put("last_name", u.lastName);
                    u.put("pass", u.pass);
                    u.saveInBackground();
                    u.setRetString(null);
                }
            }
        });
        System.err.println("RETSTRING:" + u.retString);
        return retString;
    }

    public void printData() {
        System.err.println(firstName);
        System.err.println(lastName);
        System.err.println(email);
        System.err.println(pass);

    }

    @Override
    public int describeContents() {
        return 0;
    }
    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mData);
    }

    public User( Parcel p ) {

    }
    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}