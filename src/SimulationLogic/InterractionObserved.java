package SimulationLogic;

import hla.rti.LogicalTime;
import hla.rti.ReceivedInteraction;

/**
 * Created by osiza on 28.05.2019.
 */
public interface InterractionObserved {
    void inform(int interractionClass, LogicalTime theTime, ReceivedInteraction theInterraction);
    void setObserver(InterractionObserver io);
    void removeObserver(InterractionObserver io);
}
