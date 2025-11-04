public class State {

    //Attributes
    private Board board; //The current board of the state
    private Direction lastDirection; //The last direction of the action that led to the current state


    //Constructors
    //Constructor that implements attributes from the user
    public State(Board input,Direction lastDirection) {
        this.board = input;
        this.lastDirection = lastDirection;
    }


    //Getters & Setters
    public Board getBoard() {
        return board;
    }
    public void setBoard(Board board) {
        this.board = board;
    }

    public Direction getLastDirection() {
        return lastDirection;
    }
    public void setLastDirection(Direction lastDirection) {
        this.lastDirection = lastDirection;
    }


    //Main Methods
    //Find whether the current state's board is the goal board
    public boolean isGoal (){
        //Create a target board
        Board targetBoard = new Board(Board.getNumOfRows(board.toString()), Board.getNumOfColumns(board.toString()));
        return board.equals(targetBoard); //Check if the current board is the same to the target board
    }

    //Return all the possible actions from the current state
    public Action[] actions() {
        int[] zeroIndex = this.board.findValueOnBoard(0); //Find the empty tile (0) location
        Action[] allActions = new Action[4]; //Initialize an array with the maximum amount of directions possible
        int counter = 0; //Counter represents the amount of possible actions, starts at default 0

        //Check if each possible direction is possible
        //Don't go back on your last action (for example, don't move up, and then go back down)
        //If an action is possible, add 1 to the amount of possible actions, and add that action to allActions

        //Can make an "up" action when the empty tile isn't at the bottom row, and the last direction wasn't down
        if (zeroIndex[0] < Board.getNumOfRows(this.board.toString()) - 1 && this.lastDirection != Direction.DOWN) {
            allActions[counter] = new Action(Direction.UP, board.getTiles()[zeroIndex[0] + 1][zeroIndex[1]]);
            counter++;
        }

        //Can make a "down" action when the empty tile isn't at the top row, and the last direction wasn't up
        if (zeroIndex[0] > 0 && this.lastDirection != Direction.UP) {
            allActions[counter] = new Action(Direction.DOWN, board.getTiles()[zeroIndex[0] - 1][zeroIndex[1]]);
            counter++;
        }

        //Can make a "right" action when the empty tile isn't at the most left column, and the last direction wasn't left
        if (zeroIndex[1] > 0 && this.lastDirection != Direction.LEFT) {
            allActions[counter] = new Action(Direction.RIGHT,board.getTiles()[zeroIndex[0]][zeroIndex[1]-1]);
            counter++;
        }

        //Can make a "left" action when the empty tile isn't at the most right column, and the last direction wasn't right
        if (zeroIndex[1] < Board.getNumOfColumns(this.board.toString())-1 && this.lastDirection != Direction.RIGHT) {
            allActions[counter] = new Action(Direction.LEFT, board.getTiles()[zeroIndex[0]][zeroIndex[1] + 1]);
            counter++;
        }

        Action[] actions = new Action[counter]; //Initialize the return array to the correct size
        for(int i=0; i<counter ; i++){ //Run through all possible actions
            actions[i] = allActions[i]; //Implement each possible action to the return array
        }
        return actions; //Return the actions array
    }

    //Return a new state made after an action has been made on the current state
    public State result (Action action){
        int[] zeroIndex = this.board.findValueOnBoard(0); //Find the empty tile (0) location
        Board copyBoard = new Board(this.board.toString()); //Create an identical board to the current one

        //Swap the location of the tile that's being moved with the empty tile, according to the action
        switch (action.getMovingDirection()) {
            case UP: //When the action's direction is up
                copyBoard.getTiles()[zeroIndex[0]][zeroIndex[1]].setValue(action.getMovingTile().getValue());
                copyBoard.getTiles()[zeroIndex[0]+1][zeroIndex[1]].setValue(0);
                break;

            case DOWN: //When the action's direction is down
                copyBoard.getTiles()[zeroIndex[0]][zeroIndex[1]].setValue(action.getMovingTile().getValue());
                copyBoard.getTiles()[zeroIndex[0]-1][zeroIndex[1]].setValue(0);
                break;

            case RIGHT: //When the action's direction is right
                copyBoard.getTiles()[zeroIndex[0]][zeroIndex[1]].setValue(action.getMovingTile().getValue());
                copyBoard.getTiles()[zeroIndex[0]][zeroIndex[1]-1].setValue(0);
                break;

            case LEFT: //When the action's direction is left
                copyBoard.getTiles()[zeroIndex[0]][zeroIndex[1]].setValue(action.getMovingTile().getValue());
                copyBoard.getTiles()[zeroIndex[0]][zeroIndex[1]+1].setValue(0);
                break;
        }

        //Return the new state, with the new board, and the direction of the action
        return new State(copyBoard,action.getMovingDirection());
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof State)) {
            return false;
        }
        State state = (State) other;
        return board.equals(state.board);
    }

    @Override
    public int hashCode() {
        return board.hashCode();
    }
}