import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;


public class GamePanel extends JPanel implements ActionListener {
 //First declare items that is needed for the game
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 30; //how big we want the items in this game
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 90;

    //create two arrays
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];

    int bodyParts = 6; //body part of the snake
    int applesEaten;
    int appleX; //x - corrdination
    int appleY;
    char direction = 'R'; //R = right
    boolean running = false;
    Timer timer;
    Random random;


    //make a constructure for the Game Panel
    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.PINK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    //Function called start game
    public void startGame(){
        newApple(); //to create new apple for us
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    //Function called paint component, with a parameter
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    //Draw function
    public void draw(Graphics g){

     if(running) {
         //grid lines
         for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
             g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT); //draw line accros the x and y-access
             g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE); //Making a grid with x % y

         }

         //draw the apple
         g.setColor(Color.red);
         g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

         //draw the snake
         for (int i = 0; i < bodyParts; i++) {
             if (i == 0) {
                 g.setColor(Color.green);
                 g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
             }
             else {
                 g.setColor(new Color(45, 180, 0));
                 //random colors for snake
                 g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                 g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
             }
         }
         g.setColor(Color.black);
         g.setFont(new Font("Ink Free", Font.BOLD, 30));
         FontMetrics metrics = getFontMetrics(g.getFont());
         //putting the String at the middle of the screen
         g.drawString("Score: " +applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " +applesEaten))/2,
                 g.getFont().getSize());
     }
     else {
         gameOver(g);
     }

    }

    //Method for new apple - so anytime we start, loose or view the game we get new apple
    public void newApple(){
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;

    }

    //Move function - move the snake
    public void move(){
        for (int i = bodyParts; i > 0; i--){
            x[i] = x[i-1]; //shifting the body parts of our snake
            y[i] = y[i-1]; //doing the same for y
        }
        //create a switch that will change the direction where the snake will be headed
        switch (direction){ //we will use our direction values here (R, L, U(up), D(down))
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }

    }

    //Method to check score (to score points - when the snake eats the apples)
    public void checkApples(){
        if((x[0] == appleX) && (y[0] == appleY)){
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    //Method for collisions in the game
    public void checkCollisions(){
        //checks if head collided with body
        for (int i = bodyParts; i > 0; i--){
            if ((x[0] == x[i]) && (y[0] == y[i])){
                running= false;
            }
        }
        //checks if head touches left border
        if (x[0] < 0) {
            running = false;
        }

        //checks if head touches right border
        if (x[0] > SCREEN_WIDTH){
            running = false;
        }

        //checks if head touches top border
        if (y[0] < 0){
            running = false;
        }

        //checks if head touches bottom border
        if (y[0] > SCREEN_HEIGHT){
            running = false;
        }

        //Stop the timer
        if (!running){
            timer.stop();
        }
    }

    //Method for game over
    public void gameOver(Graphics g){
        //Displaying score
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        //putting the String at the middle of the screen
        g.drawString("Score: " +applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " +applesEaten))/2,
                g.getFont().getSize());

        //Game over text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        //putting the String at the middle of the screen
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2,SCREEN_HEIGHT/2);
    }

    @Override
    public void actionPerformed(ActionEvent e){
    if (running) {
        move();
        checkApples();
        checkCollisions();
    }
    repaint();    //if the game is no longer running call the repaint method
    }

    //Method to move the snake with our key adapter
    public class MyKeyAdapter extends KeyAdapter{
        //one method inside this class
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U'){
                        direction = 'D';
                    }
            }
        }
    }


}
