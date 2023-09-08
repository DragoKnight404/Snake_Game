import java.util.Scanner;
public class SnakeGame
{
    public static void main(String[] args)
    {
        String directoryPath = "C:\\Users\\Aryan Kanyawar\\Desktop\\PSOOP (JAVA)\\GUI_Project2";
        //System.out.println(directoryPath.length()); //   \\ => 1 length
        //System.out.println(System.getProperty("user.dir"));
        if(!directoryPath.equals(System.getProperty("user.dir")))
        {
            System.out.print("Provide Absolute Path of SnakeGame(Dot class File) : ");
            Scanner sc = new Scanner(System.in);
            directoryPath = sc.nextLine();
            char[] path = new char[200];
            char[] copy = directoryPath.toCharArray();
            int j=0;
            for(int i=0 ; i<copy.length ; i++,j++)
            {
                if(copy[i]=='"'){j--;continue;}
                if(copy[i]=='\\')
                {
                    path[j]=copy[i];
                    j++;
                    path[j]=copy[i];
                    continue;
                }
                path[j]=copy[i];
            }
            char[] temp = new char[j];
            for(int i=0 ; i<j ; i++) { temp[i] = path[i];}
            //System.out.println(j);
            //System.out.println(path.length);
            directoryPath = String.valueOf(temp);
        }
        //System.out.println(directoryPath);
        System.setProperty("user.dir", directoryPath);
        new GameFrame(directoryPath);
    }
}
