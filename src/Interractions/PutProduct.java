package Interractions;

import Interractions.ActionOnShelf;
import hla.rti.*;
import hla.rti.jlc.EncodingHelpers;
import hla.rti.jlc.RtiFactoryFactory;

/**
 * Created by osiza on 03.04.2019.
 */
public class PutProduct extends ActionOnShelf {
    int workerId;

    public PutProduct(int shelfId,int workerId, int amount, LogicalTime time) {

        super(shelfId, amount, time);
        this.workerId=workerId;
    }

    public PutProduct(LogicalTime time, ReceivedInteraction ri) throws ArrayIndexOutOfBounds {
        super(time, ri);
    }

    @Override
    public void fillWithInterraction(ReceivedInteraction interaction) throws ArrayIndexOutOfBounds
    {

        this.shelfId = EncodingHelpers.decodeInt(interaction.getValue(0));
        this.workerId = EncodingHelpers.decodeInt(interaction.getValue(1));
        this.amount= EncodingHelpers.decodeInt(interaction.getValue(2));
       log();
    }

    @Override
    public void sendInterraction(RTIambassador rtiamb) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, InteractionClassNotDefined, RestoreInProgress, InteractionClassNotPublished, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted, InteractionParameterNotDefined {
        SuppliedParameters parameters =
                RtiFactoryFactory.getRtiFactory().createSuppliedParameters();

        byte[] shelfIdByte = EncodingHelpers.encodeInt(this.shelfId);
        byte[] workerIdByte = EncodingHelpers.encodeInt(this.workerId);
        byte[] amountByte=EncodingHelpers.encodeInt(this.amount);



        int shelfRegistrationHandler = rtiamb.getInteractionClassHandle("InteractionRoot.PutProduct");
        int shelfId=rtiamb.getParameterHandle("shelfId", shelfRegistrationHandler);
        int workerIdHandler=rtiamb.getParameterHandle("workerId", shelfRegistrationHandler);
        int amountHandler=rtiamb.getParameterHandle("amount", shelfRegistrationHandler);
        parameters.add(shelfId, shelfIdByte);
        parameters.add(workerIdHandler, workerIdByte);
        parameters.add(amountHandler, amountByte);
        rtiamb.sendInteraction( shelfRegistrationHandler, parameters, "tag".getBytes(), this.getTime() );
        log();
    }

    @Override
    public void log() {
        System.out.println("Put on shelf "+ this.shelfId+ " products "+ amount+" woker: "+this.workerId );
    }

    public int getWorkerId() {
        return workerId;
    }
}
