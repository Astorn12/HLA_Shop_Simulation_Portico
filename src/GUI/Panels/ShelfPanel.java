package GUI.Panels;

import net.miginfocom.swing.MigLayout;
import Support.shelves.Shelf;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by osiza on 31.05.2019.
 */
public class ShelfPanel extends JPanel {

    String path="src\\images\\shelf.png";
    JLabel shelfFill;
    Shelf shelf;
    JLabel stock;
    BufferedImage myPicture = null;
    public ShelfPanel(Shelf shelf) {
        this.shelf = shelf;
        this.setLayout(new MigLayout());



        try {
            myPicture = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }






        JLabel id = new JLabel(String.valueOf(this.shelf.getId()));
        id.setFont(new Font(Font.SANS_SERIF,  Font.BOLD, 16));
        this.add(id,"wrap,center");
        JLabel product = new JLabel(String.valueOf(this.shelf.getProduct()));
        product.setFont(new Font(Font.SANS_SERIF,  Font.BOLD, 16));
        this.add(product,"wrap,center");
        stock = new JLabel(this.shelf.getFill()+" /" +this.shelf.getSize());
        stock.setFont(new Font(Font.SANS_SERIF,  Font.BOLD, 16));
        this.add(stock,"wrap,center");
    }
    public void update()
    {
        this.stock.setText(this.shelf.getFill()+" /" +this.shelf.getSize());
        this.stock.revalidate();
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

    public Shelf getShelf() {
        return shelf;
    }
}