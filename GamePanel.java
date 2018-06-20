package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JPanel;


public class GamePanel extends JPanel
  implements Runnable, KeyListener
{
  public static final int WIDTH = 400;
  public static final int HEIGHT = 400;
  private Graphics2D g2d;
  private BufferedImage image;
  private Thread thread;
  private boolean running;
  private long targetTime;
  private final int SIZE = 10;
  
  private Entity head;
  
  private Entity apple;
  
  private ArrayList<Entity> snake;
  private int score;
  private int level;
  private boolean gameover;
  
  public GamePanel()
  {
    setPreferredSize(new Dimension(400, 400));
    setFocusable(true);
    requestFocus();
    addKeyListener(this);
  }
  
  public void addNotify() {
    super.addNotify();
    thread = new Thread(this);
    thread.start();
  }
  
  private void setFPS(int fps) { targetTime = (1000 / fps); }
  
  public void keyPressed(KeyEvent e)
  {
    int k = e.getKeyCode();
    
    if (k == 38) up = true;
    if (k == 40) down = true;
    if (k == 37) left = true;
    if (k == 39) right = true;
    if (k == 10) start = true;
  }
  
  public void keyReleased(KeyEvent e)
  {
    int k = e.getKeyCode();
    
    if (k == 38) up = false;
    if (k == 40) down = false;
    if (k == 37) left = false;
    if (k == 39) right = false;
    if (k == 10) start = false;
  }
  
  private int dx;
  private int dy;
  private boolean up;
  private boolean down;
  private boolean right;
  private boolean left;
  private boolean start;
  public void keyTyped(KeyEvent arg0) {}
  
  public void run() {
    if (running) return;
    init();
    


    while (running) {
      long startTime = System.nanoTime();
      
      update();
      requestRender();
      

      long elapsed = System.nanoTime() - startTime;
      long wait = targetTime - elapsed / 1000000L;
      if (wait > 0L) {
        try {
          Thread.sleep(wait);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }
  
  private void init()
  {
    image = new BufferedImage(400, 400, 2);
    g2d = image.createGraphics();
    running = true;
    setUplevel();
  }
  
  private void setUplevel()
  {
    snake = new ArrayList<>();
    head = new Entity(10);
    head.setPosition(200, 200);
    snake.add(head);
    for (int i = 1; i < 3; i++) {
      Entity e = new Entity(10);
      e.setPosition(head.getX() + i * 10, head.getY());
      snake.add(e);
    }
    apple = new Entity(10);
    setApple();
    score = 0;
    gameover = false;
    level = 1;
    dx = (this.dy = 0);
    setFPS(level * 10);
  }
  
  public void setApple() { int x = (int)(Math.random() * 390.0D);
    int y = (int)(Math.random() * 390.0D);
    x -= x % 10;
    y -= y % 10;
    apple.setPosition(x, y);
  }
  
  private void requestRender() { render(g2d);
    Graphics g = getGraphics();
    g.drawImage(image, 0, 0, null);
    g.dispose();
  }
  
  private void update()
  {
    if (gameover) {
      if (start) {
        setUplevel();
      }
      return;
    }
    if ((up) && (dy == 0)) {
      dy = -10;
      dx = 0;
    }
    
    if ((down) && (dy == 0)) {
      dy = 10;
      dx = 0;
    }
    if ((left) && (dx == 0)) {
      dy = 0;
      dx = -10;
    }
    
    if ((right) && (dx == 0) && (dy != 0)) {
      dy = 0;
      dx = 10;
    }
    

    if ((dx != 0) || (dy != 0)) {
      for (int i = snake.size() - 1; i > 0; i--)
      {
        ((Entity)snake.get(i)).setPosition(
          ((Entity)snake.get(i - 1)).getX(), 
          ((Entity)snake.get(i - 1)).getY());
      }
      
      head.move(dx, dy);
    }
    
    for (Entity e : snake) {
      if (e.isCollision(head)) {
        gameover = true;
        break;
      }
    }
    if (apple.isCollision(head)) {
      score += 1;
      setApple();
      
      Entity e = new Entity(10);
      e.setPosition(-100, -100);
      snake.add(e);
      if (score % 10 == 0) {
        level += 1;
        if (level > 10) level = 10;
        setFPS(level * 10);
      }
    }
    
    if (head.getX() < 0) head.setX(400);
    if (head.getY() < 0) head.setY(400);
    if (head.getX() > 400) head.setX(0);
    if (head.getY() > 400) head.setY(0);
  }
  
  public void render(Graphics2D g2d) {
    g2d.clearRect(0, 0, 400, 400);
    

    g2d.setColor(Color.GREEN);
    for (Entity e : snake) {
      e.render(g2d);
    }
    
    g2d.setColor(Color.RED);
    apple.render(g2d);
    if (gameover) {
      g2d.drawString("GameOver!", 150, 200);
    }
    

    g2d.setColor(Color.WHITE);
    g2d.drawString("Score : " + score + "level :" + level, 10, 10);
    if ((dx == 0) && (dy == 0)) {
      g2d.drawString("Ready!", 150, 200);
    }
  }
}