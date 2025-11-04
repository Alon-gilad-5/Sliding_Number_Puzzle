public class Node {

    //Attributes
    State state; //The state of the node
    Node parent; //The father node
    Action action; //The action that led to the node


    //Constructors
    //Constructor that implements attributes from the user
    public Node(State state, Node parent, Action action) {
        this.state = state;
        this.parent = parent;
        this.action = action;
    }


    //Getters & Setters
    public State getState() {
        return state;
    }
    public void setState(State state) {
        this.state = state;
    }

    public Node getParent() {
        return parent;
    }
    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Action getAction() {
        return action;
    }
    public void setAction(Action action) {
        this.action = action;
    }


    //Main Methods
    //Expand the current node to all the possible nodes that can be made from it
    public Node[] expand(){
        Node[] returnNodes = new Node[state.actions().length];
        for (int i = 0; i<state.actions().length ; i++){ //Run through all the possible actions
            //From each action create a new node and add it to the return array
            returnNodes[i]=new Node(state.result(state.actions()[i]) , this , state.actions()[i]);
        }
        return returnNodes; //Return the result array
    }

    //Calculate the heuristic value for a node
    public int heuristicValue(){
        int returnValue = 0;
        returnValue += this.manhattanHeuristic(); //Add the heuristic value from the manhattan calculation
        returnValue += this.linearConflictsHeuristic(); //Add the heuristic value from the linear conflict calculation
        return returnValue; //return the heuristic value
    }

    /*
    Manhattan heuristic value calculates the distance each tile is from its wanted location and adds it all together

    Each 1 value represents at minimum 1 move
    for example, in board "0 1", 1's manhattan value is 1, so it takes at least 1 move to get it to its place
     */
    //Calculates the heuristic value from the manhattan calculation
    private int manhattanHeuristic(){
        int manhattanHeuristic = 0; //Default manhattan value is 0
        int numOfRows = state.getBoard().getTiles().length ; //Get the number of rows on the board
        int numOfColumns = state.getBoard().getTiles()[0].length ; //Get the number of columns on the board

        for (int i=0 ; i<numOfRows ; i++){ //Run through all rows
            for (int j=0 ; j<numOfColumns ; j++){ //Run through all columns

                int currentValue = state.getBoard().getTiles()[i][j].getValue();
                int wantedRow = (currentValue-1)/numOfColumns; //Calculate the current's tile wanted row
                int wantedColumn = (currentValue-1)%numOfColumns; //Calculate the current's tile wanted column
                if (currentValue==0){
                    //For the empty tile, the manhattan value is 0 always
                    wantedRow = i;
                    wantedColumn = j;
                }

                //For each tile (that isn't 0) add the distance from the tile to its needed location to the manhattan value
                manhattanHeuristic += Math.abs(wantedRow - i) + Math.abs(wantedColumn - j);
            }
        }

        return manhattanHeuristic; //Return the manhattan value
    }

    /*
    Linear conflict heuristic adds on top of the manhattan heuristic extra moves when a specific situation occurs

    The situation is when 2 or more tiles are in their needed rows/columns, but are swapped in their order
    For example: in a 3X3 board "1 2 3|5 4 6|7 8 0", 4 and 5 are in linear conflict
    Another example: in a 3X3 board "7 2 3|4 5 6|1 8 0", 1,4 and 7 are all linear conflict with one another

    A regular linear conflict adds 2 more moves to the manhattan value
    for example, in the first example, the manhattan value says 4 has 1 move to the left and 5 has 1 move to the right,
    but that cannot happen since in order to move 4 left, 5 needs to move from there, and vice-versa
    The solution is to move 5 down once, then 4 left, and then 5 right and up: overall 4 moves, 2 more than manhattan

    In more complex linear conflict situations (i.e. 3 or more tiles are in linear conflict with each other), we'll find
    the tile with the most conflicts, and solve it (i.e. add 2 moves), and then after we've solved that conflict, remove
    it from the other's conflict and look for the next tile with the most conflicts...
    Keep solving conflicts for the tile with the most conflicts until there are none left
     */
    //Calculates the heuristic value from the linear conflict calculation
    private int linearConflictsHeuristic(){
        Tile[][] board = state.getBoard().getTiles(); //Get the current board
        int numOfRows = board.length ; //Get the number of rows on the board
        int numOfColumns = board[0].length ; //Get the number of columns on the board
        int linearConflictsHeuristic = 0; //Default linear conflict value is 0

        //First, find and solve linear conflicts in each row

        for (int row=0 ; row<numOfRows ; row++){ //Run through all rows
            //ConflictAmounts represents the amount of conflicts each tile on the row has
            int[] conflictAmounts = new int[numOfColumns];
            //ConflictRecords keeps records of the tiles that have a conflict with each tile, for example,
            //conflictRecords[0] holds all the columns that the first tile in the row has conflicts with
            int[][] conflictRecords = new int[numOfColumns][numOfColumns-1];

            //Find all conflicts in the row, and set records and amounts accordingly
            findConflictsInRow(conflictRecords,conflictAmounts,row,numOfRows,numOfColumns,board);

            //Find whether there are conflicts in the row
            boolean areThereConflicts = areThereConflicts = areThereConflicts(conflictAmounts);;

            while (areThereConflicts){ //Solve conflicts until there are none left
                //Find the column with the most conflicts
                int columnWithMostConflicts=findMostConflicts(conflictAmounts);

                //Solve the conflicts of that tile
                linearConflictsHeuristic+=2;
                conflictAmounts[columnWithMostConflicts] = 0;

                //Remove records of conflicts of the solved tile
                removeRecordsOfTile(conflictRecords,columnWithMostConflicts);

                //Decrease the amount of conflicts other tiles have, if they had a conflict with the solved tile
                decreaseAmountsOfSolvedTile(conflictRecords,conflictAmounts,columnWithMostConflicts);

                //Find whether there are still conflicts in the row
                areThereConflicts = areThereConflicts = areThereConflicts(conflictAmounts);;
            }
        }

        //Now, find and solve linear conflicts in each column

        for (int column=0 ; column<numOfColumns ; column++){ //Run through all columns
            //ConflictAmounts represents the amount of conflicts each tile on the column has
            int[] conflictAmounts = new int[numOfRows];
            //ConflictRecords keeps records of the tiles that have a conflict with each tile, for example,
            //conflictRecords[0] holds all the rows that the first tile in the column has conflicts with
            int[][] conflictRecords = new int[numOfRows][numOfRows-1];

            //Find all conflicts in the column, and set records and amounts accordingly
            findConflictsInColumn(conflictRecords,conflictAmounts,column,numOfRows,numOfColumns,board);

            //Find whether there are conflicts in the column
            boolean areThereConflicts = areThereConflicts = areThereConflicts(conflictAmounts);;

            while (areThereConflicts){ //Solve conflicts until there are none left
                //Find the row with the most conflicts
                int rowWithMostConflicts=findMostConflicts(conflictAmounts);

                //Solve the conflicts of that tile
                linearConflictsHeuristic+=2;
                conflictAmounts[rowWithMostConflicts] = 0;

                //Remove records of conflicts of the solved tile
                removeRecordsOfTile(conflictRecords,rowWithMostConflicts);

                //Decrease the amount of conflicts other tiles have, if they had a conflict with the solved tile
                decreaseAmountsOfSolvedTile(conflictRecords,conflictAmounts,rowWithMostConflicts);

                //Find whether there are still conflicts in the column
                areThereConflicts = areThereConflicts(conflictAmounts);
            }
        }
        return linearConflictsHeuristic; //Return the final linear conflict value
    }

    //Private methods for linear conflict heuristic:
    //Find conflicts in a row and sets records and amounts accordingly
    private static void findConflictsInRow (int[][] conflictRecords , int[] conflictAmounts , int row ,
                                            int numOfRows , int numOfColumns , Tile[][]board){

        for (int leftColumn=0 ; leftColumn<numOfColumns-1 ; leftColumn++){//Run through all columns, exclude last

            if (board[row][leftColumn].getValue() != 0){ //0 doesn't conflict with anyone
                //Calculate the current tile's wanted row and column
                int leftTileWantedRow = (board[row][leftColumn].getValue()-1) / numOfColumns;
                int leftTileWantedColumn = (board[row][leftColumn].getValue()-1) % numOfColumns;

                //If the current tile is in its wanted row, search for conflicts after it on the row
                for (int rightColumn=leftColumn+1 ; rightColumn<numOfColumns && leftTileWantedRow==row ; rightColumn++) {

                    //There is a conflict if both tiles aren't 0, both are on their wanted rows,
                    //and they need to go through each other to get to their wanted columns
                    if ((leftTileWantedColumn > (board[row][rightColumn].getValue() - 1) % numOfColumns) &&
                            (board[row][rightColumn].getValue() != 0) &&
                            (leftTileWantedRow == (board[row][rightColumn].getValue() - 1) / numOfColumns)) {

                        //If there is a conflict, add a conflict to each tile's records,
                        //and add a conflict to each tile's conflict amounts
                        conflictRecords[leftColumn][conflictAmounts[leftColumn]] = rightColumn + 1;
                        conflictRecords[rightColumn][conflictAmounts[rightColumn]] = leftColumn + 1;
                        conflictAmounts[leftColumn]++;
                        conflictAmounts[rightColumn]++;
                    }
                }
            }
        }
    }

    //Find conflicts in a column and sets records and amounts accordingly
    private static void findConflictsInColumn (int[][] conflictRecords , int[] conflictAmounts , int column ,
                                               int numOfRows , int numOfColumns , Tile[][]board){

        for (int topRow=0 ; topRow<numOfRows-1 ; topRow++){//Run through all rows, exclude last

            if (board[topRow][column].getValue() != 0){ //0 doesn't conflict with anyone
                //Calculate the current tile's wanted row and column
                int topTileWantedRow = (board[topRow][column].getValue()-1) / numOfColumns;
                int topTileWantedColumn = (board[topRow][column].getValue()-1) % numOfColumns;

                //If the current tile is in its wanted column, search for conflicts after it on the column
                for (int bottomRow=topRow+1 ; bottomRow<numOfRows && topTileWantedColumn==column ; bottomRow++) {

                    //There is a conflict if both tiles aren't 0, both are on their wanted columns,
                    //and they need to go through each other to get to their wanted rows
                    if ((topTileWantedColumn == (board[bottomRow][column].getValue() - 1) % numOfColumns) &&
                            (board[bottomRow][column].getValue() != 0) &&
                            (topTileWantedRow > ((board[bottomRow][column].getValue() - 1) / numOfColumns))) {

                        //If there is a conflict, add a conflict to each tile's records,
                        //and add a conflict to each tile's conflict amounts
                        conflictRecords[topRow][conflictAmounts[topRow]] = bottomRow + 1;
                        conflictRecords[bottomRow][conflictAmounts[bottomRow]] = topRow + 1;
                        conflictAmounts[topRow]++;
                        conflictAmounts[bottomRow]++;
                    }
                }
            }
        }
    }

    //Find the tile with the most conflicts
    private static int findMostConflicts (int[] conflictAmounts){
        int MostConflicts=0;
        for (int i=0 ; i<conflictAmounts.length ; i++){ //Run through all tiles
            if (conflictAmounts[MostConflicts] < conflictAmounts[i])
                MostConflicts = i; //Update the most conflicts when appropriate
        }
        return MostConflicts;
    }

    //Decrease the amount of conflicts other tiles have, if they had a conflict with the solved tile
    private static void decreaseAmountsOfSolvedTile(int[][] conflictRecords , int[] conflictAmounts , int solvedTile){
        for (int i=0 ; i<conflictRecords.length ; i++){ //Run through all tiles
            for (int j=0 ; j<conflictRecords[i].length ; j++){ //Run through each conflict the tile has

                //Decrease a conflict of a tile if it had conflicts with the solved tile
                if (solvedTile == conflictRecords[i][j]-1)
                    conflictAmounts[i]--;
            }
        }
    }

    //Remove records of conflicts of the solved tile
    private static void removeRecordsOfTile (int[][] conflictRecords , int tileLocation){
        for (int i=0 ; i<conflictRecords[tileLocation].length ; i++){
            //Set all records of the solved tile to 0
            conflictRecords[tileLocation][i]=0;
        }
    }

    //Find whether there are conflicts in the array
    private static boolean areThereConflicts (int[] conflictAmounts){
        boolean areThereConflicts = false;
        for (int i=0 ; i<conflictAmounts.length && !areThereConflicts ; i++){
            if (conflictAmounts[i]!=0) //There are conflicts if at least one tile has more than 0 conflicts
                areThereConflicts = true;
        }
        return areThereConflicts;
    }
}