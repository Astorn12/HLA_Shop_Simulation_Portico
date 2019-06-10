package Interractions;

import SimulationLogic.Interraction;
import hla.rti.*;
import hla.rti.jlc.EncodingHelpers;
import hla.rti.jlc.RtiFactoryFactory;
import Support.shelves.Shelf;

/**
 * Created by osiza on 28.05.2019.
 */
public class ShelfRegistration extends Interraction {
    int shelfId;
    String product;
    int size;

    public ShelfRegistration(LogicalTime time,ReceivedInteraction ri) throws ArrayIndexOutOfBounds {

        super(time);
        fillWithInterraction(ri);
    }//przyjmujemy interrakcję
    public ShelfRegistration(LogicalTime time, int shelfId, String product,int size) {
        super(time);
        this.shelfId = shelfId;
        this.product = product;
        this.size=size;
    } //będziemy wysyłać interrakcję
    public ShelfRegistration(LogicalTime time, Shelf shelf)
    {
        super(time);
        this.shelfId=shelf.getId();
        this.product=shelf.getProduct();
        this.size=shelf.getSize();
    }

    @Override
    public void fillWithInterraction(ReceivedInteraction ri) throws ArrayIndexOutOfBounds {
        this.shelfId= EncodingHelpers.decodeInt(ri.getValue(0));
        this.product= EncodingHelpers.decodeString(ri.getValue(1));
        this.size= EncodingHelpers.decodeInt(ri.getValue(2));

    }

    @Override
    public void sendInterraction(RTIambassador rtiamb) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, InteractionClassNotDefined, RestoreInProgress, InteractionClassNotPublished, SaveInProgress, InvalidFederationTime, ConcurrentAccessAttempted, InteractionParameterNotDefined {
        SuppliedParameters parameters =
                RtiFactoryFactory.getRtiFactory().createSuppliedParameters();

        byte[] shelfIdByte = EncodingHelpers.encodeInt(this.shelfId);

        byte[] productByte=EncodingHelpers.encodeString(product);
        byte[] sizeByte=EncodingHelpers.encodeInt(this.size);


        int shelfRegistrationHandler = rtiamb.getInteractionClassHandle("InteractionRoot.ShelfRegistration");
        int shelfId=rtiamb.getParameterHandle("shelfId", shelfRegistrationHandler);
        int productHandler=rtiamb.getParameterHandle("product", shelfRegistrationHandler);
        int sizeHandler=rtiamb.getParameterHandle("size", shelfRegistrationHandler);

        parameters.add(shelfId, shelfIdByte);
        parameters.add(productHandler, productByte);
        parameters.add(sizeHandler, sizeByte);

        rtiamb.sendInteraction( shelfRegistrationHandler, parameters, "tag".getBytes(), this.getTime() );
        log();
    }

    @Override
    public void log() {
        System.out.println("Shelf registration: id="+shelfId+", product="+ product+" size "+this.size+" in time:"+this.getTime());
    }

    public int getShelfId() {
        return shelfId;
    }

    public void setShelfId(int shelfId) {
        this.shelfId = shelfId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getSize() {
        return size;
    }
}
