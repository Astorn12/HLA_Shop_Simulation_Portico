package Federates;

import Interractions.*;
import SimulationLogic.Interraction;
import Support.cashesandcashiers.Cashier;
import Support.cashesandcashiers.CashierState;
import Universal.UniversalFederate;
import hla.rti.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by osiza on 06.06.2019.
 */
public class CashiersFederate extends UniversalFederate{

    List<Cashier> cashiers;
    private static final int amountOfCahiers=4;




    public CashiersFederate() {
        this.cashiers= new LinkedList<>();
        initialCashiers();
    }
    public static void main(String[] args) {
        try {
            new CashiersFederate().runFederate();
        } catch (RTIexception rtIexception) {
            rtIexception.printStackTrace();
        }
    }
    @Override
    protected void publishAndSubscribe() throws RTIexception {
        rtiamb.publishInteractionClass(rtiamb.getInteractionClassHandle( "InteractionRoot.CashierRegister" ));
        rtiamb.subscribeInteractionClass(rtiamb.getInteractionClassHandle( "InteractionRoot.CashRegister" ));
        rtiamb.publishInteractionClass(rtiamb.getInteractionClassHandle( "InteractionRoot.ClientServed" ));
        rtiamb.subscribeInteractionClass(rtiamb.getInteractionClassHandle( "InteractionRoot.ServeClient" ));
        rtiamb.subscribeInteractionClass(rtiamb.getInteractionClassHandle( "InteractionRoot.Finish" ));


    }

    @Override
    protected void reaction(Interraction interraction) {
            if(interraction.getClass().equals(CashRegister.class))
            {
                CashRegister c=(CashRegister) interraction;
                Cashier w=getWaitingCashier();
                w.setState(CashierState.SERVING);
                w.setCashId(c.getCashId());
                try {
                    CashierRegister r= new CashierRegister(convertTime(fedamb.getFederateTime()+3),w.getId(),c.getCashId());
                    this.sendBuffor.add(r);
                    w.setCashId(c.getCashId());
                } catch (ArrayIndexOutOfBounds arrayIndexOutOfBounds) {
                    arrayIndexOutOfBounds.printStackTrace();
                }
            }else if(interraction.getClass().equals(ServeClient.class)) {
                ServeClient sc=(ServeClient)interraction;
                Cashier c=getResposnibleCashier(sc.getCashId());
                ClientServed cs= new ClientServed(convertTime(fedamb.getFederateTime()+c.getServingTime(sc.getAmount())),sc.getClientId());
                this.sendBuffor.add(cs);
            }
    }

    private Cashier getWaitingCashier() {
        for(Cashier c:this.cashiers)
        {
            if( c.getState().equals(CashierState.WAITING))
            {
                return c;
            }
        }
        return null;
    }

    @Override
    protected String getFederationName() {
        return "CashiersFederate";
    }

    @Override
    protected void inicialization() {
        /*for(Cashier c:this.cashiers)
        {
            CashierRegister sr= null;
            try {
                sr = new CashierRegister(TimeConverter.convertTime(fedamb.getFederateTime()+1),c.getId());
            } catch (ArrayIndexOutOfBounds arrayIndexOutOfBounds) {
                arrayIndexOutOfBounds.printStackTrace();
            }
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
        }*/
    }

    @Override
    protected double getAdvanceTime() {
        return 2;
    }

    private void initialCashiers()
    {
        for(int i=0;i<amountOfCahiers;i++)
        {
            this.cashiers.add(new Cashier(i+1));
        }
    }

    private Cashier getResposnibleCashier( int cashId)
    {
     for(Cashier c: this.cashiers)
     {
         if(c.getCashId()==cashId)return c;
     }
     return null;
    }



}
