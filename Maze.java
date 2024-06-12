import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Maze implements Iterable<Maze.Punkt>  {
    private char[][] maze;
    private int[][] dist;
    private int n;
    private int m;
    private Punkt start;
    private Punkt stop;
    private ArrayList<Punkt> kolejne;
    public ArrayList<Punkt> wszystkie;

    Maze()
    {
        maze=new char[2050][2050];
        dist=new int[2050][2050];
    }
    int getDist(int wie, int kol)
    {
        return dist[wie][kol];
    }
    char getPole(int wie, int kol)
    {
        return maze[wie][kol];
    }
    Punkt getStart()
    {
        return start;
    }
    Punkt getStop()
    {
        return stop;
    }
    int getN()
    {
        return n;
    }
    int getM()
    {
        return m;
    }

    public void podajRozmiar(int n, int m) {
        this.m=m;
        this.n=n;
        for(int i=0;i<n;i++)
        {
            for(int j=0;j<m;j++)
            {

                dist[i][j]=-1;
            }
        }
    }

    int BFS()
    {
        Queue<Punkt> q=new LinkedList<Punkt>();
        Punkt p=new Punkt(start.getWiersz(),start.getKolumna());
        q.add(p);
        wszystkie=new ArrayList<>();
        wszystkie.add(p);
        dist[start.getWiersz()][start.getKolumna()]=0;
        while(!q.isEmpty())
        {
            Punkt teraz=q.poll();
            int nn=teraz.getWiersz();
            int mm= teraz.getKolumna();
            if(maze[nn][mm]=='K') return dist[nn][mm];
            if(teraz.getWiersz()>0)
            {
                if(dist[nn-1][mm]==-1 && maze[nn-1][mm]!='X')
                {
                    dist[nn-1][mm]=dist[nn][mm]+1;
                    q.offer(new Punkt(nn-1,mm));
                    wszystkie.add(new Punkt(nn-1,mm));
                }
            }
            if(teraz.getWiersz()<(n-1))
            {
                if(dist[nn+1][mm]==-1 && maze[nn+1][mm]!='X' )
                {
                    dist[nn+1][mm]=dist[nn][mm]+1;
                    q.offer(new Punkt(nn+1,mm));
                    wszystkie.add(new Punkt(nn+1,mm));
                }
            }
            if(teraz.getKolumna()>0)
            {
                if(dist[nn][mm-1]==-1 && maze[nn][mm-1]!='X')
                {
                    dist[nn][mm-1]=dist[nn][mm]+1;
                    q.offer(new Punkt(nn,mm-1));
                    wszystkie.add(new Punkt(nn,mm-1));
                }
            }
            if(teraz.getKolumna()<(m-1))
            {
                if(dist[nn][mm+1]==-1 && maze[nn][mm+1]!='X')
                {
                    dist[nn][mm+1]=dist[nn][mm]+1;
                    q.offer(new Punkt(nn,mm+1));
                    wszystkie.add(new Punkt(nn,mm+1));
                }
            }

        }
        return -1;
    }
    void loadFromFile(String filename)
    {
        try
        {

            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            int row = 0;
            int pom = 0;
            while ((line = reader.readLine()) != null) {
                pom=line.length();
                for (int col = 0; col < line.length(); col++) {
                    maze[row][col] = line.charAt(col);
                    if(maze[row][col]=='P') start=new Punkt(row,col);
                    else if(maze[row][col]=='K') stop=new Punkt(row,col);
                }
                System.out.println(line);
                row++;
            }
            podajRozmiar(row,pom);
            reader.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

    }
    void goBack()
    {
        for(int i=0;i<n;i++)
        {
            for(int j=0;j<m;j++)
            {

                System.out.print(dist[i][j]+" ");
            }
            System.out.print("\n");
        }
        kolejne=new ArrayList<Punkt>();
        Punkt place = stop;
        int w=dist[stop.getWiersz()][stop.getKolumna()];
        while(place != start) {
            if(place.getWiersz()==start.getWiersz() && place.getKolumna()==start.getKolumna()) break;
            kolejne.add(place);
            int nn=place.getWiersz();

            int mm=place.getKolumna();
            if(place.getWiersz()>0)
            {
                if(dist[nn-1][mm]==dist[nn][mm]-1)
                {
                    place=new Punkt(nn-1,mm);
                    continue;
                }
            }
            if(place.getWiersz()<(n-1))
            {
                if(dist[nn+1][mm]==dist[nn][mm]-1)
                {
                    place=new Punkt(nn+1,mm);
                    continue;
                }
            }
            if(place.getKolumna()>0)
            {
                // System.out.println("fskdfjskfd");
                if(dist[nn][mm-1]==dist[nn][mm]-1)
                {
                    place=new Punkt(nn,mm-1);
                    continue;
                }
            }
            if(place.getKolumna()<(m-1))
            {
                if(dist[nn][mm+1]==dist[nn][mm]-1)
                {
                    place=new Punkt(nn,mm+1);
                    continue;
                }
            }
        }


        kolejne.add(start);
        Collections.reverse(kolejne);
    }
    @Override
    public Iterator<Punkt> iterator() {
        return new IteratorMaze(this);
    }
    public class Punkt
    {
        private int wiersz;
        private int kolumna;
        Punkt(int w, int k)
        {
            wiersz=w;
            kolumna=k;
        }
        public int getWiersz() {
            return wiersz;
        }
        public int getKolumna() {
            return kolumna;
        }
        @Override
        public String toString()
        {
            return String.format("%d %d", wiersz,kolumna);
        }
    }

    public class IteratorMaze implements Iterator<Punkt>
    {
        private Maze ite;
        int id;
        IteratorMaze(Maze cos)
        {
            ite=cos;
        }
        @Override
        public boolean hasNext()
        {
            if(ite.kolejne.size()>id) return true;
            else return false;
        }

        @Override
        public Punkt next()
        {
            if(!hasNext())
            {
                throw new java.util.NoSuchElementException();
            }
            return ite.kolejne.get(id++);
        }

    }
}