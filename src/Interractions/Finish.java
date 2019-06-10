package Interractions;

import SimulationLogic.Interraction;
import hla.rti.*;
import hla.rti.jlc.EncodingHelpers;
import hla.rti.jlc.RtiFactoryFactory;

/**
 * Created by osiza on 31.05.2019.
 */
public class Finish extends Interraction {
    public Finish(LogicalTime time) {
        super(time);
    }

    @Override
    public void fillWithInterraction(ReceivedInteraction ri) throws ArrayIndexOutOfBounds {

    }

    @Override
    public void sendInterraction(RTIambassador rtiamb) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, InteractionClassNotDefined, RestoreInProgress, InteractionClassNotPublished, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted, InteractionParameterNotDefined {
        SuppliedParameters parameters =
                RtiFactoryFactory.getRtiFactory().createSuppliedParameters();

        int shelfRegistrationHandler = rtiamb.getInteractionClassHandle("InteractionRoot.Finish");

        rtiamb.sendInteraction( shelfRegistrationHandler, parameters, "tag".getBytes(),getTime());
        log();
    }

    @Override
    public void log() {
        System.out.println("KONIEC SYMULACJI");
    }
}
