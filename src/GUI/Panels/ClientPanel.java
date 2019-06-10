package GUI.Panels;

import Support.client.Client;
import Support.client.LProduct;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

/**
 * Created by osiza on 05.06.2019.
 */
public class ClientPanel extends JPanel {

    String path="src\\images\\janusz.png";
  Client client;
   // BufferedImage myPicture = null;
    JLabel state;
    JPanel shoopingBasket;
    ImagePanel janusz;
    JPanel informations;
    public ClientPanel(Client client) {
        this.client=client;
        this.informations= new JPanel();
        this.informations.setLayout(new MigLayout());
        this.setLayout(new MigLayout());


         janusz= new ImagePanel(path);
        this.add(janusz,"w 100!, h 200!");
        janusz.revalidate();

        this.add(informations);


        JLabel id = new JLabel("id "+String.valueOf(this.client.getId()));
        informations.add(id,"wrap");
        shoopingBasket= new JPanel();
        shoopingBasket.setLayout(new MigLayout());


        shoppingBasketShow();
        state = new JLabel(String.valueOf(this.client.getState()));
        informations.add(state,"wrap");
        informations.add(shoopingBasket);

    }

    private void shoppingBasketShow()
    {
        for(LProduct p: client.getShoppingList())
        {
            shoopingBasket.add(new JLabel(p.getProduct()+":  "+p.getFill()+"/"+p.getAmount()),"wrap");
        }
    }
    public void update()
    {
        this.state.setText(client.getState().toString());

        this.shoopingBasket.removeAll();
        shoppingBasketShow();
        this.state.revalidate();
    }
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
