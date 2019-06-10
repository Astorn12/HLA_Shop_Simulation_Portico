package Interractions;

import SimulationLogic.Interraction;
import hla.rti.*;
import hla.rti.jlc.EncodingHelpers;
import hla.rti.jlc.RtiFactoryFactory;

/**
 * Created by osiza on 29.05.2019.
 */
public class ProductAssigned extends Interraction {
    int clientId;
    int shelfId;
    int amount;

    public ProductAssigned(int clientId, int shelfId, int amount,LogicalTime time) {
        super(time);
        this.clientId = clientId;
        this.shelfId = shelfId;
        this.amount = amount;
    }

    public ProductAssigned(LogicalTime time, ReceivedInteraction ri) throws ArrayIndexOutOfBounds {
        super(time, ri);
    }

    @Override
    public void fillWithInterraction(ReceivedInteraction ri) throws ArrayIndexOutOfBounds {
        this.clientId= EncodingHelpers.decodeInt(ri.getValue(0));
        this.shelfId= EncodingHelpers.decodeInt(ri.getValue(1));
        this.amount= EncodingHelpers.decodeInt(ri.getValue(2));
    }

    @Override
    public void sendInterraction(RTIambassador rtiamb) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, InteractionClassNotDefined, RestoreInProgress, InteractionClassNotPublished, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted, InteractionParameterNotDefined {
        SuppliedParameters parameters =
                RtiFactoryFactory.getRtiFactory().createSuppliedParameters();

        byte[] clientIdByte = EncodingHelpers.encodeInt(this.clientId);

        byte[] productIdByte=EncodingHelpers.encodeInt(this.shelfId);
        byte[] amountByte=EncodingHelpers.encodeInt(this.amount);


        int shelfRegistrationHandler = rtiamb.getInteractionClassHandle("InteractionRoot.ProductAssigned");
        int clientIdHandler=rtiamb.getParameterHandle("clientId", shelfRegistrationHandler);
        int shelfIdHandler=rtiamb.getParameterHandle("shelfId", shelfRegistrationHandler);
        int amountHandler=rtiamb.getParameterHandle("amount", shelfRegistrationHandler);

        parameters.add(clientIdHandler, clientIdByte);
        parameters.add(shelfIdHandler, productIdByte);
        parameters.add(amountHandler, amountByte);

        rtiamb.sendInteraction( shelfRegistrationHandler, parameters, "tag".getBytes(), this.getTime() );
        log();
    }

    @Override
    public void log() {
        System.out.println("Product "+ this.shelfId+" assigned to client "+this.clientId+" amount: "+ this.amount+" in  time: "+this.getTime());
    }

    public int getClientId() {
        return clientId;
    }

    public int getProductId() {
        return shelfId;
    }

    public int getAmount() {
        return amount;
    }

    public int getShelfId() {
        return shelfId;
    }
}
