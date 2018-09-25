import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * An implementation of Conway's Game of Life with a graphical user interface.
 *
 * @author Saim Ibrahim
 */
public class GameOfLife implements Runnable {
    // class fields
    private static final Color ALIVE_COLOUR = Color.GREEN;
    private static final Color DEAD_COLOUR = Color.LIGHT_GRAY;
    private static final int DEFAULT_LENGTH = 5;
    private static final int DEFAULT_WIDTH = 5;
    private static final int DELAY = 500;
    // instance fields
    private Board gameBoard;
    private JButton cellButtonGrid[][];
    private JFrame frame;
    private Thread thread = new Thread(this);

    /**
     * Constructs a GameOfLife object with the specified length and width.
     *
     * @param length the length of this GameOfLife; cannot be negative
     * @param width  the width of this GameOfLife; cannot be negative
     */
    public GameOfLife(int length, int width) {
        if (length < 0) {
            length = 0;
        }
        if (width < 0) {
            width = 0;
        }
        cellButtonGrid = new JButton[length][width];
        gameBoard = new Board(length, width);
        for (int xValue = 0; xValue < length; xValue++) {
            for (int yValue = 0; yValue < width; yValue++) {
                cellButtonGrid[xValue][yValue] = new JButton();
                cellButtonGrid[xValue][yValue].addActionListener(new cellButtonListener(xValue, yValue));
                cellButtonGrid[xValue][yValue].setBackground(DEAD_COLOUR);
                cellButtonGrid[xValue][yValue].setFocusPainted(false);
            }
        }
        makeFrame();
        makeMenuBar();
        makeGridLayout();
    }

    /*
     * Constructors
     */

    /**
     * Creates an instance of the GameOfLife class on a 50 x 50 grid.
     *
     * @param argument not used
     */
    public static void main(String[] argument) {
        GameOfLife simulation = new GameOfLife(DEFAULT_LENGTH, DEFAULT_WIDTH);
    }

    /*
     * Mutators
     */

    /**
     * Applies the rules of Conway's Game of Life and and updates the GUI to reflect changes.
     */
    public void applyRules() {
        gameBoard.checker();
        buildGUIBoard();
    }

    /**
     * Builds the GUI interface and assigns the JButtons cells with a color corresponding to their state.
     */
    public void buildGUIBoard() {
        for (int xValue = 0; xValue < gameBoard.getMaxX(); xValue++) {
            for (int yValue = 0; yValue < gameBoard.getMaxY(); yValue++) {
                if (gameBoard.getStateOfCell(xValue, yValue)) {
                    cellButtonGrid[xValue][yValue].setBackground(ALIVE_COLOUR);
                } else if (!gameBoard.getStateOfCell(xValue, yValue)) {
                    cellButtonGrid[xValue][yValue].setBackground(DEAD_COLOUR);
                }
            }
        }
    }

    /**
     * Changes the state of each cell represented by the JButton to alive or dead.
     *
     * @param xValue the x coordinate of the JButton; cannot be negative
     * @param yValue the y coordinate of the JButton; cannot be negative
     */
    public void buttonPress(int xValue, int yValue) {
        if (!gameBoard.getStateOfCell(xValue, yValue)) {
            gameBoard.setCellAlive(xValue, yValue);
            buildGUIBoard();
        } else if (gameBoard.getStateOfCell(xValue, yValue)) {
            gameBoard.setCellDead(xValue, yValue);
            buildGUIBoard();
        }
    }

    /**
     * Creates the Grid Layout and adds JButtons to the frame.
     */
    public void makeGridLayout() {
        GridLayout gameBoardBoard = new GridLayout(gameBoard.getMaxX(), gameBoard.getMaxY());
        frame.setLayout(gameBoardBoard);
        for (int xValue = 0; xValue < gameBoard.getMaxX(); xValue++) {
            for (int yValue = 0; yValue < gameBoard.getMaxY(); yValue++) {
                frame.add(cellButtonGrid[xValue][yValue]);
            }
        }
        frame.pack();
    }

    /**
     * Creates the Swing frame and its content.
     */
    public void makeFrame() {
        frame = new JFrame("Game of Life");
        Container contentPane = frame.getContentPane();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * Creates the JMenuBar for the GUI implementation.
     */
    public void makeMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        menuBar.setBackground(Color.WHITE);
        menuBar.setForeground(Color.WHITE);

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        JMenu gameBoardMenu = new JMenu("Game");
        menuBar.add(gameBoardMenu);
        JMenu helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);

        JMenuItem runMenuItem = new JMenuItem("Run");
        fileMenu.add(runMenuItem);
        runMenuItem.addActionListener(new runItemListener());

        JMenuItem quitMenuItem = new JMenuItem("Quit");
        fileMenu.add(quitMenuItem);
        quitMenuItem.addActionListener(new quitItemListener());

        JMenuItem randomMenuItem = new JMenuItem("Randomize");
        gameBoardMenu.add(randomMenuItem);
        randomMenuItem.addActionListener(new randomItemListener());

        JMenuItem pauseMenuItem = new JMenuItem("Pause");
        gameBoardMenu.add(pauseMenuItem);
        pauseMenuItem.addActionListener(new pauseItemListener());

        JMenuItem resumeMenuItem = new JMenuItem("Resume");
        gameBoardMenu.add(resumeMenuItem);
        resumeMenuItem.addActionListener(new resumeItemListener());

        JMenuItem aboutMenuItem = new JMenuItem("About");
        helpMenu.add(aboutMenuItem);
    }

    /**
     * Pauses the execution of the rules in the GUI application.
     */
    public void pauseGame() {
        thread.suspend();
    }

    /**
     * Sets random JButton cells to be set to alive.
     */
    public void randomizeGame() {
        gameBoard.randomAlive();
        buildGUIBoard();
    }

    /**
     * Resumes the execution of the rules in the GUI application.
     */
    public void resumeGame() {
        thread.resume();
    }

    /**
     * Runs the rules of Conway's Game of Life with a one second delay inside of a loop.
     */
    public void run() {
        do {
            applyRules();
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException error) {
                System.err.println("Error: Could not delay.");
            }
        }
        while (!gameBoard.getStability());
    }

    /**
     * Executes the <code>run()</code> method on the press of the Run JMenuItem.
     */
    public void runFile() {
        try {
            thread.start();
        } catch (IllegalThreadStateException exception) {
            System.err.println("Error: Cannot start thread twice.");
        }
    }

    /**
     * Closes the GUI application and stops the JVM.
     */
    public void quitFile() {
        System.exit(0);
    }

    class cellButtonListener implements ActionListener {
        private int x;
        private int y;

        public cellButtonListener(int xValue, int yValue) {
            x = xValue;
            y = yValue;
        }

        public void actionPerformed(ActionEvent event) {
            buttonPress(x, y);
        }
    }

    class runItemListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            runFile();
        }
    }

    class quitItemListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            quitFile();
        }
    }

    class resumeItemListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            resumeGame();
        }
    }

    class pauseItemListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            pauseGame();
        }
    }

    class randomItemListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            randomizeGame();
        }
    }
}