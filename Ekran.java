import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Ekran extends JFrame {
    private Tile[][] tab;
    private JPanel gridPanel;
    private JButton findPathButton;
    private JButton setStartButton;
    private JButton setEndButton;
    private JMenuItem loadMenuItem;
    private JMenuItem saveMenuItem;
    private Maze labirynt;
    private boolean startKlik;
    private boolean stopKlik;
    int n;
    int m;

    public Ekran(Maze ma) {
        n = ma.getN();
        m = ma.getM();
        setTitle("Labyrinth Slayer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(new Dimension(1024, 1024));
        this.setMaximumSize(new Dimension(1024, 1024));
        //this.setResizable(false);

        labirynt = ma;

        tab = new Tile[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                tab[i][j] = new Tile(i, j);
                tab[i][j].setSize(new Dimension(1024 / Math.max(n, m), 1024 / Math.max(n, m)));
                final int ii=i;
                final int jj=j;
                tab[i][j].addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        if(startKlik && !(ii==labirynt.getStop().getWiersz() && jj==labirynt.getStop().getKolumna()) ) {

                            labirynt.setStart(ii,jj);
                            koloruj(labirynt,false);
                            startKlik=false;
                        }
                        else if(stopKlik && !(ii==labirynt.getStart().getWiersz() && jj==labirynt.getStart().getKolumna()))
                        {
                            labirynt.setStop(ii,jj);
                            koloruj(labirynt,false);
                            stopKlik=false;
                        }

                    }
                });

            }
        }

        gridPanel = new JPanel(new GridLayout(n, m));

        koloruj(labirynt,false);
        add(gridPanel, BorderLayout.CENTER);

        // Tworzenie paska menu
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Menu");
        loadMenuItem = new JMenuItem("Wczytaj labirynt");
        saveMenuItem = new JMenuItem("Zapisz obraz");

        loadMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();

                // Dodanie filtrów dla plików .bin i .txt
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Pliki tekstowe i binarne", "txt", "bin");
                fileChooser.setFileFilter(filter);
                fileChooser.addChoosableFileFilter(filter);

                int result = fileChooser.showOpenDialog(Ekran.this);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();

                    // Możesz dalej kontynuować z operacjami na wybranym pliku
                    labirynt.loadFromFile(selectedFile.getPath());
                    Maze labirynt = new Maze();
                    labirynt.loadFromFile(selectedFile.getPath());
                    System.out.println(labirynt.BFS());
                    labirynt.goBack();

                    for (Maze.Punkt x : labirynt) {
                        System.out.println(x);
                    }

                    Ekran teraz = new Ekran(labirynt);
                    teraz.koloruj(labirynt,false);
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

        // Tworzenie panelu narzędziowego
        JPanel toolbarPanel = new JPanel();
        toolbarPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        findPathButton = new JButton("Znajdź najkrótszą ścieżkę");
        setStartButton = new JButton("Wybierz start");
        setEndButton = new JButton("Wybierz koniec");

        toolbarPanel.add(findPathButton);
        toolbarPanel.add(setStartButton);
        toolbarPanel.add(setEndButton);

        add(toolbarPanel, BorderLayout.NORTH);

        // Ustawienia rozmiaru i widoczności
        pack();
        setVisible(true);

        // Dodanie action listeners dla przycisków
        findPathButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                labirynt.BFS();
                labirynt.goBack();
                koloruj(labirynt,true);
            }
        });

        setStartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implementacja zmiany pola startowego
                    startKlik=true;
                    stopKlik=false;
            }
        });

        setEndButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopKlik=true;
                startKlik=false;
            }
        });

        // Kolorowanie labiryntu bez rysowania ścieżki
        //koloruj(labirynt);
    }

    void koloruj(Maze ma, boolean sciezka) {
        gridPanel.updateUI();
        gridPanel.removeAll();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                tab[i][j] = new Tile(i, j);
                final int ii=i;
                final int jj=j;
                tab[i][j].addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent evt) {

                        if(startKlik && !(ii==labirynt.getStop().getWiersz() && jj==labirynt.getStop().getKolumna()) ) {

                            labirynt.setStart(ii,jj);
                            koloruj(labirynt,false);
                            startKlik=false;
                        }
                        else if(stopKlik && !(ii==labirynt.getStart().getWiersz() && jj==labirynt.getStart().getKolumna()))
                        {
                            labirynt.setStop(ii,jj);
                            koloruj(labirynt,false);
                            stopKlik=false;
                        }

                    }
                });
                tab[i][j].setSize(new Dimension(1024 / Math.max(n, m), 1024 / Math.max(n, m)));
                if (ma.getPole(i, j) == 'X') {
                    tab[i][j].setBackground(Color.BLACK);
                } else if (ma.getStart().getWiersz() == i && ma.getStart().getKolumna()==j ) {
                    tab[i][j].setBackground(Color.GREEN);
                } else if (ma.getStop().getWiersz() == i && ma.getStop().getKolumna()==j) {
                    tab[i][j].setBackground(Color.RED);
                } else {
                    tab[i][j].setBackground(Color.WHITE);
                }

            }
        }

        if(sciezka)
        {
            for (Maze.Punkt x : ma) {
                tab[x.getWiersz()][x.getKolumna()].setBackground(Color.PINK);
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                gridPanel.add(tab[i][j]);
            }
        }
        gridPanel.repaint();
        gridPanel.revalidate();
        // Resetowanie kolorowania dla punktu startowego i końcowego (poprawne początkowe oznaczenie)
        tab[ma.getStart().getWiersz()][ma.getStart().getKolumna()].setBackground(Color.GREEN);
        tab[ma.getStop().getWiersz()][ma.getStop().getKolumna()].setBackground(Color.RED);

    }

    public static void main(String[] args) {
        // Przykładowe użycie
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Maze labirynt = new Maze();
                labirynt.loadFromFile("maze21x21.bin");
                Ekran teraz = new Ekran(labirynt); // Tworzy ekran z gridem 5x5 bez ścieżki
            }
        });
    }
}
