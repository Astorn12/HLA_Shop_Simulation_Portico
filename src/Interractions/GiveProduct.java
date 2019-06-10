package Interractions;

import SimulationLogic.Interraction;
import hla.rti.*;
import hla.rti.jlc.EncodingHelpers;
import hla.rti.jlc.RtiFactoryFactory;

/**
 * Created by osiza on 06.06.2019.
 */
public class GiveProduct  extends Interraction {
    int workerId;
    String product;
    int amount;

    public GiveProduct(int workerId,String product, int amount, LogicalTime time) {

        super(time);
        this.workerId=workerId;
        this.product=product;
        this.amount=amount;
    }

    public GiveProduct(LogicalTime time, ReceivedInteraction ri) throws ArrayIndexOutOfBounds {
        super(time, ri);
    }


    @Override
    public void fillWithInterraction(ReceivedInteraction ri) throws ArrayIndexOutOfBounds {
        this.workerId= EncodingHelpers.decodeInt(ri.getValue(0));
        this.product= EncodingHelpers.decodeString(ri.getValue(1));
        this.amount= EncodingHelpers.decodeInt(ri.getValue(2));
    }

    @Override
    public void sendInterraction(RTIambassador rtiamb) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, InteractionClassNotDefined, RestoreInProgress, InteractionClassNotPublished, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted, InteractionParameterNotDefined {
        SuppliedParameters parameters =
                RtiFactoryFactory.getRtiFactory().createSuppliedParameters();

        byte[] workerIdByte = EncodingHelpers.encodeInt(this.workerId);
        byte[] productByte = EncodingHelpers.encodeString(this.product);
        byte[] amountByte=EncodingHelpers.encodeInt(this.amount);



        int shelfRegistrationHandler = rtiamb.getInteractionClassHandle("InteractionRoot.GiveProduct");
        int workerIdHandle=rtiamb.getParameterHandle("workerId", shelfRegistrationHandler);
        int productHandler=rtiamb.getParameterHandle("product", shelfRegistrationHandler);
        int amountHandler=rtiamb.getParameterHandle("amount", shelfRegistrationHandler);
        parameters.add(workerIdHandle, workerIdByte);
        parameters.add(productHandler, productByte);
        parameters.add(amountHandler, amountByte);
        rtiamb.sendInteraction( shelfRegistrationHandler, parameters, "tag".getBytes(), this.getTime() );
        log();
    }

    @Override
    public void log() {
        System.out.println("GiveProduct worker: "+workerId+" product: "+ product+ " amount: "+ amount );
    }

    public int getWorkerId() {
        return workerId;
    }

    public String getProduct() {
        return product;
    }

    public int getAmount() {
        return amount;
    }
}
