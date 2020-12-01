/**
 *  Repository class represents a repository of integer arrays from which the threads can withdraw from and deposit to.
 *  Repository is implemented by an ArrayList which is initially comprised of multiple integer arrays containing a single integer.
 */


import java.util.ArrayList;

public class Repository {
    private ArrayList <  int[] > repository;

    /*Repository is built by copying each randomly created integer of the integer array to a new integer array
     and adding it to the repository */
    public Repository(int [] arr){
        repository = new ArrayList< int[] >(arr.length);
        for (int num:arr) {
            repository.add(new int[]{num});
        }
    }

    /*Returns first integer array of the ArrayList or null if ArrayList is empty.
    * Synchronization is redundant since mutual exclusion is guaranteed at run method. */
    public  int[] removeArr() {
            int[] res;
            try {res = repository.remove(0);}
            catch (IndexOutOfBoundsException e) { res = null;}
            return res;
    }
    // adds new integer array to ArrayList in a synchronized manner
    public synchronized void addArr(int[] arr){
            repository.add(arr);
    }
}
