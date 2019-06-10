package GUI.Panels;

import net.miginfocom.swing.MigLayout;
import Support.workers.Worker;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by osiza on 02.06.2019.
 */
public class WorkerPanel extends JPanel {

    String path="src\\images\\worker.png";
    Worker worker ;
    BufferedImage myPicture = null;
    JLabel state;
    public WorkerPanel(Worker worker) {
       this.worker=worker;
        this.setLayout(new MigLayout());



        try {
            myPicture = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JLabel id = new JLabel(String.valueOf(this.worker.getId()));
        id.setFont(new Font(Font.SANS_SERIF,  Font.BOLD, 24));
        this.add(id,"wrap");
       state = new JLabel(String.valueOf(this.worker.getState()));
       state.setFont(new Font(Font.SANS_SERIF,  Font.BOLD, 18));
        this.add(state,"wrap");

    }
    public void update()
    {
        this.state.setText(worker.getState().toString());
        this.state.revalidate();
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

    public Worker getWorker() {
        return worker;
    }
}
