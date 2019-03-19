import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

class BoardPanel {
    private BufferedImage emptyBoardImage;
    static Scene gameBoard;
    private Group gameComponents;
    static ImageView gameView;
    static Image boardImage;
    private Dice dice;
    private UserInterface UI;
    static final Coordinates[][] BOARD = new Coordinates[12][12];
    static final Coordinates[][] BEAR = new Coordinates[1][30];
    static final Coordinates[][] BAR = new Coordinates[1][10];
    static Checkers[] redCheckers = new Checkers[15];
    static Checkers[] blueCheckers = new Checkers[15];
    private Effects effects = new Effects();
    private final int RADIUS = 15;

    BoardPanel(Stage primaryWindow) {
        gameComponents = new Group();
        dice = new Dice();
        UI = new UserInterface(primaryWindow);
        setBoardImage();
        setupBoard();
        setupBear();
        setupBar();
        setupCheckers();
        setGameView();
        setGameComponents();
        setGameBoard();
    }
    
    private void setGameComponents() {
        gameComponents.getChildren().addAll(gameView, UI, dice);
        for (int i = 0; i < 15; i++) { // Add checkers to board
            gameComponents.getChildren().addAll(redCheckers[i].getCircle(), blueCheckers[i].getCircle());
        }
    }

    private void setGameBoard() {
        gameBoard = new Scene(gameComponents, 1060, 600);
        gameBoard.setFill(Color.DARKRED);
    }

    private void setGameView() {
        gameView = new ImageView(boardImage);
        gameView.setX(25);
        gameView.setY(60);
        gameView.setFitHeight(600);
        gameView.setFitWidth(600);
        gameView.setPreserveRatio(true);
    }

    private void setBoardImage() {
        try {
            emptyBoardImage = ImageIO.read(this.getClass().getResource("/images/empty_board.jpg"));
        } catch (IOException ex) {
            System.out.println("Image not showing / not found " + ex.toString());
        }
        boardImage = SwingFXUtils.toFXImage(emptyBoardImage, null);
    }

    private void setupBear() {
        int col = 0;
        final double bearX = 592.575;
        double bearY = 91;
        double BEAR_OFFSET = 11.867;

        for(int row = 0; row < 30; row++) {
            if(row == 15) {
                bearY = 317;
            }
            BEAR[col][row] = new Coordinates(bearX, bearY);
            bearY += BEAR_OFFSET;
        }
    }

    private void setupBar() {
        int col = 0;
        final double barX = 325.775;
        double barY = 121;
        double BAR_OFFSET = 30;

        for(int row = 0; row < 10; row++) {
            BAR[col][row] = new Coordinates(barX, barY);
            barY += BAR_OFFSET;
        }
    }

    private void setupBoard() {
        double currentColumn= 109;
        double currentRow = 91;
        final double VERTICAL_OFFSET = 30;
        final double HORIZONTAL_OFFSET = 33.35;

        for(int col = 0; col < 12; col++) {
            if (col == 6) { // Skip the bar and go to next pip
                currentColumn = 375.8;
            }

            for(int row = 0; row < 12; row++) {
                if (row == 6) {
                    currentRow = 331; // Jump to top of bottom pip and leave gap between top and bottom pips
                }

                BOARD[col][row] = new Coordinates(currentColumn, currentRow);
                currentRow += VERTICAL_OFFSET;
            }
            currentRow = 91;
            currentColumn += HORIZONTAL_OFFSET;
        }
    }

    private void setupCheckers() {
        createRedCheckers();
        createBlueCheckers();
    }

    private void createRedCheckers() {
        int numReds = 0;
        int currentPip = 13; // PIP TO BE CONVERTED TO COLUMN
        int column = convertPipToColumn(currentPip); // COLUMN OF PIP THAT WAS CONVERTED
        int row;

        for(int numCheckersInPip = 0; numCheckersInPip < 5; numCheckersInPip++) // Counts the checkers
        {
            row = nextRow(column, currentPip, 0);
            Circle red = new Circle(BoardPanel.BOARD[column][row].getX(), BoardPanel.BOARD[column][row].getY(), RADIUS); // Circle with coordinates
            BoardPanel.BOARD[column][row].occupyCoordinate(); // Position now occupied
            red.setStroke(Color.BLACK);
            red.setFill(effects.checkerRed);
            redCheckers[numReds] = new Checkers(red);
            numReds++;
        }

        currentPip = 24;
        column = convertPipToColumn(currentPip);

        for(int numCheckersInPip = 0; numCheckersInPip < 2; numCheckersInPip++)
        {
            row = nextRow(column, currentPip, 0);
            Circle red = new Circle(BoardPanel.BOARD[column][row].getX(), BoardPanel.BOARD[column][row].getY(), RADIUS);
            BoardPanel.BOARD[column][row].occupyCoordinate();
            red.setStroke(Color.BLACK);
            red.setFill(effects.checkerRed);
            redCheckers[numReds] = new Checkers(red);
            numReds++;

        }

        currentPip = 8;
        column = convertPipToColumn(currentPip);

        for(int numCheckersInPip = 0; numCheckersInPip < 3; numCheckersInPip++)
        {
            row = nextRow(column, currentPip, 0);
            Circle red = new Circle(BoardPanel.BOARD[column][row].getX(), BoardPanel.BOARD[column][row].getY(), RADIUS);
            BoardPanel.BOARD[column][row].occupyCoordinate();
            red.setStroke(Color.BLACK);
            red.setFill(effects.checkerRed);
            redCheckers[numReds] = new Checkers(red);
            numReds++;
        }

        currentPip = 6;
        column = convertPipToColumn(currentPip);

        for(int numCheckersInPip = 0; numCheckersInPip < 5; numCheckersInPip++)
        {
            row = nextRow(column, currentPip, 0);
            Circle red = new Circle(BoardPanel.BOARD[column][row].getX(), BoardPanel.BOARD[column][row].getY(), RADIUS);
            BoardPanel.BOARD[column][row].occupyCoordinate();
            red.setStroke(Color.BLACK);
            red.setFill(effects.checkerRed);
            redCheckers[numReds] = new Checkers(red);
            numReds++;
        }
    }

    private void createBlueCheckers() {
        int numBlues = 0;
        int currentPip = 17; // Pip to be placed on
        int column = convertPipToColumn(currentPip);
        int row;

        for(int numCheckersInPip = 0; numCheckersInPip < 3; numCheckersInPip++)
        {
            row = nextRow(column, currentPip, 0);
            Circle blue = new Circle(BoardPanel.BOARD[column][row].getX(), BoardPanel.BOARD[column][row].getY(), RADIUS);
            BoardPanel.BOARD[column][row].occupyCoordinate();
            blue.setStroke(Color.BLACK);
            blue.setFill(effects.checkerBlue);
            blueCheckers[numBlues] = new Checkers(blue);
            numBlues++;
        }

        currentPip = 19;
        column = convertPipToColumn(currentPip);

        for(int numCheckersInPip = 0; numCheckersInPip < 5; numCheckersInPip++)
        {
            row = nextRow(column, currentPip, 0);
            Circle blue = new Circle(BoardPanel.BOARD[column][row].getX(), BoardPanel.BOARD[column][row].getY(), RADIUS);
            BoardPanel.BOARD[column][row].occupyCoordinate();
            blue.setStroke(Color.BLACK);
            blue.setFill(effects.checkerBlue);
            blueCheckers[numBlues] = new Checkers(blue);
            numBlues++;
        }

        currentPip = 12;
        column = convertPipToColumn(currentPip);

        for(int numCheckersInPip = 0; numCheckersInPip < 5; numCheckersInPip++)
        {
            row = nextRow(column, currentPip,0);
            Circle blue = new Circle(BoardPanel.BOARD[column][row].getX(), BoardPanel.BOARD[column][row].getY(), RADIUS);
            BoardPanel.BOARD[column][row].occupyCoordinate();
            blue.setStroke(Color.BLACK);
            blue.setFill(effects.checkerBlue);
            blueCheckers[numBlues] = new Checkers(blue);
            numBlues++;        }

        currentPip = 1;
        column = convertPipToColumn(currentPip);

        for(int numCheckersInPip = 0; numCheckersInPip < 2; numCheckersInPip++)
        {
            row = nextRow(column, currentPip, 0);
            Circle blue = new Circle(BoardPanel.BOARD[column][row].getX(), BoardPanel.BOARD[column][row].getY(), RADIUS);
            BoardPanel.BOARD[column][row].occupyCoordinate();
            blue.setStroke(Color.BLACK);
            blue.setFill(effects.checkerBlue);
            blueCheckers[numBlues] = new Checkers(blue);
            numBlues++;        }
    }

    // CAN BE REMOVED AFTER SPRINT 3
    static void setUpCheat() {
        for(int i = 0; i < 12; i++) {
            for(int j = 0; j < 12; j++) {
                BoardPanel.BOARD[i][j].releaseCoordinate();
            }
        }

        for(int i = 0; i < 1; i++) {
            for(int j = 0; j < 30; j++) {
                BoardPanel.BEAR[i][j].releaseCoordinate();
            }
        }

        for(int i = 0; i < 1; i++) {
            for(int j = 0; j < 10; j++) {
                BoardPanel.BAR[i][j].releaseCoordinate();
            }
        }

        int numReds = 0;
        int numBlues = 0;
        int currentPip = 1; // PIP TO BE CONVERTED TO COLUMN
        int column = convertPipToColumn(currentPip); // COLUMN OF PIP THAT WAS CONVERTED
        int row;

        for(int numCheckersInPip = 0; numCheckersInPip < 2; numCheckersInPip++) // Counts the checkers
        {
            row = nextRow(column, currentPip, 0);
            Checkers.moveCircle(redCheckers[numReds].getCircle(), column, row, currentPip);
            BoardPanel.BOARD[column][row].occupyCoordinate(); // Position now occupied
            numReds++;
        }

        currentPip = 2;
        column = convertPipToColumn(currentPip);
        for(int numCheckersInPip = 0; numCheckersInPip < 2; numCheckersInPip++) // Counts the checkers
        {
            row = nextRow(column, currentPip, 0);
            Checkers.moveCircle(redCheckers[numReds].getCircle(), column, row, currentPip);
            BoardPanel.BOARD[column][row].occupyCoordinate(); // Position now occupied
            numReds++;
        }

        currentPip = 3;
        column = convertPipToColumn(currentPip);
        for(int numCheckersInPip = 0; numCheckersInPip < 2; numCheckersInPip++) // Counts the checkers
        {
            row = nextRow(column, currentPip, 0);
            Checkers.moveCircle(redCheckers[numReds].getCircle(), column, row, currentPip);
            BoardPanel.BOARD[column][row].occupyCoordinate(); // Position now occupied
            numReds++;
        }

        currentPip = 4;
        column = convertPipToColumn(currentPip);
        for(int numCheckersInPip = 0; numCheckersInPip < 2; numCheckersInPip++) // Counts the checkers
        {
            row = nextRow(column, currentPip, 0);
            Checkers.moveCircle(redCheckers[numReds].getCircle(), column, row, currentPip);
            BoardPanel.BOARD[column][row].occupyCoordinate(); // Position now occupied
            numReds++;
        }

        currentPip = 5;
        column = convertPipToColumn(currentPip);
        for(int numCheckersInPip = 0; numCheckersInPip < 2; numCheckersInPip++) // Counts the checkers
        {
            row = nextRow(column, currentPip, 0);
            Checkers.moveCircle(redCheckers[numReds].getCircle(), column, row, currentPip);
            BoardPanel.BOARD[column][row].occupyCoordinate(); // Position now occupied
            numReds++;
        }

        column = 0; // BAR RED
        row = 0;
        for(int numCheckersInPip = 0; numCheckersInPip < 3; numCheckersInPip++) {
            Checkers.moveCircle(redCheckers[numReds].getCircle(), column, row, 0);
            BoardPanel.BAR[column][row].occupyCoordinate();
            numReds++;
            row++;
        }

        column = 0; // BEAR RED
        row = 15;
        for(int numCheckersInPip = 0; numCheckersInPip < 2; numCheckersInPip++) {
            Checkers.moveCircle(redCheckers[numReds].getCircle(), column, row, 25);
            BoardPanel.BEAR[column][row].occupyCoordinate();
            numReds++;
            row++;
        }

        column = 0; // BAR BLUE
        row = 5;
        for(int numCheckersInPip = 0; numCheckersInPip < 3; numCheckersInPip++) {
            Checkers.moveCircle(blueCheckers[numBlues].getCircle(), column, row, 0);
            BoardPanel.BAR[column][row].occupyCoordinate();
            numBlues++;
            row++;
        }

        column = 0; // BEAR BLUE
        row = 0;
        for(int numCheckersInPip = 0; numCheckersInPip < 3; numCheckersInPip++) {
            Checkers.moveCircle(blueCheckers[numBlues].getCircle(), column, row, 25);
            BoardPanel.BEAR[column][row].occupyCoordinate();
            numBlues++;
            row++;
        }


        currentPip = 24;
        column = convertPipToColumn(currentPip);
        for(int numCheckersInPip = 0; numCheckersInPip < 3; numCheckersInPip++) // Counts the checkers
        {
            row = nextRow(column, currentPip, 0);
            Checkers.moveCircle(blueCheckers[numBlues].getCircle(), column, row, currentPip);
            BoardPanel.BOARD[column][row].occupyCoordinate(); // Position now occupied
            numBlues++;
        }

        currentPip = 22;
        column = convertPipToColumn(currentPip);
        for(int numCheckersInPip = 0; numCheckersInPip < 3; numCheckersInPip++) // Counts the checkers
        {
            row = nextRow(column, currentPip, 0);
            Checkers.moveCircle(blueCheckers[numBlues].getCircle(), column, row, currentPip);
            BoardPanel.BOARD[column][row].occupyCoordinate(); // Position now occupied
            numBlues++;
        }

        currentPip = 21;
        column = convertPipToColumn(currentPip);
        for(int numCheckersInPip = 0; numCheckersInPip < 3; numCheckersInPip++) // Counts the checkers
        {
            row = nextRow(column, currentPip, 0);
            Checkers.moveCircle(blueCheckers[numBlues].getCircle(), column, row, currentPip);
            BoardPanel.BOARD[column][row].occupyCoordinate(); // Position now occupied
            numBlues++;
        }
    }

    static int getWinner() { // Check if last position in Bear Off is taken
        if(BoardPanel.BEAR[0][29].isTaken()) { // If last position for reds Bear off is taken then red wins
            // PLAYER RED WINS
            return Player.playerRed.getTurn();
        }

        if(BoardPanel.BEAR[0][14].isTaken()) { // If last position for blues Bear off is taken then blue wins
            // PLAYER BLUE WINS
            return Player.playerBlue.getTurn();
        }
        return 0;
    }

    // Convert pip to a column integer which is used as X index in array
    static int convertPipToColumn(int pip) {
        int column = 0;

        if(pip>12) { // TOP HALF OF BOARD
            column = pip-13;
        }

        if(pip <= 12) { // BOTTOM HALF OF BOARD
            column = Math.abs(pip-12);
        }

        if(pip == 0 || pip == 25) { // BAR OR BEAR
            column = 0;
        }

        return column;
    }

    // Look for available row in pip to place checker, checks who's turn it is
    static int nextRow(int column, int pip, int currentPlayer) {
        int freeRow = -1;

        if(currentPlayer == 0 || currentPlayer == Player.playerRed.getTurn()) { // IF BOARD IS NOT FLIPPED
            if(pip > 12) { // Top half of board
                for(int rowCheck = 0; rowCheck < 6; rowCheck++) { // Max 6 checkers in each column // Start at top and go down
                    if(!BoardPanel.BOARD[column][rowCheck].isTaken()) { // If position in column is free then break
                        freeRow = rowCheck;
                        break;
                    }

                    if(rowCheck == 5) { // Pip is full with 6 checkers
                        break;
                    }
                }
            }

            if(pip <= 12) { // Bottom half of board
                for(int rowCheck = 11; rowCheck > 5; rowCheck--) { // Max 6 checkers in each column // Start bottom and go up
                    if(!BoardPanel.BOARD[column][rowCheck].isTaken()) { // If position in column is free then break
                        freeRow = rowCheck;
                        break;
                    }

                    if(rowCheck == 6) { // Pip is full with 6 checkers
                        break;
                    }
                }
            }

            if(pip == 0) {
                for(int rowCheck = 0; rowCheck < 10; rowCheck++) {
                    if(!BoardPanel.BAR[0][rowCheck].isTaken()) {
                        freeRow = rowCheck;
                        break;
                    }
                }
            }

            if(pip == 25) {
                for(int rowCheck = 15; rowCheck < 30; rowCheck++) {
                    if(!BoardPanel.BEAR[0][rowCheck].isTaken()) {
                        freeRow = rowCheck;
                        break;
                    }
                }
            }
        }

        else if(currentPlayer == Player.playerBlue.getTurn()) { // IF BOARD IS FLIPPED
            if(pip > 12) { // Bottom half of the board
                for(int rowCheck = 11; rowCheck > 5; rowCheck--) { // Max 6 checkers in each column // Start bottom and go up
                    if(!BoardPanel.BOARD[column][rowCheck].isTaken()) { // If position in column is free then break
                        freeRow = rowCheck;
                        break;
                    }

                    if(rowCheck == 6) { // Pip is full with 6 checkers
                        break;
                    }
                }
            }

            if(pip <= 12) { // Top half of the board
                for(int rowCheck = 0; rowCheck < 6; rowCheck++) { // Max 6 checkers in each column // Start at top and go down
                    if(!BoardPanel.BOARD[column][rowCheck].isTaken()) { // If position in column is free then break
                        freeRow = rowCheck;
                        break;
                    }

                    if(rowCheck == 5) { // Pip is full with 6 checkers
                        break;
                    }
                }
            }

            if(pip == 0) {
                for(int rowCheck = 0; rowCheck < 10; rowCheck++) {
                    if(!BoardPanel.BAR[0][rowCheck].isTaken()) {
                        freeRow = rowCheck;
                        break;
                    }
                }
            }

            if(pip == 25) {
                for(int rowCheck = 0; rowCheck < 15; rowCheck++) {
                    if(!BoardPanel.BEAR[0][rowCheck].isTaken()) {
                        freeRow = rowCheck;
                        break;
                    }
                }
            }
        }
        return freeRow; // Return position in column that is free
    }

    static int topCheckerInPip(int column, int pip, int currentPlayer) {
        int top = 0;

        if(currentPlayer == Player.playerRed.getTurn()) {
            if(pip >= 13) { // Top half of board
                for(int nextRow = 0; nextRow <= 6; nextRow++) { // Max 6 checkers in each column // Start at top and go down
                    if(!BoardPanel.BOARD[column][nextRow].isTaken()) { // If position in column is free then break
                        top = nextRow - 1; // Decrement by 1 to get top checker, as counting down from the top
                        break;
                    }
                    if(nextRow == 5) { // If 6 checkers in a column,
                        top = 5;
                        break;
                    }
                }
            }

            if(pip <= 12) { // Bottom half of board
                for(int nextRow = 11; nextRow >= 6; nextRow--) { // Max 6 checkers in each column // Start bottom and go up
                    if(!BoardPanel.BOARD[column][nextRow].isTaken()) { // If position in column is free then break and return checker before it
                        top = nextRow + 1; // Increment by 1 to get top checker, as counting up from the bottom
                        break;
                    }
                    if(nextRow == 6) { // If 6 checkers in a column, return top checker
                        top = 6;
                        break;
                    }
                }
            }

            if(pip == 0) {
                for(int nextRow = 0; nextRow < 5; nextRow++) {
                    if(!BoardPanel.BAR[0][nextRow].isTaken()) {
                        top = nextRow - 1;
                        break;
                    }
                }
            }
        }

        else if(currentPlayer == Player.playerBlue.getTurn()) {
            if(pip >= 13) { // Bottom half of board
                for(int nextRow = 11; nextRow >= 6; nextRow--) { // Max 6 checkers in each column // Start bottom and go up
                    if(!BoardPanel.BOARD[column][nextRow].isTaken()) { // If position in column is free then break and return checker before it
                        top = nextRow + 1; // Increment by 1 to get top checker, as counting up from the bottom
                        break;
                    }
                    if(nextRow == 6) { // If 6 checkers in a column, return top checker
                        top = 6;
                        break;
                    }
                }
            }

            if(pip <= 12) { // Top half of board
                for(int nextRow = 0; nextRow <= 6; nextRow++) { // Max 6 checkers in each column // Start at top and go down
                    if(!BoardPanel.BOARD[column][nextRow].isTaken()) { // If position in column is free then break
                        top = nextRow - 1; // Decrement by 1 to get top checker, as counting down from the top
                        break;
                    }
                    if(nextRow == 5) { // If 6 checkers in a column,
                        top = 5;
                        break;
                    }
                }
            }

            if(pip == 0) {
                for(int nextRow = 5; nextRow < 10; nextRow++) {
                    if(!BoardPanel.BAR[0][nextRow].isTaken()) {
                        top = nextRow - 1;
                        break;
                    }
                }
            }
        }
        return top; // Return position in column that is free
    }

    // CAN BE REMOVED ONCE LEGAL MOVES CAN BE DISPLAYED
    static Circle checkerAtStartingPip(int column, int row, int currentPlayer, int pip) {
        Circle toBeMoved = null;

        if(currentPlayer == Player.playerRed.getTurn()) {
            for (int i = 0; i < 15; i++) {
                if(pip == 0) { // Moving from bar
                    if (redCheckers[i].getCircleX() == BAR[0][row].getX() && redCheckers[i].getCircleY() == BAR[0][row].getY()) {
                        toBeMoved = redCheckers[i].getCircle();
                        break;
                    }
                }

                else {
                    if (redCheckers[i].getCircleX() == BOARD[column][row].getX() && redCheckers[i].getCircleY() == BOARD[column][row].getY()) {
                        toBeMoved = redCheckers[i].getCircle();
                        break;
                    }
                }
            }
        }

        if(currentPlayer == Player.playerBlue.getTurn()) {
            for(int i = 0; i < 15; i++) {
                if(pip == 0) {
                    if(blueCheckers[i].getCircleX() == BAR[0][row].getX() && blueCheckers[i].getCircleY() == BAR[0][row].getY()) {
                        toBeMoved = blueCheckers[i].getCircle();
                        break;
                    }
                }

                else {
                    if(blueCheckers[i].getCircleX() == BOARD[column][row].getX() && blueCheckers[i].getCircleY() == BOARD[column][row].getY()) {
                        toBeMoved = blueCheckers[i].getCircle();
                        break;
                    }
                }
            }
        }
        return toBeMoved;
    }
}

