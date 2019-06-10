package Interractions;

import Interractions.ActionOnShelf;
import hla.rti.*;
import hla.rti.jlc.EncodingHelpers;
import hla.rti.jlc.RtiFactoryFactory;

import java.sql.Time;

/**
 * Created by osiza on 03.04.2019.
 */
public class TakeProduct extends ActionOnShelf {
    int clientId;
    public TakeProduct(int shelfId, int amount, LogicalTime time,int clientId) {
        super(shelfId,amount, time);
        this.clientId=clientId;
    }

    public TakeProduct(LogicalTime time, ReceivedInteraction ri) throws ArrayIndexOutOfBounds {
        super(time, ri);
        this.clientId= EncodingHelpers.decodeInt(ri.getValue(2));
    }

    public void log()
    {
        System.out.println("Client "+ this.clientId+", TakeProduct from shelve "+this.shelfId+ " amount "+ amount + "in time "+this.getTime());
    }

    @Override
    public void sendInterraction(RTIambassador rtiamb) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, InteractionClassNotDefined, RestoreInProgress, InteractionClassNotPublished, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted, InteractionParameterNotDefined {
        SuppliedParameters parameters =
                RtiFactoryFactory.getRtiFactory().createSuppliedParameters();

        byte[] shelfIdByte = EncodingHelpers.encodeInt(this.shelfId);

        byte[] amountByte=EncodingHelpers.encodeInt(this.amount);
        byte[] clientIdByte=EncodingHelpers.encodeInt(this.clientId);


        int shelfRegistrationHandler = rtiamb.getInteractionClassHandle("InteractionRoot.TakeProduct");
        int shelfId=rtiamb.getParameterHandle("shelfId", shelfRegistrationHandler);
        int amountHandler=rtiamb.getParameterHandle("amount", shelfRegistrationHandler);
        int clientIdHandler=rtiamb.getParameterHandle("clientId", shelfRegistrationHandler);

        parameters.add(shelfId, shelfIdByte);
        parameters.add(amountHandler, amountByte);
        parameters.add(clientIdHandler, clientIdByte);

        rtiamb.sendInteraction( shelfRegistrationHandler, parameters, "tag".getBytes(), this.getTime() );
        log();
    }

    public int getClientId() {
        return clientId;
    }
}
