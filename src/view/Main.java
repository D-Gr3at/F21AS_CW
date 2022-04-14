package view;

import java.io.IOException;

public class Main{

    /*Entry method to the Program*/
    public static void main(String[] args) {
        try {
            new Gui().setVisible(true);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
