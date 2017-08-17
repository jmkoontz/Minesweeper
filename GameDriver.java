import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;

/**
 * Creates the user interface and handles interactions with the user.
 *
 * @author Jake Koontz
 * Last update: 8/5/17
 */

public class GameDriver {
    // constants to hold difficulty levels
    public static final int BEGINNER = 1;
    public static final int INTERMEDIATE = 2;
    public static final int EXPERT = 3;

    // constants to hold size properties
    private static final int CELL_SIZE = 24;    // side length of cell
    private static final int MAX_HEIGHT = 37;
    private static final int MAX_WIDTH = 80;
    private static final int MIN_HEIGHT = 3;
    private static final int MIN_WIDTH = 8;
    private static final int MIN_NUM_MINES = 1;

    // GUI components
    private JFrame frame;
    private JPanel board;
    private JButton gameButton;
    private JLabel minesLabel;
    private JLabel timeLabel;
    private JMenuItem statsItem;

    // images for buttons
    private Image imgSmileFace, imgWinnerFace, imgDeadFace, imgClickedFace;
    private Image imgUnopened, imgFlag, imgQMark, imgMine, imgClickedMine, imgWrongMine;
    private Image imgZero, imgOne, imgTwo, imgThree, imgFour, imgFive, imgSix, imgSeven, imgEight;

    // game properties
    private Game game;
    private Scores scores;
    private int difficulty;
    private boolean qMarks;
    private int height;
    private int width;
    private int numMines;

    // timer properties
    private Timer timer;
    private long startTime;

    // miscellaneous fields
    private Cell[][] spaces;    // all cells on the board
    private int mineCount;  // tracks number of mines remaining
    private boolean firstSpace = true;  // signifies first space to be clicked
    private boolean gameOver;   // signifies game being over

    /**
     * Set properties of the game.
     *
     * @param game the current game being played
     * @param scores the lists of high scores
     * @param difficulty the difficulty level
     * @param qMarks boolean representing whether or not question marks are allowed
     */
    public GameDriver(Game game, Scores scores, int difficulty, boolean qMarks) {
        // set properties of game
        this.game = game;
        this.scores = scores;
        this.difficulty = difficulty;
        this.qMarks = qMarks;

        height = game.getHeight();
        width = game.getWidth();
        numMines = game.getNumMines();

        mineCount = numMines;   // sets number of mines remaining
        spaces = new Cell[height][width];   // array of spaces on board

        init();     // set up frame
        setBoard();     // set up board
    }

    /**
     * Set up the frame and load images.
     */
    private void init() {
        // set properties of frame
        frame = new JFrame("Minesweeper");
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);  // opens frame in center of screen
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // load files for new game button
        ImageIcon smileFace = new ImageIcon("C:\\Users\\Jake\\IdeaProjects\\Side Projects\\Minesweeper\\src\\Smile.jpg");
        ImageIcon winnerFace = new ImageIcon("C:\\Users\\Jake\\IdeaProjects\\Side Projects\\Minesweeper\\src\\Winner.jpg");
        ImageIcon deadFace = new ImageIcon("C:\\Users\\Jake\\IdeaProjects\\Side Projects\\Minesweeper\\src\\Dead.jpg");
        ImageIcon clickedFace = new ImageIcon("C:\\Users\\Jake\\IdeaProjects\\Side Projects\\Minesweeper\\src\\Gasp.jpg");

        // create images for new game button
        imgSmileFace = smileFace.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        imgWinnerFace = winnerFace.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        imgDeadFace = deadFace.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        imgClickedFace = clickedFace.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);

        // load files for special game pieces
        ImageIcon unopened = new ImageIcon("C:\\Users\\Jake\\IdeaProjects\\Side Projects\\Minesweeper\\src\\Unopened.png");
        ImageIcon flag = new ImageIcon("C:\\Users\\Jake\\IdeaProjects\\Side Projects\\Minesweeper\\src\\Flag.png");
        ImageIcon qMark = new ImageIcon("C:\\Users\\Jake\\IdeaProjects\\Side Projects\\Minesweeper\\src\\Question Mark.png");
        ImageIcon mine = new ImageIcon("C:\\Users\\Jake\\IdeaProjects\\Side Projects\\Minesweeper\\src\\Bomb.jpg");
        ImageIcon clickedMine = new ImageIcon("C:\\Users\\Jake\\IdeaProjects\\Side Projects\\Minesweeper\\src\\ClickedBomb.jpg");
        ImageIcon wrongMine = new ImageIcon("C:\\Users\\Jake\\IdeaProjects\\Side Projects\\Minesweeper\\src\\WrongBomb.png");

        // create images for special game pieces
        imgUnopened = unopened.getImage().getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_SMOOTH);
        imgFlag = flag.getImage().getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_SMOOTH);
        imgQMark = qMark.getImage().getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_SMOOTH);
        imgMine = mine.getImage().getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_SMOOTH);
        imgClickedMine = clickedMine.getImage().getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_SMOOTH);
        imgWrongMine = wrongMine.getImage().getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_SMOOTH);

        // load files for numbered game pieces
        ImageIcon zero = new ImageIcon("C:\\Users\\Jake\\IdeaProjects\\Side Projects\\Minesweeper\\src\\0.png");
        ImageIcon one = new ImageIcon("C:\\Users\\Jake\\IdeaProjects\\Side Projects\\Minesweeper\\src\\1.png");
        ImageIcon two = new ImageIcon("C:\\Users\\Jake\\IdeaProjects\\Side Projects\\Minesweeper\\src\\2.png");
        ImageIcon three = new ImageIcon("C:\\Users\\Jake\\IdeaProjects\\Side Projects\\Minesweeper\\src\\3.png");
        ImageIcon four = new ImageIcon("C:\\Users\\Jake\\IdeaProjects\\Side Projects\\Minesweeper\\src\\4.png");
        ImageIcon five = new ImageIcon("C:\\Users\\Jake\\IdeaProjects\\Side Projects\\Minesweeper\\src\\5.png");
        ImageIcon six = new ImageIcon("C:\\Users\\Jake\\IdeaProjects\\Side Projects\\Minesweeper\\src\\6.png");
        ImageIcon seven = new ImageIcon("C:\\Users\\Jake\\IdeaProjects\\Side Projects\\Minesweeper\\src\\7.png");
        ImageIcon eight = new ImageIcon("C:\\Users\\Jake\\IdeaProjects\\Side Projects\\Minesweeper\\src\\8.png");

        // create images for numbered game pieces
        imgZero = zero.getImage().getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_SMOOTH);
        imgOne = one.getImage().getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_SMOOTH);
        imgTwo = two.getImage().getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_SMOOTH);
        imgThree = three.getImage().getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_SMOOTH);
        imgFour = four.getImage().getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_SMOOTH);
        imgFive = five.getImage().getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_SMOOTH);
        imgSix = six.getImage().getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_SMOOTH);
        imgSeven = seven.getImage().getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_SMOOTH);
        imgEight = eight.getImage().getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_SMOOTH);

        createMenu();   // create menu and add to frame
        createGameBar();    // create gameBar and add to frame
    }

    /**
     * Create menu and menu items and add to the frame.
     */
    private void createMenu() {
        // create menuBar to hold options, stats, and high scores
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");     // menu to hold menu items

        JMenuItem optionsItem = new JMenuItem("Options");   // item to hold game options
        gameMenu.add(optionsItem);

        statsItem = new JMenuItem("Stats");     // item to hold stats
        statsItem.setEnabled(false);    // prevents user from viewing stats before mines are generated
        gameMenu.add(statsItem);

        JMenuItem scoresItem = new JMenuItem("High Scores");    // item to hold high scores
        gameMenu.add(scoresItem);

        menuBar.add(gameMenu);  // add menu to menu bar

        // display options when optionsItem is clicked
        optionsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel optionsPanel = new JPanel(new GridLayout(1, 2));     // panel to hold sub-panels

                JPanel diffPanel = new JPanel(new GridLayout(6, 1));    // panel to hold difficulty levels
                diffPanel.setBackground(Color.LIGHT_GRAY);
                optionsPanel.add(diffPanel);

                JPanel detailsPanel = new JPanel(new GridLayout(6, 3));     // panel to hold details of difficulties
                optionsPanel.add(detailsPanel);

                // array to allow repetition
                String[] titles = {"Height", "Width", "Mines", "9", "9", "10", "16", "16", "40", "16", "30", "99"};

                // add each label
                for (int i = 0; i < titles.length; i++) {
                    JLabel l = new JLabel(titles[i]);   // create JLabel with specific title

                    // make specific titles bold
                    if (i < 3) {
                        l.setOpaque(true);  // allows background color to change
                        l.setBackground(Color.LIGHT_GRAY);
                        l.setFont(new Font("Default", Font.BOLD, 16));
                    } else
                        l.setFont(new Font("Default", Font.PLAIN, 16));

                    detailsPanel.add(l);    // add label to panel
                }

                // arrays to allow repetition
                JTextField[] tfs = new JTextField[3];
                JRadioButton[] rbs = new JRadioButton[4];

                // add text and text fields
                for (int i = 0; i < tfs.length; i++) {
                    tfs[i] = new JTextField();
                    tfs[i].setFont(new Font("Default", Font.PLAIN, 16));

                    // allows action to happen when text field is clicked
                    tfs[i].addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            rbs[3].setSelected(true);   // ensures "Custom" radio button is selected
                        }
                    });

                    detailsPanel.add(tfs[i]);   // add text field to panel
                }

                // create and format checkbox to handle question marks
                JCheckBox cBox = new JCheckBox(" Marks (?)");
                cBox.setFont(new Font("Default", Font.PLAIN, 16));
                cBox.setBackground(Color.LIGHT_GRAY);
                cBox.setFocusPainted(false);    // removes border around selected button
                if (qMarks)     // check if question marks are enabled
                    cBox.setSelected(true);
                detailsPanel.add(cBox);     // add checkbox to panel

                // add blank labels to fill in space
                for (int i = 0; i < 3; i++) {
                    JLabel blank = new JLabel();
                    blank.setOpaque(true);  // allows background color to change
                    blank.setBackground(Color.LIGHT_GRAY);

                    // add label to correct panel
                    if (i == 0)
                        diffPanel.add(blank);
                    else
                        detailsPanel.add(blank);
                }

                ButtonGroup bg = new ButtonGroup();     // ensures only one radio button can be selected
                String[] rbsTitles = {"Beginner", "Intermediate", "Expert", "Custom"};   // array to allow repetition

                // add radio buttons
                for (int i = 0; i < rbsTitles.length; i++) {
                    rbs[i] = new JRadioButton(rbsTitles[i]);     // create JRadioButton with specific text
                    rbs[i].setFocusPainted(false);  // removes border around selected button
                    bg.add(rbs[i]);     // add radio button to button group

                    // make specific radio button titles bold
                    if (i < 3)
                        rbs[i].setFont(new Font("Default", Font.BOLD, 16));
                    else
                        rbs[i].setFont(new Font("Default", Font.PLAIN, 16));

                    diffPanel.add(rbs[i]);  // add radio button to panel
                }

                // set options for current game
                switch (difficulty) {
                    case BEGINNER:
                        rbs[0].setSelected(true);
                        break;
                    case INTERMEDIATE:
                        rbs[1].setSelected(true);
                        break;
                    case EXPERT:
                        rbs[2].setSelected(true);
                        break;
                    default:
                        rbs[3].setSelected(true);

                        tfs[0].setText("" + height);
                        tfs[1].setText("" + width);
                        tfs[2].setText("" + numMines);
                }

                // create and format button to make new game
                JButton newGameButton = new JButton("New Game");
                newGameButton.setFont(new Font("Default", Font.PLAIN, 18));
                newGameButton.setBackground(Color.LIGHT_GRAY);
                newGameButton.setFocusPainted(false);   // removes border around selected button

                // create new game when button is clicked
                newGameButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // set minimum values for game properties
                        int tempHeight = MIN_HEIGHT;
                        int tempWidth = MIN_WIDTH;
                        int tempNumMines = MIN_NUM_MINES;

                        int tempDifficulty = 0;     // holds difficulty so that it can be changed and set at the end

                        // set correct difficulty
                        if (rbs[0].isSelected()) {
                            tempHeight = 9;
                            tempWidth = 9;
                            tempNumMines = 10;
                            tempDifficulty = BEGINNER;
                        } else if (rbs[1].isSelected()) {
                            tempHeight = 16;
                            tempWidth = 16;
                            tempNumMines = 40;
                            tempDifficulty = INTERMEDIATE;
                        } else if (rbs[2].isSelected()) {
                            tempHeight = 16;
                            tempWidth = 30;
                            tempNumMines = 99;
                            tempDifficulty = EXPERT;
                        } else if (rbs[3].isSelected()) {   // if custom difficulty is selected
                            // read and set values from each text field
                            for (int i = 0; i < tfs.length; i++) {
                                Scanner scan = new Scanner(tfs[i].getText());   // get text from text field

                                // set value from each text field
                                if (scan.hasNextInt()) {    // check if there is a valid entry
                                    switch (i) {
                                        case 0:     // first text field
                                            tempHeight = scan.nextInt();    // set tempHeight

                                            // check bounds of value
                                            if (tempHeight > MAX_HEIGHT)
                                                tempHeight = MAX_HEIGHT;
                                            else if (tempHeight < MIN_HEIGHT)
                                                tempHeight = MIN_HEIGHT;

                                            break;
                                        case 1:     // second text field
                                            tempWidth = scan.nextInt();     // set tempWidth

                                            // check bounds of value
                                            if (tempWidth > MAX_WIDTH)
                                                tempWidth = MAX_WIDTH;
                                            else if (tempWidth < MIN_WIDTH)
                                                tempWidth = MIN_WIDTH;

                                            break;
                                        case 2:     // third text field
                                            tempNumMines = scan.nextInt();  // set tempNumMines

                                            // check bounds of value
                                            if (tempNumMines > (tempHeight * tempWidth - 10))
                                                tempNumMines = tempHeight * tempWidth - 10;
                                            else if (tempNumMines < MIN_NUM_MINES)
                                                tempNumMines = MIN_NUM_MINES;

                                            break;
                                    }

                                    scan.close();
                                } else {
                                    // clear text fields if value is invalid or blank
                                    for (JTextField tf : tfs)
                                        tf.setText("");

                                    return;
                                }
                            }
                        } else
                            return;

                        boolean tempQMarks = false;     // holds qMarks so that it can be changed and set at the end
                        if (cBox.isSelected())
                            tempQMarks = true;

                        frame.dispose();    // remove old board

                        Game g = new Game(tempHeight, tempWidth, tempNumMines);     // creates model of game mechanics
                        new GameDriver(g, scores, tempDifficulty, tempQMarks);  // creates gameDriver to run game
                    }
                });

                diffPanel.add(newGameButton);   // add newGameButton to panel

                UIManager.put("OptionPane.border", new EmptyBorder(5, 0, 0, 0));    // removes padding around dialog box

                // create and show dialog with no button
                JOptionPane.showOptionDialog(frame, optionsPanel, "Game Options", JOptionPane.DEFAULT_OPTION,
                        JOptionPane.PLAIN_MESSAGE, null, new Object[]{}, null);
            }
        });

        // display stats when statsItem is clicked
        statsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameStats gameStats = new GameStats(game);  // create instance of gameStats for current game

                JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JTextArea statsTextArea = new JTextArea();

                // array to allow repetition
                String[] labels = {"Height:  " + game.getHeight(), "Width:  " + game.getWidth(),
                        "Mines:  " + game.getNumMines(), gameStats.getNumSpaces(), gameStats.getMineDensity(),
                        gameStats.getFrequencies()};

                // add each stat
                for (int i = 0; i < labels.length; i++) {
                    statsTextArea.append(labels[i]);

                    // add newline for every stat but the last
                    if (i != labels.length - 1)
                        statsTextArea.append("\n");
                }

                // format statsTextArea
                statsTextArea.setFont(new Font("Default", Font.PLAIN, 16));
                statsTextArea.setBackground(null);  // sets background to light gray
                statsTextArea.setEditable(false);
                statsPanel.add(statsTextArea);

                // create and show dialog with no button
                JOptionPane.showOptionDialog(frame, statsPanel, "Stats", JOptionPane.DEFAULT_OPTION,
                        JOptionPane.PLAIN_MESSAGE, null, new Object[]{}, null);
            }
        });

        // display scores when scoresItem is clicked
        scoresItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel scoresPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JTextArea scoresTextArea = new JTextArea();

                int[] levels = {BEGINNER, INTERMEDIATE, EXPERT};    // array to allow repetition

                // add scores from each difficulty
                for (int i = 0; i < levels.length; i++) {
                    scoresTextArea.append(scores.getScores(levels[i]));

                    // add newline for every difficulty but the last
                    if (i != levels.length - 1)
                        scoresTextArea.append("\n");
                }

                // format scoresTextArea
                scoresTextArea.setFont(new Font("Default", Font.PLAIN, 16));
                scoresTextArea.setBackground(null);     // sets background to light gray
                scoresTextArea.setEditable(false);
                scoresPanel.add(scoresTextArea);

                // create and show dialog with no button
                JOptionPane.showOptionDialog(frame, scoresPanel, "Best Times", JOptionPane.DEFAULT_OPTION,
                        JOptionPane.PLAIN_MESSAGE, null, new Object[]{}, null);
            }
        });

        frame.add(menuBar, BorderLayout.NORTH);     // add menuBar to frame
    }

    /**
     * Create panel to hold various game properties and add it to the frame.
     */
    private void createGameBar() {
        // set up top game panel
        JPanel gameBar = new JPanel(new GridBagLayout());
        gameBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));     // top, left, bottom, right
        gameBar.setBackground(Color.LIGHT_GRAY);
        GridBagConstraints constraints = new GridBagConstraints();    // controls placement of items in gameBar

        // set up label to display number of mines remaining
        minesLabel = new JLabel("" + mineCount);
        minesLabel.setHorizontalAlignment(JLabel.CENTER);
        minesLabel.setFont(new Font("Stencil", Font.PLAIN, 24));
        minesLabel.setPreferredSize(new Dimension(60, minesLabel.getPreferredSize().height));   // prevents label from moving other components
        constraints.gridx = 0;    // column location
        constraints.gridy = 0;    // row location
        constraints.weightx = 1;  // creates distance between other items
        gameBar.add(minesLabel, constraints);

        // set up button to control game
        gameButton = new JButton();
        gameButton.setPreferredSize(new Dimension(40, 40));
        gameButton.setBorder(new LineBorder(Color.GRAY));   // removes rounded edges
        gameButton.setIcon(new ImageIcon(imgSmileFace));
        gameButton.addActionListener(new ActionListener() {     // starts a new game when clicked
            @Override
            public void actionPerformed(ActionEvent e) {
                newGame();
            }
        });
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.weightx = 1;
        gameBar.add(gameButton, constraints);

        // set up label to display elapsed time
        timeLabel = new JLabel("0");
        timeLabel.setHorizontalAlignment(JLabel.CENTER);
        timeLabel.setFont(new Font("Stencil", Font.PLAIN, 24));
        timeLabel.setPreferredSize(new Dimension(60, timeLabel.getPreferredSize().height));
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.weightx = 1;
        gameBar.add(timeLabel, constraints);

        // add timer to update gameBar
        timer = new Timer(100, new ActionListener() {   // updates every tenth of a second
            @Override
            public void actionPerformed(ActionEvent e) {
                minesLabel.setText("" + mineCount);
                timeLabel.setText("" + (System.nanoTime() - startTime) / 1000000000);
            }
        });

        frame.add(gameBar, BorderLayout.CENTER);     // add gameBar to frame
    }

    /**
     * Set up the board that holds each space.
     */
    private void setBoard() {
        board = new JPanel(new GridLayout(height, width));

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Cell c = new Cell(i, j);
                spaces[i][j] = c;   // adds cell to list of all spaces

                // set properties of cell
                c.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
                c.setIcon(new ImageIcon(imgUnopened));
                c.setBorderPainted(false);  // disables glow around selected cell

                // add listener to handle interaction with user
                c.addMouseListener(new MouseAdapter() {
                    boolean pressed;    // tracks whether cell is pressed

                    @Override
                    public void mousePressed(MouseEvent e) {
                        c.getModel().setArmed(true);
                        c.getModel().setPressed(true);
                        pressed = true;

                        // update face on gameButton
                        if (!SwingUtilities.isRightMouseButton(e) && !gameOver)
                            gameButton.setIcon(new ImageIcon(imgClickedFace));
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        c.getModel().setArmed(false);
                        c.getModel().setPressed(false);

                        // update face on gameButton
                        if (!gameOver)
                            gameButton.setIcon(new ImageIcon(imgSmileFace));

                        // handle clicked cell
                        if (pressed && !c.isCleared()) {    // check that space has not been cleared
                            if (SwingUtilities.isRightMouseButton(e)) {     // check for right click
                                if (c.isFlagged()) {    // check for flag
                                    // set correct image
                                    if (qMarks)     // check if question marks are allowed
                                        c.setIcon(new ImageIcon(imgQMark));
                                    else {
                                        c.setIcon(new ImageIcon(imgUnopened));
                                        c.setMarked(false);
                                    }

                                    c.setFlagged(false);
                                    mineCount++;    // increment count of remaining mines
                                } else if (qMarks && c.isMarked()) {  // check for question mark
                                    c.setIcon(new ImageIcon(imgUnopened));
                                    c.setMarked(false);
                                } else {
                                    c.setIcon(new ImageIcon(imgFlag));
                                    c.setFlagged(true);
                                    c.setMarked(true);

                                    mineCount--;    // decrement count of remaining mines
                                }
                            } else {    // left click
                                if (!c.isMarked()) {      // check for no mark
                                    for (int i = 0; i < height; i++) {
                                        for (int j = 0; j < width; j++) {
                                            if (spaces[i][j] == c) {    // check for correct cell
                                                if (firstSpace) {   // check for first click of game
                                                    game.placeMines(i, j);  // place mines on board
                                                    game.generateNumbers();     // generate number for each space

                                                    startTime = System.nanoTime();  // get time at start of game
                                                    timer.restart();    // start timer
                                                    firstSpace = false;
                                                    statsItem.setEnabled(true);     // allows user to view stats
                                                }

                                                c.setCleared(true);     // clear space to prevent future click
                                                showSpace(c, game.getValue(i, j));  // display space on screen
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        pressed = false;
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        pressed = false;
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        pressed = true;
                    }
                });

                board.add(c);   // add cell to board
            }

            frame.add(board, BorderLayout.SOUTH);   // add board to frame
            frame.pack();   // pack all components together
            frame.setVisible(true);     // displays frame on screen
        }
    }

    /**
     * Accessor method for Cell.
     *
     * @param row vertical position of cell
     * @param col horizontal position of cell
     * @return cell at specified position
     */
    private Cell getCell(int row, int col) {
        return spaces[row][col];
    }

    /**
     * Find and display empty spaces adjacent to the clicked cell.
     *
     * @param row vertical position of cell
     * @param col horizontal position of cell
     */
    private void showEmptySpaces(int row, int col) {
        if (row >= 0 && row < height && col >= 0 && col < width) {  // check bounds
            Cell c = getCell(row, col);     // finds cell at specified location

            // prevent infinite search
            if (c.isChecked()) {    // check to see if previously checked
                if (c.isMarked())   // check for mark
                    c.setMarkedAndEmpty(true);  // sets cell as marked and empty

                return;
            }

            // handle space
            if (!c.isMarked()) {    // check for mark
                c.setCleared(true);

                int value = game.getValue(row, col);    // get value of space

                if (value == 0)
                    c.setIcon(new ImageIcon(imgZero));
                else if (value != Game.MINE) {   // check that value is a nonzero number
                    showSpace(c, value);    // show number
                    return;
                }
            }

            c.setChecked(true);

            // check each adjacent cell
            showEmptySpaces(row - 1, col - 1);
            showEmptySpaces(row - 1, col);
            showEmptySpaces(row - 1, col + 1);
            showEmptySpaces(row, col - 1);
            showEmptySpaces(row, col + 1);
            showEmptySpaces(row + 1, col - 1);
            showEmptySpaces(row + 1, col);
            showEmptySpaces(row + 1, col + 1);
        }
    }

    /**
     * Show value of the selected cell on the board.
     *
     * @param c selected cell
     * @param value value of space corresponding to the cell
     */
    private void showSpace(Cell c, int value) {
        // set image of cell based on value of space
        switch (value) {
            case Game.MINE:
                c.setIcon(new ImageIcon(imgClickedMine));
                gameOver(c, false);     // indicates game ending in loss
                break;
            case 0:
                if (c.isMarkedAndEmpty())   // checks for marked empty cell
                    c.setIcon(new ImageIcon(imgZero));
                else
                    showEmptySpaces(c.getRow(), c.getCol());    // find and show empty adjacent spaces

                break;
            case 1:
                c.setIcon(new ImageIcon(imgOne));
                break;
            case 2:
                c.setIcon(new ImageIcon(imgTwo));
                break;
            case 3:
                c.setIcon(new ImageIcon(imgThree));
                break;
            case 4:
                c.setIcon(new ImageIcon(imgFour));
                break;
            case 5:
                c.setIcon(new ImageIcon(imgFive));
                break;
            case 6:
                c.setIcon(new ImageIcon(imgSix));
                break;
            case 7:
                c.setIcon(new ImageIcon(imgSeven));
                break;
            case 8:
                c.setIcon(new ImageIcon(imgEight));
                break;
        }

        if (getNumActiveSpaces() == 0 && value != Game.MINE)     // checks if no active spaces are remaining
            gameOver(c, true);  // indicates game ending in win
    }

    /**
     * Determine number of non-mine spaces remaining.
     *
     * @return number of active non-mine spaces
     */
    private int getNumActiveSpaces() {
        int numActiveSpaces = height * width - numMines;

        // check each cell
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (getCell(i, j).isCleared())  // checks if cell is cleared
                    numActiveSpaces--;
            }
        }

        return numActiveSpaces;
    }

    /**
     * Handle behavior at end of game.
     *
     * @param c last-clicked cell
     * @param winner boolean to signify winning or losing
     */
    private void gameOver(Cell c, boolean winner) {
        gameOver = true;    // signifies end of game

        double endTime = System.nanoTime() - startTime;    // gets time at end of game
        timer.stop();   // stop timer

        // clear each cell
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                getCell(i, j).setCleared(true);
            }
        }

        // complete board and gameBar
        if (winner) {   // signifies winning game
            gameButton.setIcon(new ImageIcon(imgWinnerFace));
            minesLabel.setText("0");    // clear minesLabel

            double score = endTime / 1000000000.0;  // converts time to seconds
            double roundedScore = Math.round(score * 100);
            roundedScore /= 100.0;  // rounds time to two decimal places

            boolean highScore = scores.submitScore(roundedScore, difficulty);   // submit score

            // add message with winning time
            JPanel msgPanel = new JPanel(new GridLayout(0, 1));     // allows varying number of components to be added
            JLabel timeMsg = new JLabel("Time: " + roundedScore);
            timeMsg.setFont(new Font("Default", Font.PLAIN, 16));
            timeMsg.setHorizontalAlignment(SwingConstants.CENTER);
            msgPanel.add(timeMsg);

            // add message if time made the high scores list
            if (highScore) {
                JLabel highScoreMsg = new JLabel("You made the high scores list!");
                highScoreMsg.setFont(new Font("Default", Font.PLAIN, 16));
                highScoreMsg.setHorizontalAlignment(SwingConstants.CENTER);
                msgPanel.add(highScoreMsg);
            }

            // create and show dialog with no button
            JOptionPane.showOptionDialog(frame, msgPanel, "You Won!", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE, null, new Object[]{}, null);
        } else {    // signifies losing game
            gameButton.setIcon(new ImageIcon(imgDeadFace));

            // display mines
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    Cell cc = getCell(i, j);    // get current cell

                    // display every mine except for one that was clicked
                    if (game.getValue(i, j) == Game.MINE && !cc.isFlagged() && (i != c.getRow() || j != c.getCol()))
                        cc.setIcon(new ImageIcon(imgMine));

                    // display mines in cells with wrongly-placed flags
                    if (cc.isFlagged() && game.getValue(i, j) != Game.MINE)
                        cc.setIcon(new ImageIcon(imgWrongMine));
                }
            }
        }
    }

    /**
     * Reset current game and begin a new one.
     */
    private void newGame() {
        gameOver = false;   // signifies starting of game
        firstSpace = true;  // indicates first click of game
        statsItem.setEnabled(false);    // prevents user from viewing stats before mines are generated

        gameButton.setIcon(new ImageIcon(imgSmileFace));
        timer.stop();
        timeLabel.setText("0");     // reset timeLabel

        // reset components of board
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                getCell(i, j).reset();  // reset cell
                game.setValue(i, j, Game.VOID);     // completely empties space
            }
        }

        mineCount = numMines;   // reset count of remaining mines
        minesLabel.setText("" + mineCount);     // set minesLabel

        frame.remove(board);    // remove old board from frame
        setBoard();     // set up new board
        frame.revalidate();     // reload frame
        frame.repaint();    // reload graphics on frame
    }

    /**
     * Create game and run it.
     *
     * @param args none
     */
    public static void main(String[] args) {
        int height = 16;
        int width = 30;
        int numMines = 99;

        Game g = new Game(height, width, numMines);     // creates model of game mechanics
        Scores s = new Scores();    // initializes high scores

        new GameDriver(g, s, EXPERT, false);  // creates gameDriver to run game, false signifies no question marks
    }
}