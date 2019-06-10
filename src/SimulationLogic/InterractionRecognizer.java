package SimulationLogic;

import Interractions.*;
import hla.rti.*;
import hla.rti.jlc.NullFederateAmbassador;
import hla.rti.jlc.RtiFactoryFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

/**
 * Created by osiza on 28.05.2019.
 */
public class InterractionRecognizer {

    public static Interraction recognize(int interractionClass, LogicalTime time, ReceivedInteraction theInteraction, RTIambassador rtiamb) {

        try {
            if (interractionClass == rtiamb.getInteractionClassHandle("InteractionRoot.ShelfRegistration")) {
                return new ShelfRegistration(time, theInteraction);
            } else if (interractionClass == rtiamb.getInteractionClassHandle("InteractionRoot.TakeProduct")) {
                return new TakeProduct(time,theInteraction);
            } else if (interractionClass == rtiamb.getInteractionClassHandle("InteractionRoot.PutProduct")) {
                return new PutProduct(time,theInteraction);
            } else if (interractionClass == rtiamb.getInteractionClassHandle("InteractionRoot.ProductAssigned")) {
                return new ProductAssigned(time,theInteraction);
            }else if (interractionClass == rtiamb.getInteractionClassHandle("InteractionRoot.NotEnoughtProducts")) {
                return new NotEnoughtProducts(time,theInteraction);
            }
            else if (interractionClass == rtiamb.getInteractionClassHandle("InteractionRoot.Finish")) {
                return new Finish(time);
            } else if (interractionClass == rtiamb.getInteractionClassHandle("InteractionRoot.CheckInMagazine")) {
                return new CheckInMagazine(time,theInteraction);
            }else if (interractionClass == rtiamb.getInteractionClassHandle("InteractionRoot.GiveProduct")) {
                return new GiveProduct(time,theInteraction);
            }else if (interractionClass == rtiamb.getInteractionClassHandle("InteractionRoot.FillMagazine")) {
                return new FillMagazine(time,theInteraction);
            }else if (interractionClass == rtiamb.getInteractionClassHandle("InteractionRoot.ClientComming")) {
                return new ClientComing(time,theInteraction);
            }else if (interractionClass == rtiamb.getInteractionClassHandle("InteractionRoot.ProductFromShoppingListBroadcast")) {
                return new ProductFromShoppingListBroadcast(time,theInteraction);
            }else if (interractionClass == rtiamb.getInteractionClassHandle("InteractionRoot.CashRegister")) {
                return new CashRegister(time,theInteraction);
            }else if (interractionClass == rtiamb.getInteractionClassHandle("InteractionRoot.CashierRegister")) {
                return new CashierRegister(time,theInteraction);
            }else if (interractionClass == rtiamb.getInteractionClassHandle("InteractionRoot.AssignToCash")) {
                return new AssignToCash(time,theInteraction);
            }else if (interractionClass == rtiamb.getInteractionClassHandle("InteractionRoot.ServeClient")) {
                return new ServeClient(time,theInteraction);
            }else if (interractionClass == rtiamb.getInteractionClassHandle("InteractionRoot.ClientServed")) {
                return new ClientServed(time,theInteraction);
            }
            return null;
        } catch (NameNotFound nameNotFound) {
            nameNotFound.printStackTrace();
        } catch (FederateNotExecutionMember federateNotExecutionMember) {
            federateNotExecutionMember.printStackTrace();
        } catch (RTIinternalError rtIinternalError) {
            rtIinternalError.printStackTrace();
        } catch (ArrayIndexOutOfBounds arrayIndexOutOfBounds) {
            arrayIndexOutOfBounds.printStackTrace();
        }

            return null;
    }
}
