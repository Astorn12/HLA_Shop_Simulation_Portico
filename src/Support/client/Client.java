package Support.client;

import hla.rti.LogicalTime;
import hla.rti.RTIambassador;

/**
 * Created by osiza on 03.04.2019.
 */
public class Client {
    int id;

    ShoppingList shoppingList;
    ClientState state;
    boolean waitingForProduct;
    public Client(int id) {
        this.id = id;
        this.state = ClientState.SHOOPING;
        this.shoppingList= new ShoppingList();
        log();
        this.waitingForProduct=false;
    }

    //public ShoppingList getShoppingList() {
      //  return shoppingList;
    //}

    public void setShoppingList(ShoppingList shoppingList) {
        this.shoppingList = shoppingList;
    }

    public ClientState getState() {
        return state;
    }

    public void setState(ClientState state) {
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }






    public void log()
    {
        System.out.println( "Client "+this.id+ "  " +this.state+"\n"+this.shoppingList.toString());
    }

    public boolean isFull()
    {
        return this.shoppingList.isFull();
    }
    public LProduct getUnCheckedProduct()
    {
        return this.shoppingList.getUnCheckedProduct();
    }

    public void broadcast(RTIambassador rtiamb, LogicalTime logicalTime, int id) {
        this.shoppingList.broadcast(rtiamb,logicalTime,id);
    }

    public void fillProduct(String productName, int amount) {
        this.shoppingList.fillProduct(productName,amount);
        if(this.isFull())this.state=ClientState.WAITINGFORQUEUE;
        this.waitingForProduct=false;
    }

    public ShoppingList getShoppingList() {
        return this.shoppingList;
    }

    public void setWaitingForProduct(boolean waitingForProduct) {
        this.waitingForProduct = waitingForProduct;
    }

    public boolean isWaitingForProduct() {
        return waitingForProduct;
    }

    public int getSigma() {
       return shoppingList.getSigma();
    }
}
