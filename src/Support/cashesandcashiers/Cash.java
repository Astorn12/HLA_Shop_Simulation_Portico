package Support.cashesandcashiers;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by osiza on 06.06.2019.
 */
public class Cash extends LinkedList<QueueClient> {
    int id;
    boolean open;

    public Cash(Collection<? extends QueueClient> c) {
        super(c);
        this.open =false;
    }

    public Cash(int id) {
        super();
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public void removeClient(int id) {
        for(int i=0;i<this.size();i++)
        {
            if(this.get(i).getClientId()==id)
            {
                this.remove(i);
                break;
            }
        }
    }
}
