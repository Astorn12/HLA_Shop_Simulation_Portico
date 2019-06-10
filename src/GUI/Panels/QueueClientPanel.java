package GUI.Panels;

import Support.cashesandcashiers.QueueClient;
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
public class QueueClientPanel  extends JPanel {

    String path="src\\images\\janusz.png";
    QueueClient queueClient ;
    BufferedImage myPicture = null;
    JLabel id;
    JLabel amount;
    public QueueClientPanel(QueueClient queueClient) {
        this.queueClient=queueClient;
        this.setLayout(new MigLayout());
        this.setSize(50,50);


        try {
            myPicture = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

         id = new JLabel(String.valueOf(this.queueClient.getClientId()));
        id.setForeground(Color.white);
        this.add(id,"wrap");
        amount = new JLabel(String.valueOf(this.queueClient.getProductsAmount()));
        this.add(amount,"wrap");

    }
    public void update()
    {

    }

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

    public QueueClient getQueueClient() {
        return queueClient;
    }
}
