package Federates;

import Interractions.*;
import SimulationLogic.Interraction;
import Support.workers.Order;
import Support.workers.Worker;
import Support.workers.WorkerState;
import SupporterClasses.TimeConverter;
import Universal.UniversalFederate;
import hla.rti.*;
import org.portico.impl.hla13.types.DoubleTime;
import Support.shelves.Shelf;
import Support.magazine.StoredProduct;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by osiza on 04.06.2019.
 */
public class WorkersFederate extends UniversalFederate {

    private List<Shelf> shelves;
    int workerIdIterator;
    private int amountOfWorkers=4;
    private static final double orderingTime=5.0;
    private static final double checkingTime=5.0;
    private static final double putingTime=5.0;

    private List<Order> orders;
    private List<Worker> workers;

    private List<NotEnoughtProducts> notServedRequest;


    public WorkersFederate() {
        this.shelves= new LinkedList<>();

        this.workers= new LinkedList<>();
        this.workerIdIterator=0;// czyli najpierw zwiększamy id a poten przypisujemy

        this.orders= new LinkedList<>();

        initialWorkers();
        this.notServedRequest= new LinkedList<>();
    }
    private void initialWorkers()/** tworzenie półek, półki inondeksowane są od 1 */
    {

        for(int i=0;i<amountOfWorkers;i++)
        {
            this.workers.add(new Worker(i+1 ));
        }
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

    public static void main(String[] args) {
        try {
            new WorkersFederate().runFederate();
        } catch (RTIexception rtIexception) {
            rtIexception.printStackTrace();
        }
    }

    private boolean isAlreadyOrdered(String product)
    {
        for(Order o : this.orders)
        {
            if(o.getProduct().equals(product)) return true;
        }
        return false;
    }

    public double getShortestTime(double time)
    {
        double tmp= 1000000;
        for(Interraction i: this.sendBuffor)
        {
            if(i.getTime().isGreaterThan(convertTime(time))&&i.getTime().isLessThan(convertTime(tmp)) )
            {
                tmp=((DoubleTime) i.getTime()).getTime();
            }
        }
        if(tmp==1000000) {
            tmp =5;

        }
        return tmp;
    }

    private Worker getFreeWorker()
    {

        for(Worker w:this.workers)
        {
            if(w.getState().equals(WorkerState.FREE)) return w;

        }
        return null;
    }
    public Worker getWorker(int id)
    {
        for(Worker w: this.workers)
        {
            if(w.getId()==id) return w;
        }
        return null;
    }

    private int getShelfId(String product)
    {
        for(Shelf s: shelves)
        {
            if(s.getProduct().equals(product)) return s.getId();
        }
        return 0;
    }


    private void freeingWorkers()
    {
        for(Worker w: workers)
        {
            if(w.getWorkEnd()!=0&&w.getWorkEnd()<fedamb.getFederateTime()&&w.getState().equals(WorkerState.PUTINGPRODUCT))
            {
                w.release();
            }
        }
    }





    @Override
    protected void publishAndSubscribe() throws RTIexception {
        /**rejestrowanie półek, przez publikowanie interrakcji*/
        int shelfRegistration= rtiamb.getInteractionClassHandle("InteractionRoot.ShelfRegistration");
        rtiamb.subscribeInteractionClass(shelfRegistration);



        int notEnoughtProductsHandle = rtiamb.getInteractionClassHandle( "InteractionRoot.NotEnoughtProducts" );
        rtiamb.subscribeInteractionClass(notEnoughtProductsHandle);
        rtiamb.subscribeInteractionClass(rtiamb.getInteractionClassHandle( "InteractionRoot.Finish" ));
        rtiamb.subscribeInteractionClass(rtiamb.getInteractionClassHandle( "InteractionRoot.GiveProduct" ));

        rtiamb.publishInteractionClass(rtiamb.getInteractionClassHandle( "InteractionRoot.PutProduct" ));
        rtiamb.publishInteractionClass(rtiamb.getInteractionClassHandle( "InteractionRoot.CheckInMagazine" ));
        rtiamb.publishInteractionClass(rtiamb.getInteractionClassHandle( "InteractionRoot.FillMagazine" ));
    }

    @Override
    protected void reaction(Interraction interraction) {


          //  Collections.sort(this.receiveBuffor,Interraction::compare);


                if(interraction.getClass().equals(ShelfRegistration.class)) {
                    ShelfRegistration sr= (ShelfRegistration) interraction;
                    sr.log();
                    this.shelves.add(new Shelf(sr));
                }


                /**Trzeba wymyślić jak będe robić z tymi odpowiedziami bo sprawdzanie w magazynie powinno trwać trochę czasu
                 * a do tego trzeba zdecydować kto będzie ten czas liczył worker czy magzin
                 * Liczenie będzie tak if(rtiamb.currenttime + rtiamb.lookahead < wyliczonyczas (np.current +5) czyli czy lookahad jest dłiższe od trwania czynności
                 * jeśli tak to wysyłamy od razu z czasem jeśli nie to zostawiamy sobie do następnej iteracji, tutaj też trzebasię zastanowić co będzie kolejnym żądaniem
                 * jeśli dłuższy dod lookahead to to będzie czas naszego następnego żądania, najmniejszy większy od loookahea
                 * czyli trzeba też stworzyć sortowaną listę elementów do wysłania, bo na razie mamy lisę przychodzących elementów*/


                else if(interraction.getClass().equals(NotEnoughtProducts.class)) {
                    NotEnoughtProducts nep=(NotEnoughtProducts)interraction;
                    nep.log();
                    String product =getShelfById(nep.getShelfId()).getProduct();//tego productu brakuje
                    if(isAlreadyOrdered(product))
                    {
                        //w sumie to nic nie robimy
                    }else{

                        //sprawdzamy w magazynie

                        Worker worker = getFreeWorker();
                        if(worker==null)
                        {
                            /**nic nie robimy nie usuwamy zapytania z listy interrakcji, będzie czekało aż ktoś będzie wolny*/
                            placeInRequestQueue(nep);
                        }
                        else {
                            Shelf shelf= getShelfById(nep.getShelfId());
                            //System.out.println("Produkt z półki "+ shelf.getProduct());
                            CheckInMagazine cim = new CheckInMagazine(TimeConverter.convertTime(fedamb.getFederateTime() + this.checkingTime),worker.getId(),shelf.getProduct(),shelf.getSize());
                            this.sendBuffor.add(cim);
                            worker.checkInMagazine(new StoredProduct(shelf.getProduct(),shelf.getSize()));
                        }
                    }
                }
                else if(interraction.getClass().equals(GiveProduct.class)) {
                    GiveProduct gp=(GiveProduct) interraction;
                    gp.log();
                    System.out.println("Worker id:"+ gp.getWorkerId());

                    Worker worker=getWorker(gp.getWorkerId());
                    System.out.println("Worker:"+ worker.getId());
                    System.out.println("State"+ worker.getState());
                    System.out.println("WorkEnd"+ worker.getWorkEnd());
                    System.out.println("Request"+ worker.getRequest());
                    if(worker.getRequest().getAmount()>gp.getAmount())
                    {
                        //tworzenie zamówienia na produkt
                        FillMagazine fm= new FillMagazine(TimeConverter.convertTime(fedamb.getFederateTime()+orderingTime),gp.getProduct(),30,worker.getId());//tytaj sztywno ustawione na składanie zamóienia na 30 sztuk produktu
                        this.sendBuffor.add(fm);
                        System.out.println("FILLUJEMY TEN MAGAZYN");
                       // worker.setState(WorkerState.MAKINGORDER);

                    }

                    PutProduct pp= new PutProduct(getShelfId(worker.getRequest().getProduct()),worker.getId(),gp.getAmount(),TimeConverter.convertTime(fedamb.getFederateTime()+putingTime));
                    this.sendBuffor.add(pp);
                    worker.setState(WorkerState.PUTINGPRODUCT);
                    worker.setWorkEnd(fedamb.getFederateTime()+putingTime);
                }




    }

    private void placeInRequestQueue(NotEnoughtProducts nep) {

        for(NotEnoughtProducts n: this.notServedRequest)
        {
            if(n.getShelfId()==nep.getShelfId())break;
        }
        this.notServedRequest.add(nep);
    }

    @Override
    protected String getFederationName() {
        return "WorkersFederate";
    }

    @Override
    protected void inicialization() {

    }

    @Override
    protected double getAdvanceTime() {
        return 5;
    }


    @Override
    protected void mainLoopInnerAction()
    {
        freeingWorkers();
        waitingRequestServing();
    }

    private void waitingRequestServing() {
        for(Worker w: this.workers)
        {
            if(w.getState().equals(WorkerState.FREE))
            {
                if(notServedRequest.size()>0){
                NotEnoughtProducts nep= notServedRequest.get(0);

                Shelf shelf= getShelfById(nep.getShelfId());
                //System.out.println("Produkt z półki "+ shelf.getProduct());
                CheckInMagazine cim = new CheckInMagazine(TimeConverter.convertTime(fedamb.getFederateTime() + this.checkingTime),w.getId(),shelf.getProduct(),shelf.getSize());
                this.sendBuffor.add(cim);
                w.checkInMagazine(new StoredProduct(shelf.getProduct(),shelf.getSize()));
                notServedRequest.remove(0);
                }
            }
        }
    }
}
