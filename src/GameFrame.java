import javax.swing.*;

public class GameFrame extends JFrame {

    //make a constructure
    GameFrame(){
        GamePanel panel = new GamePanel();

        this.add(panel);
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null); //apperance on computer (where it is going to appear)

    }
}
