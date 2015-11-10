package fabienflorek.slip.uk.smartlock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by fabienflorek on 11/2/15.
 */
public class Util {

    private static final String prefFileName = "smart_lock_pref";
    private static final String nameForPref = "username";
    private static final String passForPref = "password";

    public static void saveUserNameAndPass(Context context,String name, String pass) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(nameForPref, name);
        editor.putString(passForPref, pass);
        editor.commit();
    }

    public static String readUserName(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String name = sharedPref.getString(nameForPref, "");
        return name;
    }


    public static String readPassword(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String pass = sharedPref.getString(passForPref,"");
        return pass;
    }

    public static void saveLockList(Context context,Set<String> set) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet("lockList",set);
        editor.commit();
    }

    public static Set<String> readLockList(Context context)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> set =  sharedPref.getStringSet("lockList",new HashSet<String>());
        return set;
    }

    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;


    }

    public static void registerUser(final String name, final String pass, final Context context) {
        if (isNetworkAvailable(context)) {
            //create queue for requests
            RequestQueue queue = Volley.newRequestQueue(context);
            //URL for registering
            final String URL = "https://slip-d.herokuapp.com/register-user";
            //final String URL = "https://httpbin.org/post";
            boolean success = false;
            //Start request
            StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(context, "sucesfully registered", Toast.LENGTH_LONG).show();
                            Logging(response);
                            //start Logged in activity
                            Intent intent = new Intent(context, LoggedInActivity.class);
                            context.startActivity(intent);
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Logging(error.toString());
                            String response = "";
                            //deal with error based on status code
                            switch (error.networkResponse.statusCode) {
                                case (406): {response = "user is already in database"; break;}
                                default: response = "unknown response";
                            }
                            Toast.makeText(context,response,Toast.LENGTH_LONG).show();

                        }
                    }
            ) {
                // override this to set body with register details
                @Override
                public byte[] getBody()  {
                    String httpPostBody="email="+name+"&password="+pass;
                    return httpPostBody.getBytes();
                }
            };
            //send out request
            queue.add(postRequest);
        }else
            Toast.makeText(context,"No internet connection",Toast.LENGTH_LONG).show();
    }
    private static void Logging(String msg) {
        Log.d("Util", msg);
    }


    public static void loggedInWorkWithLocks(final String name, final String pass, String URL, final Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, "sucesfully logged in", Toast.LENGTH_LONG).show();
                        Logging(response);

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "problem with operation"+error.networkResponse.statusCode, Toast.LENGTH_LONG).show();
                        Logging(error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<String, String>();
                String creds = String.format("%s:%s",name,pass);
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }
        };
        queue.add(postRequest);

    }
}
