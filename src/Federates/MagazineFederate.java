package Federates;

import Interractions.CheckInMagazine;
import Interractions.FillMagazine;
import Interractions.GiveProduct;
import SimulationLogic.Interraction;
import SimulationLogic.ProductSet;
import Universal.UniversalFederate;
import hla.rti.*;
import Support.magazine.StoredProduct;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by osiza on 05.06.2019.
 */
public class MagazineFederate extends UniversalFederate {



    Random random;
    List<StoredProduct> storedProducts;
    int magazineSize=30;




    public MagazineFederate() {
        this.random= new Random();
        this.storedProducts= new LinkedList<>();
        initialMagazine();
    }

    private void initialMagazine()
    {
        List<String> set= ProductSet.getInstance().getChosenList();

        for(String s:set)
        {
            this.storedProducts.add(new StoredProduct(s,magazineSize));
        }
    }

    public static void main(String[] args) {
        try {
            new MagazineFederate().runFederate();
        } catch (RTIexception rtIexception) {
            rtIexception.printStackTrace();
        }
    }

    public int takeFromMagazine(StoredProduct product)
    {
        for(StoredProduct sp: this.storedProducts)
        {
            if(sp.getProduct().equals(product.getProduct()))
            {
                return sp.tryToTake(product.getAmount());
            }
        }
        return 0;
    }
    public void completeMagazine(StoredProduct storedProduct)
    {
        StoredProduct wsk=getStoredProduct(storedProduct.getProduct());
        wsk.setAmount(wsk.getAmount()+storedProduct.getAmount());
        if(wsk.getAmount()>this.magazineSize)wsk.setAmount(magazineSize);
    }
    private StoredProduct getStoredProduct(String name)
    {
        for(StoredProduct sp: this.storedProducts)
        {
            if(sp.getProduct().equals(name)) return sp;
        }
        return null;
    }


    @Override
    protected void publishAndSubscribe() throws RTIexception {
        rtiamb.subscribeInteractionClass(rtiamb.getInteractionClassHandle( "InteractionRoot.Finish" ));
        rtiamb.subscribeInteractionClass(rtiamb.getInteractionClassHandle( "InteractionRoot.CheckInMagazine" ));
        rtiamb.subscribeInteractionClass(rtiamb.getInteractionClassHandle( "InteractionRoot.FillMagazine" ));



        rtiamb.publishInteractionClass(rtiamb.getInteractionClassHandle( "InteractionRoot.GiveProduct" ));
    }

    @Override
    protected void reaction(Interraction interraction) {
        if(interraction.getClass().equals(CheckInMagazine.class)) {
            CheckInMagazine cim=(CheckInMagazine) interraction;
            int packedProduct= this.takeFromMagazine(new StoredProduct(cim.getProduct(),cim.getAmount()));
            System.out.println("Wysłano produkt "+ cim.getProduct());
            GiveProduct giveProducts= new GiveProduct(cim.getWorkerId(),cim.getProduct(),packedProduct,convertTime(fedamb.getFederateTime()+1));
            this.sendBuffor.add(giveProducts);

        }
        /**Trzeba wymyślić jak będe robić z tymi odpowiedziami bo sprawdzanie w magazynie powinno trwać trochę czasu
         * a do tego trzeba zdecydować kto będzie ten czas liczył worker czy magzin
         * Liczenie będzie tak if(rtiamb.currenttime + rtiamb.lookahead < wyliczonyczas (np.current +5) czyli czy lookahad jest dłiższe od trwania czynności
         * jeśli tak to wysyłamy od razu z czasem jeśli nie to zostawiamy sobie do następnej iteracji, tutaj też trzebasię zastanowić co będzie kolejnym żądaniem
         * jeśli dłuższy dod lookahead to to będzie czas naszego następnego żądania, najmniejszy większy od loookahea
         * czyli trzeba też stworzyć sortowaną listę elementów do wysłania, bo na razie mamy lisę przychodzących elementów*/
        else if(interraction.getClass().equals(FillMagazine.class)) {
            FillMagazine fm= (FillMagazine)  interraction ;
            completeMagazine(new StoredProduct(fm.getProduct(),fm.getAmount()));
        }
    }

    @Override
    protected String getFederationName() {
        return "MagazineFederate";
    }

    @Override
    protected void inicialization() {

    }

    @Override
    protected double getAdvanceTime() {
        return 3;
    }
}
