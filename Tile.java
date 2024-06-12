import javax.swing.*;
import java.awt.*;

public class Tile extends JPanel {
    int n;
    int m;
    boolean sciezka;
    Tile(int n, int m) {
        this.n = n;
        this.m = m;
        this.sciezka = false; // Domyślna wartość, można ją zmienić
        this.setBorder(null);
        setPreferredSize(new Dimension(50, 50)); // Ustawiamy rozmiar pola
    }
    int getDist(Maze ma)
    {
        return ma.getDist(n,m);
    }
    char getPole(Maze ma)
    {
        return ma.getPole(n,m);
    }
}
