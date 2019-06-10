package Interractions;

import SimulationLogic.Interraction;
import hla.rti.*;
import hla.rti.jlc.EncodingHelpers;
import hla.rti.jlc.RtiFactoryFactory;

/**
 * Created by osiza on 06.06.2019.
 */
public class AssignToCash  extends Interraction {
    int clientId;
    int amount;
    public AssignToCash(LogicalTime time, int clientId,int amount) throws ArrayIndexOutOfBounds {
        super(time);
        this.clientId = clientId;
        this.amount=amount;
    }
    public AssignToCash(LogicalTime time, ReceivedInteraction ri) throws ArrayIndexOutOfBounds {
        super(time, ri);

    }

    @Override
    public void fillWithInterraction(ReceivedInteraction ri) throws ArrayIndexOutOfBounds {
        this.clientId= EncodingHelpers.decodeInt(ri.getValue(0));
        this.amount= EncodingHelpers.decodeInt(ri.getValue(0));

    }

    @Override
    public void sendInterraction(RTIambassador rtiamb) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, InteractionClassNotDefined, RestoreInProgress, InteractionClassNotPublished, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted, InteractionParameterNotDefined {
        SuppliedParameters parameters =
                RtiFactoryFactory.getRtiFactory().createSuppliedParameters();

        byte[] cashIdByte = EncodingHelpers.encodeInt(this.clientId);
        byte[] amountdByte = EncodingHelpers.encodeInt(this.amount);




        int shelfRegistrationHandler = rtiamb.getInteractionClassHandle("InteractionRoot.AssignToCash");
        int clientIdHandler=rtiamb.getParameterHandle("clientId", shelfRegistrationHandler);
        int amountHandler=rtiamb.getParameterHandle("amount", shelfRegistrationHandler);



        parameters.add(clientIdHandler, cashIdByte);
        parameters.add(amountHandler, amountdByte);



        rtiamb.sendInteraction( shelfRegistrationHandler, parameters, "tag".getBytes(), this.getTime() );
        log();
    }

    @Override
    public void log() {
        System.out.println("AssigToCash:  client: "+this.clientId+"with amount: "+this.amount+" in time "+this.getTime());
    }

    public int getClientId() {
        return clientId;
    }

    public int getAmount() {
        return amount;
    }
}
