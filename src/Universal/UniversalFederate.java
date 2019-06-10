package Universal;

import Interractions.*;
import SimulationLogic.Interraction;
import SimulationLogic.InterractionObserver;
import SimulationLogic.InterractionRecognizer;
import SupporterClasses.TimeConverter;
import hla.rti.*;
import hla.rti.jlc.RtiFactoryFactory;
import org.portico.impl.hla13.types.DoubleTime;
import org.portico.impl.hla13.types.DoubleTimeInterval;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by osiza on 03.06.2019.
 */
public abstract class UniversalFederate implements InterractionObserver {

    public static final String READY_TO_RUN = "ReadyToRun";

    protected RTIambassador rtiamb;
    protected UniversalAmbassador fedamb;
    Random random;

    protected List<Interraction> receiveBuffor;
    protected  List<Interraction> sendBuffor;

    public UniversalFederate() {
        this.random= new Random();
        this.receiveBuffor=new LinkedList<>();
        this.sendBuffor= new LinkedList<>();
    }







    protected abstract void publishAndSubscribe() throws RTIexception;

    public void runFederate() throws RTIexception {

        /**Tworzeni rtiamb recognizera*/
        rtiamb = RtiFactoryFactory.getRtiFactory().createRtiAmbassador();

        try
        {
            File fom = new File( "shop.fed" );
            rtiamb.createFederationExecution( "ShopFederation",
                    fom.toURI().toURL() );
            log( "Created Federation" );
        }

        catch( FederationExecutionAlreadyExists exists )
        {
            log( "Didn't create federation, it already existed" );
        }
        catch( MalformedURLException urle )
        {
            log( "Exception processing fom: " + urle.getMessage() );
            urle.printStackTrace();
            return;
        }

        fedamb = new UniversalAmbassador(this);
        rtiamb.joinFederationExecution( getFederationName(), "ShopFederation", fedamb );
        log( "Joined Federation as "+ getFederationName());

        rtiamb.registerFederationSynchronizationPoint( READY_TO_RUN, null );

        while( fedamb.isAnnounced == false )
        {
            rtiamb.tick();
        }

        waitForUser();

        rtiamb.synchronizationPointAchieved( READY_TO_RUN );
        log( "Achieved sync point: " +READY_TO_RUN+ ", waiting for federation..." );
        while( fedamb.isReadyToRun == false )
        {
            rtiamb.tick();
        }

        enableTimePolicy();

        publishAndSubscribe();





        advanceTime(1);
        inicialization();
        sendReadyInterractions();
        advanceTime(1);
        reactionForInterraction();
        while(fedamb.running) {

            double d= getAdvanceTime();
            advanceTime(d);
            //fedamb.federateTime=d;
            mainLoopInnerAction();

            reactionForInterraction();

            sendReadyInterractions();


            rtiamb.tick();
        }


    }



    private void waitForUser()
    {
        log( " >>>>>>>>>> Press Enter to Continue <<<<<<<<<<" );
        BufferedReader reader = new BufferedReader( new InputStreamReader(System.in) );
        try
        {
            reader.readLine();
        }
        catch( Exception e )
        {
            log( "Error while waiting for user input: " + e.getMessage() );
            e.printStackTrace();
        }
    }

    private void enableTimePolicy() throws RTIexception
    {
        LogicalTime currentTime = convertTime( fedamb.federateTime );
        LogicalTimeInterval lookahead = convertInterval( fedamb.federateLookahead );

        this.rtiamb.enableTimeRegulation( currentTime, lookahead );

        while( fedamb.isRegulating == false )
        {
            rtiamb.tick();
        }

        this.rtiamb.enableTimeConstrained();

        while( fedamb.isConstrained == false )
        {
            rtiamb.tick();
        }
    }
    protected void advanceTime(double timestep) throws RTIexception
    {
        log("requesting time advance for: " + timestep);

        fedamb.isAdvancing = true;
        LogicalTime newTime = convertTime( fedamb.federateTime + timestep );
        rtiamb.timeAdvanceRequest( newTime );
        while( fedamb.isAdvancing)
        {
            rtiamb.tick();

        }

    }

    protected double randomTime() {
        Random r = new Random();
        return 1 +(4 * r.nextDouble());
    }

    protected LogicalTime convertTime( double time )
    {
        return new DoubleTime( time );
    }

    /**
     * Same as for {@link #convertTime(double)}
     */
    protected LogicalTimeInterval convertInterval( double time )
    {
        // PORTICO SPECIFIC!!
        return new DoubleTimeInterval( time );
    }

    protected void log( String message )
    {
        System.out.println( getFederationName()+"   : " + message );
    }
    @Override
    public void update(int interractionClass, LogicalTime theTime, ReceivedInteraction theInterraction) {
        Interraction interraction=InterractionRecognizer.recognize(interractionClass,theTime,theInterraction,this.rtiamb);
        log("RECEIVE: \n");
        interraction.log();
        if(interraction.getClass().equals(Finish.class)) {
            System.exit(0);
        }
        this.receiveBuffor.add(interraction);
    }


    public LogicalTime getShortestTime(LogicalTime time)
    {
        LogicalTime tmp= TimeConverter.convertTime(1000000);
        for(Interraction i: this.receiveBuffor)
        {
            if(i.getTime().isGreaterThan(time)&&i.getTime().isLessThan(tmp) )
            {
                tmp=i.getTime();
            }
        }
        return tmp;
    }

    private void reactionForInterraction() throws SaveInProgress, InteractionClassNotPublished, NameNotFound, RestoreInProgress, InvalidFederationTime, InteractionParameterNotDefined, ConcurrentAccessAttempted, FederateNotExecutionMember, RTIinternalError, InteractionClassNotDefined {
        List<Interraction> fullFilled= new LinkedList<>();
            Collections.sort(this.receiveBuffor,Interraction::compare);

            for(Interraction i: receiveBuffor)
            {
                System.out.println(i.getTime()+"\n");
            }
            for(Interraction interraction : this.receiveBuffor)
            {
                reaction(interraction);
                fullFilled.add(interraction);

            }
            this.receiveBuffor.removeAll(fullFilled);
            fullFilled.clear();
    }

    protected abstract void reaction(Interraction interraction);

    public void sendReadyInterractions() throws SaveInProgress, InteractionClassNotPublished, NameNotFound, RestoreInProgress, InvalidFederationTime, InteractionParameterNotDefined, ConcurrentAccessAttempted, FederateNotExecutionMember, RTIinternalError, InteractionClassNotDefined {
        List<Interraction> interractionsToRemove= new LinkedList<>();
        for(Interraction interraction :this.sendBuffor)
        {
            log("SEND:\n");
                interraction.sendInterraction(this.rtiamb);
                interractionsToRemove.add(interraction);
        }

        this.sendBuffor.removeAll(interractionsToRemove);
    }

    protected abstract String getFederationName();

    protected void mainLoopInnerAction()
    {

    }
    protected abstract void inicialization();
    protected abstract double getAdvanceTime();


}
