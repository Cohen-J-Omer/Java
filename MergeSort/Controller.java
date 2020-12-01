/**
 * Controller class is a thread manager in charge of blocking main thread until all threads finished their task.
 */

public class Controller {
    private int maxThreads;
    private int finishedThreads;


    public Controller(int m){
        maxThreads=m;
        finishedThreads=0;
    }

    // blocks main until all threads finished their task.
    public synchronized void waitForThreads(){
        //using while instead of if in the unlikely case the main thread will wake up for
        //unexpected reason.
        while (finishedThreads<maxThreads)
            try{wait();} //waits on controller
            catch(InterruptedException e){System.out.println("Interrupted while waiting");}
    }
    //keeps track of all finished threads, and wakes main thread once all threads finished.
    public synchronized void finished(){
        finishedThreads++;
        if (finishedThreads>=maxThreads) notify(); //wakes main thread which sleeps on controller.
    }
}
