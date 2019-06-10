package Support.workers;

import Support.magazine.StoredProduct;

/**
 * Created by osiza on 01.06.2019.
 */
public class Worker {
    int id;
    private WorkerState state;

    private StoredProduct sp;
    private double workEnd;

    public Worker(int id) {
        this.id = id;
        this.state= WorkerState.FREE;
        sp=null;
    }

    public int getId() {
        return id;
    }

    public WorkerState getState() {
        return state;
    }

    public void setState(WorkerState state) {
        this.state = state;
    }

    public void checkInMagazine(StoredProduct sp)
    {
        this.sp=sp;
        this.setState(WorkerState.CHECKINGINMAGAZINE);
    }

    public StoredProduct getRequest()
    {
        if(this.state.equals(WorkerState.CHECKINGINMAGAZINE))
        {
            return this.sp;
        }
        else return null;
    }

    public double getWorkEnd() {
        return workEnd;
    }

    public void setWorkEnd(double workEnd) {
        this.workEnd = workEnd;
    }

    public void release()
    {
        this.workEnd=0;
        this.setState(WorkerState.FREE);
    }
}
