package SupporterClasses;

import hla.rti.LogicalTime;
import org.portico.impl.hla13.types.DoubleTime;

/**
 * Created by osiza on 28.05.2019.
 */
public class TimeConverter {

    public static LogicalTime convertTime(double time)
    {
        return new DoubleTime( time );
    }
}
