package Federates;

import GUI.Panels.*;
import Interractions.*;
import SimulationLogic.Interraction;
import Support.cashesandcashiers.Cash;
import Support.cashesandcashiers.Cashier;
import Support.cashesandcashiers.QueueClient;
import Support.client.Client;
import Support.client.ClientState;
import Support.client.LProduct;
import SupporterClasses.TimeConverter;
import Universal.UniversalFederate;
import hla.rti.*;
import Support.magazine.StoredProduct;
import net.miginfocom.swing.MigLayout;
import Support.shelves.Shelf;
import Support.workers.Worker;
import Support.workers.WorkerState;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * Created by osiza on 03.06.2019.
 */
public class GUIFederate extends UniversalFederate {


    List<ShelfPanel> shelves;
    List<WorkerPanel> workers;
    List<ClientPanel> clients;
    List<MagazinePanel> storedProducts;
    List<CashierPanel> cashiers;
    List<CashPanel> cashes;

    boolean endFlag = false;

    JFrame frame;
    JPanel stockPanel;
    JPanel workerdPanel;
    JPanel shelvesPanel;
    JPanel clientsPanel;
    JPanel cashiesPanel;
    JLabel time;

    JScrollPane scrolStock;
    JScrollPane scrolClient;


    int odd=0;
    public GUIFederate() {
        this.shelves = new LinkedList<>();
        this.workers = new LinkedList<>();
        this.clients = new LinkedList<>();
        this.storedProducts = new LinkedList<>();
        this.cashes = new LinkedList<>();
        this.cashiers = new LinkedList<>();

        this.frame = new JFrame();

        // frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                endFlag = true;
            }
        });
        frame.setSize(1800, 1200);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLayout(new MigLayout());

        time = new JLabel("0");
        time.setFont(new Font(Font.SANS_SERIF,  Font.BOLD, 24));
        frame.add(time, "wrap,center");

        this.stockPanel = new JPanel();
        stockPanel.setLayout(new MigLayout());
        stockPanel.setBackground(java.awt.Color.cyan);

        JLabel stockText = new JLabel("Stock");
        stockText.setFont(new Font(Font.SANS_SERIF,  Font.BOLD, 24));
        stockPanel.add(stockText, "wrap, center");
        this.scrolStock = new JScrollPane(stockPanel);
        frame.add(scrolStock, "w 400!, h 1000!");

        this.workerdPanel = new JPanel();
        workerdPanel.setLayout(new MigLayout());
        workerdPanel.setBackground(java.awt.Color.blue);
        JLabel wp=new JLabel("Workers");
        wp.setFont(new Font(Font.SANS_SERIF,  Font.BOLD, 24));
        workerdPanel.add(wp, "wrap,center");
        frame.add(workerdPanel, "w 300!, h 1000!");


        this.shelvesPanel = new JPanel();
        shelvesPanel.setLayout(new MigLayout());
        shelvesPanel.setBackground(java.awt.Color.GREEN);
        JLabel shelvesLabel=new JLabel("Shelves");
        shelvesLabel.setFont(new Font(Font.SANS_SERIF,  Font.BOLD, 24));
        shelvesPanel.add(shelvesLabel, "wrap");
        frame.add(shelvesPanel, "w 300!, h 1000!");

        this.clientsPanel = new JPanel();
        clientsPanel.setLayout(new MigLayout());
        clientsPanel.setBackground(java.awt.Color.pink);
        JLabel balelCP=new JLabel("Clients");
        balelCP.setFont(new Font(Font.SANS_SERIF,  Font.BOLD, 24));
        clientsPanel.add(balelCP, "wrap");
        scrolClient = new JScrollPane(clientsPanel);
        frame.add(scrolClient, "w 300!, h 1000!");
        this.cashiesPanel = new JPanel();
        cashiesPanel.setLayout(new MigLayout());
        cashiesPanel.setBackground(java.awt.Color.GREEN);
        JLabel cashLabel=new JLabel("Cashies");
        cashLabel.setFont(new Font(Font.SANS_SERIF,  Font.BOLD, 24));
        cashiesPanel.add(cashLabel, "wrap");
        frame.add(cashiesPanel, "w 700!, h 1000!");


    }

    public static void main(String[] args) {
        try {
            new GUIFederate().runFederate();
        } catch (RTIexception rtIexception) {
            rtIexception.printStackTrace();
        }
    }


  /*  @Override
    public void update(int interractionClass, LogicalTime theTime, ReceivedInteraction theInterraction) {
        Interraction interraction= InterractionRecognizer.recognize(interractionClass,theTime,theInterraction,this.rtiamb);
        this.reactionForInterraction(interraction);

    }*/

    /*private void reactionForInterraction(Interraction interraction)
    {
        if(interraction.getClass().equals(TakeProduct.class))
        {
            TakeProduct tp=(TakeProduct) interraction;
        }
        else if(interraction.getClass().equals(PutProduct.class)) {

        } else if(interraction.getClass().equals(ShelfRegistration.class)) {
            ShelfRegistration sr= (ShelfRegistration)interraction;
            this.Support.shelves.add(new ShelfPanel(new Shelf(sr)));
            this.shelvesPanel.add(Support.shelves.get(Support.shelves.size()-1),"wrap,w 300!, h 150");

            this.shelvesPanel.revalidate();
        }else if(interraction.getClass().equals(ProductAssigned.class)) {
            ProductAssigned pa=(ProductAssigned) interraction;
            ShelfPanel sp= getShelfPanelById(pa.getProductId());
            sp.shelf.decreaseStock(pa.getAmount());
            sp.update();

        }else if(interraction.getClass().equals(NotEnoughtProducts.class)) {
            NotEnoughtProducts nep=(NotEnoughtProducts) interraction;
        }
        else if(interraction.getClass().equals(CheckInMagazine.class)) {
            CheckInMagazine cim= (CheckInMagazine) interraction;
            if(!isAlreadyAddedWorker(cim.getWorkerId()))
            {
                Worker worker= new Worker(cim.getWorkerId());
                worker.setState(WorkerState.CHECKINGINMAGAZINE);
                WorkerPanel wp= new WorkerPanel(worker);
                this.workerdPanel.add(wp,"wrap");
            }
        }

    }*/
    private void end() {
        System.out.println("Koniec działania");
        Finish finish = new Finish(TimeConverter.convertTime(fedamb.getFederateTime() + 1));

        try {
            finish.sendInterraction(rtiamb);
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
        endFlag = true;
        frame.dispose();
    }

    private ShelfPanel getShelfPanelById(int id) {
        for (ShelfPanel sp : this.shelves) {
            if (sp.getShelf().getId() == id) return sp;
        }
        return null;
    }

    private void updateTime() {
        this.time.setText(fedamb.getFederateTime() + "");
        this.time.revalidate();
    }

    private boolean isAlreadyAddedWorker(int workerId) {
        for (WorkerPanel wp : this.workers) {
            if (wp.getWorker().getId() == workerId) return true;
        }
        return false;
    }

    @Override
    protected void mainLoopInnerAction() {
        updateTime();
        if (endFlag) {
            fedamb.stop();
            end();
        }

        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void publishAndSubscribe() throws RTIexception {
        /**rejestrowanie półek, przez publikowanie interrakcji*/
        int shelfRegistration = rtiamb.getInteractionClassHandle("InteractionRoot.ShelfRegistration");
        rtiamb.subscribeInteractionClass(shelfRegistration);

        /** Ustalanie subskrypcji na takeProductInterracion*/
        int takeProductHandle = rtiamb.getInteractionClassHandle("InteractionRoot.TakeProduct");
        rtiamb.subscribeInteractionClass(takeProductHandle);
        int productAssignerHandle = rtiamb.getInteractionClassHandle("InteractionRoot.ProductAssigned");
        rtiamb.subscribeInteractionClass(productAssignerHandle);

        int notEnoughtProductsHandle = rtiamb.getInteractionClassHandle("InteractionRoot.NotEnoughtProducts");
        rtiamb.subscribeInteractionClass(notEnoughtProductsHandle);

        rtiamb.publishInteractionClass(rtiamb.getInteractionClassHandle("InteractionRoot.Finish"));
        rtiamb.subscribeInteractionClass(rtiamb.getInteractionClassHandle("InteractionRoot.CheckInMagazine"));
        rtiamb.subscribeInteractionClass(rtiamb.getInteractionClassHandle("InteractionRoot.ClientComming"));
        rtiamb.subscribeInteractionClass(rtiamb.getInteractionClassHandle("InteractionRoot.ProductFromShoppingListBroadcast"));
        rtiamb.subscribeInteractionClass(rtiamb.getInteractionClassHandle("InteractionRoot.GiveProduct"));
        rtiamb.subscribeInteractionClass(rtiamb.getInteractionClassHandle("InteractionRoot.PutProduct"));
        rtiamb.subscribeInteractionClass(rtiamb.getInteractionClassHandle("InteractionRoot.FillMagazine"));
        rtiamb.subscribeInteractionClass(rtiamb.getInteractionClassHandle("InteractionRoot.CashRegister"));
        rtiamb.subscribeInteractionClass(rtiamb.getInteractionClassHandle("InteractionRoot.CashierRegister"));
        rtiamb.subscribeInteractionClass(rtiamb.getInteractionClassHandle("InteractionRoot.AssignToCash"));
        rtiamb.subscribeInteractionClass(rtiamb.getInteractionClassHandle("InteractionRoot.ClientServed"));
    }

    @Override
    protected void reaction(Interraction interraction) {
        if (interraction.getClass().equals(TakeProduct.class)) {
            TakeProduct tp = (TakeProduct) interraction;
        } else if (interraction.getClass().equals(PutProduct.class)) {
            PutProduct pp = (PutProduct) interraction;
            ShelfPanel s = getShelfPanelById(pp.getShelfId());
            s.getShelf().increaseStock(pp.getAmount());
            s.update();
            s.revalidate();
            WorkerPanel w = getWorker(pp.getWorkerId());
            w.getWorker().setState(WorkerState.FREE);
            w.update();
            w.revalidate();

        } else if (interraction.getClass().equals(ShelfRegistration.class)) {
            ShelfRegistration sr = (ShelfRegistration) interraction;
            this.shelves.add(new ShelfPanel(new Shelf(sr)));
            this.shelvesPanel.add(shelves.get(shelves.size() - 1), "wrap,w 300!, h 130");
            this.shelvesPanel.revalidate();

            this.storedProducts.add(new MagazinePanel(new StoredProduct(sr.getProduct(), 30)));

            odd+=1;
            if(odd%2==1)
            this.stockPanel.add(storedProducts.get(storedProducts.size() - 1), "w 140!, h 90");
            else
                this.stockPanel.add(storedProducts.get(storedProducts.size() - 1), "wrap,w 140!, h 90");
            this.stockPanel.revalidate();


        } else if (interraction.getClass().equals(ProductAssigned.class)) {

            ProductAssigned pa = (ProductAssigned) interraction;
            ShelfPanel sp = getShelfPanelById(pa.getProductId());
            sp.getShelf().decreaseStock(pa.getAmount());
            sp.update();
            ClientPanel client = getClient(pa.getClientId());
            System.out.println(client);
            System.out.println(client.getClient());
            System.out.println(client.getClient().getShoppingList());

            client.getClient().getShoppingList().fillProduct(getProductName(pa.getProductId()), pa.getAmount());
            if (client.getClient().getShoppingList().isFull()) client.getClient().setState(ClientState.WAITINGFORQUEUE);
            client.update();


        } else if (interraction.getClass().equals(NotEnoughtProducts.class)) {
            NotEnoughtProducts nep = (NotEnoughtProducts) interraction;
            if(nep.getAmount()!=0) {
                ShelfPanel sp = getShelfPanelById(nep.getShelfId());
                sp.getShelf().decreaseStock(nep.getAmount());
                sp.update();
                ClientPanel client = getClient(nep.getClientId());
                client.getClient().getShoppingList().fillProduct(getProductName(nep.getShelfId()), nep.getAmount());
                if (client.getClient().getShoppingList().isFull())
                    client.getClient().setState(ClientState.WAITINGFORQUEUE);
                client.update();
            }
        } else if (interraction.getClass().equals(CheckInMagazine.class)) {
            CheckInMagazine cim = (CheckInMagazine) interraction;
            if (!isAlreadyAddedWorker(cim.getWorkerId())) {
                Worker worker = new Worker(cim.getWorkerId());
                worker.setState(WorkerState.CHECKINGINMAGAZINE);
                WorkerPanel wp = new WorkerPanel(worker);
                this.workerdPanel.add(wp, "wrap,w 200!, h 200!");
                this.workers.add(wp);
                this.workerdPanel.revalidate();
                System.out.println("Dodano workera, ilosć w liście: " + workers.size());
            } else {
                WorkerPanel worker = getWorker(cim.getWorkerId());
                worker.getWorker().setState(WorkerState.CHECKINGINMAGAZINE);
                worker.update();
                worker.revalidate();
            }
        } else if (interraction.getClass().equals(ClientComing.class)) {
            ClientComing clientComing = (ClientComing) interraction;

            Client client = new Client(clientComing.getClientId());
            this.clients.add(new ClientPanel(client));
            this.clientsPanel.add(clients.get(clients.size() - 1), "wrap,w 300!, h 150");
        } else if (interraction.getClass().equals(ProductFromShoppingListBroadcast.class)) {
            System.out.println("Najpierw rejestracje koszyka");
            ProductFromShoppingListBroadcast p = (ProductFromShoppingListBroadcast) interraction;
            ClientPanel cp = getClient(p.getClientId());
            cp.getClient().getShoppingList().add(new LProduct(p.getProduct(), p.getAmount()));
            cp.update();
            clientsPanel.revalidate();
        } else if (interraction.getClass().equals(GiveProduct.class)) {
            GiveProduct giveProducts = (GiveProduct) interraction;
            System.out.println("Product" + giveProducts.getProduct());
            MagazinePanel m = this.getMagazinePanel(giveProducts.getProduct());

            m.getStoredProduct().decreaseStock(giveProducts.getAmount());
            m.update();
            m.revalidate();

            WorkerPanel w = getWorker(giveProducts.getWorkerId());
            w.getWorker().setState(WorkerState.PUTINGPRODUCT);
            w.update();
            w.revalidate();
        } else if (interraction.getClass().equals(FillMagazine.class)) {
            FillMagazine f = (FillMagazine) interraction;
            MagazinePanel m = getMagazinePanel(f.getProduct());
            m.getStoredProduct().increaseStock(f.getAmount());
            m.update();
            m.revalidate();
        } else if (interraction.getClass().equals(CashRegister.class)) {
            CashRegister c = (CashRegister) interraction;

            this.cashes.add(new CashPanel(new Cash(c.getCashId())));

            this.cashiesPanel.add(cashes.get(cashes.size() - 1), "wrap,w 400!, h 150");
            this.cashiesPanel.revalidate();
        } else if (interraction.getClass().equals(CashierRegister.class)) {
            CashierRegister cr = (CashierRegister) interraction;
            CashPanel p =getCashPanel(cr.getCashierId());
            CashierPanel cp= new CashierPanel(new Cashier(cr.getCashierId()));

            p.setCashierPanel(cp);
            p.update();
            p.revalidate();


        } else if (interraction.getClass().equals(FillMagazine.class)) {
            CashierRegister c = (CashierRegister) interraction;
            Cashier cashier = new Cashier(c.getCashierId());
            this.cashiers.add(new CashierPanel(cashier));
        } else if (interraction.getClass().equals(AssignToCash.class)) {
            AssignToCash a = (AssignToCash) interraction;
            CashPanel c = getShortestCashPanel();
            c.getCash().add(new QueueClient(a.getClientId(), a.getAmount()));
            c.update();
            c.revalidate();

            ClientPanel p = getClient(a.getClientId());
            this.clients.remove(p);
            this.clientsPanel.remove(p);
        }else if (interraction.getClass().equals(ClientServed.class)) {
            ClientServed cs=(ClientServed) interraction;
            CashPanel cp=getCashWithClient(cs.getclientId());
            cp.getCash().removeClient(cs.getclientId());
            cp.update();
            cp.revalidate();
        }

    }

    private CashPanel getCashWithClient(int id) {
        for(CashPanel cash:this.cashes)
        {
            for(QueueClient client:cash.getCash())
            {
                if(client.getClientId()==id)return cash;
            }
        }
        return null;
    }


    private CashPanel getShortestCashPanel() {
        int size = 1000;
        CashPanel tmp = null;
        for (CashPanel c : this.cashes) {
            if (c.getCash().size() < size) {
                tmp = c;
                size = c.getCash().size();
            }
        }
        return tmp;
    }

    private WorkerPanel getWorker(int workerId) {
        for (WorkerPanel w : this.workers) {
            if (w.getWorker().getId() == workerId) return w;
        }
        return null;
    }

    @Override
    protected String getFederationName() {
        return "GUIFederate";
    }

    @Override
    protected void inicialization() {
        /**noting*/
    }

    @Override
    protected double getAdvanceTime() {
        return 1;
    }

    public ClientPanel getClient(int clientId) {
        for (ClientPanel c : this.clients) {
            if (c.getClient().getId() == clientId) return c;
        }
        return null;
    }

    public String getProductName(int shelfId) {
        for (ShelfPanel s : this.shelves) {
            if (s.getShelf().getId() == shelfId) return s.getShelf().getProduct();
        }
        return null;
    }

    public MagazinePanel getMagazinePanel(String string) {
        for (MagazinePanel m : this.storedProducts) {
            if (m.getStoredProduct().getProduct().equals(string)) return m;
        }
        return null;
    }

    private CashPanel getCashPanel(int id) {
        for (CashPanel cashPanel : this.cashes) {
            if(cashPanel.getCash().getId()==id) return cashPanel;
        }
        return null;
    }










































}
