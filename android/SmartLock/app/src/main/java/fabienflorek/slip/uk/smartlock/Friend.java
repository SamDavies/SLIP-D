package fabienflorek.slip.uk.smartlock;

import java.util.List;

/**
 * Created by fabienflorek on 11/18/15.
 */
public class Friend {
    public Friend(String firstName, String lastName, int id,List<Integer> myLocks) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.myLocks = myLocks;
    }
    public Friend(String firstName, String lastName, int id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    String firstName;
    String lastName;
    int id;

    public List<Integer> getMyLocks() {
        return myLocks;
    }

    public void setMyLocks(List<Integer> myLocks) {
        this.myLocks = myLocks;
    }

    List<Integer> myLocks;
}
