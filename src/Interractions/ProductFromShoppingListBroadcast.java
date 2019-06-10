package Interractions;

import SimulationLogic.Interraction;
import hla.rti.*;
import hla.rti.jlc.EncodingHelpers;
import hla.rti.jlc.RtiFactoryFactory;

/**
 * Created by osiza on 05.06.2019.
 */
public class ProductFromShoppingListBroadcast extends Interraction {
    int clientId;
    String product;
    int amount;

    public ProductFromShoppingListBroadcast(int clientId, String product, int amount, LogicalTime time) {
        super(time);
        this.clientId = clientId;
        this.product = product;
        this.amount = amount;
    }


    public ProductFromShoppingListBroadcast(LogicalTime time, ReceivedInteraction ri) throws ArrayIndexOutOfBounds {
        super(time, ri);
    }

    @Override
    public void fillWithInterraction(ReceivedInteraction ri) throws ArrayIndexOutOfBounds {
        this.clientId = EncodingHelpers.decodeInt(ri.getValue(0));
        this.product = EncodingHelpers.decodeString(ri.getValue(1));
        this.amount = EncodingHelpers.decodeInt(ri.getValue(2));
    }

    @Override
    public void sendInterraction(RTIambassador rtiamb) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, InteractionClassNotDefined, RestoreInProgress, InteractionClassNotPublished, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted, InteractionParameterNotDefined {
        SuppliedParameters parameters =
                RtiFactoryFactory.getRtiFactory().createSuppliedParameters();

        byte[] clientIdByte = EncodingHelpers.encodeInt(this.clientId);

        byte[] productIdByte = EncodingHelpers.encodeString(this.product);
        byte[] amountByte = EncodingHelpers.encodeInt(this.amount);


        int shelfRegistrationHandler = rtiamb.getInteractionClassHandle("InteractionRoot.ProductFromShoppingListBroadcast");
        int clientIdHandler = rtiamb.getParameterHandle("clientId", shelfRegistrationHandler);
        int shelfIdHandler = rtiamb.getParameterHandle("product", shelfRegistrationHandler);
        int amountHandler = rtiamb.getParameterHandle("amount", shelfRegistrationHandler);

        parameters.add(clientIdHandler, clientIdByte);
        parameters.add(shelfIdHandler, productIdByte);
        parameters.add(amountHandler, amountByte);

        rtiamb.sendInteraction(shelfRegistrationHandler, parameters, "tag".getBytes(), this.getTime());
        log();
    }

    @Override
    public void log() {
        System.out.println("Product " + this.product + " amount: " + this.amount + " from client " + this.clientId + " basket in time" + this.getTime());
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}


