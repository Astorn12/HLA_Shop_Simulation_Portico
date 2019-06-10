package GUI.Panels;

import Support.cashesandcashiers.Cash;
import Support.cashesandcashiers.QueueClient;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Created by osiza on 06.06.2019.
 */
public class CashPanel extends JPanel {



    Cash cash;
    JLabel id;
    CashierPanel cashierPanel;
    JPanel chair;

    JPanel queue;
    public CashPanel(Cash cash) {
        this.cash=cash;

        this.setLayout(new MigLayout());
       queue= new JPanel();
       queue.setLayout(new MigLayout());
       queue.setBackground(Color.pink);
       //this.setSize(300,300);

        for(QueueClient q: this.cash)
        {
            QueueClientPanel p= new QueueClientPanel(q);
            queue.add(p,"w 100!, h 100!");
        }

        this.add(queue,"w 150!,h 100!");

        chair=new JPanel();
        chair.setLayout(new MigLayout());
        this.add(chair,"w 100!, h 100!");




        id = new JLabel(String.valueOf(this.cash.getId()));
        this.add(id);

    }
    public void update()
    {

        System.out.println("Usunięto wszystkie");
        queue.removeAll();
        queue.revalidate();
        for(QueueClient q: this.cash)
        {
            System.out.println("W kolejce kient "+ q.getClientId());
            QueueClientPanel p= new QueueClientPanel(q);
            queue.add(p);
        }
        //if(cashierPanel!=null)
        //cashierPanel.update();

    }

    public Cash getCash() {
        return cash;
    }

    public CashierPanel getCashierPanel() {
        return cashierPanel;
    }

    public void setCashierPanel(CashierPanel cashierPanel) {
        this.cashierPanel = cashierPanel;

        this.chair.removeAll();
        chair.setBackground(Color.red);
        this.chair.add(cashierPanel,"w 100!, h 100!");
    }
}
