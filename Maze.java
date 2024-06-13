import java.io.*;
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
    void czysc()
    {
        for(int i=0;i<n;i++)
        {
            for(int j=0;j<m;j++)
            {
                dist[i][j] = -1;
            }
        }
        kolejne.clear();
        wszystkie.clear();
    }
    Maze()
    {
        kolejne=new ArrayList<>();
        wszystkie=new ArrayList<>();
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
    void setStart(int p1, int p2){
        start=new Punkt(p1,p2);
    }
    void setStop(int p1, int p2){
        stop=new Punkt(p1,p2);
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
        czysc();
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
            if(stop.getWiersz()==nn && stop.getKolumna()==mm) return dist[nn][mm];
            if(teraz.getWiersz()>0)
            {
                if(dist[nn-1][mm]==-1 && (maze[nn-1][mm]!='X' || (stop.getWiersz()==(nn-1) && stop.getKolumna()==(mm)) ))
                {
                    dist[nn-1][mm]=dist[nn][mm]+1;
                    q.offer(new Punkt(nn-1,mm));
                    wszystkie.add(new Punkt(nn-1,mm));
                }
            }
            if(teraz.getWiersz()<(n-1))
            {
                if(dist[nn+1][mm]==-1 && (maze[nn+1][mm]!='X'|| (stop.getWiersz()==(nn+1) && stop.getKolumna()==(mm)) ) )
                {
                    dist[nn+1][mm]=dist[nn][mm]+1;
                    q.offer(new Punkt(nn+1,mm));
                    wszystkie.add(new Punkt(nn+1,mm));
                }
            }
            if(teraz.getKolumna()>0)
            {
                if(dist[nn][mm-1]==-1 && (maze[nn][mm-1]!='X'|| (stop.getWiersz()==(nn) && stop.getKolumna()==(mm-1)) ))
                {
                    dist[nn][mm-1]=dist[nn][mm]+1;
                    q.offer(new Punkt(nn,mm-1));
                    wszystkie.add(new Punkt(nn,mm-1));
                }
            }
            if(teraz.getKolumna()<(m-1))
            {
                if(dist[nn][mm+1]==-1 && (maze[nn][mm+1]!='X' || (stop.getWiersz()==(nn) && stop.getKolumna()==(mm+1)) ))
                {
                    dist[nn][mm+1]=dist[nn][mm]+1;
                    q.offer(new Punkt(nn,mm+1));
                    wszystkie.add(new Punkt(nn,mm+1));
                }
            }

        }
        return -1;
    }

    public void loadFromFile(String filename) {
        if (filename.endsWith(".txt")) {
            loadFromTextFile(filename);
        } else if (filename.endsWith(".bin")) {
            loadFromBinaryFile(filename);
        } else {
            System.out.println("Plik nieobslugiwany (zly typ)!");
        }
    }


    private static class MazeHeader {
        int fileId;
        byte escape;
        short columns;
        short lines;
        short entryX;
        short entryY;
        short exitX;
        short exitY;
        byte[] reserved = new byte[12];
        int counter;
        int solutionOffset;
        byte separator;
        byte wall;
        byte path;
    }

    private void loadFromBinaryFile(String filename) {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(filename))) {
            MazeHeader header = new MazeHeader();
            header.fileId = dis.readInt();
            header.escape = dis.readByte();
            header.columns = dis.readShort();
            header.lines = dis.readShort();
            header.entryX = dis.readShort();
            header.entryY = dis.readShort();
            header.exitX = dis.readShort();
            header.exitY = dis.readShort();
            dis.skipBytes(12);
            header.counter = dis.readInt();
            header.solutionOffset = dis.readInt();
            header.separator = (byte) (dis.readByte() & 0xFF);
            header.wall = (byte) (dis.readByte() & 0xFF);
            header.path = (byte) (dis.readByte() & 0xFF);

            short columns = Short.reverseBytes(header.columns);
            int fileId = Integer.reverseBytes(header.fileId);
            short lines = Short.reverseBytes(header.lines);
            short entryX = Short.reverseBytes(header.entryX);
            short entryY = Short.reverseBytes(header.entryY);
            short exitX = Short.reverseBytes(header.exitX);
            short exitY = Short.reverseBytes(header.exitY);
            byte[] reserved = new byte[12];
            int counter = Integer.reverseBytes(header.counter);
            int solutionOffset = Integer.reverseBytes(header.solutionOffset);


            int suma = 0;
            byte[] values = new byte[3];
            int row = 0;
            int col = 0;
            int ile;
            int iloraz = (int)(columns*lines);
            for(int i=0; i<iloraz; i+=(ile+1)) {
                for (int j = 0; j < 3; j++) {
                    values[j] = (byte) (dis.readByte() & 0xFF);
                }

                ile = (int) values[2];
                if(ile == -1) {
                    ile = 512;
                }


                if (values[1] == header.wall) {
                    for(int ii = 0; ii<=ile; ii++){
                        maze[row][col] = 'X';
                        col++;
                        if(col>=columns){
                            col%=columns;
                            row++;
                        }
                        suma++;
                    }

                } else if (values[1] == header.path) {
                    for(int ii = 0; ii<=ile; ii++){
                        maze[row][col] = ' ';
                        col++;
                        if(col>=columns){
                            col%=columns;
                            row++;
                        }
                        suma++;
                    }
                }
            }

            start = new Punkt(entryY-1, entryX-1);
            stop = new Punkt(exitY-1, exitX-1);
            maze[entryY-1][entryX-1] = 'P';
            maze[exitY-1][exitX-1] = 'K';

            podajRozmiar(lines, columns);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void loadFromTextFile(String filename)
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
                    try {
                        maze[row][col] = line.charAt(col);
                        if (maze[row][col] == 'P') start = new Punkt(row, col);
                        else if (maze[row][col] == 'K') stop = new Punkt(row, col);
                    } catch (Exception e) {
                        System.err.println("Coś się nie zgadza z plikiem: " + e.getMessage());
                        System.exit(1);
                    }
                }
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
            System.out.println("->"+place);
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
