package SupporterClasses;

import java.util.Random;

/**
 * Created by osiza on 03.04.2019.
 */
public class Uniform  implements  Generator{

    double max,min;
    Random random;
    public Uniform(double min, double max)
    {
        this.min=min;
        this.max=max;
    }

    public void  changeRange(double min, double max)
    {
        this.min=min;
        this.max=max;
    }

    @Override
    public double next() {


        return random.nextDouble()*(max-min) +min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }
}
