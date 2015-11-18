package fabienflorek.slip.uk.smartlock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;


public class FriendListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    ExpandableListView listView;
    FloatingActionButton floatingActionButton;

    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<Lock> lockList;
    ArrayList<Friend> friendList;
    FriendListExpandableAdapter friendListExpandableAdapter;
    String URL_LIST;

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
    public static FriendListFragment newInstance(int sectionNumber) {
        FriendListFragment fragment = new FriendListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public FriendListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_screen_with_lists_friends, container, false);


        listView = (ExpandableListView) rootView.findViewById(R.id.expandable_listview_lock_list);
        floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.fab_friends);
        lockList = new ArrayList<Lock>();
        friendList = new ArrayList<Friend>();
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout_friends);
        //create new list of locks from preferences

        //create adapter
        friendListExpandableAdapter = new FriendListExpandableAdapter(getContext(),friendList,lockList,getActivity().getLayoutInflater());
        listView.setAdapter(friendListExpandableAdapter);
        //handles on click of a lock, opening closing it
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    final int position, long id) {
                friendListExpandableAdapter.notifyDataSetChanged();

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
                                        onRefresh();

                                    }
                                }
        );

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        onAddFriendFabClick();
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

    public void onAddFriendFabClick() {
        Intent intent = new Intent(getContext(), AddFriendActivity.class);
        startActivityForResult(intent, 3);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {
                String id = data.getStringExtra("id");
                if (id!="") {
                    Util.addFriend(Integer.parseInt(id),getContext());
                    friendListExpandableAdapter.notifyDataSetChanged();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
            }
        }


    }





    @Override
    public void onRefresh() {
        Util.getFriendList(friendList, friendListExpandableAdapter, swipeRefreshLayout, getContext());
        Util.getLockList(lockList,friendListExpandableAdapter,swipeRefreshLayout,getContext());
    }
}
