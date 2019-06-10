package Interractions;

import SimulationLogic.Interraction;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import hla.rti.*;
import hla.rti.jlc.EncodingHelpers;
import hla.rti.jlc.RtiFactoryFactory;

/**
 * Created by osiza on 02.06.2019.
 */
public class FillMagazine extends Interraction {


    String product;
    int amount;
    int workerId;
    public FillMagazine(LogicalTime time, String product, int amount,int workerId) {
        super(time);
        this.product = product;
        this.amount = amount;
        this.workerId=workerId;
    }

    public FillMagazine(LogicalTime time, ReceivedInteraction ri) throws ArrayIndexOutOfBounds {
        super(time, ri);
    }

    public void log()
    {
        System.out.println("Fill Support.magazine "+ this.product+" amount "+ amount+ "in time "+this.getTime());
    }

    @Override
    public void fillWithInterraction(ReceivedInteraction ri) throws ArrayIndexOutOfBounds {

        this.product=EncodingHelpers.decodeString(ri.getValue(0));
        this.amount=EncodingHelpers.decodeInt(ri.getValue(1));
        this.workerId=EncodingHelpers.decodeInt(ri.getValue(2));

    }

    @Override
    public void sendInterraction(RTIambassador rtiamb) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, InteractionClassNotDefined, RestoreInProgress, InteractionClassNotPublished, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted, InteractionParameterNotDefined {
        SuppliedParameters parameters =
                RtiFactoryFactory.getRtiFactory().createSuppliedParameters();


        byte[] productByte=EncodingHelpers.encodeString(this.product);
        byte[] amountByte=EncodingHelpers.encodeInt(this.amount);
        byte[] workerIdByte=EncodingHelpers.encodeInt(this.workerId);



        int shelfRegistrationHandler = rtiamb.getInteractionClassHandle("InteractionRoot.FillMagazine");
        int productHandler=rtiamb.getParameterHandle("product", shelfRegistrationHandler);
        int amountHandler=rtiamb.getParameterHandle("amount", shelfRegistrationHandler);
        int workerIdHandler=rtiamb.getParameterHandle("workerId", shelfRegistrationHandler);



        parameters.add(productHandler, productByte);
        parameters.add(amountHandler, amountByte);
        parameters.add(workerIdHandler, workerIdByte);


        rtiamb.sendInteraction( shelfRegistrationHandler, parameters, "tag".getBytes(), this.getTime() );
        log();
        System.out.println("MAGAZINE WAS FILLED");
    }


    public String getProduct() {
        return product;
    }

    public int getAmount() {
        return amount;
    }
}

