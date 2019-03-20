class Coordinates {

    private double x; // column
    private double y; // row
    private char player;
    private boolean taken;


    Coordinates(double column, double row, char player) {
        this.x = column;
        this.y = row;
        this.player = player;
        this.taken = false;
    }

    double getX() { return this.x; }

    double getY() { return this.y; }

    char getPlayer() {
        return this.player;
    }

    void setPlayer(char player) {
        this.player = player;
    }

    void occupyCoordinate(char player)
    {
        this.taken = true;
        this.player = player;
    }

    void releaseCoordinate()
    {
        this.taken = false;
        this.player = 'E';
    }

    boolean isTaken() { return this.taken; }
}
