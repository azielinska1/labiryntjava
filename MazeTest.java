import java.io.IOException;
import java.util.Iterator;

public class MazeTest {
    public static void main(String[] args)
    {
        Maze labirynt=new Maze();
        labirynt.loadFromFile("C:\\Pulpit\\lab.txt");
        System.out.println(labirynt.BFS());
        labirynt.goBack();
        for(Maze.Punkt x : labirynt)
        {
            System.out.println(x);

        }




    }
}
