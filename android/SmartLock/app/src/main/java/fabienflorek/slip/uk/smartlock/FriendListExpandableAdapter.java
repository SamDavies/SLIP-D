package fabienflorek.slip.uk.smartlock;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import fabienflorek.slip.uk.smartlock.identicon.HashGeneratorInterface;
import fabienflorek.slip.uk.smartlock.identicon.IdenticonGenerator;
import fabienflorek.slip.uk.smartlock.identicon.MessageDigestHashGenerator;

/**
 * Created by fabienflorek on 11/10/15.
 */
public class FriendListExpandableAdapter extends BaseExpandableListAdapter {
    private final Context context;
    LayoutInflater inflater;
    private List<Lock> lockList;
    private List<Friend> friendList;


    public FriendListExpandableAdapter(Context context, List<Friend> friendList,List<Lock> lockList,LayoutInflater inflater) {
        this.context = context;
        this.friendList = friendList;
        this.lockList = lockList;
        this.inflater = inflater;
    }

    @Override
    public int getGroupCount() {
        return friendList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return lockList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return friendList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return  lockList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_lists_friend_list_row, parent,false);
        }
        Friend friend = (Friend) getGroup(groupPosition);
        TextView textViewFirst = (TextView) convertView.findViewById(R.id.list_row_friends_firstLine);
        ImageView imageViewPlace = (ImageView) convertView.findViewById(R.id.list_row_friends_iconplace);
        ImageView imageViewIndicator = (ImageView) convertView.findViewById(R.id.list_row_friends_dropdown);

        String name = friend.getFirstName() + " " + friend.getLastName();
        textViewFirst.setText(name);


        HashGeneratorInterface hashGenerator = new MessageDigestHashGenerator(
                "MD5");
        Bitmap identicon = IdenticonGenerator.generate(name,
                hashGenerator);

        int size = context.getResources().getDimensionPixelSize(R.dimen.list_identicon_size);
        identicon = Bitmap.createScaledBitmap(identicon,size , size, false);
        imageViewPlace.setImageBitmap(identicon);

        imageViewIndicator.setColorFilter(context.getResources().getColor(R.color.accent));
        if (isExpanded)
            imageViewIndicator.setImageResource(R.drawable.group_up);
        else
            imageViewIndicator.setImageResource(R.drawable.group_down);

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_list_friend_list_row_detail, parent,false);
        }
        TextView textViewFirst = (TextView) convertView.findViewById(R.id.list_row_friends_firstLine_detail);
        Lock lock = lockList.get(childPosition);
        textViewFirst.setText(lock.getName());


        final SwitchCompat switchCompat = (SwitchCompat) convertView.findViewById(R.id.list_row_friends_switch);
        //if current lock is present in the list of locks the friends has access to
        if (friendList.get(groupPosition).getMyLocks().contains(lockList.get(childPosition).getId()))
            switchCompat.setChecked(true);
        else
            switchCompat.setChecked(false);
        /*
        switchCompat.setOnCheckedChangeListener(new SwitchCompat.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (buttonView.getId()) {

                    case R.id.list_row_friends_switch:

                        if(isChecked){
                            Log.e("checking","checked");
                            //Util.addFriendToLock(friendList.get(groupPosition).getId(), lockList.get(childPosition).getId(), context);
                        }else{
                            //Util.removeFriendFromLock(friendList.get(groupPosition).getId(), lockList.get(childPosition).getId(), context);

                        }
                        break;

                    default:
                        break;
                }
            }
        });
        */

        switchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("checking", "checked" + switchCompat.isChecked());
                checkChanged(switchCompat,groupPosition,childPosition);

            }
        });

        //anywhere on the item you click it will be intercepted as checking the switch
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchCompat.setChecked(!switchCompat.isChecked());
                checkChanged(switchCompat,groupPosition,childPosition);
            }
        });

        return convertView;
    }

    private void checkChanged(SwitchCompat switchCompat,int groupPosition, int childPosition) {
        if (switchCompat.isChecked())
            Util.addFriendToLock(friendList.get(groupPosition).getId(), lockList.get(childPosition).getId(), context);
         else
            Util.removeFriendFromLock(friendList.get(groupPosition).getId(), lockList.get(childPosition).getId(), context);
        

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
