package Support.cashesandcashiers;

/**
 * Created by osiza on 06.06.2019.
 */
public class QueueClient {
    int clientId;
    int productsAmount;

    public QueueClient(int clientId, int productsAmount) {
        this.clientId = clientId;
        this.productsAmount = productsAmount;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getProductsAmount() {
        return productsAmount;
    }

    public void setProductsAmount(int productsAmount) {
        this.productsAmount = productsAmount;
    }
}
