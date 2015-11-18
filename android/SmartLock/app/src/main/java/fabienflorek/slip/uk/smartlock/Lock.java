package fabienflorek.slip.uk.smartlock;

/**
 * Created by fabienflorek on 11/10/15.
 */
public class Lock {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    String name;
    Integer id;
    //false close, true open
    boolean status;
    int place;

    public boolean isStatusRequested() {
        return statusRequested;
    }

    public void setStatusRequested(boolean statusRequested) {
        this.statusRequested = statusRequested;
    }

    boolean statusRequested;



    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }



    public Lock(String name, Integer id, int place) {
        this.name = name;
        this.id = id;
        status = false;
        this.place = place;
    }
    public Lock(String name, Integer id, boolean status,int place) {
        this.name = name;
        this.id = id;
        this.status = status;
        this.place = place;
        this.statusRequested = status;
    }
    public Lock(String name, Integer id, boolean status,boolean statusRequested,int place) {
        this.name = name;
        this.id = id;
        this.status = status;
        this.place = place;
        this.statusRequested = statusRequested;
    }

    @Override
    public String toString() {
        return name+","+id+","+ place;
    }
}

