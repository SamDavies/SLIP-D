package fabienflorek.slip.uk.smartlock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddFriendActivity extends AppCompatActivity {

    @Bind(R.id.listview_user_list)
    ListView listView;
    ArrayList<Friend> friendList;
    FriendListAdapter friendListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ButterKnife.bind(this);

        //create new list of locks from preferences
        friendList= new ArrayList<Friend>();
        //create adapter
        friendListAdapter = new FriendListAdapter(this, friendList);
        listView.setAdapter(friendListAdapter);
        //populate list
        Util.getUserList(friendList,friendListAdapter,this);
        //handles on click of a lock, opening closing it
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    final int position, long id) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("id",String.valueOf(friendList.get(position).getId()));
                setResult(Activity.RESULT_OK, returnIntent);
                finish();

            }

        });
    }

}
