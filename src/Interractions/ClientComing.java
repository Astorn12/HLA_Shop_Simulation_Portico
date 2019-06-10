package Interractions;

import SimulationLogic.Interraction;
import hla.rti.*;
import hla.rti.jlc.EncodingHelpers;
import hla.rti.jlc.RtiFactoryFactory;

/**
 * Created by osiza on 05.06.2019.
 */
public class ClientComing extends Interraction {
   int clientId;

    public ClientComing(LogicalTime time, ReceivedInteraction ri) throws ArrayIndexOutOfBounds {

        super(time);
        fillWithInterraction(ri);
    }//przyjmujemy interrakcję
    public ClientComing(LogicalTime time, int clientId) {
        super(time);
       this.clientId=clientId;
    } //będziemy wysyłać interrakcję


    @Override
    public void fillWithInterraction(ReceivedInteraction ri) throws ArrayIndexOutOfBounds {
        this.clientId= EncodingHelpers.decodeInt(ri.getValue(0));


    }

    @Override
    public void sendInterraction(RTIambassador rtiamb) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, InteractionClassNotDefined, RestoreInProgress, InteractionClassNotPublished, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted, InteractionParameterNotDefined {
        SuppliedParameters parameters =
                RtiFactoryFactory.getRtiFactory().createSuppliedParameters();

        byte[] shelfIdByte = EncodingHelpers.encodeInt(this.clientId);




        int shelfRegistrationHandler = rtiamb.getInteractionClassHandle("InteractionRoot.ClientComming");
        int shelfId=rtiamb.getParameterHandle("clientId", shelfRegistrationHandler);


        parameters.add(shelfId, shelfIdByte);


        rtiamb.sendInteraction( shelfRegistrationHandler, parameters, "tag".getBytes(), this.getTime() );
        log();
    }

    @Override
    public void log() {
        System.out.println("Client "+this.clientId+ "coming in time:"+this.getTime());
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }
}
