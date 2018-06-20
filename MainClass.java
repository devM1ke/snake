package game;

import javax.swing.JFrame;

public class MainClass{
	
  public MainClass() {}
  
  public static void main(String[] args) {
    JFrame frame = new JFrame("Snake");
    frame.setContentPane(new GamePanel());
    frame.setDefaultCloseOperation(3);
    frame.setResizable(false);
    frame.pack();
    
    frame.setPreferredSize(new java.awt.Dimension(400, 400));
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
}
