package fabienflorek.slip.uk.smartlock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created by fabienflorek on 11/2/15.
 */
public class Util {

    private static final String nameForPref = "username";
    private static final String passForPref = "password";
    private static final String URL_REGISTER_USER = "https://slip-d-4.herokuapp.com/user";
    private static final String URL_LOGIN = "https://slip-d-4.herokuapp.com/me";
    //"https://httpbin.org/get"
    private static final String URL_REGISTER_LOCK = "https://slip-d-4.herokuapp.com/lock";
    public static final String URL_LOCK_LIST = "https://slip-d-4.herokuapp.com/lock";
    public static final String URL_FRIEND_LIST = "https://slip-d-4.herokuapp.com/friend";
    public static final String URL_USER_LIST = "https://slip-d-4.herokuapp.com/user";
    public static final String URL_LOCK_OPEN = "https://slip-d-4.herokuapp.com/open/";
    public static final String URL_LOCK_CLOSED = "https://slip-d-4.herokuapp.com/close/";
    public static final String URL_ADD_FRIEND = "https://slip-d-4.herokuapp.com/friend";
    public static final String URL_DELETE_FRIEND = "https://slip-d-4.herokuapp.com/friend?friend_id=";
    public static final String URL_ADD_LOCK_TO_FRIEND = "https://slip-d-4.herokuapp.com/friend-lock";
    public static final String URL_REMOVE_LOCK_FROM_FRIEND = "https://slip-d-4.herokuapp.com/friend-lock?friend_id=";

    public static int MY_ID = 0;
    private static RequestQueue queue;

    public static void instantiate(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    public static void saveUserNameAndPass(Context context, String name, String pass) {
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
        String pass = sharedPref.getString(passForPref, "");
        return pass;
    }

    public static void saveLockList(Context context, Set<String> set) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet("lockList", set);
        editor.commit();
    }

    public static Set<String> readLockList(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> set = sharedPref.getStringSet("lockList", new HashSet<String>());
        return set;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();


    }

    public static void registerUser(final String name, final String pass, final String first, final String last, final Context context) {
        if (isNetworkAvailable(context)) {
            //create queue for requests

            //Start request
            StringRequest postRequest = new StringRequest(Request.Method.POST, URL_REGISTER_USER,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(context, "Registered!", Toast.LENGTH_SHORT).show();
                            Logging(response);
                            //start Logged in activity
                            Intent intent = new Intent(context, MainScreenWithListsActivity.class);
                            context.startActivity(intent);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Logging(error.toString());
                            String response = "";
                            //deal with error based on status code
                            switch (error.networkResponse.statusCode) {
                                case (406): {
                                    response = "user is already in database";
                                    break;
                                }
                                case (500): {
                                    response = "The server encountered an error, sorry.";
                                    break;
                                }
                                default:
                                    response = "unknown response";
                            }
                            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();

                        }
                    }
            ) {
                // override this to set body with register details
                @Override
                public byte[] getBody() {
                    String httpPostBody = "email=" + name + "&password=" + pass + "&first_name=" + first + "&last_name=" + last;
                    return httpPostBody.getBytes();
                }
            };
            //send out request
            queue.add(postRequest);
        } else
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
    }

    private static void getIdFromJson(JSONObject obj) throws JSONException {
        MY_ID = obj.getInt("id");
    }

    public static void checkUser(final String name, final String pass, final Context context) {
        if (isNetworkAvailable(context)) {
            //create queue for requests

            //Start request
            StringRequest postRequest = new StringRequest(Request.Method.GET, URL_LOGIN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(context, "Logged In", Toast.LENGTH_SHORT).show();
                            Logging(response);
                            try {
                                getIdFromJson(new JSONObject(response));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //start Logged in activity
                            Intent intent = new Intent(context, MainScreenWithListsActivity.class);
                            context.startActivity(intent);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Logging(error.toString());
                            String response = "";
                            //deal with error based on status code
                            switch (error.networkResponse.statusCode) {
                                case (401): {
                                    response = "User is not present or the password is wrong!";
                                    break;
                                }
                                case (404): {
                                    response = "User is not present or the password is wrong!";
                                    break;
                                }
                                case (500): {
                                    response = "The server encountered an error, sorry.";
                                    break;
                                }
                                default:
                                    response = "unknown response";
                            }
                            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();

                        }
                    }
            ) {
                //This method adds our basic authentication token
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    String creds = String.format("%s:%s", name, pass);
                    String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                    params.put("Authorization", auth);
                    return params;
                }
            };
            //send out request
            queue.add(postRequest);
        } else
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
    }


    public static void registerLock(final int lockId, final String lockName, final Context context) {
        if (isNetworkAvailable(context)) {
            //create queue for requests
            final String name = Util.readUserName(context);
            final String pass = Util.readPassword(context);


            //Start request
            StringRequest postRequest = new StringRequest(Request.Method.POST, URL_REGISTER_LOCK,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(context, "Registered a lock!", Toast.LENGTH_SHORT).show();
                            Logging(response);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Logging(error.toString());
                            String response = "";
                            //deal with error based on status code
                            switch (error.networkResponse.statusCode) {
                                case (406): {
                                    response = "Lock is owned by someone else!";
                                    break;
                                }
                                case (500): {
                                    response = "The server encountered an error, sorry.";
                                    break;
                                }
                                default:
                                    response = "unknown response";
                            }
                            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();

                        }
                    }
            ) {
                //This method adds our basic authentication token
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    String creds = String.format("%s:%s", name, pass);
                    String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                    params.put("Authorization", auth);
                    return params;
                }

                // override this to set body with register details
                @Override
                public byte[] getBody() {
                    String httpPostBody = "lock_name=" + lockName + "&lock_id=" + lockId;
                    return httpPostBody.getBytes();
                }
            };

            //send out request
            queue.add(postRequest);
        } else
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
    }

    public static void openLock(final int lockId, final Context context) {
        if (isNetworkAvailable(context)) {
            //create queue for requests
            final String name = Util.readUserName(context);
            final String pass = Util.readPassword(context);


            //Start request
            StringRequest postRequest = new StringRequest(Request.Method.PUT, URL_LOCK_OPEN + lockId,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Logging(response);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Logging(error.toString());
                            String response = "";
                            //deal with error based on status code
                            switch (error.networkResponse.statusCode) {
                                case (406): {
                                    response = "Lock is owned by someone else!";
                                    break;
                                }
                                case (500): {
                                    response = "The server encountered an error, sorry.";
                                    break;
                                }
                                default:
                                    response = "unknown response";
                            }
                            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();

                        }
                    }
            ) {
                //This method adds our basic authentication token
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    String creds = String.format("%s:%s", name, pass);
                    String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                    params.put("Authorization", auth);
                    return params;
                }

                // override this to set body with register details
                @Override
                public byte[] getBody() {
                    String httpPostBody = "lock_id=" + lockId;
                    return httpPostBody.getBytes();
                }
            };

            //send out request
            queue.add(postRequest);
        } else
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
    }


    public static void closeLock(final int lockId, final Context context) {
        if (isNetworkAvailable(context)) {
            //create queue for requests
            final String name = Util.readUserName(context);
            final String pass = Util.readPassword(context);


            //Start request
            StringRequest postRequest = new StringRequest(Request.Method.PUT, URL_LOCK_CLOSED + lockId,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Logging(response);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Logging(error.toString());
                            String response = "";
                            //deal with error based on status code
                            switch (error.networkResponse.statusCode) {
                                case (406): {
                                    response = "Lock is owned by someone else!";
                                    break;
                                }
                                case (500): {
                                    response = "The server encountered an error, sorry.";
                                    break;
                                }
                                default:
                                    response = "unknown response";
                            }
                            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();

                        }
                    }
            ) {
                //This method adds our basic authentication token
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    String creds = String.format("%s:%s", name, pass);
                    String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                    params.put("Authorization", auth);
                    return params;
                }

                // override this to set body with register details
                @Override
                public byte[] getBody() {
                    String httpPostBody = "lock_id=" + lockId;
                    return httpPostBody.getBytes();
                }
            };

            //send out request
            queue.add(postRequest);
        } else
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
    }


    public static void getLockList(final ArrayList<Lock> lockList, final LockListAdapter lockListAdapter, final SwipeRefreshLayout swipeRefreshLayout, final Context context) {
        if (Util.isNetworkAvailable(context)) {
            //create queue for requests
            final String name = Util.readUserName(context);
            final String pass = Util.readPassword(context);


            //Start request
            JsonArrayRequest jReq = new JsonArrayRequest(Util.URL_LOCK_LIST,
                    new Response.Listener<JSONArray>() {

                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d("List", response.toString());
                            //clear the list at the start to we won't add more items
                            lockList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    lockList.add(convertJsonToLock(response
                                            .getJSONObject(i)));
                                } catch (JSONException e) {
                                    Log.e("Json List", e.toString());
                                }
                            }
                            //lockListAdapter.
                            lockListAdapter.notifyDataSetChanged();
                            // stopping swipe refresh
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("List", error.toString());
                    String response = "";
                    //deal with error based on status code
                    switch (error.networkResponse.statusCode) {
                        case (500): {
                            response = "The server encountered an error, sorry.";
                            //server encountered an error, retry again
                            getLockList(lockList, lockListAdapter, swipeRefreshLayout, context);
                            break;
                        }
                        default:
                            response = "unknown response";
                    }
                    // stopping swipe refresh
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(context, response, Toast.LENGTH_SHORT).show();

                }
            }) {
                //This method adds our basic authentication token
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    String creds = String.format("%s:%s", name, pass);
                    String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                    params.put("Authorization", auth);
                    return params;
                }
            };

            //send out request
            queue.add(jReq);
        } else
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
    }


    public static void getLockList(final ArrayList<Lock> lockList, final FriendListExpandableAdapter friendListExpandableAdapter, final SwipeRefreshLayout swipeRefreshLayout, final Context context) {
        if (Util.isNetworkAvailable(context)) {
            //create queue for requests
            final String name = Util.readUserName(context);
            final String pass = Util.readPassword(context);


            //Start request
            JsonArrayRequest jReq = new JsonArrayRequest(Util.URL_LOCK_LIST,
                    new Response.Listener<JSONArray>() {

                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d("List", response.toString());
                            //clear the list at the start to we won't add more items
                            lockList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    Lock lock = convertJsonToLock(response
                                                    .getJSONObject(i));
                                    //only add lock into list of locks we can add to users if we own it
                                    if (lock.isOwner()) lockList.add(lock);
                                } catch (JSONException e) {
                                    Log.e("Json List", e.toString());
                                }
                            }
                            //lockListAdapter.
                            friendListExpandableAdapter.notifyDataSetChanged();
                            // stopping swipe refresh
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("List", error.toString());
                    String response = "";
                    //deal with error based on status code
                    switch (error.networkResponse.statusCode) {
                        case (500): {
                            response = "The server encountered an error, sorry.";
                            //server encountered an error, retry again
                            getLockList(lockList, friendListExpandableAdapter, swipeRefreshLayout, context);
                            break;
                        }
                        default:
                            response = "unknown response";
                    }
                    // stopping swipe refresh
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(context, response, Toast.LENGTH_SHORT).show();

                }
            }) {
                //This method adds our basic authentication token
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    String creds = String.format("%s:%s", name, pass);
                    String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                    params.put("Authorization", auth);
                    return params;
                }
            };

            //send out request
            queue.add(jReq);
        } else
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
    }


    private static Lock convertJsonToLock(JSONObject obj) throws JSONException {
        String name = obj.getString("name");
        boolean status = obj.getBoolean("actually_open");
        boolean statusRequested = obj.getBoolean("requested_open");
        int id = obj.getInt("id");
        //Creating places in random as there is no place for this in the API
        Random rand = new Random();
        int place = rand.nextInt(3);
        int owner = obj.getInt("owner_id");
        return new Lock(name, id, status, statusRequested, place, owner);
    }

    public static void getFriendList(final ArrayList<Friend> friendList, final FriendListAdapter friendListAdapter, final SwipeRefreshLayout swipeRefreshLayout, final Context context) {
        if (Util.isNetworkAvailable(context)) {
            //create queue for requests
            final String name = Util.readUserName(context);
            final String pass = Util.readPassword(context);


            //Start request
            JsonArrayRequest jReq = new JsonArrayRequest(Util.URL_FRIEND_LIST,
                    new Response.Listener<JSONArray>() {

                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d("Friend List", response.toString());
                            //clear the list at the start to we won't add more items
                            friendList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    friendList.add(convertJsonToFriend(response
                                            .getJSONObject(i)));
                                } catch (JSONException e) {
                                    Log.e("Json List", e.toString());
                                }
                            }
                            //lockListAdapter.
                            friendListAdapter.notifyDataSetChanged();
                            // stopping swipe refresh
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("List", error.toString());
                    String response = "";
                    //deal with error based on status code
                    switch (error.networkResponse.statusCode) {
                        case (500): {
                            response = "The server encountered an error, sorry.";
                            //server encountered an error, retry again
                            getFriendList(friendList, friendListAdapter, swipeRefreshLayout, context);
                            break;
                        }
                        default:
                            response = "unknown response";
                    }
                    // stopping swipe refresh
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(context, response, Toast.LENGTH_SHORT).show();

                }
            }) {
                //This method adds our basic authentication token
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    String creds = String.format("%s:%s", name, pass);
                    String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                    params.put("Authorization", auth);
                    return params;
                }
            };

            //send out request
            queue.add(jReq);
        } else
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
    }

    public static void getFriendList(final ArrayList<Friend> friendList, final FriendListExpandableAdapter friendListAdapter, final SwipeRefreshLayout swipeRefreshLayout, final Context context) {
        if (Util.isNetworkAvailable(context)) {
            //create queue for requests
            final String name = Util.readUserName(context);
            final String pass = Util.readPassword(context);


            //Start request
            JsonArrayRequest jReq = new JsonArrayRequest(Util.URL_FRIEND_LIST,
                    new Response.Listener<JSONArray>() {

                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d("Friend List", response.toString());
                            //clear the list at the start to we won't add more items
                            friendList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    friendList.add(convertJsonToFriend(response
                                            .getJSONObject(i)));
                                } catch (JSONException e) {
                                    Log.e("Json List", e.toString());
                                }
                            }
                            //lockListAdapter.
                            friendListAdapter.notifyDataSetChanged();
                            // stopping swipe refresh
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("List", error.toString());
                    String response = "";
                    //deal with error based on status code
                    switch (error.networkResponse.statusCode) {
                        case (500): {
                            response = "The server encountered an error, sorry.";
                            //server encountered an error, retry again
                            getFriendList(friendList, friendListAdapter, swipeRefreshLayout, context);
                            break;
                        }
                        default:
                            response = "unknown response";
                    }
                    // stopping swipe refresh
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(context, response, Toast.LENGTH_SHORT).show();

                }
            }) {
                //This method adds our basic authentication token
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    String creds = String.format("%s:%s", name, pass);
                    String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                    params.put("Authorization", auth);
                    return params;
                }
            };

            //send out request
            queue.add(jReq);
        } else
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
    }


    private static Friend convertJsonToFriend(JSONObject obj) throws JSONException {
        String firstName = obj.getString("first_name");
        String lastName = obj.getString("last_name");
        int id = obj.getInt("id");
        List<Integer> myLocks = new ArrayList<Integer>();
        JSONArray lockArray = obj.getJSONArray("your_locks");
        for (int i=0; i<lockArray.length();i++) {
            if (lockArray.getJSONObject(i).getBoolean("has_access"))
                myLocks.add(lockArray.getJSONObject(i).getInt("id"));
        }
        return new Friend(firstName, lastName, id,myLocks);
    }


    private static Friend convertJsonToUser(JSONObject obj) throws JSONException {
        String firstName = obj.getString("first_name");
        String lastName = obj.getString("last_name");
        int id = obj.getInt("id");
        boolean isFriend = obj.getBoolean("is_friend");
        //if we are already friends we don't want to return this user
        return isFriend ? null :new Friend(firstName, lastName, id);
    }


    public static void getUserList(final ArrayList<Friend> friendList, final FriendListAdapter friendListAdapter, final Context context) {
        if (Util.isNetworkAvailable(context)) {
            //create queue for requests
            final String name = Util.readUserName(context);
            final String pass = Util.readPassword(context);

            //Start request
            JsonArrayRequest jReq = new JsonArrayRequest(Util.URL_USER_LIST,
                    new Response.Listener<JSONArray>() {

                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d("User List", response.toString());
                            //clear the list at the start to we won't add more items
                            friendList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    Friend notYetFriend = convertJsonToUser(response
                                                    .getJSONObject(i));
                                    if (notYetFriend!=null) friendList.add(notYetFriend);
                                } catch (JSONException e) {
                                    Log.e("Json List", e.toString());
                                }
                            }
                            friendListAdapter.notifyDataSetChanged();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("List", error.toString());
                    String response = "";
                    //deal with error based on status code
                    switch (error.networkResponse.statusCode) {
                        case (500): {
                            response = "The server encountered an error, sorry.";
                            //server encountered an error, retry again
                            getUserList(friendList, friendListAdapter, context);
                            break;
                        }
                        default:
                            response = "unknown response";
                    }
                    // stopping swipe refresh
                    Toast.makeText(context, response, Toast.LENGTH_SHORT).show();

                }
            }) {
                //This method adds our basic authentication token
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    String creds = String.format("%s:%s", name, pass);
                    String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                    params.put("Authorization", auth);
                    return params;
                }
            };

            //send out request
            queue.add(jReq);
        } else
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
    }

    public static void addFriend(final int id, final Context context) {
        if (isNetworkAvailable(context)) {
            //create queue for requests
            final String name = Util.readUserName(context);
            final String pass = Util.readPassword(context);

            //Start request
            StringRequest postRequest = new StringRequest(Request.Method.POST, URL_ADD_FRIEND,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(context, "Friend added!", Toast.LENGTH_SHORT).show();
                            Logging(response);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Logging(error.toString());
                            String response = "";
                            //deal with error based on status code
                            switch (error.networkResponse.statusCode) {
                                case (401): {
                                    response = "Already Friends";
                                    break;
                                }
                                case (500): {
                                    response = "The server encountered an error, sorry.";
                                    break;
                                }
                                default:
                                    response = "unknown response";
                            }
                            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();

                        }
                    }
            ) {
                //This method adds our basic authentication token
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    String creds = String.format("%s:%s", name, pass);
                    String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                    params.put("Authorization", auth);
                    return params;
                }

                // override this to set body with register details
                @Override
                public byte[] getBody() {
                    String httpPostBody = "friend_id=" + id;
                    return httpPostBody.getBytes();
                }
            };
            //send out request
            queue.add(postRequest);
        } else
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
    }

    public static void removeFriend(final int id, final Context context) {
        if (isNetworkAvailable(context)) {
            //create queue for requests
            final String name = Util.readUserName(context);
            final String pass = Util.readPassword(context);

            //Start request
            StringRequest postRequest = new StringRequest(Request.Method.DELETE, URL_DELETE_FRIEND+id,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(context, "Friend added!", Toast.LENGTH_SHORT).show();
                            Logging(response);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Logging(error.toString());
                            String response = "";
                            //deal with error based on status code
                            switch (error.networkResponse.statusCode) {
                                case (401): {
                                    response = "Not a friend";
                                    break;
                                }
                                case (500): {
                                    response = "The server encountered an error, sorry.";
                                    break;
                                }
                                default:
                                    response = "unknown response";
                            }
                            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();

                        }
                    }
            ) {
                //This method adds our basic authentication token
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    String creds = String.format("%s:%s", name, pass);
                    String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                    params.put("Authorization", auth);
                    return params;
                }

                // override this to set body with register details
                @Override
                public byte[] getBody() {
                    String httpPostBody = "friend_id=" + id;
                    return httpPostBody.getBytes();
                }
            };
            //send out request
            queue.add(postRequest);
        } else
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
    }

    public static void addFriendToLock(final int friendId, final int lock_id, final Context context) {
        if (isNetworkAvailable(context)) {
            //create queue for requests
            final String name = Util.readUserName(context);
            final String pass = Util.readPassword(context);

            //Start request
            StringRequest postRequest = new StringRequest(Request.Method.POST, URL_ADD_LOCK_TO_FRIEND,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(context, "Lock added to friend !", Toast.LENGTH_SHORT).show();
                            Logging(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Logging(error.toString());
                            String response = "";
                            //deal with error based on status code
                            switch (error.networkResponse.statusCode) {
                                case (400): {
                                    response = "You don't own this lock or you are not friends";
                                    break;
                                }
                                case (500): {
                                    response = "The server encountered an error, sorry.";
                                    //addFriendToLock(friendId,lock_id,context);
                                    break;
                                }
                                default:
                                    response = "unknown response";
                            }
                            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();

                        }
                    }
            ) {
                //This method adds our basic authentication token
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    String creds = String.format("%s:%s", name, pass);
                    String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                    params.put("Authorization", auth);
                    return params;
                }

                // override this to set body with register details
                @Override
                public byte[] getBody() {
                    String httpPostBody = "friend_id=" + friendId + "&lock_id=" + lock_id;
                    return httpPostBody.getBytes();
                }
            };
            //send out request
            queue.add(postRequest);
        } else
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
    }

    public static void removeFriendFromLock(final int friendId, final int lock_id, final Context context) {
        if (isNetworkAvailable(context)) {
            //create queue for requests
            final String name = Util.readUserName(context);
            final String pass = Util.readPassword(context);

            //Start request
            StringRequest postRequest = new StringRequest(Request.Method.DELETE, URL_REMOVE_LOCK_FROM_FRIEND+friendId+"&lock_id="+lock_id,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(context, "Lock removed from friend !", Toast.LENGTH_SHORT).show();
                            Logging(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Logging(error.toString());
                            String response = "";
                            //deal with error based on status code
                            switch (error.networkResponse.statusCode) {
                                case (400): {
                                    response = "You don't own this lock or you are not friends";
                                    break;
                                }
                                case (500): {
                                    response = "The server encountered an error, sorry.";
                                    //removeFriendFromLock(friendId,lock_id,context);

                                    break;
                                }
                                default:
                                    response = "unknown response";
                            }
                            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();

                        }
                    }
            ) {
                //This method adds our basic authentication token
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    String creds = String.format("%s:%s", name, pass);
                    String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                    params.put("Authorization", auth);
                    return params;
                }

                // override this to set body with register details
                @Override
                public byte[] getBody() {
                    String httpPostBody = "friend_id=" + friendId + "&lock_id=" + lock_id;
                    return httpPostBody.getBytes();
                }
            };
            //send out request
            queue.add(postRequest);
        } else
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
    }


    private static void Logging(String msg) {
        Log.d("Util", msg);
    }


}
