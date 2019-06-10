package GUI.Panels;

import Support.magazine.StoredProduct;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

/**
 * Created by osiza on 05.06.2019.
 */
public class MagazinePanel extends JPanel {

    String path="src\\images\\";
    StoredProduct storedProduct;
    ImagePanel productImage;
    JLabel amount;

    public MagazinePanel(StoredProduct storedProduct) {
        this.storedProduct=storedProduct;
        this.productImage= new ImagePanel(path+storedProduct.getProduct()+".png");
        this.setLayout(new MigLayout());



        this.add(productImage,"w 100!, h 200!,cell 0 0 0 1");
        JLabel name= new JLabel(this.storedProduct.getProduct());
        this.add(name,"cell 1 0");
        this.amount= new JLabel(this.storedProduct.getAmount()+"");
        this.add(amount,"cell 1 1");
    }


    public void update()
    {
        this.amount.setText(this.storedProduct.getAmount()+"");
    }

    public StoredProduct getStoredProduct() {
        return storedProduct;
    }

    public void setStoredProduct(StoredProduct storedProduct) {
        this.storedProduct = storedProduct;
    }

}
