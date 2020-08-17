/**
 * Controller class is a thread manager in charge of keeping track of threads that finished their current task and
 * blocking them, (aside from the last one) until all threads are ready to continue.
 */

public class Controller {
    private final int maxThreads;
    private int finished;
    private final int SLEEP=1;

    public Controller(int m){
        maxThreads=m;
    }

    /*Blocks all threads aside from the last one which will wake up the rest.
    * Inserting the "SLEEP" input will trigger the last thread to create a short delay in addition. */
    public synchronized void barrier(int action) {
        finished++;
        if (finished >= maxThreads) { //  protects originalBoard until all threads finished reading it .
            finished=0;
            if (action==SLEEP){ //last thread will sleep, thus initiating requested delay
                try { Thread.sleep(1500);}
                catch (InterruptedException e) { System.out.println("Interrupted while sleeping");}
            }
            notifyAll();
        }
        else{
            try { wait(); }          //threads waiting for last thread to wake them up
            catch (InterruptedException e) { System.out.println("Interrupted while waiting");}
        }

    }





}




