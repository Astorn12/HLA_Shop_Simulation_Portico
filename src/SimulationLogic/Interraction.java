package SimulationLogic;

import SimulationLogic.IInterraction;
import hla.rti.ArrayIndexOutOfBounds;
import hla.rti.LogicalTime;
import hla.rti.ReceivedInteraction;

import java.sql.Time;
import java.util.Comparator;

/**
 * Created by osiza on 03.04.2019.
 */
public abstract class Interraction implements IInterraction {
    LogicalTime time;

    public Interraction(LogicalTime time) {
        this.time = time;
    }
    public Interraction(LogicalTime time, ReceivedInteraction ri) throws ArrayIndexOutOfBounds {
        this(time);
        fillWithInterraction(ri);
    }



    public int compare(Interraction interraction) {
        InterracionComperator comperator= new InterracionComperator();
        return comperator.compare(this,interraction);
    }

    static class InterracionComperator implements Comparator<Interraction> {
        @Override
        public int compare(Interraction o1, Interraction o2) {
            return o1.time.isGreaterThanOrEqualTo(o2.time) ? 1:-1;
        }
    }

    public LogicalTime getTime() {
        return time;
    }

    public void setTime(LogicalTime time) {
        this.time = time;
    }
}
