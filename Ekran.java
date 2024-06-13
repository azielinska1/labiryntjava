import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Ekran extends JFrame {
    private Tile[][] tab;
    private JPanel gridPanel;
    private JButton button;
    int n;
    int m;

    public Ekran(Maze ma) {
        n = ma.getN();
        m = ma.getM();
        setTitle("Animacja");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(new Dimension(1024, 1024));
        this.setMaximumSize(new Dimension(1024, 1024));
        //this.setResizable(false);

        tab = new Tile[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                tab[i][j] = new Tile(i, j);
                tab[i][j].setSize(new Dimension(1024 / Math.max(n, m), 1024 / Math.max(n, m)));
            }
        }

        gridPanel = new JPanel(new GridLayout(n, m));
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                gridPanel.add(tab[i][j]);
            }
        }
        add(gridPanel, BorderLayout.CENTER);

        // Tworzenie przycisku
        button = new JButton("Kliknij mnie!");
        add(button, BorderLayout.SOUTH);

        // Tworzenie paska menu
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Menu");
        JMenuItem loadMenuItem = new JMenuItem("Wczytaj labirynt");
        JMenuItem saveMenuItem = new JMenuItem("Zapisz obraz");

        loadMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(Ekran.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    Maze labirynt = new Maze();
                    labirynt.loadFromFile(selectedFile.getPath());
                    System.out.println(labirynt.BFS());
                    labirynt.goBack();
                    
                    Ekran teraz = new Ekran(labirynt); // Tworzy ekran z gridem 5x5
                    teraz.koloruj(labirynt);
                }
            }
        });

        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BufferedImage image = new BufferedImage(gridPanel.getWidth(), gridPanel.getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = image.createGraphics();
                gridPanel.printAll(g2d);
                g2d.dispose();

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setSelectedFile(new File("rozwiazany_lab.png"));
                int result = fileChooser.showSaveDialog(Ekran.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        ImageIO.write(image, "png", file);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        fileMenu.add(loadMenuItem);
        fileMenu.add(saveMenuItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // Ustawienia rozmiaru i widoczności
        pack();
        setVisible(true);
    }

    void koloruj(Maze ma) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (ma.getPole(i, j) == 'X') {
                    tab[i][j].setBackground(Color.BLACK);
                } else if (ma.getPole(i, j) == 'P') {
                    tab[i][j].setBackground(Color.GREEN);
                } else if (ma.getPole(i, j) == 'K') {
                    tab[i][j].setBackground(Color.RED);
                }
            }
        }
        for (Maze.Punkt x : ma) {
            tab[x.getWiersz()][x.getKolumna()].setBackground(Color.PINK);
        }
        tab[ma.getStart().getWiersz()][ma.getStart().getKolumna()].setBackground(Color.GREEN);
        tab[ma.getStop().getWiersz()][ma.getStop().getKolumna()].setBackground(Color.RED);
    }

    public static void main(String[] args) {
        // Przykładowe użycie
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Maze labirynt = new Maze();
                labirynt.loadFromFile("maze21x21.bin");
                System.out.println(labirynt.BFS());
                labirynt.goBack();
                for (Maze.Punkt x : labirynt) {
                    System.out.println(x);
                }
                Ekran teraz = new Ekran(labirynt); // Tworzy ekran z gridem 5x5
                teraz.koloruj(labirynt);
            }
        });
    }
}
