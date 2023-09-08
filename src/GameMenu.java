import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;
public class GameMenu extends JPanel
{
    String directoryPath;
    MakeButtonsWork makeButtonsWork = new MakeButtonsWork();
    GameFrame gameFrame;
    JLabel gameTitleText,highScoreText;
    JButton playButton,optionsButton;
    static final int SCREEN_WIDTH=600,SCREEN_HEIGHT=600;
    boolean isFancySnakeOn=false,isBorderButtonOn=false;
    Color[] snakeColorArray = new Color[5];
    int highScore=0,selectedGameModePosition=0,selectedSpeed;
    Clip gameMenuClip;
    GameMenu(GameFrame gf, String directoryPath)
    {
        //System.out.println("In GameMenu Constructor"); // Executes once
        this.directoryPath = directoryPath;
        gameFrame = gf;
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setLayout(null);
        this.setBackground(Color.black);
        setGameMenuSound();
        playMenuSound(true);
        setGameTitleText();
        setPlayButton();
        setHighScoreText();
        setOptionsButton();
        addComponents();
        //loopMenuSound();
    }
    public void setGameTitleText()
    {
        gameTitleText = new JLabel("Snake Game");
        gameTitleText.setFont(new Font("Ink Free",Font.BOLD,90));
        gameTitleText.setForeground(new Color(10,180,140));
        gameTitleText.setBounds(50,100,500,200);
        gameTitleText.setBackground(null);
    }
    public void addComponents()
    {
        //System.out.println("Play Button added");
        this.add(gameTitleText);
        this.add(playButton);
        this.add(optionsButton);
        this.add(highScoreText);
    }
    public void setHighScoreText()
    {
        highScoreText = new JLabel("HighScore : "+highScore);
        highScoreText.setFont(new Font("Ink Free",Font.ITALIC,50));
        highScoreText.setBounds(50,50,350,60);
        highScoreText.setForeground(new Color(90,180,100));

    }
    public void setPlayButton()
    {
        snakeColorArray=null;// initializes the array containing colors to null runs only 1 time.
        playButton = new JButton("PLAY");
        playButton.setForeground(Color.white);
        playButton.setFont(new Font("Times New Roman",Font.BOLD,25));
        playButton.setFocusable(false);
        playButton.setBackground(null);
        playButton.setBounds(225,300,150,40);
        playButton.addActionListener(makeButtonsWork);
       // System.out.println("Play button created");
    }
    public void handleIfClickedDirectlyOnPlay()
    {
        if(snakeColorArray==null)
        {
            snakeColorArray = new Color[5];
            for(int i=1 ; i<5 ; i++)
            {
                snakeColorArray[i] = new Color(40,180,150);
            }
            snakeColorArray[0] = new Color(0,255,0);
            selectedSpeed=1;
        }
    }
    public void setOptionsButton()
    {
        optionsButton = new JButton("Options");
        optionsButton.setForeground(Color.white);
        optionsButton.setFont(new Font("Times New Roman",Font.BOLD,25));
        optionsButton.setFocusable(false);
        optionsButton.setBackground(null);
        optionsButton.setBounds(225,350,150,40);
        optionsButton.addActionListener(makeButtonsWork);
    }
    public void passDataToMenu(Color[] c,boolean isFancySnakeOn,boolean isBorderButtonOn,int selectedGameModePosition,int selectedSpeed )
    {
        snakeColorArray = c;
        this.isFancySnakeOn = isFancySnakeOn;
        this.isBorderButtonOn = isBorderButtonOn;
        this.selectedGameModePosition = selectedGameModePosition;
        this.selectedSpeed = selectedSpeed;
    }
    public void setGameMenuFeatures(boolean b)
    {
        playButton.setVisible(b);
        gameTitleText.setVisible(b);
        optionsButton.setVisible(b);
        highScoreText.setVisible(b);
        if(b)
        {
            gameFrame.add(this);
        }
        else
        {
            gameFrame.remove(this);
        }
        playMenuSound(b);
    }
    public void setHighScore(int h)
    {
        highScore=h;
        highScoreText.setText("HighScore : "+highScore);
    }
    public void startGameOptions()
    {
        //System.out.println(selectedGameModePosition +"in menu");
        new GameOptions(gameFrame,this,snakeColorArray,isFancySnakeOn,isBorderButtonOn,selectedGameModePosition,selectedSpeed);
    }
    public GameMenu passGameMenuObject()
    {return this;}

    public void passHighScore(int h)
    {
        highScore = h;
    }
    public void setGameMenuSound()
    {
        File cottagecoreFile = new File(directoryPath+"\\Sound Effects\\cottagecore-17463.wav");
        AudioInputStream cottageInputStream = null;
        try {
            cottageInputStream = AudioSystem.getAudioInputStream(cottagecoreFile);
            gameMenuClip = AudioSystem.getClip();
            gameMenuClip.open(cottageInputStream);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("LOL Error");
        }
    }
    public void loopMenuSound()
    {
        while(true)
        {
            if(!gameMenuClip.isRunning())
            {
                gameMenuClip.setMicrosecondPosition(0);
                gameMenuClip.start();
            }
        }
    }
    public void playMenuSound(Boolean b)
    {
        gameMenuClip.setMicrosecondPosition(0);
        if(b)
        {
            gameMenuClip.start();
        }
        else {
            gameMenuClip.stop();
        }
    }
    class MakeButtonsWork implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(e.getSource()==playButton)
            {
                setGameMenuFeatures(false);
                handleIfClickedDirectlyOnPlay();
                GamePanel.passData(highScore,selectedGameModePosition,selectedSpeed);
                gameFrame.startGame(snakeColorArray,isFancySnakeOn,isBorderButtonOn,passGameMenuObject());
            }
            else if(e.getSource()==optionsButton)
            {
                setGameMenuFeatures(false);
                GameOptions.passHighScoreToGameOptions(highScore);
                //System.out.println("at options Button highScore "+highScore);
                startGameOptions();
            }
        }
    }
}
