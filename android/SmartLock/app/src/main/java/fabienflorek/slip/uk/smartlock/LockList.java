package fabienflorek.slip.uk.smartlock;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by fabienflorek on 11/10/15.
 */
public class LockList extends ArrayList<Lock>{

    public LockList(Context context) {
        this.addAll(loadListFromPref(context));
    }
    //creates test lock list
    public LockList() {
        super();
        this.add(new Lock("westminister",1));
    }

    private Set<String> convertListToStringSet() {
        Set<String> set = new TreeSet<String>();
        List<Lock> lockList = this;
        for (Lock lock:lockList) {
            set.add(lock.toString());
        }
        return set;
    }
    private List<Lock> convertSetToList(Set<String> set) {
        List<Lock> list = new ArrayList<Lock>();
        for (String string: set) {
            list.add(parseStringToLock(string));
        }
        return list;
    }

    private Lock parseStringToLock(String string) {
        String[] parts = string.split(",");
        Lock lock = new Lock(parts[0],Integer.parseInt(parts[1]));
        return lock;
    }

    public void storeListToPref(Context context) {
        Util.saveLockList(context,convertListToStringSet());
    }

    private List<Lock> loadListFromPref(Context context) {
        Set<String> set = Util.readLockList(context);
        return convertSetToList(set);
    }


}
