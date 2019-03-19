import javafx.scene.Group;
import javafx.scene.shape.Circle;

class Checkers extends Group {

    private Circle checker;

    Checkers(Circle circle) {
        this.checker = circle;
    }

    Circle getCircle() {
        return this.checker;
    }

    double getCircleX() {
        return this.checker.getCenterX();
    }

    double getCircleY() {
        return this.checker.getCenterY();
    }

    static void moveCircle(Circle circle, int column, int row, int pip) {
        if (pip == 0) {
            circle.setCenterX(BoardPanel.BAR[column][row].getX());
            circle.setCenterY(BoardPanel.BAR[column][row].getY());
        }

        if (pip == 25) {
            circle.setCenterX(BoardPanel.BEAR[column][row].getX());
            circle.setCenterY(BoardPanel.BEAR[column][row].getY());
        }

        if (pip >= 1 && pip <= 24) {
            circle.setCenterX(BoardPanel.BOARD[column][row].getX());
            circle.setCenterY(BoardPanel.BOARD[column][row].getY());
        }
    }
}


