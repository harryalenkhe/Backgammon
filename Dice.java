import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.Random;

class Dice extends Group {

    private static Text diceNumber1;
    private static Text diceNumber2;
    private static Text cubeNumber;
    static int roll1;
    static int roll2;
    private static StackPane dice1;
    private static StackPane dice2;
    private static StackPane doublingCube;
//    static int x = 1;

    Dice() {
        setUpDice();
        setUpDoublingCube();
        getChildren().addAll(dice1,dice2,doublingCube);
    }

    static void rollDice() {
        Random r = new Random();
        roll1 = r.nextInt(6);
        roll2 = r.nextInt(6);

        dice1.getChildren().remove(diceNumber1);
        dice2.getChildren().remove(diceNumber2);

        diceNumber1.setFill(Color.BLACK);
        diceNumber1.setText(Integer.toString(roll1 + 1));
        dice1.getChildren().add(diceNumber1);
        dice1.setLayoutX(175.7);
        dice1.setLayoutY(271);

        diceNumber2.setFill(Color.BLACK);
        diceNumber2.setText(Integer.toString(roll2 + 1));
        dice2.getChildren().add(diceNumber2);
        dice2.setLayoutX(442.5);
        dice2.setLayoutY(271);
    }

    private void setUpDice() {
        dice1 = new StackPane();
        diceNumber1 = new Text("6");
        Rectangle outerShape = new Rectangle();
        outerShape.setWidth(30); outerShape.setHeight(30);
        outerShape.setStroke(Color.RED); outerShape.setFill(Color.WHITE);
        dice1.getChildren().addAll(outerShape, diceNumber1); // Make rectangle into die
        dice1.setLayoutX(42.5);
        dice1.setLayoutY(76);

        dice2 = new StackPane(); // Container for rectangle and diceNumber1
        diceNumber2 = new Text("6");
        Rectangle die2 = new Rectangle();
        die2.setWidth(30);
        die2.setHeight(30);
        die2.setStroke(Color.BLUE);
        die2.setFill(Color.WHITE);
        dice2.getChildren().addAll(die2, diceNumber2);
        dice2.setLayoutX(42.5);
        dice2.setLayoutY(107);
    }

    private void setUpDoublingCube() {
        doublingCube = new StackPane();
        cubeNumber = new Text("1");
        cubeNumber.setFill(Color.WHITE);
        Rectangle doubleCube = new Rectangle();
        doubleCube.setHeight(45);
        doubleCube.setWidth(45);
        doubleCube.setStroke(Color.WHITE);
        doubleCube.setFill(Color.DARKGREEN);
        doublingCube.getChildren().addAll(doubleCube, cubeNumber);
        doublingCube.setLayoutY(271);
        doublingCube.setLayoutX(35);
    }

//    static void firstDoubleOdds() {
//        doublingCube.getChildren().remove(cubeNumber);
//        x = x*2;
//        String num = Integer.toString(x);
//        cubeNumber = new Text(num);
//        cubeNumber.setFill(Color.WHITE);
//        doublingCube.getChildren().add(cubeNumber);
//    }
//
//    static int doubleOdds(int currentPlayer, int currentHolder) {
//        if(canPlayerDouble(currentPlayer ,currentHolder)) { // If player wants to double
//            if(currentHolder == Player.playerRed.getTurn()) { // Red Player
//                doublingCube.getChildren().remove(cubeNumber);
//                x = x*2;
//                String num = Integer.toString(x);
//                cubeNumber = new Text(num);
//                cubeNumber.setFill(Color.WHITE);
//                doublingCube.getChildren().add(cubeNumber);
//                currentHolder = Player.playerBlue.getTurn();
//                UserInterface.textArea.appendText("Odds are now " + Dice.x + "\n");
//                UserInterface.textArea.appendText(Player.playerBlue.getName() + " now holds the Doubling Cube\n");
//            }
//
//            else {
//                doublingCube.getChildren().remove(cubeNumber);
//                x = x * 2;
//                String num = Integer.toString(x);
//                cubeNumber = new Text(num);
//                cubeNumber.setFill(Color.WHITE);
//                doublingCube.getChildren().add(cubeNumber);
//                currentHolder = Player.playerRed.getTurn();
//                UserInterface.textArea.appendText("Odds are now " + Dice.x + "\n");
//                UserInterface.textArea.appendText(Player.playerRed.getName() + " now holds the Doubling Cube\n");
//            }
//        }
//
//        else {
//            UserInterface.textArea.appendText("Player can not double cube\n");
//        }
//
//        return currentHolder;
//    }

//    private static boolean canPlayerDouble(int currentPlayer, int currentHolder) {
//        boolean canDouble;
//        canDouble = currentPlayer == currentHolder;
//        return canDouble;
//    }
//
//    static boolean acceptDouble(String acceptOrNot) {
//        boolean decision;
//
//        decision = acceptOrNot.equalsIgnoreCase("Yes");
//
//        return decision;
//    }
}
