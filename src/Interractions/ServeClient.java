package Interractions;

import SimulationLogic.Interraction;
import hla.rti.*;
import hla.rti.jlc.EncodingHelpers;
import hla.rti.jlc.RtiFactoryFactory;

/**
 * Created by osiza on 07.06.2019.
 */
public class ServeClient extends Interraction {
    int clientId;
    int cashId;
    int amount;
    public ServeClient(LogicalTime time, int clientId, int cashId,int amount) throws ArrayIndexOutOfBounds {
        super(time);
        this.clientId = clientId;
        this.cashId=cashId;
        this.amount=amount;

    }
    public ServeClient(LogicalTime time, ReceivedInteraction ri) throws ArrayIndexOutOfBounds {
        super(time, ri);

    }

    @Override
    public void fillWithInterraction(ReceivedInteraction ri) throws ArrayIndexOutOfBounds {
        this.clientId= EncodingHelpers.decodeInt(ri.getValue(0));
        this.cashId= EncodingHelpers.decodeInt(ri.getValue(1));
        this.amount= EncodingHelpers.decodeInt(ri.getValue(2));

    }

    @Override
    public void sendInterraction(RTIambassador rtiamb) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, InteractionClassNotDefined, RestoreInProgress, InteractionClassNotPublished, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted, InteractionParameterNotDefined {
        SuppliedParameters parameters =
                RtiFactoryFactory.getRtiFactory().createSuppliedParameters();

        byte[] clientIdByte = EncodingHelpers.encodeInt(this.clientId);
        byte[] cashIdByte = EncodingHelpers.encodeInt(this.cashId);
        byte[] amountdByte = EncodingHelpers.encodeInt(this.amount);




        int shelfRegistrationHandler = rtiamb.getInteractionClassHandle("InteractionRoot.ServeClient");
        int clientIdHandler=rtiamb.getParameterHandle("clientId", shelfRegistrationHandler);
        int cashIdHandler=rtiamb.getParameterHandle("cashId", shelfRegistrationHandler);
        int amountHandler=rtiamb.getParameterHandle("amount", shelfRegistrationHandler);



        parameters.add(clientIdHandler, clientIdByte);
        parameters.add(cashIdHandler, cashIdByte);
        parameters.add(amountHandler, amountdByte);



        rtiamb.sendInteraction( shelfRegistrationHandler, parameters, "tag".getBytes(), this.getTime() );
        log();
    }

    @Override
    public void log() {
        System.out.println("ServeClient:  client: "+this.clientId+" in cash: "+this.cashId+" with amount: "+this.amount+" in time "+this.getTime());
    }

    public int getClientId() {
        return clientId;
    }

    public int getAmount() {
        return amount;
    }

    public int getCashId() {
        return cashId;
    }
}
