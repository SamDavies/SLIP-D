package fabienflorek.slip.uk.smartlock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LockListAcitivity extends AppCompatActivity {

    @Bind(R.id.listview_lock_list)
    ListView listView;
    @Bind(R.id.fab)
    FloatingActionButton floatingActionButton;

    LockList lockList;
    LockListAdapter lockListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_list_acitivity);
        ButterKnife.bind(this);

        //create new list of locks from preferences
        lockList = new LockList(this);
        //create adapter
        lockListAdapter = new LockListAdapter(this,lockList);
        listView.setAdapter(lockListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    final int position, long id) {
                lockList.get(position).setStatus(true);
                lockListAdapter.notifyDataSetChanged();
            }

        });
    }

    @OnClick(R.id.fab)
    public void onAddLockFabClick()
    {
        Intent intent = new Intent(this,AddLockActivity.class);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == 2) {
            if(resultCode == Activity.RESULT_OK){
                String name = data.getStringExtra("name");
                String id = data.getStringExtra("id");
                String place = data.getStringExtra("place");
                addNewLockSaveAndNotify(name, Integer.parseInt(id),Integer.parseInt(place));
            }
            if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
    }
    //adds new lock to lists saves to preferences and updates listview
    private void addNewLockSaveAndNotify(String name,Integer id,Integer lockType) {
        Lock lock = new Lock(name,id,lockType);
        lockList.add(lock);
        lockList.storeListToPref(this);
        lockListAdapter.notifyDataSetChanged();

    }

}
