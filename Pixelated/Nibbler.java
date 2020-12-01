/**
 * Class Nibbler represents the multiple threads nibbling away at the black tiles of the matrix.
 * A white tile will remain white at all times. A black tile will remain black if all of his neighbors (up to 8) are also black.
 */

import javax.swing.*;
import java.awt.*;

public class Nibbler implements Runnable {
    private static JButton[][] originalBoard; //All threads share the same originalBoard
    private static boolean[][] tempBoard;   // All threads share the same tempBoard to keep changes done based on originalBoard
    private static Controller controller;  // All threads share the same controller provided through the constructor
    private static int size,rounds;        //All threads share the same size of matrix provided and number of rounds they're required to go through said matrix
    private int beginning,end;      //each thread has it's own row to start from and to end in.
    private final int SLEEP=1,WAIT=0;


    public Nibbler(JButton[][] originalBoard,boolean[][] tempBoard,Controller controller,int rounds,int size,int beginning,int end){
        Nibbler.tempBoard=tempBoard;
        Nibbler.originalBoard = originalBoard;
        Nibbler.controller=controller;
        this.beginning = beginning;
        this.end = end;
        Nibbler.rounds=rounds;
        Nibbler.size=size;

    }

    public void run(){

        for(int r = 0 ; r < rounds ; r++ ){  //r for rounds that user has given as input
            for (int i = beginning ; i < end ; i++){ //each thread has it's own section of rows to nibble through
                for (int j = 0 ; j < size ; j++){    //each thread goes through the whole row it's in charge of
				    if((originalBoard[i][j].getBackground()).equals(Color.black))//no use in checking neighbors if current cell is white
				        tempBoard[i][j]= checkNeighbors(i, j) ;
			    }
		    }
            controller.barrier(WAIT); // Wait for all threads to finish reading from originalBoard before updating it
            update(beginning, end);
        }
    }


    //update original board to display changes to user and reset temp board
    private void update(int beginning,int end){
        for (int i = beginning ; i < end ; i++) //updates originalBoard
            for (int j = 0 ; j < size ; j++){
                originalBoard[i][j].setBackground(tempBoard[i][j] ? Color.black:Color.white );
                tempBoard[i][j]=false; //Resetting tempBoard to false(==white tiles) towards next round, since all threads finished reading from it.
            }
        controller.barrier(SLEEP);//Waiting for all threads to finish last round tasks and initiating requested delay.

    }



    /*inspecting provided tile's neighbors. A black tile will remain black if all of his neighbors (up to 8) are also black.
    * returns false(=white tile) if at least one his neighbors is a white tile, otherwise returns true. */
    private boolean checkNeighbors(int row,int column){
        if((row!=0 && originalBoard[row-1][column].getBackground()==Color.white)|| //Checks cell above

         (row!=size-1 && originalBoard[row+1][column].getBackground()==Color.white)|| //Checks cell below

         (column!=0 && originalBoard[row][column-1].getBackground()==Color.white)|| //Checks cell to the left

         (column!=size-1 && originalBoard[row][column+1].getBackground()==Color.white)|| //Checks cell to the right

         (row!=0 && column!=0 && originalBoard[row-1][column-1].getBackground()==Color.white)|| //Checks upper left cell

         (row!=0 && column!=size-1 && originalBoard[row-1][column+1].getBackground()==Color.white)|| //Checks upper right cell

         (row!=size-1 && column!=0 && originalBoard[row+1][column-1].getBackground()==Color.white)|| //Checks lower left cell

         (row!=size-1 && column!=size-1 && originalBoard[row+1][column+1].getBackground()==Color.white)) //Checks lower right cell

            return false;

        return true;    //if none of the adjacent tiles of the provided tile is white return true(=black)
    }


}

