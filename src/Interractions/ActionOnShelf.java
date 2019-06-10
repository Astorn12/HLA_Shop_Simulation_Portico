package Interractions;

import SimulationLogic.Interraction;
import hla.rti.ArrayIndexOutOfBounds;
import hla.rti.LogicalTime;
import hla.rti.ReceivedInteraction;
import hla.rti.jlc.EncodingHelpers;

/**
 * Created by osiza on 03.04.2019.
 */
public abstract class ActionOnShelf extends Interraction {

    int shelfId;
    int amount;



    public ActionOnShelf(int shelfId,int amount, LogicalTime time) {
        super(time);
        this.shelfId=shelfId;
        this.amount=amount;
    }

    public ActionOnShelf(LogicalTime time,ReceivedInteraction ri) throws ArrayIndexOutOfBounds {
        super(time);
        fillWithInterraction(ri);
    }

    @Override
    public void fillWithInterraction(ReceivedInteraction interaction) throws ArrayIndexOutOfBounds
    {

        this.shelfId = EncodingHelpers.decodeInt(interaction.getValue(0));
        this.amount= EncodingHelpers.decodeInt(interaction.getValue(1));
    }


    public int getShelfId() {
        return shelfId;
    }

    public void setShelfId(int shelfId) {
        this.shelfId = shelfId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}
