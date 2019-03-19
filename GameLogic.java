import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GameLogic {

    static int[] findOwnCheckers(int currentPlayer) {
        double checkerX;
        double checkerY;
        int column;
        int pip;
        Set set = new HashSet();
        int[] array = new int[15];
        int[] newArr = new int[15];
        int i = 0;

        if (currentPlayer == Player.playerRed.getTurn()) { // If red players turn
            for (int index = 0; index < 15; index++) {
                checkerX = BoardPanel.redCheckers[index].getCircleX();
                checkerY = BoardPanel.redCheckers[index].getCircleY();

                column = (int) (((checkerX - 109) / 33.35) + 0.5d); // Get column from X coordinate
                if (column > 5) { // To make up for skipping the bar
                    column -= 2;
                }
                if (checkerY <= 241) { // If top half of board
                    pip = column + 13;
                    array[index] = pip; // Store pip in array
                }

                if (checkerY > 241) { // If bottom half of board
                    pip = Math.abs(column - 12);
                    array[index] = pip; // Store pip in array
                }
            }
        }

        if (currentPlayer == Player.playerBlue.getTurn()) { // If red players turn
            for (int index = 0; index < 15; index++) {
                checkerX = BoardPanel.blueCheckers[index].getCircleX();
                checkerY = BoardPanel.blueCheckers[index].getCircleY();

                column = (int) (((checkerX - 109) / 33.35) + 0.5d); // Get column from X coordinate
                if (column > 5) { // To make up for skipping the bar
                    column -= 2;
                }
                if (checkerY <= 241) { // If top half of board
                    pip = Math.abs(column - 12);
                    array[index] = pip; // Store pip in array
                }

                if (checkerY > 241) { // If bottom half of board
                    pip = column + 13;
                    array[index] = pip; // Store pip in array
                }
            }
        }

        for (int x : array) {
            set.add(x);
        }

        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            newArr[i++] = (int) iterator.next();
        }

        return newArr;
    }

    static int[][] findMoveTo(int[] array) {
        int[][] moveTo = new int[array.length][2];
        for (int index = 0; index < array.length; index++) {
            moveTo[index][0] = array[index];
            moveTo[index][0] -= Dice.roll1 + 1;
            moveTo[index][1] = array[index];
            moveTo[index][1] -= Dice.roll2 + 1;
        }
        return moveTo;
    }
}
