package Interractions;

import SimulationLogic.Interraction;
import hla.rti.*;
import hla.rti.jlc.EncodingHelpers;
import hla.rti.jlc.RtiFactoryFactory;

/**
 * Created by osiza on 06.06.2019.
 */
public class CashierRegister extends Interraction {
    int cashierId;
    int cashId;
    public CashierRegister(LogicalTime time,  int cashierId,int cashId) throws ArrayIndexOutOfBounds {
        super(time);
        this.cashierId = cashierId;
        this.cashierId = cashId;
    }
    public CashierRegister(LogicalTime time, ReceivedInteraction ri) throws ArrayIndexOutOfBounds {
        super(time, ri);

    }

    @Override
    public void fillWithInterraction(ReceivedInteraction ri) throws ArrayIndexOutOfBounds {
        this.cashierId= EncodingHelpers.decodeInt(ri.getValue(0));
        this.cashId= EncodingHelpers.decodeInt(ri.getValue(0));

    }

    @Override
    public void sendInterraction(RTIambassador rtiamb) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, InteractionClassNotDefined, RestoreInProgress, InteractionClassNotPublished, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted, InteractionParameterNotDefined {
        SuppliedParameters parameters =
                RtiFactoryFactory.getRtiFactory().createSuppliedParameters();

        byte[] cashierIdByte = EncodingHelpers.encodeInt(this.cashierId);
        byte[] cashIdByte = EncodingHelpers.encodeInt(this.cashId);




        int shelfRegistrationHandler = rtiamb.getInteractionClassHandle("InteractionRoot.CashierRegister");
        int cashierIdHandler=rtiamb.getParameterHandle("cashierId", shelfRegistrationHandler);
        int cashIdHandler=rtiamb.getParameterHandle("cashId", shelfRegistrationHandler);



        parameters.add(cashierIdHandler, cashierIdByte);
        parameters.add(cashIdHandler, cashIdByte);



        rtiamb.sendInteraction( shelfRegistrationHandler, parameters, "tag".getBytes(), this.getTime() );
        log();
    }

    @Override
    public void log() {
        System.out.println("CashierRegister: "+this.cashierId+" in time "+this.getTime());
    }

    public int getCashierId() {
        return cashierId;
    }

}
