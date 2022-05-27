import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class AqueductsGUI extends JFrame implements MouseListener, MouseMotionListener {
    // Variables
    private final JPanel game;
    private final JList levelList;
    private final GridBagConstraints c;
    private final JTextField gameStatus;
    private final JTextField levelName;
    private final JTextField endpointProgress;
    private final JTextField cellProgress;
    private final JTextField title1;
    private final JTextField title2;
    private final JButton resetButton;
    private final JButton colorChanger;
    private final GameCell[][] gameCells = new GameCell[7][7];
    private final Level[] gameLevels = new Level[24];
    private final Color[] mainColor;
    private int mainColorIndex;
    private final String[] colorChangerFilePaths = new String[]{"images/ColorChangerC.png", "images/ColorChangerR.png",
            "images/ColorChangerW.png", "images/ColorChangerM.png", "images/ColorChangerO.png", "images/ColorChangerY.png"};
    private final String[] resetButtonFilePaths = new String[]{"images/ResetIconC.png", "images/ResetIconR.png",
            "images/ResetIconW.png", "images/ResetIconM.png", "images/ResetIconO.png", "images/ResetIconY.png"};
    private boolean isCreatingPath = false;
    private final ArrayList<Path> paths = new ArrayList<>();
    private Path currentPath;
    private GameCell current, previous, previous2;
    private int completePathsReq = 0;
    private int levelIndex = 0;
    private boolean gameComplete = false;

    // Constructor
    public AqueductsGUI() {
        // Figuring out what OS program is being run in
        String OS = System.getProperty("os.name");

        // Initializing
        JFrame window = new JFrame();
        GridBagLayout layout = new GridBagLayout();
        // Window size differs from Windows to Mac because the title bar, which is included in window size has different heights
        Dimension windowSize;
        if (OS.contains("Windows")) {
            windowSize = new Dimension(700, 730);
        } else {
            windowSize = new Dimension(700, 720);
        }
        title1 = new JTextField("FLOW PAID:");
        title2 = new JTextField("AQUEDUCTS");
        resetButton = new JButton(new ImageIcon(resetButtonFilePaths[0]));
        colorChanger = new JButton(new ImageIcon(colorChangerFilePaths[0]));

        game = new JPanel(layout);
        c = new GridBagConstraints();
        levelList = new JList();
        gameStatus = new JTextField("INCOMPLETE");
        levelName = new JTextField();
        endpointProgress = new JTextField();
        cellProgress = new JTextField();
        mainColor = new Color[]{Color.CYAN, Color.RED, Color.WHITE, Color.MAGENTA, Color.ORANGE, Color.YELLOW};
        mainColorIndex = 0;

        // Initializing all levels
        // 1
        String title = "Level 1: Uno";
        int[] xValues = {5,5,6,0,0,1,1,2,0,3,6,5};
        int[] yValues = {1,4,2,6,1,2,1,3,2,5,1,5};
        String[] colors = {"purple","red","blue","yellow","orange","green"};
        gameLevels[0] = new Level(title, xValues, yValues, colors);
        // 2
        title = "Level 2: Long Blue";
        xValues = new int[]{0,3,6,4,0,2,6,5,6,1,1,4};
        yValues = new int[]{0,1,0,1,1,1,1,5,2,4,5,6};
        colors = new String[]{"purple","red","orange","blue","green","yellow"};
        gameLevels[1] = new Level(title, xValues, yValues, colors);
        // 3
        title = "Level 3: Sunset";
        xValues = new int[]{0,2,2,3,3,3,4,6,6,1,0,0};
        yValues = new int[]{0,3,0,2,0,3,0,6,0,5,1,3};
        colors = new String[]{"blue","purple","green","orange","red","yellow"};
        gameLevels[2] = new Level(title, xValues, yValues, colors);
        // 4
        title = "Level 4: Compact";
        xValues = new int[]{4,1,5,1,1,3,3,4,5,4,1,2};
        yValues = new int[]{0,6,0,2,1,3,1,4,1,5,4,6};
        colors = new String[]{"yellow","orange","red","green","purple","blue"};
        gameLevels[3] = new Level(title, xValues, yValues, colors);
        // 5 - Ethan Custom Level
        title = "Level 5: Etan";
        xValues = new int[]{6,1,1,0,4,6,6,4,2,0,5,2};
        yValues = new int[]{0,4,2,5,2,2,3,6,4,6,4,6};
        colors = new String[]{"orange","green","blue","yellow","purple","red"};
        gameLevels[4] = new Level(title, xValues, yValues, colors);
        // 6 - Noah Custom Level
        title = "Level 6: Noah's Ark";
        xValues = new int[]{0,3,1,6,5,2,4,0,1,4,0,2};
        yValues = new int[]{0,6,0,4,1,2,3,5,5,5,6,6};
        colors = new String[]{"blue","red","green","purple","yellow","orange"};
        gameLevels[5] = new Level(title, xValues, yValues, colors);
        // 7
        title = "Level 7: Ripple";
        xValues = new int[]{6,1,3,4,5,6,6,6,3,5};
        yValues = new int[]{0,6,1,6,2,5,2,6,3,4};
        colors = new String[]{"blue","green","orange","yellow","red"};
        gameLevels[6] = new Level(title, xValues, yValues, colors);


        // Setting up the window
        window.setTitle("Flow Paid: Aqueducts");
        window.setPreferredSize(windowSize);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Setting up JList
        levelList.setListData(gameLevels);
        levelList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        levelList.setBackground(Color.BLACK);
        levelList.setForeground(mainColor[mainColorIndex]);
        levelList.setFont(new Font("Apple Casual",Font.ITALIC,18));
        levelList.addListSelectionListener(e -> {
            resetLevel(levelList.getSelectedIndex());
            levelName.setText(gameLevels[levelList.getSelectedIndex()].getTitle());
        });

        // Setting up the reset button
        resetButton.setBackground(Color.BLACK);
        resetButton.setFocusPainted(false);
        resetButton.addActionListener(e -> resetLevel(levelIndex));

        // Setting up the color changer button
        colorChanger.setBackground(Color.BLACK);
        colorChanger.setFocusPainted(false);
        colorChanger.addActionListener(e -> {
            // Cycles through the mainColor array
            if (mainColorIndex < mainColor.length - 1) {
                mainColorIndex++;
            } else {
                mainColorIndex = 0;
            }
            // Setting everything to the new color, doesn't work if game is complete
            if (!gameComplete) {
                colorChanger.setIcon(new ImageIcon(colorChangerFilePaths[mainColorIndex]));
                resetButton.setIcon(new ImageIcon(resetButtonFilePaths[mainColorIndex]));
                levelList.setForeground(mainColor[mainColorIndex]);
                title1.setForeground(mainColor[mainColorIndex]);
                title2.setForeground(mainColor[mainColorIndex]);
                gameStatus.setForeground(mainColor[mainColorIndex]);
                levelName.setForeground(mainColor[mainColorIndex]);
                endpointProgress.setForeground(mainColor[mainColorIndex]);
                cellProgress.setForeground(mainColor[mainColorIndex]);
            }
        });

        // Adding all the components for the GUI
        // Getting rid of the TextAreas ruins the grid, not sure why, GridBagLayout is not good
        c.weightx = 1;
        c.weighty = 1;
        addComponent(title1, 0, 0, 3, 1);
        addComponent(title2, 0, 1, 3, 1);
        addComponent(new JScrollPane(levelList), 0, 2, 3, 7);
        addComponent(new TextArea(""), 0, 8, 1, 1);
        addComponent(colorChanger, 0, 9, 3, 1);
        addComponent(new TextArea(""), 1, 9, 1, 1);
        addComponent(new TextArea(""), 2, 2, 1, 1);
        addComponent(new TextArea(""), 2, 3, 1, 1);
        addComponent(new TextArea(""), 2, 4, 1, 1);
        addComponent(new TextArea(""), 2, 5, 1, 1);
        addComponent(new TextArea(""), 2, 6, 1, 1);
        addComponent(new TextArea(""), 2, 7, 1, 1);
        addComponent(new TextArea(""), 2, 8, 1, 1);
        addComponent(new TextArea(""), 2, 9, 1, 1);
        addComponent(levelName, 3, 0, 7, 1);
        addComponent(gameStatus, 3, 1, 7, 1);
        addComponent(new TextArea(""), 3, 1, 1, 1);
        addComponent(endpointProgress, 3, 9, 2, 1);
        addComponent(new TextArea(""), 4, 1, 1, 1);
        addComponent(new TextArea(""), 5, 1, 1, 1);
        addComponent(cellProgress, 5, 9, 3, 1);
        addComponent(new TextArea(""), 6, 1, 1, 1);
        addComponent(new TextArea(""), 7, 1, 1, 1);
        addComponent(new TextArea(""), 8, 1, 1, 1);
        addComponent(resetButton, 8, 9, 2, 1);
        addComponent(new TextArea(""), 9, 0, 1, 1);

        // Creating the GameCell grid and its endpoints
        createGameCellGrid();
        createEndPoints(0);
        levelName.setText(gameLevels[0].getTitle());

        // Setting up title TextFields
        title1.setHorizontalAlignment(SwingConstants.CENTER);
        title1.setBackground(Color.BLACK);
        title1.setForeground(mainColor[mainColorIndex]);
        title1.setEditable(false);
        title1.setFont(new Font("Apple Casual",Font.BOLD,30));
        title2.setHorizontalAlignment(SwingConstants.CENTER);
        title2.setBackground(Color.BLACK);
        title2.setForeground(mainColor[mainColorIndex]);
        title2.setEditable(false);
        title2.setFont(new Font("Apple Casual",Font.BOLD,30));

        // Setting up gameStatus TextField
        gameStatus.setHorizontalAlignment(SwingConstants.CENTER);
        gameStatus.setBackground(Color.BLACK);
        gameStatus.setForeground(mainColor[mainColorIndex]);
        gameStatus.setEditable(false);
        gameStatus.setFont(new Font("Apple Casual",Font.BOLD,40));

        // Setting up levelName TextField
        levelName.setHorizontalAlignment(SwingConstants.CENTER);
        levelName.setBackground(Color.BLACK);
        levelName.setForeground(mainColor[mainColorIndex]);
        levelName.setEditable(false);
        levelName.setFont(new Font("Apple Casual",Font.BOLD,40));

        // Setting up endpointProgress TextField
        endpointProgress.setHorizontalAlignment(SwingConstants.CENTER);
        endpointProgress.setBackground(Color.BLACK);
        endpointProgress.setForeground(mainColor[mainColorIndex]);
        endpointProgress.setEditable(false);
        endpointProgress.setFont(new Font("Apple Casual",Font.ITALIC,24));
        endpointProgress.setText(completePaths() + " / " + completePathsReq);

        // Setting up cellProgress TextField
        cellProgress.setHorizontalAlignment(SwingConstants.CENTER);
        cellProgress.setBackground(Color.BLACK);
        cellProgress.setForeground(mainColor[mainColorIndex]);
        cellProgress.setEditable(false);
        cellProgress.setFont(new Font("Apple Casual",Font.ITALIC,24));
        cellProgress.setText((gameCells.length * gameCells[0].length - getEmptyCells()) + " / " + (gameCells.length * gameCells[0].length));

        // Finalizing the game window
        window.add(game);
        window.pack();
        window.setVisible(true);
        window.setResizable(false);
        game.addMouseListener(this);
        game.addMouseMotionListener(this);

    }

    // Private Methods
    // Adding a component to the gridbaglayout
    private void addComponent(Component com, int x, int y, int width, int height) {
        c.fill = GridBagConstraints.BOTH;
        c.gridx = x;
        c.gridy = y;
        c.gridwidth = width;
        c.gridheight = height;
        game.add(com, c);
    }

    // Resets the level
    private void resetLevel(int index) {
        // Removing all paths
        int j = 0;
        while (j < paths.size()) {
            paths.remove(j);
        }

        // Performing a reset by overwriting everything and adding the endpoints again
        for (GameCell[] gameCell : gameCells) {
            for (int i = 0; i < gameCells[0].length; i++) {
                gameCell[i].setColor("");
                gameCell[i].setType(0);
            }
        }
        // Recreates endpoints based on the user-chosen index and changes progress texts based on the level
        createEndPoints(index);
        endpointProgress.setText(completePaths() + " / " + completePathsReq);
        cellProgress.setText((gameCells.length * gameCells[0].length - getEmptyCells()) + " / " + (gameCells.length * gameCells[0].length));

        // Resetting font and icon colors that change when the game is complete
        if (gameComplete) {
            resetButton.setIcon(new ImageIcon(resetButtonFilePaths[mainColorIndex]));
            colorChanger.setIcon(new ImageIcon(colorChangerFilePaths[mainColorIndex]));
            title1.setForeground(mainColor[mainColorIndex]);
            title2.setForeground(mainColor[mainColorIndex]);
            levelList.setForeground(mainColor[mainColorIndex]);
            levelName.setForeground(mainColor[mainColorIndex]);
            gameStatus.setText("INCOMPLETE");
            gameStatus.setForeground(mainColor[mainColorIndex]);
            endpointProgress.setForeground(mainColor[mainColorIndex]);
            cellProgress.setForeground(mainColor[mainColorIndex]);
        }

        // Resetting game status
        levelIndex = index;
        gameComplete = false;
    }

    // Creates the empty GameCell grid
    private void createGameCellGrid() {
        for (int i = 3; i < 10; i++) {
            for (int j = 2; j < 9; j++) {
                // Manipulating the row and column values so that the grid is its own thing starting from 0,0 to 7,7
                gameCells[i-3][j-2] = new GameCell(i-3,j-2);
                addComponent(gameCells[i-3][j-2], i, j, 1, 1);
            }
        }
    }

    // Creates the endpoints for the GameCell grid
    private void createEndPoints(int index) {
        for (int i = 0; i < gameLevels[index].size(); i++) {
            gameCells[gameLevels[index].getxValues()[i]][gameLevels[index].getyValues()[i]].setColor(gameLevels[index].getColors()[i/2]);
            gameCells[gameLevels[index].getxValues()[i]][gameLevels[index].getyValues()[i]].setType(7);
        }

        completePathsReq = gameLevels[index].size() / 2;
    }

    // Removes a path from the GameCell grid
    private void removePath(int index) {
        Path pathToDelete = paths.get(index);

        // Going through the entire path resetting everything
        for (int i = 0; i < pathToDelete.size(); i++) {
            // If it is the endpoints then they are reset differently
            if (pathToDelete.get(i).getType() < 7) {
                pathToDelete.get(i).setColor("");
                pathToDelete.get(i).setType(0);
            } else {
                pathToDelete.get(i).setType(7);
            }
        }
    }

    // Determines which the current GameCell's type should be
    private int selectCurrentType(GameCell current, GameCell previous) {
        // Figuring out where the previous cell was relative to the current cell, can only be a horizontal or vertical pipe
        if (current.getxPos() > previous.getxPos() || current.getxPos() < previous.getxPos()) {
            return 1;
        } else {
            return 2;
        }
    }

    // Determines which the previous GameCell's type should be
    private int selectPreviousType(GameCell current, GameCell previous, GameCell previous2) {
        int cX = current.getxPos();
        int cY = current.getyPos();
        int pX = previous.getxPos();
        int pY = previous.getyPos();
        int pX2 = previous2.getxPos();
        int pY2 = previous2.getyPos();
        // Changing the previous cell based on what came before and after it
        if (((pY == cY + 1) && (pX2 == pX - 1)) || ((pX == cX + 1) && (pY2 == pY - 1))) {
            return 3;
        } else if (((pY == cY - 1) && (pX2 == pX - 1)) || ((pX == cX + 1) && (pY2 == pY + 1))) {
            return 4;
        } else if (((pY == cY - 1) && (pX2 == pX + 1)) || ((pX == cX - 1) && (pY2 == pY + 1))) {
            return 5;
        } else if (((pY == cY + 1) && (pX2 == pX + 1)) || ((pX == cX - 1) && (pY2 == pY - 1))) {
            return 6;
        }
        return previous.getType();
    }

    // Changes the endpoint sprite so that it cleanly connects with the path
    private int selectEndpointType(GameCell endpoint, GameCell nextToEndPoint) {
        int eX = endpoint.getxPos();
        int eY = endpoint.getyPos();
        int pX = nextToEndPoint.getxPos();
        int pY = nextToEndPoint.getyPos();

        // Determining which direction the next cell is from the endpoint
        if ((eY - 1 >= 0) && (eY - 1 == pY)) {
            return 8;
        } else if ((eX - 1 >= 0) && (eX - 1 == pX)) {
            return 9;
        } else if ((eY + 1 <= 6) && (eY + 1 == pY)) {
            return 10;
        } else {
            return 11;
        }
    }

    // Checking if the game is complete or not
    private boolean isGameComplete() {
        return ((getEmptyCells() == 0) && (completePaths() == completePathsReq));
    }

    // Checking to see how many paths are completed
    private int completePaths() {
        int count = 0;
        for (Path i : paths) {
            if (i.isComplete) {
                count++;
            }
        }
        return count;
    }

    // Returns how many filled empty cells there are
    private int getEmptyCells() {
        int count = 0;
        for (GameCell[] i : gameCells) {
            for (GameCell j : i) {
                if (j.getColor().equals("")) {
                    count++;
                }
            }
        }
        return count;
    }

    // Changes the GUI to signify they completed the puzzle
    private void completePuzzleGUI() {
        gameComplete = true;
        resetButton.setIcon(new ImageIcon("images/ResetIconG.png"));
        colorChanger.setIcon(new ImageIcon("images/ColorChangerG.png"));
        title1.setForeground(Color.GREEN);
        title2.setForeground(Color.GREEN);
        levelName.setForeground(Color.GREEN);
        gameStatus.setText("COMPLETE");
        gameStatus.setForeground(Color.GREEN);
        endpointProgress.setForeground(Color.GREEN);
        cellProgress.setForeground(Color.GREEN);
        levelList.setForeground(Color.GREEN);
        // Doot sound plays for Noah's level only
        if (levelIndex != 5) {
            Sound.play("sounds/GameComplete.wav");
        } else {
            Sound.play("sounds/Doot.wav");
        }
    }

    // Mouse Methods
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Need to account for when mousePressed is not on a GameCell at some point
        current = previous = previous2 = (GameCell) (game.getComponentAt(e.getX(), e.getY()));

        // Path creation only occurs when the game is incomplete and the cursor is starting
        if (!gameComplete && current.getType() >= 7) {
            isCreatingPath = true;
            // Looking at the starting point of each path to see if the user is trying to start a brand-new path
            // or rewrite an existing path
            for (int i = 0; i < paths.size(); i++) {
                if ((paths.get(i).size() > 0) && (paths.get(i).get(0).getColor().equals(current.getColor()))) {
                    removePath(i);
                    paths.remove(i);
                    // Changing the progress texts
                    endpointProgress.setText(completePaths() + " / " + completePathsReq);
                    cellProgress.setText((gameCells.length * gameCells[0].length - getEmptyCells()) + " / " + (gameCells.length * gameCells[0].length));
                }
            }
            // Creates a new path and adds the starting cell as its first
            paths.add(new Path());
            paths.get(paths.size() - 1).add(current);
            currentPath = paths.get(paths.size() - 1);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // If path ended right before the other endpoint
        if (current != currentPath.get(0) && isCreatingPath) {
            int cX = current.getxPos();
            int cY = current.getyPos();
            boolean isNextToEndpoint = false;
            previous = current;
            // Checking the four cells around it
            if ((cX - 1 >= 0) && (gameCells[cX - 1][cY].getType() == 7) && (!gameCells[cX - 1][cY].equals(currentPath.get(0))) && (gameCells[cX - 1][cY].getColor().equals(currentPath.get(0).getColor()))) {
                current = gameCells[cX - 1][cY];
                isNextToEndpoint = true;
            } else if ((cY - 1 >= 0) && (gameCells[cX][cY - 1].getType() == 7) && (!gameCells[cX][cY - 1].equals(currentPath.get(0))) && (gameCells[cX][cY - 1].getColor().equals(currentPath.get(0).getColor()))) {
                current = gameCells[cX][cY - 1];
                isNextToEndpoint = true;
            } else if ((cX + 1 <= 6) && (gameCells[cX + 1][cY].getType() == 7) && (!gameCells[cX + 1][cY].equals(currentPath.get(0))) && (gameCells[cX + 1][cY].getColor().equals(currentPath.get(0).getColor()))) {
                current = gameCells[cX + 1][cY];
                isNextToEndpoint = true;
            } else if ((cY + 1 <= 6) && (gameCells[cX][cY + 1].getType() == 7) && (!gameCells[cX][cY + 1].equals(currentPath.get(0))) && (gameCells[cX][cY + 1].getColor().equals(currentPath.get(0).getColor()))) {
                current = gameCells[cX][cY + 1];
                isNextToEndpoint = true;

            }
            // Setting the sprites and adding the endpoint to the path, and noting that it is complete
            if (isNextToEndpoint) {
                previous.setType(selectPreviousType(current, previous, previous2));
                current.setType(selectEndpointType(current, previous));
                currentPath.add(current);
                currentPath.setComplete(true);
                endpointProgress.setText(completePaths() + " / " + completePathsReq);
            }

            // Checking if the game was complete
            if (currentPath.isComplete() && !isGameComplete()) {
                Sound.play("sounds/Filled.wav");
            } else if (isGameComplete()) {
                completePuzzleGUI();
            }
        }
        isCreatingPath = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // Can only drag if a path is being created and the game isn't completed
        if (!gameComplete && isCreatingPath) {
            GameCell temp = (GameCell) (game.getComponentAt(e.getX(), e.getY()));

            // Prevents the code from running everytime the mouse gets dragged a pixel
            if ((temp != previous) && (temp.getColor().equals(""))) {
                current = temp;
                int cX = current.getxPos();
                int cY = current.getyPos();
                int pX = previous.getxPos();
                int pY = previous.getyPos();
                // Also making sure that the new cell is adjacent to the previous one
                if ((cX + 1 == pX && cY == pY) || (cY + 1 == pY && cX == pX) || (pX + 1 == cX && cY == pY) || (pY + 1 == cY && cX == pX)) {
                    current.setColor(previous.getColor());
                    currentPath.add(current);

                    // Prevents the endpoints from being switched by cell type switching methods
                    if (current.getType() < 7) {
                        current.setType(selectCurrentType(current, previous));
                        cellProgress.setText((gameCells.length * gameCells[0].length - getEmptyCells()) + " / " + (gameCells.length * gameCells[0].length));
                    }
                    if (previous.getType() < 7) {
                        previous.setType(selectPreviousType(current, previous, previous2));
                    }
                    // Changing endpoint to make connect better
                    if (currentPath.size() == 2) {
                        previous.setType(selectEndpointType(previous, current));
                    }

                    // Updating what cells are being keep track of
                    previous2 = previous;
                    previous = current;
                } else {
                    isCreatingPath = false;
                }
            }
            // If the path has reached the other endpoint
            else if ((currentPath.size() > 0) && (temp.getType() == 7) && (temp.getColor().equals(currentPath.get(0).getColor()) && (temp != currentPath.get(0)))) {
                // Adding endpoint to path
                current = temp;
                currentPath.add(current);

                // Making sure previous cell has correct sprite loaded
                previous.setType(selectPreviousType(current, previous, previous2));
                currentPath.setComplete(true);

                // Changing endpoint to look cleaner
                current.setType(selectEndpointType(current, previous));

                // Changing the progress bar
                endpointProgress.setText(completePaths() + " / " + completePathsReq);

                // Checking if the game was complete
                if (!isGameComplete()) {
                    Sound.play("sounds/Filled.wav");
                } else {
                    completePuzzleGUI();
                }
                isCreatingPath = false;
            }
            // If the mouse is dragged on to a different color endpoint
            else if (temp.getType() == 7 && (!temp.getColor().equals(currentPath.get(0).getColor()))) {
                isCreatingPath = false;
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
