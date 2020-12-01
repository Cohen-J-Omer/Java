/**
 * Author: Omer Cohen
 * ID: 307916395
 * Program description: Concurrently sorting a Randomly initialized integer array.
 *                      Array's size and number of threads are collected from user.
 */

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        int arrSize = getInput("Please enter the random array size you would like to have sorted: ");
        int maxThreads = getInput("Please enter the number of threads you would like to sort the array: ");
        int[] randomArr = createRandomArr(arrSize);
        printArray("Random array created is: ", randomArr);
        Repository repository = new Repository(randomArr);
        Controller c = new Controller(maxThreads);
        startThreads(maxThreads, repository, c);
        c.waitForThreads(); //main thread will wait for all threads created to be finish their task.
        printArray("Sorted array is: ", repository.removeArr());
    }


    //Creates requested number of runnable MergeSort objects and start them.
    private static void startThreads(int maxThreads,Repository repository,Controller c) {
        for (int i=0; i < maxThreads; i++) {
            new Thread(new MergeSort(repository,c)).start();
        }
    }

    //Prints array along with a message to user.
    private static void printArray(String msg,int[] arr){
        System.out.println(msg + Arrays.toString(arr));
    }

    //Creates array with random number ranging for 1 to 100.
    private static int[] createRandomArr(int size){
        int[] arr = new int[size];
        SecureRandom rand = new SecureRandom();
        for(int i=0; i < size; i++)
            arr[i]=rand.nextInt(100) + 1;
        return arr;
    }

    /*Receiving single number from user with a validation check, meaning if user fails to provide number it will be
      presented with an error message allowing him to insert new input. */
    public static int getInput(String msg) {
        System.out.println(msg);
        Scanner scanner = new Scanner(System.in);
        boolean inputNotCompatible = true;
        int num = 0;
        do {
            try {
                num = Integer.parseInt(scanner.nextLine());
                inputNotCompatible = false;
            }
            catch (NumberFormatException e) {
                System.out.println("Please enter a valid single integer number. Please try again:");
            }
        }
        while (inputNotCompatible);
        return num;
    }
}
