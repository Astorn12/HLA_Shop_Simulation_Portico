package SimulationLogic;

import hla.rti.LogicalTime;
import hla.rti.ReceivedInteraction;

/**
 * Created by osiza on 28.05.2019.
 */
public interface InterractionObserver {
    void update(int interractionClass, LogicalTime theTime, ReceivedInteraction theInterraction);
}
