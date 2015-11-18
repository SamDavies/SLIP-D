package fabienflorek.slip.uk.smartlock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.melnykov.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LockListAcitivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    @Bind(R.id.listview_lock_list)
    ListView listView;
    @Bind(R.id.fab)
    FloatingActionButton floatingActionButton;

    static SwipeRefreshLayout swipeRefreshLayout;
    static LockList lockList;
    static LockListAdapter lockListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_list_acitivity);
        ButterKnife.bind(this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        //create new list of locks from preferences
        lockList = new LockList();
        //create adapter
        lockListAdapter = new LockListAdapter(this, lockList);
        listView.setAdapter(lockListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    final int position, long id) {
                lockList.get(position).setStatus(true);
                Util.openLock(lockList.get(position).getId(), getApplicationContext());
                lockListAdapter.notifyDataSetChanged();
            }

        });

        swipeRefreshLayout.setOnRefreshListener(this);
        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        getLockList(getApplicationContext());

                                    }
                                }
        );
    }

    @OnClick(R.id.fab)
    public void onAddLockFabClick() {
        Intent intent = new Intent(this, AddLockActivity.class);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                String name = data.getStringExtra("name");
                String id = data.getStringExtra("id");
                String place = data.getStringExtra("place");
                if (name!="" && id!="") {
                    addNewLockSaveAndNotify(name, Integer.parseInt(id), Integer.parseInt(place));
                    Util.registerLock(Integer.parseInt(id), name, this);
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
            }
        }


    }

    //adds new lock to lists saves to preferences and updates listview
    private void addNewLockSaveAndNotify(String name, Integer id, Integer lockType) {
        Lock lock = new Lock(name, id, lockType);
        lockList.add(lock);
        lockList.storeListToPref(this);
        lockListAdapter.notifyDataSetChanged();

    }

    //This method cannot be part of Util class as others, beacuse it needs access to the list adapter
    public static void getLockList(final Context context) {
        //clear the list at the start to we won't add more items
        lockList.clear();
        if (Util.isNetworkAvailable(context)) {
            //create queue for requests
            RequestQueue queue = Volley.newRequestQueue(context);
            final String name = Util.readUserName(context);
            final String pass = Util.readPassword(context);


            //Start request
            JsonArrayRequest jReq = new JsonArrayRequest(Util.URL_LOCK_LIST,
                    new Response.Listener<JSONArray>() {

                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d("List", response.toString());
                            //lockList = new LockList();
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    lockList.add(convertJsonToLock(response
                                            .getJSONObject(i)));
                                } catch (JSONException e) {
                                    Log.e("Json List",e.toString());
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
                            getLockList(context);
                            break;
                        }
                        default:
                            response = "unknown response";
                    }
                    // stopping swipe refresh
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(context, response, Toast.LENGTH_SHORT).show();

                }
            }){
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
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
    }




    private static Lock convertJsonToLock(JSONObject obj) throws JSONException {
        String name = obj.getString("name");
        boolean status = obj.getBoolean("actually_open");
        int id = obj.getInt("id");
        //Creating places in random as there is no place for this in the API
        Random rand = new Random();
        int place = rand.nextInt(3);
        return new Lock(name,id,status,place);
    }


    @Override
    public void onRefresh() {
        getLockList(this);
    }
}
