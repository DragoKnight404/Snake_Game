import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Container;
public class GameOptions extends JPanel
{
    GameFrame gameFrame;
    //Container container = new Container();
    GameMenu gameMenu;
    int numberOfGameModes=4;
    MakeOptionButtonsWork makeOptionButtonsWork = new MakeOptionButtonsWork();
    Color[] snakeColorArray = new Color[5];
    JLabel snakeColorText,gameModeText,speedText;
    JButton redButton, greenButton, blueButton, resetButton, fancyColorButton, backButton, borderButton, normalGameModeButton, teleportGameModeButton;
    JButton[] snakeButton = new JButton[5];
    JButton[] gameModeButton = new JButton[numberOfGameModes];
    JButton[] speedButton = new JButton[3];
    int incrementAmt = 20;
    int[] snakeColorR = new int[5];
    int[] snakeColorG = new int[5];
    int[] snakeColorB = new int[5];
    boolean isFancySnakeOn = false, firstTime = false, isBorderButtonOn=false;
    boolean[] isSnakeButtonClicked = new boolean[5];
    boolean[] isGameModeButtonClicked = new boolean[numberOfGameModes];
    boolean[] isSpeedButtonClicked = new boolean[3];
    String arrow = "I";
    JLabel[] arrowArray = new JLabel[5];
    static int highScore;
    Color onClickColor = new Color(160,160,170);
    Color textColor = new Color(85, 129, 208);
    int selectedGameModePosition,selectedSpeed;
    GameOptions(GameFrame gameFrame, GameMenu gm, Color[] c,boolean isFancySnakeOn,boolean isBorderButtonOn,int selectedGameModePosition,int selectedSpeed)
    {
        //System.out.println("in GameOptions constructor"); // Runs Everytime when we click options Button
        gameMenu = gm;
        this.gameFrame = gameFrame;
        this.isFancySnakeOn = isFancySnakeOn;
        this.isBorderButtonOn = isBorderButtonOn;
        this.selectedGameModePosition = selectedGameModePosition;
        this.selectedSpeed = selectedSpeed;
        this.setPreferredSize(new Dimension(gameMenu.SCREEN_WIDTH, gameMenu.SCREEN_HEIGHT));
        this.setLayout(null);
        //container.setBackground(Color.white);

        rememberPreviouslySetColor(c);

        //this.setOpaque(true);   // underlying components will not be displayed if set opaque is true
        this.setBackground(Color.black);

        initializeValues();

        setBackButton();
        setText();
        setSnakeButton();
        setColorButtons();
        setGameplayRelatedButtons();
        setSpeedButton();
        addComponentsToGameOptionsPanel();
        //this.setBackground(Color.black);
        gameFrame.add(this,BorderLayout.CENTER);
        rememberPreviouslySetButtons();
        highlightChosenOptions();
    }
    public void rememberPreviouslySetColor(Color[] c)
    {
        if (c != null) {
            //System.out.println("Not null recognised");
            firstTime = false;
            snakeColorArray = c;
        }
        else {
            //System.out.println("null recognised");
            firstTime = true;
        }
    }
    public void addComponentsToGameOptionsPanel() {
        this.add(backButton);
        this.add(snakeColorText);
        this.add(redButton);
        this.add(greenButton);
        this.add(blueButton);
        this.add(resetButton);
        for (int i = 0; i < 5; i++) {
            this.add(snakeButton[i]);
            this.add(arrowArray[i]);
        }
        this.add(fancyColorButton);
        this.add(borderButton);
        this.add(gameModeText);
        this.add(speedText);
        for(int i=0 ; i<numberOfGameModes ; i++)
        {
            this.add(gameModeButton[i]);
        }
        for(int i=0 ; i<3 ; i++)
        {
            this.add(speedButton[i]);
        }
    }

    public void setBackButton() {
        backButton = new JButton("Back");
        backButton.setFont(new Font("New Times Roman", Font.BOLD, 25));
        backButton.setForeground(new Color(220, 59, 125));
        backButton.setFocusable(false);
        backButton.setBounds(10, 10, 100, 40);
        backButton.setBackground(null);
        backButton.addActionListener(makeOptionButtonsWork);
    }
    public void initializeValues() {
        for (int i = 1; i < 5; i++) {
            if (firstTime)
            {
                snakeColorR[i] = 40;
                snakeColorG[i] = 180;
                snakeColorB[i] = 150;
                snakeColorArray[i] = new Color(snakeColorR[i], snakeColorG[i], snakeColorB[i]);
            }
            else
            {
                snakeColorR[i] = snakeColorArray[i].getRed();
                snakeColorG[i] = snakeColorArray[i].getGreen();
                snakeColorB[i] = snakeColorArray[i].getBlue();
            }
        }
        if (firstTime)
        {
            snakeColorR[0] = 0;
            snakeColorG[0] = 255;
            snakeColorB[0] = 0;
            snakeColorArray[0] = new Color(snakeColorR[0], snakeColorG[0], snakeColorB[0]);
        }
        else
        {
            snakeColorR[0] = snakeColorArray[0].getRed();
            snakeColorG[0] = snakeColorArray[0].getGreen();
            snakeColorB[0] = snakeColorArray[0].getBlue();
        }
        for (int i = 0; i < 5; i++) {
            arrowArray[i] = new JLabel();
            arrowArray[i].setFont(new Font("New Times Roman", Font.BOLD, 40));
            arrowArray[i].setBounds(240 + i * 40, 20, 40, 40);
            arrowArray[i].setForeground(Color.white);
            arrowArray[i].setHorizontalAlignment(SwingConstants.CENTER);

            //arrowArray[i].setOpaque(true);
            //arrowArray[i].setBackground(Color.blue);
        }
        for(int i=0 ; i<numberOfGameModes ; i++)
        {
            if(i==selectedGameModePosition) {continue;}
            isGameModeButtonClicked[i]=false;
        }
        for(int i=0 ; i<3 ; i++)
        {
            if(i==selectedSpeed){ continue;}
            isSpeedButtonClicked[i]=false;
        }
    }
    public void setText() {
        snakeColorText = new JLabel("Color :");
        snakeColorText.setFont(new Font("Times New Roman", Font.ITALIC, 25));
        snakeColorText.setForeground(textColor);
        snakeColorText.setFocusable(false);
        snakeColorText.setBackground(null);
        snakeColorText.setOpaque(true);
        snakeColorText.setBounds(120, 60, 100, 40);

        gameModeText = new JLabel("Select Game Mode :");
        gameModeText.setFont(new Font("Times New Roman", Font.ITALIC, 25));
        gameModeText.setForeground(textColor);
        gameModeText.setFocusable(false);
        gameModeText.setBackground(null);
        gameModeText.setOpaque(true);
        gameModeText.setBounds(10,210,230,40);

        speedText = new JLabel("Speed :");
        speedText.setFont(new Font("Times New Roman", Font.ITALIC, 25));
        speedText.setForeground(textColor);
        speedText.setFocusable(false);
        speedText.setBackground(null);
        speedText.setOpaque(true);
        speedText.setBounds(120,160,100,40);
    }

    public void setColorButtons() {
        int width = 100;
        redButton = new JButton("Red");
        redButton.setFocusable(false);
        redButton.setBackground(null);
        redButton.setForeground(Color.white);
        redButton.setBounds(240, 110, width, 40);
        redButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
        redButton.setOpaque(true);
        redButton.addActionListener(makeOptionButtonsWork);

        greenButton = new JButton("Green");
        greenButton.setFocusable(false);
        greenButton.setBackground(null);
        greenButton.setForeground(Color.white);
        greenButton.setBounds(240 + width, 110, width, 40);
        greenButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
        greenButton.setOpaque(true);
        greenButton.addActionListener(makeOptionButtonsWork);

        blueButton = new JButton("Blue");
        blueButton.setFocusable(false);
        blueButton.setBackground(null);
        blueButton.setForeground(Color.white);
        blueButton.setBounds(240 + 2 * width, 110, width, 40);
        blueButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
        blueButton.setOpaque(true);
        blueButton.addActionListener(makeOptionButtonsWork);

        resetButton = new JButton("Reset");
        resetButton.setFocusable(false);
        resetButton.setBackground(null);
        resetButton.setForeground(Color.white);
        resetButton.setBounds(10, 60, width, 40);
        resetButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
        resetButton.setOpaque(true);
        resetButton.addActionListener(makeOptionButtonsWork);

        fancyColorButton = new JButton("Fancy Snake");
        fancyColorButton.setFocusable(false);
        fancyColorButton.setBackground(null);
        fancyColorButton.setForeground(Color.white);
        fancyColorButton.setBounds(10, 110, width + 50, 40);
        fancyColorButton.setFont(new Font("Ink Free", Font.BOLD, 20));
        fancyColorButton.setOpaque(true);
        fancyColorButton.addActionListener(makeOptionButtonsWork);
    }
    public void setGameplayRelatedButtons()
    {
        borderButton = new JButton("Border");
        borderButton.setFocusable(false);
        borderButton.setBackground(null);
        borderButton.setForeground(Color.white);
        borderButton.setFont(new Font("New Times Roman", Font.BOLD, 20));
        borderButton.setBounds(10,160,100,40);
        borderButton.addActionListener(makeOptionButtonsWork);
        for(int i=0 ; i<numberOfGameModes ; i++)
        {
            if(i==0)
            {
                gameModeButton[i] = new JButton("Normal");
                gameModeButton[i].setBounds(10,250,120,40);
            }
            else if(i==1)
            {
                gameModeButton[i] = new JButton("Teleport");
                gameModeButton[i].setBounds(140,250,120,40);
            }
            else if(i==2)
            {
                gameModeButton[i] = new JButton("Inverse");
                gameModeButton[i].setBounds(270,250,120,40);
            }
            else if(i==3)
            {
                gameModeButton[i] = new JButton("Twin");
                gameModeButton[i].setBounds(400,250,120,40);
            }
            gameModeButton[i].setFocusable(false);
            gameModeButton[i].setBackground(null);
            gameModeButton[i].setForeground(Color.white);
            gameModeButton[i].setFont(new Font("New Times Roman", Font.BOLD, 20));
            gameModeButton[i].addActionListener(makeOptionButtonsWork);
        }
    }
    public void setSpeedButton()
    {
        int pos=230;
        for(int i=0 ; i<3 ; i++)
        {
            if(i==0)
            {
                speedButton[i] = new JButton("Slow");
                speedButton[i].setBounds(pos,160,100,40);
            }
            if(i==1)
            {
                speedButton[i] = new JButton("Normal");
                speedButton[i].setBounds(pos+110,160,110,40);
            }
            if(i==2)
            {
                speedButton[i] = new JButton("Fast");
                speedButton[i].setBounds(pos+230,160,100,40);
            }
            speedButton[i].setFocusable(false);
            speedButton[i].setBackground(null);
            speedButton[i].setForeground(Color.white);
            speedButton[i].setFont(new Font("New Times Roman", Font.BOLD, 20));
            speedButton[i].addActionListener(makeOptionButtonsWork);
        }
    }
    public void rememberPreviouslySetButtons()
    {
        if(isFancySnakeOn)
        {
            fancyColorButton.setBackground(onClickColor);
        }
        if(isBorderButtonOn)
        {
            borderButton.setBackground(onClickColor);
        }
    }
    public void setSnakeButton() {
        for (int i = 0; i < 5; i++) {
            snakeButton[i] = new JButton();
            snakeButton[i].setFocusable(false);
            //snakeBodyButton.setOpaque(true);
            //snakeBodyButton.setContentAreaFilled(true);
            snakeButton[i].setBounds(240 + (i) * 40, 60, 40, 40);
            snakeButton[i].setBackground(snakeColorArray[i]);
            snakeButton[i].setOpaque(true);
            snakeButton[i].addActionListener(makeOptionButtonsWork);

            isSnakeButtonClicked[i] = false;
        }
    }

    public void repaintSnakeButton() {
        for (int i = 0; i < 5; i++) {
            snakeButton[i].setBackground(snakeColorArray[i]);
        }
    }
    public void highlightChosenOptions()
    {
        if(firstTime)
        {
            isSpeedButtonClicked[1]= true;
            speedButton[1].setBackground(onClickColor);
            selectedSpeed=1;

            isGameModeButtonClicked[0]=true;
            gameModeButton[0].setBackground(onClickColor);
            selectedGameModePosition=0;
        }
        else
        {
            //System.out.println(selectedGameModePosition);
            gameModeButton[selectedGameModePosition].setBackground(onClickColor);
            speedButton[selectedSpeed].setBackground(onClickColor);
        }
    }
    public void changeColorAccordingly() {
        int i;
        for (i = 0; i < 5; i++) {
            if (isSnakeButtonClicked[i]) {
                snakeColorArray[i] = new Color(snakeColorR[i], snakeColorG[i], snakeColorB[i]);
            }
        }
    }

    public void setOptionFeatures(Boolean b) {
        this.setVisible(b);
    }
    public int rgbChecker(int c) {
        if (c > 255) {
            return 255;
        }
        return c;
    }
    static public void passHighScoreToGameOptions(int h)
    {
        highScore = h;
    }
    class MakeOptionButtonsWork implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(e.getSource()==backButton)
            {
                setOptionFeatures(false);
                gameMenu.passDataToMenu(snakeColorArray,isFancySnakeOn,isBorderButtonOn,selectedGameModePosition,selectedSpeed);
                //System.out.println("at backButton highScore "+highScore);
                //System.out.println(selectedGameModePosition+" at backbutton");
                gameMenu.passHighScore(highScore);
                gameMenu.setGameMenuFeatures(true);
            }
            else if(e.getSource()==redButton)
            {
                for(int i=0 ; i<5 ; i++)
                {
                    if(isSnakeButtonClicked[i])
                    {
                        snakeColorR[i]+=incrementAmt;
                        snakeColorR[i]=rgbChecker(snakeColorR[i]);
                    }
                }
                changeColorAccordingly();
                repaintSnakeButton();
            }
            else if(e.getSource()==greenButton)
            {
                for(int i=0 ; i<5 ; i++)
                {
                    if(isSnakeButtonClicked[i])
                    {
                        snakeColorG[i]+=incrementAmt;
                        snakeColorG[i]=rgbChecker(snakeColorG[i]);
                    }
                }
                changeColorAccordingly();
                repaintSnakeButton();
            }
            else if(e.getSource()==blueButton)
            {
                for(int i=0 ; i<5 ; i++)
                {
                    if(isSnakeButtonClicked[i])
                    {
                        snakeColorB[i]+=incrementAmt;
                        snakeColorB[i]=rgbChecker(snakeColorB[i]);
                    }
                }
                changeColorAccordingly();
                repaintSnakeButton();
            }
            else if(e.getSource()==resetButton)
            {
                for(int i=0 ; i<5 ; i++)
                {
                    if(isSnakeButtonClicked[i])
                    {
                        snakeColorR[i]=0;
                        snakeColorG[i]=0;
                        snakeColorB[i]=0;
                    }
                }
                changeColorAccordingly();
                repaintSnakeButton();
            }
            for(int i=0 ; i<5 ; i++)
            {
                if(e.getSource()==snakeButton[i])
                {
                    if(!isSnakeButtonClicked[i])
                    {
                        //arrow=""+(i+1);
                        arrowArray[i].setText(arrow);
                        isSnakeButtonClicked[i]=true;
                    }
                    else
                    {
                        arrowArray[i].setText("");
                        isSnakeButtonClicked[i]=false;
                    }
                }
            }
            if(e.getSource()==fancyColorButton)
            {
                if(!isFancySnakeOn)
                {
                    isFancySnakeOn=true;
                    fancyColorButton.setBackground(onClickColor);
                }
                else
                {
                    isFancySnakeOn=false;
                    fancyColorButton.setBackground(null);
                }
            }
            else if(e.getSource()==borderButton)
            {
                if(!isBorderButtonOn)
                {
                    isBorderButtonOn=true;
                    borderButton.setBackground(onClickColor);
                }
                else
                {
                    isBorderButtonOn=false;
                    borderButton.setBackground(null);
                }
            }
            for(int i=0 ; i<numberOfGameModes ; i++)
            {
                if(e.getSource()==gameModeButton[i])
                {
                    if(!isGameModeButtonClicked[i])
                    {
                        for(int j=0 ; j<numberOfGameModes ; j++)
                        {
                            if(j==i){ continue; }
                            isGameModeButtonClicked[j]=false;
                            gameModeButton[j].setBackground(null);
                        }
                        selectedGameModePosition=i;
                        //System.out.println(i+" in action performed");
                        isGameModeButtonClicked[i]=true;
                        gameModeButton[i].setBackground(onClickColor);
                    }
                }
            }
            for(int i=0 ; i<3 ; i++)
            {
                if(e.getSource()==speedButton[i])
                {
                    if(!isSpeedButtonClicked[i])
                    {
                        for(int j=0 ; j<3 ; j++)
                        {
                            if(j==i){ continue; }
                            isSpeedButtonClicked[j]=false;
                            speedButton[j].setBackground(null);
                        }
                        selectedSpeed=i;
                        isSpeedButtonClicked[i]=true;
                        speedButton[i].setBackground(onClickColor);
                    }
                }
            }
        }
    }
}
