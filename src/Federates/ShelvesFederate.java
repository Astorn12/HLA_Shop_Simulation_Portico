package Federates;

import Interractions.*;
import SimulationLogic.Interraction;
import SimulationLogic.ProductSet;
import Support.shelves.Shelf;
import SupporterClasses.TimeConverter;
import Universal.UniversalFederate;
import hla.rti.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by osiza on 03.06.2019.
 */
public class ShelvesFederate extends UniversalFederate {

    private List<Shelf> shelves;
    //private final int amoutOfShelves=5;
    Random random;
    int shelfIdIterator;

    public ShelvesFederate() {
        this.shelves= new LinkedList<>();
        this.random= new Random();
        initialShelves();
        this.shelfIdIterator=0;// czyli najpierw zwiększamy id a poten przypisujemy
    }


    public Shelf getShelfById(int id) {
        for(Shelf s: this.shelves)
        {
            if(s.getId()==id)
            {
                return s;
            }
        }
        return null;
    }

    private void showShelves()
    {
        for(Shelf s: shelves)
        {
            log( s.toString());
        }
    }
    private void initialShelves()/** tworzenie półek, półki inondeksowane są od 1 */
    {
        List<String> productList= ProductSet.getInstance().getChosenList();
        int i=0;
        for(String p: productList)
        {
            i++;
            this.shelves.add(new Shelf(i,this.random.nextInt(11)+4,p));
            System.out.println("Product z listy: "+ p);
        }
    }
    private void shelvesRegistration()
    {

        for(Shelf shelf: this.shelves)
        {
            ShelfRegistration sr= new ShelfRegistration(TimeConverter.convertTime(fedamb.getFederateTime()+1),shelf);
            try {
                sr.sendInterraction(this.rtiamb);
            } catch (RTIinternalError rtIinternalError) {
                rtIinternalError.printStackTrace();
            } catch (NameNotFound nameNotFound) {
                nameNotFound.printStackTrace();
            } catch (FederateNotExecutionMember federateNotExecutionMember) {
                federateNotExecutionMember.printStackTrace();
            } catch (InteractionClassNotDefined interactionClassNotDefined) {
                interactionClassNotDefined.printStackTrace();
            } catch (RestoreInProgress restoreInProgress) {
                restoreInProgress.printStackTrace();
            } catch (InteractionClassNotPublished interactionClassNotPublished) {
                interactionClassNotPublished.printStackTrace();
            } catch (SaveInProgress saveInProgress) {
                saveInProgress.printStackTrace();
            } catch (InvalidFederationTime invalidFederationTime) {
                invalidFederationTime.printStackTrace();
            } catch (ConcurrentAccessAttempted concurrentAccessAttempted) {
                concurrentAccessAttempted.printStackTrace();
            } catch (InteractionParameterNotDefined interactionParameterNotDefined) {
                interactionParameterNotDefined.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            new ShelvesFederate().runFederate();
        } catch (RTIexception rtIexception) {
            rtIexception.printStackTrace();
        }
    }

    @Override
    protected void publishAndSubscribe() throws RTIexception {
        /**rejestrowanie półek, przez publikowanie interrakcji*/
        int shelfRegistration= rtiamb.getInteractionClassHandle("InteractionRoot.ShelfRegistration");
        rtiamb.publishInteractionClass(shelfRegistration);

        /** Ustalanie subskrypcji na takeProductInterracion*/
        int takeProductHandle = rtiamb.getInteractionClassHandle( "InteractionRoot.TakeProduct" );
        rtiamb.subscribeInteractionClass(takeProductHandle);
        int productAssignerHandle = rtiamb.getInteractionClassHandle( "InteractionRoot.ProductAssigned" );
        rtiamb.publishInteractionClass(productAssignerHandle);

        int notEnoughtProductsHandle = rtiamb.getInteractionClassHandle( "InteractionRoot.NotEnoughtProducts" );
        rtiamb.publishInteractionClass(notEnoughtProductsHandle);
        rtiamb.subscribeInteractionClass(rtiamb.getInteractionClassHandle( "InteractionRoot.Finish" ));
        rtiamb.subscribeInteractionClass(rtiamb.getInteractionClassHandle( "InteractionRoot.PutProduct" ));
    }

    @Override
    protected void reaction(Interraction interraction) {
        if(interraction.getClass().equals(TakeProduct.class))
        {
            TakeProduct tp=(TakeProduct) interraction;
            Shelf chosenShelf=getShelfById(tp.getShelfId());
            if(chosenShelf.getFill()>=tp.getAmount())
            {
                ProductAssigned productAssigned= new ProductAssigned(tp.getClientId(), chosenShelf.getId(),tp.getAmount(),convertTime(fedamb.getFederateTime()+1));
                chosenShelf.decreaseStock(tp.getAmount());
                log("Wzięto z półki");
                productAssigned.log();
                //tp.log();
               // showShelves();
                this.sendBuffor.add(productAssigned);
            }
            else{
                NotEnoughtProducts nep= new NotEnoughtProducts(tp.getClientId(),tp.getShelfId(),chosenShelf.getFill(),convertTime(fedamb.getFederateTime()+1));
                chosenShelf.decreaseStock(chosenShelf.getFill());
                this.sendBuffor.add(nep);
            }

        }
        else if(interraction.getClass().equals(PutProduct.class)) {
            PutProduct pp= (PutProduct) interraction;
            Shelf chosenShelf=getShelfById(pp.getShelfId());
            chosenShelf.increaseStock(pp.getAmount());
            /**Położenia na półce koniec ścieżki actywności*/
        }
    }

    @Override
    protected String getFederationName() {
        return "ShelvesFederate";
    }

    @Override
    protected void inicialization() {
        shelvesRegistration();
    }

    @Override
    protected double getAdvanceTime() {
        return 5;
    }
}
