import java.util.ArrayList;

public class Bot1 implements BotAPI {

    // The public API of Bot must not change
    // This is ONLY class that you can edit in the program
    // Rename Bot to the name of your team. Use camel case.
    // Bot may not alter the state of the game objects
    // It may only inspect the state of the board and the player objects

    private PlayerAPI me, opponent;
    private BoardAPI board;
    private CubeAPI cube;
    private MatchAPI match;
    private InfoPanelAPI info;

    Bot1 (PlayerAPI me, PlayerAPI opponent, BoardAPI board, CubeAPI cube, MatchAPI match, InfoPanelAPI info) {
        this.me = me;
        this.opponent = opponent;
        this.board = board;
        this.cube = cube;
        this.match = match;
        this.info = info;
    }

    public String getName() {
        return "Bot1"; // must match the class name
    }

    public String getCommand(Plays possiblePlays) {
        ArrayList<Integer> winPercentForMoves = getWinPercentForMoves(possiblePlays);
        int bestMove = 0;
        for(int index = 0; index < possiblePlays.plays.size(); index++) {
            if(winPercentForMoves.get(index) > bestMove) {
                bestMove = winPercentForMoves.get(index);
            }
        }
        return Integer.toString(winPercentForMoves.indexOf(bestMove) + 1);
    }

    public String getDoubleDecision() {
        if(me.getScore() > opponent.getScore()) {
            return "y";
        } else {
            return "n";
        }
    }

    private ArrayList<Integer> getWinPercentForMoves(Plays possiblePlays) {
        ArrayList<Integer> winPercentage = new ArrayList<>();
        for (Play move : possiblePlays) {
            int moveScore = 0;

            if(move.numberOfMoves() == 1) {
                moveScore += move.getMove(0).getPipDifference();
            } else {
                if(move.getMove(0).isHit() || move.getMove(1).isHit()) {
                    moveScore += 10;
                } else {
                    moveScore += 1;
                }
            }

            winPercentage.add(moveScore);
        }
        return winPercentage;
    }
}
