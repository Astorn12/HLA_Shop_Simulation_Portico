package Support.magazine;

/**
 * Created by osiza on 01.06.2019.
 */
public class StoredProduct{
    String product;
    int amount;

    public StoredProduct(String product, int amount) {
        this.product = product;
        this.amount = amount;
    }

    public void decreaseStock(int n)
    {
        if(isAvaillable(n))this.amount-=n;
        else try {
            throw new NotEnoughtProductException();
        } catch (NotEnoughtProductException e) {
            e.printStackTrace();
        }
    }

    public void increaseStock(int n)
    {
        this.amount+=n;
    }

    public boolean isAvaillable(int  n)
    {
        if(this.amount>=n) return  true;
        else return false;
    }



    public int getAmount() {
        return amount;
    }

    public String getProduct() {
        return product;
    }

    public int tryToTake(int n) {
        if(isAvaillable(n))
        {
            decreaseStock(n);
            return n;
        }
        else
        {
            int tmp=this.amount;
            this.amount=0;
            return tmp;

        }


    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
