public class Action {

    //Attributes
    private Direction movingDirection; //The direction of the action
    private Tile movingTile; //The tile that's being moved


    //Constructors
    //Constructor that implements attributes from the user
    public Action(Direction movingDirection, Tile movingTile) {
        this.movingDirection = movingDirection;
        this.movingTile = movingTile;
    }


    //Getters & Setters
    public Direction getMovingDirection() {
        return movingDirection;
    }
    public void setMovingDirection(Direction movingDirection) { // צריך לבדוק שהשינוי תקין לפני שמבצעים
        this.movingDirection = movingDirection;
    }

    public Tile getMovingTile() {
        return movingTile;
    }
    public void setMovingTile(Tile movingTile) { // צריך לבדוק שהשינוי תקין לפני שמבצעים
        this.movingTile = movingTile;
    }


    //Main Methods
    //Override toString method
    //Return a string in the format of "Move *num* *direction*", for example, "Move 7 up"
    @Override
    public String toString(){
        String directionString = "nowhere"; //Default direction
        switch (movingDirection){
            case UP: //When the direction is up
                directionString = "up"; //Return the direction up
                break;
            case DOWN: //When the direction is down
                directionString = "down"; //Return the direction down
                break;
            case RIGHT: //When the direction is right
                directionString = "right"; //Return the direction right
                break;

            case LEFT: //When the direction is left
                directionString = "left"; //Return the direction left
                break;
        }
        return "Move " + movingTile + " " + directionString; //Return string in the appropriate format
    }
}