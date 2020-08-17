/**
    PixelatedImg class is in charge of the Graphic interface to interact with the user,
    Collecting relevant input from user, Calculating workload between threads and finally starting them.
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PixelatedImg extends JPanel {
    private JButton cmdGo;
    private JButton cmdClear;
    private JButton[][] pixels;
    private int pixelsPerLine;

    public PixelatedImg() {

        cmdClear = new JButton("Clear");
        cmdGo = new JButton("Go");
        cmdGo.addActionListener(new ButtonListener());
        cmdClear.addActionListener(new ButtonListener());
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(cmdGo);
        buttonsPanel.add(cmdClear);

        pixelsPerLine = getInput("Enter number of pixels");

        pixels=new JButton[pixelsPerLine][pixelsPerLine];
        JPanel pixelPanel = new JPanel();
        pixelPanel.setLayout(new GridLayout(pixelsPerLine, pixelsPerLine));

        //Each cell of the matrix will be a white colored button
        for (int i = 0; i < pixelsPerLine; i++)
            for (int j = 0; j < pixelsPerLine; j++) {
                pixels[i][j] = new JButton();
                pixels[i][j].setBackground(Color.white);
                pixels[i][j].addActionListener(new PixelListener());
                pixelPanel.add(pixels[i][j]);
            }

        JPanel backGroundPanel = new JPanel();
        backGroundPanel.setLayout(new BorderLayout());
        backGroundPanel.add(pixelPanel, BorderLayout.CENTER);
        backGroundPanel.add(buttonsPanel, BorderLayout.SOUTH);
        backGroundPanel.setPreferredSize(new Dimension(500,500));
        add(backGroundPanel);
    }

    /*Receiving single number from user with a validation check, meaning if user fails to provide number it will be
      presented with an error message allowing him to insert new input. */
    private static int getInput(String msg) {
        System.out.println(msg);
        boolean inputNotCompatible = true;
        int num = 0;
        do {
            try {
                num = Integer.parseInt( JOptionPane.showInputDialog(msg));
                inputNotCompatible = false;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,"Please enter a valid single integer number. Please try again:","Error",JOptionPane.ERROR_MESSAGE);
            }
        }
        while (inputNotCompatible);
        return num;
    }

    private class ButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            if (e.getSource()==cmdGo){
                int maxThreads = getInput("Please enter the number of threads you would like to nibble away at the matrix:") ;
                int rounds = getInput("Please enter the number of rounds you would like the threads to go over the matrix: ") ;
                maxThreads= Math.min(maxThreads, pixelsPerLine); //using more threads than number of rows in matrix is considered redundant.
                boolean[][] tempBoard= new boolean[pixelsPerLine][pixelsPerLine]; //initialized automatically to false which represents a white tile.
                Controller controller = new Controller(maxThreads);

                int ration = (int) Math.ceil((double)pixelsPerLine/maxThreads);
                int beginning=0;
                int end;
                int remainingLines=pixelsPerLine;
                boolean fixedRation = true; //as long as fixedRation is true, each thread will receive ration calculated above.

                for (int remainingThreads=maxThreads ;remainingThreads>0 ;remainingThreads--) {
                    //checks if handing current thread the fixed ration will leave thread(s) without a single line to nibble through
                    if ( fixedRation && ( (double)(remainingLines-ration)/(remainingThreads-1) ) >= 1)
                        remainingLines -= ration;

                    else if(remainingThreads == 1)  //last thread receives remaining lines.
                        ration=remainingLines;

                    else{                 //current thread isn't last, but we can't divide remainingLines between remaining threads using fixed ration
                        fixedRation=false;
                        ration=1;
                        remainingLines -= 1;
                    }
                    end=beginning+ration;
                    new Thread(new Nibbler(pixels,tempBoard,controller,rounds,pixelsPerLine,beginning,end)).start();
                    beginning=end;
                }


                }//end of cmdGo

            else{              //user requests to clear image
                for (int i=0 ; i <pixelsPerLine ; i++ )
                    for (int j=0 ; j <pixelsPerLine ; j++ )
                        pixels[i][j].setBackground(Color.white);
            }// end of cmdClear
        }
    }//end of ButtonListener

    //switches the color of a cell from white to black or vice versa
    private class PixelListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {  //user pressed one of the buttons of the matrix.
            JButton button= (JButton) e.getSource();
            if (button.getBackground() == Color.white)
                button.setBackground(Color.black);
            else
                button.setBackground(Color.white);
        }
    }




}//end of PixelatedImg class



