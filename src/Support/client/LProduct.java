package Support.client;

/**
 * Created by osiza on 05.06.2019.
 */
public class LProduct {
    String product;
    int amount;
    int fill;
    boolean checked;

    public LProduct(String product, int amount) {
        this.product = product;
        this.amount = amount;
        this.fill=0;
        this.checked=false;
    }

    public int getAmount() {
        return amount;
    }

    public String getProduct() {
        return product;
    }

    public void fill(int n)
    {

        this.fill+=n;
        this.checked=true;
    }

    @Override
    public String toString()
    {
        return "Product: "+ this.product+ " "+this.fill+"/"+this.amount;
    }

    public boolean isFull()
    {
       // if(this.fill==this.amount) return true;
       // else return false;
        return this.checked;
    }

    public int getFill() {
        return fill;
    }

    public void checked() {
        this.checked=true;
    }
}
