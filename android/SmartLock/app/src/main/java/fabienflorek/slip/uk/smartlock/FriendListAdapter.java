package fabienflorek.slip.uk.smartlock;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import fabienflorek.slip.uk.smartlock.identicon.HashGeneratorInterface;
import fabienflorek.slip.uk.smartlock.identicon.IdenticonGenerator;
import fabienflorek.slip.uk.smartlock.identicon.MessageDigestHashGenerator;

/**
 * Created by fabienflorek on 11/10/15.
 */
public class FriendListAdapter extends ArrayAdapter<Friend> {
    private final Context context;
    private final List<Friend> friends;

    public FriendListAdapter(Context context, List<Friend> friends) {
        super(context, -1, friends);
        this.context = context;
        this.friends = friends;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.fragment_lists_friend_list_row, parent, false);
        TextView textViewFirst = (TextView) rowView.findViewById(R.id.list_row_friends_firstLine);
        ImageView imageViewPlace = (ImageView) rowView.findViewById(R.id.list_row_friends_iconplace);
        String name = friends.get(position).getFirstName() + " " + friends.get(position).getLastName();
        textViewFirst.setText(name);


        HashGeneratorInterface hashGenerator = new MessageDigestHashGenerator(
                "MD5");
        Bitmap identicon = IdenticonGenerator.generate(name,
                hashGenerator);

        int size = context.getResources().getDimensionPixelSize(R.dimen.list_identicon_size);
        identicon = Bitmap.createScaledBitmap(identicon,size , size, false);
        imageViewPlace.setImageBitmap(identicon);





        return rowView;
    }
}
