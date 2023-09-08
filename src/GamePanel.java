import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;
public class GamePanel extends JPanel implements ActionListener
{
    static final int SCREEN_WIDTH=600,SCREEN_HEIGHT=600;
    static final int UNIT_SIZE = 20;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static int DELAY = 100;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    final int twinx[] = new int[GAME_UNITS];
    final int twiny[] = new int[GAME_UNITS];
    int bodyParts = 5;
    int applesEaten= 0;
    int appleX,appleY,appleX2,appleY2;
    static int highScore,selectedGameModePosition,selectedSpeed;
    char direction = 'R',previousDirection='R',tailDirection='R';
    char[] directionArray = new char[GAME_UNITS];
    boolean running = false;
    Timer timer;
    Random random;
    Color[] snakeColorArray;
    Color[] twinSnakeColorArray = new Color[5];
    Color borderColor = new Color(51, 23, 23);
    Color borderBorderColor = new Color(150,75,0);
    boolean isFancySnakeOn,isBorderButtonOn,isProcessingKey=false,eaten=false;
    GameLevels gameLevels;
    GameMenu gameMenu;
    JButton mainMenuButton;
    Clip eatingClip,bonkClip,moveClip,teleportClip;
    GameFrame gameFrame;
    //MyKeyAdapter myKeyAdapter; // hehe
    //boolean startGameAndNeglectBugFixer=true,is180DegBugHappening=false;
    GamePanel(){}
    GamePanel (GameFrame gameFrame,Color[] snakeColorArray,Boolean isFancySnakeOn,Boolean isBorderButtonOn,GameMenu gameMenu)
    {
        gameLevels = new GameLevels();
        this.gameFrame = gameFrame;
        this.gameMenu = gameMenu;
        this.isFancySnakeOn = isFancySnakeOn;
        this.snakeColorArray = snakeColorArray;
        this.isBorderButtonOn = isBorderButtonOn;
        //this.gameFrame = gameFrame;
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        setUpSnakeSoundEffects();
        //this.addKeyListener(new MyKeyAdapter());
        gameFrame.addKeyListener(new MyKeyAdapter());
        gameFrame.revalidate();
        this.setFocusable(true);
        //this.requestFocusInWindow();
        initializeSnakeCoordinates();
        setComplimentarySnakeColorArray();
        //setObstaclesForGame();
        //setScoreLabel();
        startGame();
    }
    public void setComplimentarySnakeColorArray()
    {
        if(selectedGameModePosition==3)
        {
            for(int i=0 ; i<5 ; i++)
            {
                twinSnakeColorArray[i] = new Color(255 - snakeColorArray[i].getRed(),255 - snakeColorArray[i].getGreen(),255 - snakeColorArray[i].getBlue());
            }
        }
    }
    public void initializeSnakeCoordinates()
    {
        for(int i=0 ; i<bodyParts ; i++)
        {
            x[bodyParts-i-1] = UNIT_SIZE*(2+i);
            y[bodyParts-i-1] = UNIT_SIZE*(3);
            directionArray[i]='R';

            twinx[bodyParts-i-1] = UNIT_SIZE*(27-i);
            twiny[bodyParts-i-1] = UNIT_SIZE*(26);
        }
    }
    public void startGame()
    {
        newAppleAccordingToGameMode();
        running = true;
        setDELAYAccordingTOSelectedSpeed();
        timer = new Timer(DELAY,this);
        timer.start();
    }
    public void setDELAYAccordingTOSelectedSpeed()
    {
        switch (selectedSpeed)
        {
            case 0:
                DELAY=180;//Default = 180
                break;
            case 1:
                DELAY=100;//Default = 100
                break;
            case 2:
                DELAY=60;//Default = 60
                break;
        }
    }
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        drawAccordingToGameMode(g);
    }
    public void drawAccordingToGameMode(Graphics g)
    {
        switch (selectedGameModePosition)
        {
            case 0:
            case 2:
                draw(g);
                break;
            case 1:
                drawForTeleportGameMode(g);
                break;
            case 3:
                drawForTwinSnakeGameMode(g);
                break;
        }
    }
    public void drawScore(Graphics g)
    {
        FontMetrics metrics = getFontMetrics(g.getFont());
        String score = "Score : "+applesEaten;
        g.setColor(Color.green);
        g.setFont(new Font("Ink Free",Font.ITALIC,30));
        g.drawString(score,((SCREEN_WIDTH - metrics.stringWidth(score))/2)+170,50);
    }
    public void draw(Graphics g)
    {
        if(running)
        {
            int i,j;
            g.setColor(new Color(22,22,22));
            for ( i=0 ; i<SCREEN_HEIGHT/UNIT_SIZE ; i++)
            {
                for( j=i%2 ; j<SCREEN_WIDTH/UNIT_SIZE ; j+=2)
                {
                    g.fillRect(j*UNIT_SIZE,i*UNIT_SIZE,UNIT_SIZE,UNIT_SIZE);
                }
            }
            drawScore(g);
            g.setColor(Color.red);
            int[] xPoints = {appleX+UNIT_SIZE/4, appleX+UNIT_SIZE/8, appleX+(UNIT_SIZE*3/8), appleX+UNIT_SIZE/2, appleX+(UNIT_SIZE*5/8), appleX+(UNIT_SIZE*7/8), appleX+(UNIT_SIZE*3/4), appleX+UNIT_SIZE, appleX+(UNIT_SIZE*3/4), appleX+(UNIT_SIZE*7/8), appleX+(UNIT_SIZE*5/8), appleX+UNIT_SIZE/2, appleX+(UNIT_SIZE*3/8), appleX+UNIT_SIZE/8 ,appleX+UNIT_SIZE/4, appleX };
            int[] yPoints = {appleY+((UNIT_SIZE*3)/8), appleY+UNIT_SIZE/8, appleY+UNIT_SIZE/4, appleY, appleY+UNIT_SIZE/4, appleY+UNIT_SIZE/8, appleY+(UNIT_SIZE*3/8), appleY+UNIT_SIZE/2, appleY+(UNIT_SIZE*5/8), appleY+(UNIT_SIZE*7/8), appleY+(UNIT_SIZE*3/4), appleY+UNIT_SIZE, appleY+(UNIT_SIZE*3/4), appleY+(UNIT_SIZE*7/8),appleY+(UNIT_SIZE*5/8), appleY+UNIT_SIZE/2 };
            // Draw the apple polygon using fillPolygon
            g.fillPolygon(xPoints, yPoints, xPoints.length);
            j=0;
            for(i=0 ; i<bodyParts ; i++,j++)
            {
                if(i==0)
                {
                    g.setColor(snakeColorArray[0]);
                    g.fillRoundRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE,(int)(UNIT_SIZE*0.9),(int)(UNIT_SIZE*0.9));
                    //System.out.println("Head i = 0  x= "+x[i]+"   y= "+y[i]);
                }
                else
                {
                    if(isFancySnakeOn) { g.setColor(randomColorGenerator());}
                    else
                    {
                        if(j>4) {j=1;}
                        g.setColor(snakeColorArray[j]);
                    }
                    g.fillRoundRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE,(int)(UNIT_SIZE*0.7),(int)(UNIT_SIZE*0.7));
                    //System.out.println("body i = "+i+"  x= "+x[i]+"   y= "+y[i]);
                }
            }
            if(isBorderButtonOn) {drawBorder(g);}
            //System.out.println();
        }
        else { gameOver(g);}
    }
    public void drawForTeleportGameMode(Graphics g)
    {
        if(running)
        {
            int i,j;
           /* for(int i=0 ; i<SCREEN_HEIGHT/UNIT_SIZE ; i++)
            {
                g.drawLine(i*UNIT_SIZE,0,i*UNIT_SIZE,SCREEN_HEIGHT);
                g.drawLine(0,i*UNIT_SIZE,SCREEN_WIDTH,i*UNIT_SIZE);
            }*/
            g.setColor(new Color(22,22,22));
            for ( i=0 ; i<SCREEN_HEIGHT/UNIT_SIZE ; i++)
            {
                for( j=i%2 ; j<SCREEN_WIDTH/UNIT_SIZE ; j+=2)
                {
                    g.fillRect(j*UNIT_SIZE,i*UNIT_SIZE,UNIT_SIZE,UNIT_SIZE);
                }
            }
            drawScore(g);
            g.setColor(Color.red);
            //g.fillOval(appleX,appleY,UNIT_SIZE,UNIT_SIZE);
            //int[] xPoints = {appleX, appleX+UNIT_SIZE/2, appleX+UNIT_SIZE, appleX+UNIT_SIZE/2};
            // Create an array of y-coordinates for the apple polygon
            //int[] yPoints = {appleY+UNIT_SIZE/2, appleY, appleY+UNIT_SIZE/2, appleY+UNIT_SIZE };
            int[] xPoints = {appleX+UNIT_SIZE/4, appleX+UNIT_SIZE/8, appleX+(UNIT_SIZE*3/8), appleX+UNIT_SIZE/2, appleX+(UNIT_SIZE*5/8), appleX+(UNIT_SIZE*7/8), appleX+(UNIT_SIZE*3/4), appleX+UNIT_SIZE, appleX+(UNIT_SIZE*3/4), appleX+(UNIT_SIZE*7/8), appleX+(UNIT_SIZE*5/8), appleX+UNIT_SIZE/2, appleX+(UNIT_SIZE*3/8), appleX+UNIT_SIZE/8 ,appleX+UNIT_SIZE/4, appleX };
            int[] yPoints = {appleY+((UNIT_SIZE*3)/8), appleY+UNIT_SIZE/8, appleY+UNIT_SIZE/4, appleY, appleY+UNIT_SIZE/4, appleY+UNIT_SIZE/8, appleY+(UNIT_SIZE*3/8), appleY+UNIT_SIZE/2, appleY+(UNIT_SIZE*5/8), appleY+(UNIT_SIZE*7/8), appleY+(UNIT_SIZE*3/4), appleY+UNIT_SIZE, appleY+(UNIT_SIZE*3/4), appleY+(UNIT_SIZE*7/8),appleY+(UNIT_SIZE*5/8), appleY+UNIT_SIZE/2 };
            // Draw the apple polygon using fillPolygon
            g.fillPolygon(xPoints, yPoints, xPoints.length);
            g.setColor(new Color(183, 114, 114));
            int[] x2Points = {appleX2+UNIT_SIZE/4, appleX2+UNIT_SIZE/8, appleX2+(UNIT_SIZE*3/8), appleX2+UNIT_SIZE/2, appleX2+(UNIT_SIZE*5/8), appleX2+(UNIT_SIZE*7/8), appleX2+(UNIT_SIZE*3/4), appleX2+UNIT_SIZE, appleX2+(UNIT_SIZE*3/4), appleX2+(UNIT_SIZE*7/8), appleX2+(UNIT_SIZE*5/8), appleX2+UNIT_SIZE/2, appleX2+(UNIT_SIZE*3/8), appleX2+UNIT_SIZE/8 ,appleX2+UNIT_SIZE/4, appleX2 };
            int[] y2Points = {appleY2+((UNIT_SIZE*3)/8), appleY2+UNIT_SIZE/8, appleY2+UNIT_SIZE/4, appleY2, appleY2+UNIT_SIZE/4, appleY2+UNIT_SIZE/8, appleY2+(UNIT_SIZE*3/8), appleY2+UNIT_SIZE/2, appleY2+(UNIT_SIZE*5/8), appleY2+(UNIT_SIZE*7/8), appleY2+(UNIT_SIZE*3/4), appleY2+UNIT_SIZE, appleY2+(UNIT_SIZE*3/4), appleY2+(UNIT_SIZE*7/8),appleY2+(UNIT_SIZE*5/8), appleY2+UNIT_SIZE/2 };
            // Draw the apple polygon using fillPolygon
            g.fillPolygon(x2Points, y2Points, x2Points.length);
            j=0;
            for(i=0 ; i<bodyParts ; i++,j++)
            {
                if(i==0)
                {
                    g.setColor(snakeColorArray[0]);
                    //g.fillRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
                    //g.fillOval(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
                    g.fillRoundRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE,(int)(UNIT_SIZE*0.9),(int)(UNIT_SIZE*0.9));
                    //System.out.println("Head i = 0  x= "+x[i]+"   y= "+y[i]);
                }
                else
                {
                    //g.setColor(new Color(45,180,130));
                    //
                    if(isFancySnakeOn) { g.setColor(randomColorGenerator());}
                    else
                    {
                        if(j>4) {j=1;}
                        g.setColor(snakeColorArray[j]);
                    }
                    //g.fillRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
                    //g.fillOval(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
                    g.fillRoundRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE,(int)(UNIT_SIZE*0.7),(int)(UNIT_SIZE*0.7));
                    /*if(i<bodyParts/4)
                    {
                        g.fillRoundRect(x[i],y[i],(int)(UNIT_SIZE*1),(int)(UNIT_SIZE*1),(int)(UNIT_SIZE*0.7),(int)(UNIT_SIZE*0.7)); //0.7*Unit Size arc is best
                    }
                    else if(i>=bodyParts/4 && i<bodyParts*1/2)
                    {
                        g.fillRoundRect((int)(x[i]+UNIT_SIZE*0.05),(int)(y[i]+UNIT_SIZE*0.05),(int)(UNIT_SIZE*0.95),(int)(UNIT_SIZE*0.95),(int)(UNIT_SIZE*0.7),(int)(UNIT_SIZE*0.7)); //0.7*Unit Size arc is best
                    }
                    else if(i>=bodyParts/2 && i< bodyParts*3/4)
                    {
                        g.fillRoundRect((int)(x[i]+UNIT_SIZE*0.1),(int)(y[i]+UNIT_SIZE*0.1),(int)(UNIT_SIZE*0.9),(int)(UNIT_SIZE*0.9),(int)(UNIT_SIZE*0.7),(int)(UNIT_SIZE*0.7)); //0.7*Unit Size arc is best
                    }
                    else
                    {
                        g.fillRoundRect((int)(x[i]+UNIT_SIZE*0.15),(int)(y[i]+UNIT_SIZE*0.15),(int)(UNIT_SIZE*0.85),(int)(UNIT_SIZE*0.85),(int)(UNIT_SIZE*0.7),(int)(UNIT_SIZE*0.7)); //0.7*Unit Size arc is best
                    }*/
                    //System.out.println("Body i = "+i+"  x= "+x[i]+"  y= "+y[i]);
                }
            }
            if(isBorderButtonOn) {drawBorder(g);}
            //System.out.println();
        }
        else { gameOver(g);}
    }
    public void drawForTwinSnakeGameMode(Graphics g)
    {
        if(running)
        {
            int i,j;
            g.setColor(new Color(22,22,22));
            for ( i=0 ; i<SCREEN_HEIGHT/UNIT_SIZE ; i++)
            {
                for( j=i%2 ; j<SCREEN_WIDTH/UNIT_SIZE ; j+=2)
                {
                    g.fillRect(j*UNIT_SIZE,i*UNIT_SIZE,UNIT_SIZE,UNIT_SIZE);
                }
            }
            drawScore(g);
            g.setColor(Color.red);
            int[] xPoints = {appleX+UNIT_SIZE/4, appleX+UNIT_SIZE/8, appleX+(UNIT_SIZE*3/8), appleX+UNIT_SIZE/2, appleX+(UNIT_SIZE*5/8), appleX+(UNIT_SIZE*7/8), appleX+(UNIT_SIZE*3/4), appleX+UNIT_SIZE, appleX+(UNIT_SIZE*3/4), appleX+(UNIT_SIZE*7/8), appleX+(UNIT_SIZE*5/8), appleX+UNIT_SIZE/2, appleX+(UNIT_SIZE*3/8), appleX+UNIT_SIZE/8 ,appleX+UNIT_SIZE/4, appleX };
            int[] yPoints = {appleY+((UNIT_SIZE*3)/8), appleY+UNIT_SIZE/8, appleY+UNIT_SIZE/4, appleY, appleY+UNIT_SIZE/4, appleY+UNIT_SIZE/8, appleY+(UNIT_SIZE*3/8), appleY+UNIT_SIZE/2, appleY+(UNIT_SIZE*5/8), appleY+(UNIT_SIZE*7/8), appleY+(UNIT_SIZE*3/4), appleY+UNIT_SIZE, appleY+(UNIT_SIZE*3/4), appleY+(UNIT_SIZE*7/8),appleY+(UNIT_SIZE*5/8), appleY+UNIT_SIZE/2 };
            // Draw the apple polygon using fillPolygon
            g.fillPolygon(xPoints, yPoints, xPoints.length);
            j=0;
            for(i=0 ; i<bodyParts ; i++,j++)
            {
                if(i==0)
                {
                    g.setColor(snakeColorArray[0]);
                    g.fillRoundRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE,(int)(UNIT_SIZE*0.9),(int)(UNIT_SIZE*0.9));
                    //System.out.println("Head i = 0  x= "+x[i]+"   y= "+y[i]);
                    g.setColor(twinSnakeColorArray[0]);
                    g.fillRoundRect(twinx[i],twiny[i],UNIT_SIZE,UNIT_SIZE,(int)(UNIT_SIZE*0.9),(int)(UNIT_SIZE*0.9));
                }
                else
                {
                    if(isFancySnakeOn) { g.setColor(randomColorGenerator());}
                    else
                    {
                        if(j>4) {j=1;}
                        g.setColor(snakeColorArray[j]);
                    }
                    g.fillRoundRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE,(int)(UNIT_SIZE*0.7),(int)(UNIT_SIZE*0.7));
                    //System.out.println("body i = "+i+"  x= "+x[i]+"   y= "+y[i]);
                    if(isFancySnakeOn) { g.setColor(randomColorGenerator());}
                    else
                    {
                        g.setColor(twinSnakeColorArray[j]);
                    }
                    g.fillRoundRect(twinx[i],twiny[i],UNIT_SIZE,UNIT_SIZE,(int)(UNIT_SIZE*0.7),(int)(UNIT_SIZE*0.7));
                }
            }
            if(isBorderButtonOn) {drawBorder(g);}
            //System.out.println();
        }
        else { gameOver(g);}
    }
    public void drawBorder(Graphics g)
    {
        int i;
        for(i=0 ; i<SCREEN_WIDTH/UNIT_SIZE ; i++)
        {
            drawWallSprite(g,i*UNIT_SIZE,0,UNIT_SIZE,UNIT_SIZE);
            drawWallSprite(g,UNIT_SIZE*i,((SCREEN_HEIGHT/UNIT_SIZE)-1)*UNIT_SIZE,UNIT_SIZE,UNIT_SIZE);
            //g.fillRect(i*UNIT_SIZE,0,UNIT_SIZE,UNIT_SIZE);
            //g.fillRect(UNIT_SIZE*i,((SCREEN_HEIGHT/UNIT_SIZE)-1)*UNIT_SIZE,UNIT_SIZE,UNIT_SIZE);
        }
        for(i=0 ; i<SCREEN_HEIGHT/UNIT_SIZE ; i++)
        {
            drawWallSprite(g,((SCREEN_WIDTH/UNIT_SIZE)-1)*UNIT_SIZE , UNIT_SIZE*i,UNIT_SIZE ,UNIT_SIZE);
            drawWallSprite(g,0,UNIT_SIZE*i,UNIT_SIZE,UNIT_SIZE);
            //g.fillRect(((SCREEN_WIDTH/UNIT_SIZE)-1)*UNIT_SIZE , UNIT_SIZE*i,UNIT_SIZE ,UNIT_SIZE);
           // g.fillRect(0,UNIT_SIZE*i,UNIT_SIZE,UNIT_SIZE);
        }
    }
    public void drawWallSprite(Graphics g,int x,int y,int width,int height)
    {
        g.setColor(borderBorderColor);
        g.fillRect(x,y,width,height);
        g.setColor(borderColor);
        g.fillRect(x+UNIT_SIZE/5,y+UNIT_SIZE/5,width-2*UNIT_SIZE/5,height-2*UNIT_SIZE/5);
    }
    public void newAppleAccordingToGameMode()
    {
        switch (selectedGameModePosition)
        {
            case 0:
            case 2:
                newApple();
                break;
            case 1:
                newAppleFor2AppleTeleportGameMode();
                break;
            case 3:
                newAppleForTwinSnakeGameMode();
                break;
        }
    }
    public int newApple()
    {
        int i;
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
        for(i=0 ; i<bodyParts ; i++)
        {
            if(appleX==x[i] && appleY==y[i])
            {
                return newApple();
            }
        }
        if(isBorderButtonOn)
        {
            for(i=0 ; i<SCREEN_WIDTH/UNIT_SIZE ; i++)
            {
                if((appleX==i*UNIT_SIZE && appleY==0) || (appleX==UNIT_SIZE*i && appleY==((SCREEN_HEIGHT/UNIT_SIZE)-1)*UNIT_SIZE))
                {
                    return newApple();
                }
                if((appleX==((SCREEN_WIDTH/UNIT_SIZE)-1)*UNIT_SIZE && appleY==UNIT_SIZE*i) || (appleX==0 && appleY==UNIT_SIZE*i))
                {
                    return newApple();
                }
            }
        }
        return 0;
    }
    public int newAppleFor2AppleTeleportGameMode()
    {
        int i;
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
        appleX2 = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY2 = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
        for(i=0 ; i<bodyParts ; i++)
        {
            if((appleX==x[i] && appleY==y[i]) || (appleX2==x[i] && appleY2==y[i]))
            {
                return newAppleFor2AppleTeleportGameMode();
            }
        }
        if(appleX==appleX2 && appleY==appleY2)
        {
            return newAppleFor2AppleTeleportGameMode();
        }
        if(isBorderButtonOn)
        {
            for(i=0 ; i<SCREEN_WIDTH/UNIT_SIZE ; i++)
            {
                if((appleX==i*UNIT_SIZE && appleY==0) || (appleX==UNIT_SIZE*i && appleY==((SCREEN_HEIGHT/UNIT_SIZE)-1)*UNIT_SIZE))
                {
                    return newAppleFor2AppleTeleportGameMode();
                }
                if((appleX==((SCREEN_WIDTH/UNIT_SIZE)-1)*UNIT_SIZE && appleY==UNIT_SIZE*i) || (appleX==0 && appleY==UNIT_SIZE*i))
                {
                    return newAppleFor2AppleTeleportGameMode();
                }
                if((appleX2==i*UNIT_SIZE && appleY2==0) || (appleX2==UNIT_SIZE*i && appleY2==((SCREEN_HEIGHT/UNIT_SIZE)-1)*UNIT_SIZE))
                {
                    return newAppleFor2AppleTeleportGameMode();
                }
                if((appleX2==((SCREEN_WIDTH/UNIT_SIZE)-1)*UNIT_SIZE && appleY2==UNIT_SIZE*i) || (appleX2==0 && appleY2==UNIT_SIZE*i))
                {
                    return newAppleFor2AppleTeleportGameMode();
                }
            }
        }
        return 0;
    }
    public int newAppleForTwinSnakeGameMode()
    {
        int i;
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
        for(i=0 ; i<bodyParts ; i++)
        {
            if((appleX==x[i] && appleY==y[i]) || (appleX==twinx[i] && appleY==twiny[i]))
            {
                return newAppleForTwinSnakeGameMode();
            }
        }
        if(isBorderButtonOn)
        {
            for(i=0 ; i<SCREEN_WIDTH/UNIT_SIZE ; i++)
            {
                if((appleX==i*UNIT_SIZE && appleY==0) || (appleX==UNIT_SIZE*i && appleY==((SCREEN_HEIGHT/UNIT_SIZE)-1)*UNIT_SIZE))
                {
                    return newAppleForTwinSnakeGameMode();
                }
                if((appleX==((SCREEN_WIDTH/UNIT_SIZE)-1)*UNIT_SIZE && appleY==UNIT_SIZE*i) || (appleX==0 && appleY==UNIT_SIZE*i))
                {
                    return newAppleForTwinSnakeGameMode();
                }
            }
        }
        return 0;
    }
    public void moveAccordingToGameMode()
    {
        switch (selectedGameModePosition)
        {
            case 0:
            case 1:
                move();
                break;
            case 2:
                moveForInverseGameMode();
                break;
            case 3:
                moveForTwinSnakeGameMode();
                break;
        }
    }
    public void move()
    {
        for(int i=bodyParts ; i>0 ; i--)
        {
            x[i] = x[i-1];
            y[i] = y[i-1];
            //System.out.println("Shifting Bodyparts i = "+i);
        }
        switch (direction)
        {
            case'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
        passThroughBorder();
    }
    public void moveForInverseGameMode()
    {
        //System.out.println();
        int tempx=x[bodyParts-1],tempy=y[bodyParts-1];
        for(int i=bodyParts ; i>0 ; i--)
        {
            x[i] = x[i-1];
            y[i] = y[i-1];
            directionArray[i]=directionArray[i-1];
        }
        if(eaten)
        {
            eaten=false;
            bodyParts++;
            //System.out.println("NEW BODY PART i = "+(bodyParts-1)+"  x = "+tempx+"  y = "+tempy);
            x[bodyParts-1]=tempx;
            y[bodyParts-1]=tempy;
        }
        //System.out.println("Head x="+x[0]+" y="+y[0]);
        //System.out.println("Tail x="+x[bodyParts-1]+" y="+y[bodyParts-1]+"\n");
        directionArray[0]=direction;
        switch (direction)
        {
            case'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
        passThroughBorder();
        /*System.out.println("COORDS IN MOVE");
        for(int i=0 ;i<bodyParts ; i++)
        {
            System.out.println("i= "+i+"  x = "+x[i]+"  y = "+y[i]);
        }*/
    }
    public void moveForTwinSnakeGameMode()
    {
        for(int i=bodyParts ; i>0 ; i--)
        {
            x[i] = x[i-1];
            y[i] = y[i-1];
            //System.out.println("Shifting Bodyparts i = "+i);
            twinx[i] = twinx[i-1];
            twiny[i] = twiny[i-1];
        }
        switch(direction)
        {
            case'U':
                y[0] = y[0] - UNIT_SIZE;
                twiny[0] = twiny[0] + UNIT_SIZE;
                break;
            case'D':
                y[0] = y[0] + UNIT_SIZE;
                twiny[0] = twiny[0] - UNIT_SIZE;
                break;
            case'L':
                x[0] = x[0] - UNIT_SIZE;
                twinx[0] = twinx[0] + UNIT_SIZE;
                break;
            case'R':
                x[0] = x[0] + UNIT_SIZE;
                twinx[0] = twinx[0] - UNIT_SIZE;
                break;
        }
        passThroughBorder();
        passThroughBorderTwinSnake();
    }
    public Color randomColorGenerator()
    {
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        return new Color(r,g,b);
    }
    public void checkAppleAccordingToGameMode()
    {
        switch (selectedGameModePosition)
        {
            case 0:
                checkApple();
                break;
            case 1:
                checkAppleFor2AppleTeleportGameMode();
                break;
            case 2:
                checkAppleForInverseGameMode();
                break;
            case 3:
                checkAppleForTwinSnakeGameMode();
                break;
        }
    }
    public void checkApple()
    {
        if(x[0]==appleX && y[0]==appleY)
        {
            playEatingAudio();
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }
    public void checkAppleFor2AppleTeleportGameMode()
    {
        if((x[0]==appleX && y[0]==appleY))
        {
            x[0]=appleX2; y[0]=appleY2;
            //playEatingAudio();
            playTeleportAndEatingSoundEffect();
            bodyParts++;
            applesEaten++;
            newAppleAccordingToGameMode();
        }
        if((x[0]==appleX2 && y[0]==appleY2))
        {
            x[0]=appleX; y[0]=appleY;
            //playEatingAudio();
            playTeleportAndEatingSoundEffect();
            bodyParts++;
            applesEaten++;
            //newApple();
            newAppleAccordingToGameMode();
        }
    }
    public void checkAppleForInverseGameMode()
    {
        //System.out.println("Direction of tail = "+directionArray[bodyParts-1]);
        if(x[0]==appleX && y[0]==appleY)
        {
            playEatingAudio();
            inverseGameModeSwitchLogic();
            //bodyParts++;
            eaten=true;
            applesEaten++;
            newApple();
        }
    }
    public void inverseGameModeSwitchLogic()
    {
        int temp;
        for(int i=0 ; i<bodyParts/2 ; i++)
        {
            temp = x[bodyParts-1-i];
            x[bodyParts-1-i] = x[i];
            x[i] = temp;
            temp = y[bodyParts-1-i];
            y[bodyParts-1-i] = y[i];
            y[i] = temp;
        }
        if(directionArray[bodyParts-1]=='R'){direction='L';}
        else if(directionArray[bodyParts-1]=='L'){direction='R';}
        else if(directionArray[bodyParts-1]=='U'){direction='D';}
        else if(directionArray[bodyParts-1]=='D'){direction='U';}
        for(int i=bodyParts-1 ; i>0 ; i--)
        {
            if(x[i]>x[i-1])
            {
                directionArray[i]='L';
            }
            else if(x[i]<x[i-1])
            {
                directionArray[i]='R';
            }
            else if(y[i]>y[i-1])
            {
                directionArray[i]='U';
            }
            else if(y[i]<y[i-1])
            {
                directionArray[i]='D';
            }
        }
        /*System.out.println("_______________________________SWITCHING LOGIC_____________________________________________");
        for(int i=0 ; i<bodyParts ; i++)
        {
            System.out.println("i = "+i+"  x = "+x[i]+"  y = "+y[i]);
        }*/
    }
    public void checkAppleForTwinSnakeGameMode()
    {
        if((x[0]==appleX && y[0]==appleY) || (twinx[0]==appleX && twiny[0]==appleY))
        {
            playEatingAudio();
            bodyParts++;
            applesEaten++;
            newAppleForTwinSnakeGameMode();
        }
    }
    public void checkCollisionsAccordingToGameMode()
    {
        switch (selectedGameModePosition)
        {
            /*case 2:
                passThroughBorder();
                break;*/
            case 3:
                checkCollisionsForTwinSnakeGameMode();
                break;
            default:
                checkCollisions();
                break;
        }
    }
    public void checkCollisions()
    {
        for(int i=bodyParts ; i>0 ; i--)
        {
            if(x[0]==x[i] && y[0]==y[i])
            {
                //System.out.println("Collision with i = "+i);
                running=false;
            }
        }
        if(isBorderButtonOn)
        {
            setBorderCollision();
        }
        if(!running)
        {
            timer.stop();
        }
    }
    public void checkCollisionsForTwinSnakeGameMode()
    {
        if((x[0]==twinx[0] && y[0]==twiny[0])) { running=false;}
        for(int i=bodyParts ; i>0 ; i--)
        {
            if((x[0]==x[i] && y[0]==y[i]) || (x[0]==twinx[i] && y[0]==twiny[i]))
            {
                //System.out.println("Collision with i = "+i);
                running=false;
            }
        }
        if(isBorderButtonOn)
        {
            setBorderCollision();
        }
        if(!running)
        {
            timer.stop();
        }
    }
    public void passThroughBorder()
    {
        if(x[0]<0)
        {
            x[0]=SCREEN_WIDTH-UNIT_SIZE;
        }
        else if(x[0]>SCREEN_WIDTH-UNIT_SIZE)
        {
            x[0]=0;
        }
        else if(y[0]<0)
        {
            y[0]=SCREEN_HEIGHT-UNIT_SIZE;
        }
        else if(y[0]>SCREEN_HEIGHT-UNIT_SIZE)
        {
            y[0]=0;
        }
    }
    public void passThroughBorderTwinSnake()
    {
        if(twinx[0]<0)
        {
            twinx[0]=SCREEN_WIDTH-UNIT_SIZE;
        }
        else if(twinx[0]>SCREEN_WIDTH-UNIT_SIZE)
        {
            twinx[0]=0;
        }
        else if(twiny[0]<0)
        {
            twiny[0]=SCREEN_HEIGHT-UNIT_SIZE;
        }
        else if(twiny[0]>SCREEN_HEIGHT-UNIT_SIZE)
        {
            twiny[0]=0;
        }
    }
    public void setBorderCollision()
    {
        if(x[0]<UNIT_SIZE || x[0]>SCREEN_WIDTH-2*UNIT_SIZE || y[0]<UNIT_SIZE || y[0]>SCREEN_HEIGHT-2*UNIT_SIZE)
        {
            running=false;
        }
    }
    public void obstacleCollision()
    {

    }
    public void gameOver(Graphics g)
    {
        bonkClip.setMicrosecondPosition(500000);
        bonkClip.start();
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free",Font.BOLD,100));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("GAME OVER",(SCREEN_WIDTH - metrics.stringWidth("GAME OVER"))/2,200);
        String score = "Score : "+applesEaten;
        g.setColor(Color.green);
        g.setFont(new Font("Ink Free",Font.BOLD,50));
        g.drawString(score,((SCREEN_WIDTH - metrics.stringWidth(score))/2)+75,300);
        setMainMenuButton();
    }
    public void setMainMenuButton()
    {
        mainMenuButton = new JButton("Main Menu");
        mainMenuButton.setFocusable(false);
        mainMenuButton.setFont(new Font("Times New Roman",Font.BOLD,40));
        mainMenuButton.setBounds(175,350,250,50);
        mainMenuButton.setBackground(null);
        mainMenuButton.setForeground(Color.white);
        mainMenuButton.addActionListener(this::actionPerformed);
        this.add(mainMenuButton);
    }
    public void setUpSnakeSoundEffects()
    {
        File file1 = new File( gameFrame.directoryPath+"\\Sound Effects\\eating-sound-effect-36186.wav");
        File file2 = new File(gameFrame.directoryPath+"\\Sound Effects\\bonk-sound-effect-36055.wav");
        File file3 = new File(gameFrame.directoryPath+"\\Sound Effects\\mixkit-game-ball-tap-2073.wav");
        File file4 = new File(gameFrame.directoryPath+"\\Sound Effects\\teleport-90324.wav");
        AudioInputStream audioInputStream1 = null, audioInputStream2 = null, audioInputStream3 = null, audioInputStream4=null;
        try {
            audioInputStream1 = AudioSystem.getAudioInputStream(file1);
            eatingClip = AudioSystem.getClip();
            eatingClip.open(audioInputStream1);

            audioInputStream2 = AudioSystem.getAudioInputStream(file2);
            bonkClip = AudioSystem.getClip();
            bonkClip.open(audioInputStream2);

            audioInputStream3 = AudioSystem.getAudioInputStream(file3);
            moveClip = AudioSystem.getClip();
            moveClip.open(audioInputStream3);

            audioInputStream4 = AudioSystem.getAudioInputStream(file4);
            teleportClip = AudioSystem.getClip();
            teleportClip.open(audioInputStream4);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }
    public void playTeleportAndEatingSoundEffect()
    {
        if(teleportClip.isRunning()) { teleportClip.stop();}
        if(eatingClip.isRunning()) { eatingClip.stop();}
        //System.out.println(eatingClip.getMicrosecondLength());
        teleportClip.setMicrosecondPosition(400000);
        teleportClip.start();
        if(teleportClip.getMicrosecondLength()==1253877)
        {
            eatingClip.setMicrosecondPosition(0);
            eatingClip.start();
        }
    }
    public void playEatingAudio()
    {
        if(eatingClip.isRunning())
        {
            eatingClip.stop();
        }
        eatingClip.setMicrosecondPosition(100);
        eatingClip.start();
    }
    public void playMoveSoundEffect()
    {
        if(moveClip.isRunning())
        {
            moveClip.stop();
        }
        moveClip.setMicrosecondPosition(0);
        moveClip.start();
    }
    static public void passData(int h,int sgmp,int ss)
    {
        highScore = h;
        selectedGameModePosition = sgmp;
        selectedSpeed = ss;
    }
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(running)
        {
            moveAccordingToGameMode();
            //System.out.println("MOVE METHOD DONE");
            checkCollisionsAccordingToGameMode();
            //System.out.println("CHECKED COLLISION");
            //passThroughBorder();
            checkAppleAccordingToGameMode();
            //System.out.println("CHECKED WHETHER APPLE EATEN");
        }
        isProcessingKey=false;
        moveClip.stop();
        //System.out.println("CALLING REPAINT");
        repaint();
        if(e.getSource()==mainMenuButton)
        {
            //System.out.println("at mainMenuButton before "+highScore);
            if(!running)
            {
                this.setVisible(false);
                if(applesEaten>highScore)
                {
                    highScore = applesEaten;
                    //System.out.println("at mainMenuButton highscore "+highScore);
                    gameMenu.setHighScore(highScore);
                }
                gameMenu.setGameMenuFeatures(true);
            }
        }
        //System.out.println("Repainted method");
    }
    public class MyKeyAdapter extends KeyAdapter
    {
        @Override
        public void keyPressed(KeyEvent e)
        {
            if(!isProcessingKey)
            {
                isProcessingKey=true;
                //System.out.println("KEY PRESSED");
                previousDirection = direction;
                switch (e.getKeyCode())
                {
                    case KeyEvent.VK_A:
                    case KeyEvent.VK_LEFT:
                        if(previousDirection!='R')
                        {
                            direction='L';
                        }
                        break;
                    case KeyEvent.VK_D :
                    case KeyEvent.VK_RIGHT:
                        if(previousDirection!='L')
                        {
                            direction='R';
                        }
                        break;
                    case KeyEvent.VK_W :
                    case KeyEvent.VK_UP:
                        if(previousDirection!='D')
                        {
                            direction='U';
                        }
                        break;
                    case KeyEvent.VK_S :
                    case KeyEvent.VK_DOWN:
                        if(previousDirection!='U')
                        {
                            direction='D';
                        }
                        break;
                }
                if(previousDirection!=direction)
                {
                    playMoveSoundEffect();
                }
            }
        }
    }
}
