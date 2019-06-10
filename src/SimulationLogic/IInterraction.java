package SimulationLogic;

import hla.rti.*;

import java.sql.Time;

/**
 * Created by osiza on 03.04.2019.
 */
public interface IInterraction {
    void fillWithInterraction(ReceivedInteraction ri) throws ArrayIndexOutOfBounds;

    void sendInterraction(RTIambassador rtiamb) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, InteractionClassNotDefined, RestoreInProgress, InteractionClassNotPublished, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted, InteractionParameterNotDefined;
    void log();

}
