package fabienflorek.slip.uk.smartlock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by fabienflorek on 11/10/15.
 */
public class LockListAdapter extends ArrayAdapter<Lock> {
    private final Context context;
    private final List<Lock> locks;

    public LockListAdapter(Context context, List<Lock> locks) {
        super(context, -1, locks);
        this.context = context;
        this.locks = locks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.locklist_row, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.list_row_firstLine);
        TextView textView2 = (TextView) rowView.findViewById(R.id.list_row_secondLine);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.list_row_icon);
        textView.setText(locks.get(position).getName());
        textView2.setText("" + locks.get(position).getId());
        if (locks.get(position).status)
            imageView.setImageResource(R.drawable.ic_lock_open_black_24dp);
        else
            imageView.setImageResource(R.drawable.ic_lock_black_24dp);



        return rowView;
    }
}
