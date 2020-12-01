/**
 *   MergeSort class represents the multiple threads sorting the randomly generated array.
 *   each thread will try to remove two integer arrays from the repository, merge them, and add them back to the repository.
 *   failing to do so will trigger the thread to return the single array it might have withdrawn and be collected by controller.
 */
public class MergeSort implements Runnable{
    private final Repository repository;
    private static Controller controller;
    private boolean done;                //personal flag for each thread, indicating whether thread finished.

    public MergeSort(Repository rep,Controller c){
        repository = rep;               //ensuring repository is shared between threads.
        MergeSort.controller=c;
        done=false;                     //will enable us to reuse the same thread until the task is over.
    }

    //run method described in class description
    public void run() {
        int[] arr1,arr2;
        while (!done) {
            synchronized (repository) { //synchronizing on a shared object
                arr1 = removeArray();
                arr2 = removeArray();
            }
            done = true;
            if (!(arr1== null && arr2 == null)) {
                if (arr1 == null)       //returns arr2 since thread hasn't been able to catch both arrays.
                    addArray(arr2);
                else if (arr2 == null)  //returns arr1 since thread hasn't been able to catch both arrays.
                    addArray(arr1);
                else {                  //both arrays are initialized
                    done = false;
                    int[] mergedArray = mergeArrays(arr1, arr2);
                    addArray(mergedArray);  // returns merged result back to repository.
                }
            }
        }
        controller.finished(); //increments, in a synchronized manner, finished threads counter
    }


    //removes array from repository using Repository's method. returns removed array or null.
    public int[] removeArray(){         //synchronized in run method
        return repository.removeArr();
    }
    //adds array to repository using Repository's method.
    public void addArray(int[] arr){    //synchronized in method addArr of Repository
         repository.addArr(arr);
    }

    //Merges two sorted arrays
    private int[] mergeArrays(int[] arr1, int[] arr2) {
        int size=arr1.length + arr2.length, i1 = 0, i2 = 0;
        int[] mergedArray = new int[size];

        for(int j = 0; j < size; j++)
        {//Both arrays can't be empty at the same iteration, therefore such condition isn't inspected
            if(i1 < arr1.length){       //checks whether arr1 is empty
                if(i2 < arr2.length){   //checks whether arr2 is empty
                    if(arr1[i1]< arr2[i2]){
                        mergedArray[j] = arr1[i1];
                        i1++;
                    }
                    else {
                        mergedArray[j] = arr2[i2];
                        i2++;
                    }
                }
                else { // arr2 is depleted
                    mergedArray[j] = arr1[i1];
                    i1++;
                }
            }
            else { // arr1 is depleted
                mergedArray[j] = arr2[i2];
                i2++;
            }
        }
        return mergedArray;
    }
}
