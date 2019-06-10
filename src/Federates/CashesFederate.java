package Federates;

import Interractions.*;
import SimulationLogic.Interraction;
import Support.cashesandcashiers.Cash;
import Support.cashesandcashiers.QueueClient;
import SupporterClasses.TimeConverter;
import Universal.UniversalFederate;
import hla.rti.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by osiza on 06.06.2019.
 */
public class CashesFederate extends UniversalFederate{
        List<Cash> cashes;
        private static final int amountOfCashes= 4;


    public CashesFederate() {
        this.cashes= new LinkedList<>();
        initialCashes();
    }

    public static void main(String[] args) {
        try {
                new CashesFederate().runFederate();
        } catch (RTIexception rtIexception) {
            rtIexception.printStackTrace();
        }
    }
    @Override
    protected void publishAndSubscribe() throws RTIexception {
        rtiamb.publishInteractionClass(rtiamb.getInteractionClassHandle( "InteractionRoot.CashRegister" ));
        rtiamb.subscribeInteractionClass(rtiamb.getInteractionClassHandle( "InteractionRoot.AssignToCash" ));
        rtiamb.subscribeInteractionClass(rtiamb.getInteractionClassHandle( "InteractionRoot.ClientServed" ));
        rtiamb.publishInteractionClass(rtiamb.getInteractionClassHandle( "InteractionRoot.ServeClient" ));
        rtiamb.subscribeInteractionClass(rtiamb.getInteractionClassHandle( "InteractionRoot.Finish" ));

    }

    @Override
    protected void reaction(Interraction interraction) {
        if(interraction.getClass().equals(AssignToCash.class))
        {
            AssignToCash a=(AssignToCash)interraction;
            Cash c=getShortestCash();
            c.add(new QueueClient(a.getClientId(),a.getAmount()));
            if(c.size()==1)
            {
                try {
                    ServeClient sc= new ServeClient(convertTime(fedamb.getFederateTime()+1),a.getClientId(),c.getId(),a.getAmount());
                    sendBuffor.add(sc);
                } catch (ArrayIndexOutOfBounds arrayIndexOutOfBounds) {
                    arrayIndexOutOfBounds.printStackTrace();
                }
            }
        }else if(interraction.getClass().equals(ClientServed.class)){
            ClientServed cs=(ClientServed) interraction;

            Cash cash=getCashWithClient(cs.getclientId());
            cash.removeClient(cs.getclientId());
            if(cash.size()>0)
            {
                try {
                    QueueClient toServe=cash.get(0);
                    ServeClient sc= new ServeClient(convertTime(fedamb.getFederateTime()+1),toServe.getClientId(),cash.getId(),toServe.getProductsAmount());
                    sendBuffor.add(sc);
                } catch (ArrayIndexOutOfBounds arrayIndexOutOfBounds) {
                    arrayIndexOutOfBounds.printStackTrace();
                }
            }
        }
    }

    private Cash getCashWithClient(int id) {
        for(Cash cash:this.cashes)
        {
            for(QueueClient client:cash)
            {
                if(client.getClientId()==id)return cash;
            }
        }
        return null;
    }



    @Override
    protected String getFederationName() {
        return "CashesFederate";
    }

    @Override
    protected void inicialization() {
        for(Cash c:this.cashes)
        {
            CashRegister sr= new CashRegister(TimeConverter.convertTime(fedamb.getFederateTime()+1),c.getId());
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

    @Override
    protected double getAdvanceTime() {
        return 2;
    }

    private void initialCashes()/** tworzenie półek, półki inondeksowane są od 1 */
    {
        for(int i=0;i<amountOfCashes;i++)
        {
            this.cashes.add(new Cash(i+1));
        }
    }

    private Cash getShortestCash()
    {
        int size=10000;
        Cash tmp=null;
        for(Cash c: this.cashes)
        {
            if(c.size()<size) {
                tmp=c;
                size=c.size();
            }
        }
        return tmp;
    }
}
