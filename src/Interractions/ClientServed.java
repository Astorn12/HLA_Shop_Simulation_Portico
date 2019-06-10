package Interractions;

import SimulationLogic.Interraction;
import hla.rti.*;
import hla.rti.jlc.EncodingHelpers;
import hla.rti.jlc.RtiFactoryFactory;

/**
 * Created by osiza on 07.06.2019.
 */
public class ClientServed extends Interraction {

    int clientId;
    public ClientServed(LogicalTime time, int clientId) {
        super(time);
        this.clientId=clientId;
    }

    public ClientServed(LogicalTime time, ReceivedInteraction ri) throws ArrayIndexOutOfBounds {
        super(time, ri);
    }

    @Override
    public void fillWithInterraction(ReceivedInteraction ri) throws ArrayIndexOutOfBounds {
        this.clientId= EncodingHelpers.decodeInt(ri.getValue(0));

    }

    @Override
    public void sendInterraction(RTIambassador rtiamb) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, InteractionClassNotDefined, RestoreInProgress, InteractionClassNotPublished, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted, InteractionParameterNotDefined {
        SuppliedParameters parameters =
                RtiFactoryFactory.getRtiFactory().createSuppliedParameters();

        byte[] cashIdByte = EncodingHelpers.encodeInt(this.clientId);




        int shelfRegistrationHandler = rtiamb.getInteractionClassHandle("InteractionRoot.ClientServed");
        int cashIdHandler=rtiamb.getParameterHandle("clientId", shelfRegistrationHandler);



        parameters.add(cashIdHandler, cashIdByte);



        rtiamb.sendInteraction( shelfRegistrationHandler, parameters, "tag".getBytes(), this.getTime() );
        log();
    }

    @Override
    public void log() {
        System.out.println("ClientServed: "+this.clientId+" in time "+this.getTime());
    }

    public int getclientId() {
        return clientId;
    }
}
