// IF CAN BEAR OFF
// IF MOVETO[INDEX][3] < 0 THEN STORE AS 0 ( BEAR OFF )
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

class GameLogic {
    static ArrayList<Integer> findOwnCheckers(int currentPlayer) {
        double checkerX;
        double checkerY;
        int column;
        int pip;
        Set set = new HashSet();
        ArrayList<Integer>  newArrayList = new ArrayList<>();
        int[] array = new int[15];

        if (currentPlayer == Player.playerRed.getTurn()) { // If red players turn
            for (int index = 0; index < 15; index++) {
                checkerX = BoardPanel.redCheckers[index].getCircleX();
                checkerY = BoardPanel.redCheckers[index].getCircleY();

                if(checkerX == 325.775) {
                    pip = 0;
                    array[index] = pip;
                }

                column = (int) (((checkerX - 109)/33.35) + 0.5d); // Get column from X coordinate
                if(column > 5) { // To make up for skipping the bar
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

                if(checkerX == 325.775) {
                    pip = 0;
                    array[index] = pip;
                }

                column = (int) (((checkerX - 109) / 33.35) + 0.5d); // Get column from X coordinate
                if(column > 5) { // To make up for skipping the bar
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

        // Remove duplicates using property of hashSet
        for (int x : array) set.add(x);

        Iterator iterator = set.iterator();
        while(iterator.hasNext()) {
            newArrayList.add((int) iterator.next());
        }

        return newArrayList;
    }

    static int[][] findMoveTo(ArrayList<Integer> ownerCheckers) {
        int[][] moveTo = new int[ownerCheckers.size()][4];
        for (int index = 0; index < ownerCheckers.size(); index++) {

            int startPip = ownerCheckers.get(index);
            int singleToPip1;
            int singleToPip2;
            int doubleToPip;

            if(startPip == 0) {
                singleToPip1 = 25 - (Dice.roll1+1);
                singleToPip2 = 25 - (Dice.roll2+1);
                doubleToPip = 25 - (Dice.roll1 + Dice.roll2 + 2);
            } else {
                singleToPip1 = startPip - (Dice.roll1+1);
                singleToPip2 = startPip - (Dice.roll2+1);
                doubleToPip = startPip - (Dice.roll1 + Dice.roll2 + 2);
            }

            moveTo[index][0] = startPip; // store starting pip

            if(singleToPip1 <= 0) moveTo[index][1] = 0;
            else moveTo[index][1] = singleToPip1; // - dice roll 1 to get moveTo Pip

            if(singleToPip2 <= 0) moveTo[index][2] = 0;
            else moveTo[index][2] = singleToPip2;

            if(doubleToPip <= 0) moveTo[index][3] = 0;
            else moveTo[index][3] = doubleToPip;
        }
        return moveTo;
    }
}
