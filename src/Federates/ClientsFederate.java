package Federates;

import Interractions.*;
import SimulationLogic.Interraction;
import Support.client.ClientState;
import Support.client.LProduct;
import Support.client.ShoppingList;
import SupporterClasses.TimeConverter;
import Universal.UniversalFederate;
import Support.client.Client;
import hla.rti.*;
import Support.shelves.Shelf;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by osiza on 04.06.2019.
 */
public class ClientsFederate extends UniversalFederate {

        Random random;
        int iterator;
        List<Shelf> shelves;
        List<Interraction> waitingInterractions;
        int currentClientId=0;
        private List<Client> clientList;
        double clientAppearTime;
        private static final int clientMaxBasket=4;
        private static final int clientMaxProductsAmount=10;



    public ClientsFederate() {

        this.clientList= new LinkedList<>();
        this.random= new Random();
        iterator=0;
        this.shelves= new LinkedList<>();
        this.waitingInterractions= new LinkedList<>();
        this.clientAppearTime=0;
    }

    public void addShelf(Shelf shelf)
    {
        this.shelves.add(shelf);

    }
    public Shelf getShelf(int id)
    {
        for(Shelf s: this.shelves)
        {
            if(s.getId()==id)
            {
                return s;
            }
        }
        return null;
    }
    /*****************************************************************Metody HLA*/
    public static void main(String[] args) {
        try {
            new ClientsFederate().runFederate();
        } catch (RTIexception rtIexception) {
            rtIexception.printStackTrace();
        }
    }

    public double clientDraw()
    {
        return this.random.nextDouble()*2;
    }










    @Override
    protected void publishAndSubscribe() throws RTIexception {
/**Subowanie na pojawiające się półki*/
        int shelfRegistration=rtiamb.getInteractionClassHandle( "InteractionRoot.ShelfRegistration");
        rtiamb.subscribeInteractionClass(shelfRegistration);


        /**Publikowanie brania produktu z półki*/
        int takeProductHandle = rtiamb.getInteractionClassHandle( "InteractionRoot.TakeProduct" );
        rtiamb.publishInteractionClass(takeProductHandle);

        int productAssignedHandler = rtiamb.getInteractionClassHandle( "InteractionRoot.ProductAssigned" );
        rtiamb.subscribeInteractionClass(productAssignedHandler);
        int notEnoughtProductHandler = rtiamb.getInteractionClassHandle( "InteractionRoot.NotEnoughtProducts" );
        rtiamb.subscribeInteractionClass(notEnoughtProductHandler);
        rtiamb.subscribeInteractionClass(rtiamb.getInteractionClassHandle( "InteractionRoot.Finish" ));
        rtiamb.publishInteractionClass(rtiamb.getInteractionClassHandle( "InteractionRoot.ClientComming" ));
        rtiamb.publishInteractionClass(rtiamb.getInteractionClassHandle( "InteractionRoot.ProductFromShoppingListBroadcast" ));
        rtiamb.publishInteractionClass(rtiamb.getInteractionClassHandle( "InteractionRoot.AssignToCash" ));

    }

    @Override
    protected void reaction(Interraction interraction) {
            if(interraction.getClass().equals(ShelfRegistration.class))
            {
              ShelfRegistration sr=(ShelfRegistration) interraction;
              this.shelves.add(new Shelf(sr.getShelfId(),sr.getSize(),sr.getProduct()));
                System.out.println("Półka została zarejestrowana");
            }
            else if(interraction.getClass().equals(ProductAssigned.class))
            {
                ProductAssigned pa= (ProductAssigned) interraction ;
                Client client= getClient(pa.getClientId());
                client.fillProduct(getProductName(pa.getProductId()),pa.getAmount());
                if(client.isFull()) client.setState(ClientState.WAITINGFORQUEUE);
                try {
                    AssignToCash a= new AssignToCash(convertTime(fedamb.getFederateTime()+1),client.getId(),client.getSigma());
                    this.sendBuffor.add(a);
                } catch (ArrayIndexOutOfBounds arrayIndexOutOfBounds) {
                    arrayIndexOutOfBounds.printStackTrace();
                }
                this.clientList.remove(client);
            }else if(interraction.getClass().equals(NotEnoughtProducts.class))
            {
                NotEnoughtProducts pa= (NotEnoughtProducts) interraction ;
                if(pa.getAmount()>0){
                Client client= getClient(pa.getClientId());
                client.fillProduct(getProductName(pa.getShelfId()),pa.getAmount());
                if(client.isFull()) client.setState(ClientState.WAITINGFORQUEUE);
                try {
                    AssignToCash a= new AssignToCash(convertTime(fedamb.getFederateTime()+1),client.getId(),client.getSigma());
                    this.sendBuffor.add(a);
                } catch (ArrayIndexOutOfBounds arrayIndexOutOfBounds) {
                    arrayIndexOutOfBounds.printStackTrace();
                }
                this.clientList.remove(client);
            }
            }
    }

    @Override
    protected String getFederationName() {
        return "ClientFederate";
    }

    @Override
    protected void inicialization() {


    }

    @Override
    protected double getAdvanceTime() {
        return 3;
    }

    @Override
    protected void mainLoopInnerAction()
    {
        double jump= this.random.nextDouble()*3;
        try {
            advanceTime(jump);
        } catch (RTIexception rtIexception) {
            rtIexception.printStackTrace();
        }
        if(this.clientAppearTime<this.fedamb.getFederateTime())
        {
            this.currentClientId+=1;
            Client client= new Client(this.currentClientId);
            client.setShoppingList(generateShoppinglist());
            this.clientList.add(client);

            this.clientAppearTime=this.fedamb.getFederateTime()+clientDraw();
            client.broadcast(rtiamb, convertTime(this.fedamb.getFederateTime()+1.1),client.getId());
            ClientComing cc= new ClientComing(convertTime(this.fedamb.getFederateTime()+1),client.getId());
            this.sendBuffor.add(cc);
        }


        for(Client actingClient: this.clientList){
        //Client actingClient= getRandomShoppingClient();

        if(!actingClient.isFull()&&!actingClient.isWaitingForProduct()) {
            System.out.println("Wybrano clienta nr: "+actingClient.getId());
            LProduct lp = actingClient.getUnCheckedProduct();

            if(lp==null) System.out.println("błąd logiczny");

            TimeConverter.convertTime(this.fedamb.getFederateTime() + 1.0);
            actingClient.getId();
            TakeProduct takeProduct = new TakeProduct(getShelfId(lp.getProduct()), lp.getAmount(), TimeConverter.convertTime(this.fedamb.getFederateTime() + 1.0), actingClient.getId());
            //lp.checked();
            this.sendBuffor.add(takeProduct);
            actingClient.setWaitingForProduct(true);
        }

        }}

    private ShoppingList generateShoppinglist() {

        Random random = new Random();

        List<String> copyFromShelves= new LinkedList<>();
        for(Shelf s: this.shelves)
        {
            copyFromShelves.add(s.getProduct());
        }

        ShoppingList ret= new ShoppingList();

        int x=random.nextInt(clientMaxBasket)+1;
        for(int i=0;i<x;i++)
        {
            int m= random.nextInt(copyFromShelves.size()-1);
            ret.add(new LProduct(copyFromShelves.get(m),random.nextInt(clientMaxProductsAmount)+1));
            copyFromShelves.remove(m);
        }

        return ret;
    }

    private int getShelfId(String product)
    {
     for(Shelf s: this.shelves)
     {
         if(s.getProduct().equals(product)) return s.getId();
     }
     return 0;
    }

    public Client getClient(int id)
    {
        for(Client c: this.clientList){
            if(c.getId()==id) return c;
        }
        return null;
    }
    public String getProductName(int shelfId)
    {
        for(Shelf s: this.shelves)
        {
            if(s.getId()==shelfId)return s.getProduct();
        }
        return null;
    }

    public Client getRandomShoppingClient() {
        List<Client> sohoppingFreaks= new LinkedList<>();
        for(Client c: this.clientList)
        {
            //if(c.getState().equals(ClientState.SHOOPING)) sohoppingFreaks.add(c);
            if(!c.isFull())  sohoppingFreaks.add(c);
        }
        if(sohoppingFreaks.size()==0)return null;
        return sohoppingFreaks.get(random.nextInt(sohoppingFreaks.size()));

    }
}
