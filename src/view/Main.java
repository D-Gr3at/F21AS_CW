package view;

import java.io.IOException;

public class Main{

    public static void main(String[] args) {
        try {
            new Gui().setVisible(true);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
