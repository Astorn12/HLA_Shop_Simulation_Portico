package Support.workers;

/**
 * Created by osiza on 01.06.2019.
 */
public class Order {
    String product;
    int amount;

    public Order(String product, int amount) {
        this.product = product;
        this.amount = amount;
    }

    public String getProduct() {
        return product;
    }

    public int getAmount() {
        return amount;
    }
}
