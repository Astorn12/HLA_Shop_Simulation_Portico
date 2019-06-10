package GUI.Panels;

import Support.cashesandcashiers.Cashier;
import Support.cashesandcashiers.QueueClient;
import Support.client.Client;
import Support.client.LProduct;
import net.miginfocom.swing.MigLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by osiza on 06.06.2019.
 */
public class CashierPanel extends JPanel {

    String path="src\\images\\cashier.png";
    Cashier cashier;
    BufferedImage myPicture = null;
    JLabel id;
  //  JLabel state;
    public CashierPanel(Cashier cashier) {
        this.cashier=cashier;
        this.setLayout(new MigLayout());

        try {
            myPicture = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        id = new JLabel(String.valueOf(this.cashier.getId()));
        this.add(id,"wrap");
       // state = new JLabel(String.valueOf(this.cashier.getState().toString()));
       // this.add(state,"wrap");

    }
    /*public void update()
    {
        this.state.setText(cashier.getState().toString());
    }*/

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        g.drawImage(resize(myPicture,this.getWidth(),this.getHeight()), 0, 0, null);
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    public Cashier getCashier() {
        return cashier;
    }
}