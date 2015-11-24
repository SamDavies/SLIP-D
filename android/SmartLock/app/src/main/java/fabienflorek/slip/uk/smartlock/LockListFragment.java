package fabienflorek.slip.uk.smartlock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;


public class LockListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    ListView listView;
    FloatingActionButton floatingActionButton;

    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<Lock> lockList;
    LockListAdapter lockListAdapter;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private  int section = -1;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static LockListFragment newInstance(int sectionNumber) {
        LockListFragment fragment = new LockListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public LockListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_screen_with_lists_lock, container, false);

        int position = getArguments().getInt(ARG_SECTION_NUMBER);

        listView = (ListView) rootView.findViewById(R.id.listview_lock_list);
        floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.fab);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        //create new list of locks from preferences
        lockList = new ArrayList<Lock>();
        //create adapter
        lockListAdapter = new LockListAdapter(getContext(), lockList);
        listView.setAdapter(lockListAdapter);
        //handles on click of a lock, opening closing it
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    final int position, long id) {
                lockListAdapter.notifyDataSetChanged();
                boolean currentStatus = lockList.get(position).isStatus();
                if (currentStatus) {//lock is open
                    Util.closeLock(lockList.get(position).getId(), getContext());
                } else {
                    Util.openLock(lockList.get(position).getId(), getContext());
                }
                //lockList.get(position).setStatus(true);
            }

        });

        //On long press show snack bar with option to delete
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int pos, long id) {

                Snackbar snackbar = Snackbar
                        .make(listView, "Remove " + lockList.get(pos).getName()
                                 + "?", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Remove", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Util.removeFriend(lockList.get(pos).getId(), getContext());
                                onRefresh();
                                //Snackbar snackbar1 = Snackbar.make(listView, "Lock removed!", Snackbar.LENGTH_SHORT);
                                //snackbar1.show();
                            }
                        });

                snackbar.show();
                return true;
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
                                        Util.getLockList(lockList, lockListAdapter, swipeRefreshLayout, getContext());

                                    }
                                }
        );

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        onAddLockFabClick();
                                                    }
                                                }
        );
        /*
        //Refresh list every second
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                getLockList(getApplicationContext());
                handler.postDelayed(this, 1000);
            }
        }, 1000);
        */

        return rootView;
    }

    public void onAddLockFabClick() {
        Intent intent = new Intent(getContext(), AddLockActivity.class);
        Log.e("fragment", section + "");
        startActivityForResult(intent, 2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                String name = data.getStringExtra("name");
                String id = data.getStringExtra("id");
                String place = data.getStringExtra("place");
                if (name!="" && id!="") {
                    addNewLockSaveAndNotify(name, Integer.parseInt(id), Integer.parseInt(place));
                    Util.registerLock(Integer.parseInt(id), name, getContext());
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
        //lockList.storeListToPref(getContext());
        lockListAdapter.notifyDataSetChanged();

    }


    @Override
    public void onRefresh() {
        Util.getLockList(lockList, lockListAdapter, swipeRefreshLayout, getContext());
    }
}
