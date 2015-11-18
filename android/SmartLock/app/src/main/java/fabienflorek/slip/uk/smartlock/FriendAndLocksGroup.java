package fabienflorek.slip.uk.smartlock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fabienflorek on 11/18/15.
 */
public class FriendAndLocksGroup {

    public Friend friend;
    public List<Lock> lockList = new ArrayList<Lock>();


    public FriendAndLocksGroup(Friend friend, List<Lock> lockList) {
        this.friend = friend;
        this.lockList = lockList;
    }
}
