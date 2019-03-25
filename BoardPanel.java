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
import java.util.stream.IntStream;

class BoardPanel {
    private BufferedImage emptyBoardImage;
    static Scene gameBoard;
    private Group gameComponents;
    static ImageView gameView;
    static Image boardImage;
    private Dice dice;
    private UserInterface UI;
    static final Coordinates[][] BOARD = new Coordinates[12][30];
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
        // Add checkers to board
        IntStream.range(0, 15).forEach(i -> gameComponents.getChildren().addAll(redCheckers[i].getCircle(), blueCheckers[i].getCircle()));
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
            BEAR[col][row] = new Coordinates(bearX, bearY, 'E');
            bearY += BEAR_OFFSET;
        }
    }

    private void setupBar() {
        int col = 0;
        final double barX = 325.775;
        double barY = 121;
        double BAR_OFFSET = 30;

        for(int row = 0; row < 10; row++) {
            BAR[col][row] = new Coordinates(barX, barY, 'E');
            barY += BAR_OFFSET;
        }
    }

    private void setupBoard() {
        double currentColumn= 109;
        double currentRow = 91;
        final double VERTICAL_OFFSET = 10;
        final double HORIZONTAL_OFFSET = 33.35;

        for(int col = 0; col < 12; col++) {
            if (col == 6) { // Skip the bar and go to next pip
                currentColumn = 375.8;
            }

            for(int row = 0; row < 30; row++) {
                if (row == 15) {
                    currentRow = 341; // Jump to top of bottom pip and leave gap between top and bottom pips
                }

                BOARD[col][row] = new Coordinates(currentColumn, currentRow, 'E');
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
        int currentPip;
        int column;
        int row;

        for(int numCheckersInPip = 0; numCheckersInPip < 15; numCheckersInPip++) {
            if(numCheckersInPip < 5) {
                currentPip = 13;
                column = GameLogic.convertPipToColumn(currentPip);
            }
            else if(numCheckersInPip < 7) {
                currentPip = 24;
                column = GameLogic.convertPipToColumn(currentPip);
            }
            else if(numCheckersInPip < 10) {
                currentPip = 8;
                column = GameLogic.convertPipToColumn(currentPip);
            }
            else {
                currentPip = 6;
                column = GameLogic.convertPipToColumn(currentPip);
            }

            row = GameLogic.nextRow(column, currentPip, 0);
            Circle red = new Circle(BoardPanel.BOARD[column][row].getX(), BoardPanel.BOARD[column][row].getY(), RADIUS); // Circle with coordinates
            BoardPanel.BOARD[column][row].occupyCoordinate('R'); // Position now occupied
            red.setStroke(Color.BLACK);
            red.setFill(effects.checkerRed);
            redCheckers[numReds++] = new Checkers(red);
        }
    }

    private void createBlueCheckers() {
        int numBlues = 0;
        int currentPip;
        int column;
        int row;

        for(int numCheckersInPip = 0; numCheckersInPip < 15; numCheckersInPip++) {
            if(numCheckersInPip < 3) {
                currentPip = 17;
                column = GameLogic.convertPipToColumn(currentPip);
            }
            else if(numCheckersInPip < 8) {
                currentPip = 19;
                column = GameLogic.convertPipToColumn(currentPip);
            }
            else if(numCheckersInPip < 13) {
                currentPip = 12;
                column = GameLogic.convertPipToColumn(currentPip);
            }
            else {
                currentPip = 1;
                column = GameLogic.convertPipToColumn(currentPip);
            }
            row = GameLogic.nextRow(column, currentPip, 0);
            Circle blue = new Circle(BoardPanel.BOARD[column][row].getX(), BoardPanel.BOARD[column][row].getY(), RADIUS);
            BoardPanel.BOARD[column][row].occupyCoordinate('B');
            blue.setStroke(Color.BLACK);
            blue.setFill(effects.checkerBlue);
            blueCheckers[numBlues++] = new Checkers(blue);
        }
    }

    static Circle checkerAtStartingPip(int column, int row, int currentPlayer, int pip) {
        Circle toBeMoved = null;

        if(currentPlayer == Player.playerRed.getTurn()) {
            for (int i = 0; i < 15; i++) {
                if(pip == 25) { // Moving from bar
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
                if(pip == 25) {
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

