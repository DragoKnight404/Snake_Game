import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame
{
    GameMenu gm;
    String directoryPath;
    GameFrame(String directoryPath)
    {
        this.directoryPath = directoryPath;
        gm = new GameMenu(this,directoryPath);
        this.add(gm, BorderLayout.CENTER);
        //this.add(new GamePanel());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Snake Game");
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        ImageIcon snakeIcon = new ImageIcon(directoryPath+"\\SnakeIcon.png");
        this.setIconImage(snakeIcon.getImage());
    }
    public void startGame(Color[] snakeColorArray,Boolean isFancySnakeOn,Boolean isBorderButtonOn,GameMenu gameMenu)
    {
        this.add(new GamePanel(this,snakeColorArray,isFancySnakeOn,isBorderButtonOn,gameMenu),BorderLayout.CENTER);
    }
}
