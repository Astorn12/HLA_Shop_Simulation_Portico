package Interractions;

import SimulationLogic.Interraction;
import hla.rti.*;
import hla.rti.jlc.EncodingHelpers;
import hla.rti.jlc.RtiFactoryFactory;

/**
 * Created by osiza on 01.06.2019.
 */
public class CheckInMagazine extends Interraction {

    int workerId;
    String product;
    int amount;

    public CheckInMagazine(LogicalTime time, int workerId, String product, int amount) {
        super(time);
        this.workerId = workerId;
        this.product = product;
        this.amount = amount;
    }

    public CheckInMagazine(LogicalTime time, ReceivedInteraction ri) throws ArrayIndexOutOfBounds {
        super(time, ri);
    }

    public void log()
    {
        System.out.println("Check in magazin for "+ this.product+" amount "+ amount+ "by worker" +this.workerId +"in time "+this.getTime());
    }

    @Override
    public void fillWithInterraction(ReceivedInteraction ri) throws ArrayIndexOutOfBounds {
        this.workerId= EncodingHelpers.decodeInt(ri.getValue(0));
        this.product=EncodingHelpers.decodeString(ri.getValue(1));
        this.amount=EncodingHelpers.decodeInt(ri.getValue(2));
    }

    @Override
    public void sendInterraction(RTIambassador rtiamb) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, InteractionClassNotDefined, RestoreInProgress, InteractionClassNotPublished, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted, InteractionParameterNotDefined {
        SuppliedParameters parameters =
                RtiFactoryFactory.getRtiFactory().createSuppliedParameters();

        byte[] workerIdByte = EncodingHelpers.encodeInt(this.workerId);
        byte[] productByte=EncodingHelpers.encodeString(this.product);
        byte[] amountByte=EncodingHelpers.encodeInt(this.amount);



        int shelfRegistrationHandler = rtiamb.getInteractionClassHandle("InteractionRoot.CheckInMagazine");
        int workerId=rtiamb.getParameterHandle("workerId", shelfRegistrationHandler);
        int productHandler=rtiamb.getParameterHandle("product", shelfRegistrationHandler);
        int amountHandler=rtiamb.getParameterHandle("amount", shelfRegistrationHandler);


        parameters.add(workerId, workerIdByte);
        parameters.add(productHandler, productByte);
        parameters.add(amountHandler, amountByte);


        rtiamb.sendInteraction( shelfRegistrationHandler, parameters, "tag".getBytes(), this.getTime() );
        log();
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
