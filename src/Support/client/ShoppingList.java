package Support.client;

import Interractions.ProductFromShoppingListBroadcast;
import hla.rti.*;

import java.util.LinkedList;

/**
 * Created by osiza on 05.06.2019.
 */
public class ShoppingList extends LinkedList<LProduct> {

    public void broadcast(RTIambassador rtiamb,LogicalTime time,int clientId) {
        for(LProduct p: this)
        {
            ProductFromShoppingListBroadcast pfsb= new ProductFromShoppingListBroadcast(clientId,p.getProduct(),p.getAmount(),time);
            try {
                System.out.println("SEND:");
                pfsb.log();
                pfsb.sendInterraction(rtiamb);
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
    public String toString()
    {
        String tmp="";
        for(LProduct p: this)
        {
            tmp+=p.toString();
            tmp+="\n";
        }
       return tmp;
    }

    public void fillProduct(String product, int n){
        //System.out.println(product);\
        LProduct lp= getProductByName(product);
        lp.fill(n);


    }

    private LProduct getProductByName(String product)
    {
        for(LProduct p: this)
        {
          if(p.getProduct().equals(product))return p;
        }
        return null;
    }
    public boolean isFull()
    {
        for(LProduct p: this)
        {
            if(!p.isFull()) return false;
        }
        return true;
    }

    public LProduct getUnCheckedProduct()
    {
        for(LProduct lp: this)
        {
            if(!lp.isFull())
            {
                return lp;
            }
        }
         return null;
    }


    public int getSigma() {
        int tmp=0;
        for(LProduct lp: this)
        {
            tmp+=lp.getFill();
        }
        return tmp;
    }
}
