package Interractions;

import SimulationLogic.Interraction;
import hla.rti.*;
import hla.rti.jlc.EncodingHelpers;
import hla.rti.jlc.RtiFactoryFactory;

/**
 * Created by osiza on 06.06.2019.
 */
public class CashRegister extends Interraction{

    int cashId;
    public CashRegister(LogicalTime time, int cashId) {
        super(time);
        this.cashId=cashId;
    }

    public CashRegister(LogicalTime time, ReceivedInteraction ri) throws ArrayIndexOutOfBounds {
        super(time, ri);
    }

    @Override
    public void fillWithInterraction(ReceivedInteraction ri) throws ArrayIndexOutOfBounds {
        this.cashId= EncodingHelpers.decodeInt(ri.getValue(0));

    }

    @Override
    public void sendInterraction(RTIambassador rtiamb) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, InteractionClassNotDefined, RestoreInProgress, InteractionClassNotPublished, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted, InteractionParameterNotDefined {
        SuppliedParameters parameters =
                RtiFactoryFactory.getRtiFactory().createSuppliedParameters();

        byte[] cashIdByte = EncodingHelpers.encodeInt(this.cashId);




        int shelfRegistrationHandler = rtiamb.getInteractionClassHandle("InteractionRoot.CashRegister");
        int cashIdHandler=rtiamb.getParameterHandle("cashId", shelfRegistrationHandler);



        parameters.add(cashIdHandler, cashIdByte);



        rtiamb.sendInteraction( shelfRegistrationHandler, parameters, "tag".getBytes(), this.getTime() );
        log();
    }

    @Override
    public void log() {
        System.out.println("CashRegister: "+this.cashId+" in time "+this.getTime());
    }

    public int getCashId() {
        return cashId;
    }
}
