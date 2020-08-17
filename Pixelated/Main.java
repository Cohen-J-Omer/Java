/**
 * Author: Omer Cohen
 * ID: 307916395
 * Program description: Prompting users with a matrix in a size of their choosing to color black, pixel by pixel, at they see fit.
 *                      When users are done, they'll choose the number of threads that will nibble at the matrix
 *                      and the number of rounds each will go before terminating.
 */

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Nibble Away");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650, 650);
        frame.add(new PixelatedImg());
        frame.setVisible(true);
    }
}
