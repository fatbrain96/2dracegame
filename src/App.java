import javax.swing.*;


public class App {
    public static void main(String[] args) throws Exception {
        

        int bheight = 1920;
        int bwidth = 1080;

        JFrame Frame=  new JFrame("Danish Bhai Reborn");
        Frame.setResizable(true);
        Frame.setDefaultCloseOperation(Frame.EXIT_ON_CLOSE);
        Frame.setSize(bwidth,bheight);
        Frame.setLocationRelativeTo(null);
       // Frame.setVisible(true);
        Frame.setLocationRelativeTo(null);
        Danish danish = new Danish();
        Frame.add(danish);
        Frame.pack();
        Frame.setLocationRelativeTo(null);
        danish.requestFocus();
        Frame.setVisible(true);
    }
}
