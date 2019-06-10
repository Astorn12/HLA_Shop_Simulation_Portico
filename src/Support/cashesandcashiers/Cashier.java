package Support.cashesandcashiers;

import java.util.Random;

/**
 * Created by osiza on 06.06.2019.
 */
public class Cashier {
    int id;
    CashierState state;

    int cashId;
    double servingSkill;
    public Cashier(int id) {
        this.id = id;
        this.state = CashierState.WAITING;
        this.servingSkill=randomServingSkill();
    }

    private double randomServingSkill() {
        Random r= new Random();
        return 0.8+(double)r.nextInt(5)*0.1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CashierState getState() {
        return state;
    }

    public void setState(CashierState state) {
        this.state = state;
    }

    public int getCashId() {
        return cashId;
    }

    public void setCashId(int cashId) {
        this.cashId = cashId;
    }

    public double getServingTime(int amount)
    {
        return 1.0+((double)amount)*0.1*this.servingSkill;
    }
}
