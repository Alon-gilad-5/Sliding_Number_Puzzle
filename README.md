## Sliding Tile Puzzle Solver (A* Search Implementation)

This repository contains a complete Java implementation of a solver for the $N$-puzzle (Sliding Tile Puzzle, e.g., 8-puzzle, 15-puzzle, etc.). The solver utilizes the **A* Search Algorithm** for efficient pathfinding from an initial state to the solved goal state.

### Key Features

* **A* Search Implementation:** Uses the A* search algorithm with a priority queue to explore the state space efficiently.
* **Heuristic Function:** Employs a sophisticated, admissible, and consistent heuristic based on the sum of two components:
    * **Manhattan Distance:** Calculates the minimum number of moves each tile is away from its goal position.
    * **Linear Conflicts:** A powerful refinement that accounts for misplaced tiles on the same row or column that must pass each other to reach their final positions (each conflict adds a penalty of $+2$ to the heuristic cost).
* **State Representation:** Uses robust object-oriented classes (`Board`, `Tile`, `State`, `Action`, `Direction`) to model the puzzle, making the logic clear and maintainable.
* **Unsolvability Handling:** Includes logic to detect and handle unsolvable puzzle configurations and manage memory limits.

### Project Structure and Core Classes

The project is broken down into modular Java classes:

| Class Name | Purpose | Key Methods |
| :--- | :--- | :--- |
| **`Search.java`** | **The main solver logic.** Implements the A* search algorithm using a priority queue (frontier) and a set of visited states. | `search(String boardString)`, `extractSolution()`, `getStatus()` |
| **`Node.java`** | Represents a single state in the search space. Contains the state, the parent node, and the action that led to it. | `expand()`, `heuristicValue()`, `manhattanHeuristic()`, `linearConflictsHeuristic()` |
| **`State.java`** | Defines a single configuration of the puzzle board. Checks for the goal state and determines all possible next actions. | `isGoal()`, `actions()`, `result()` |
| **`Board.java`** | Represents the 2D grid of tiles. Handles parsing string inputs, generating the goal board, and finding the location of the empty tile (`0`). | `getNumOfRows()`, `getNumOfColumns()`, `findValueOnBoard()` |
| **`Action.java` / `Direction.java`** | Defines a potential move (which tile moves and in which direction). `Direction` is a simple `enum`. | `toString()` (for move output) |
| **`Main.java`** | Driver class that runs a set of test board configurations and reports the success rate, solution length, and number of expanded nodes. | `main()`, `searchOnce()` |

### Getting Started (Running the Project)

1.  **Clone the Repository:** Download the project files.
2.  **Open in IntelliJ IDEA:** Open the project directory as a Java project in **IntelliJ IDEA** (recommended IDE).
3.  **Run `Main.java`:** Execute the `main` method in the `Main.java` class to run the solver against the pre-defined test cases.


![Example of a Sliding Number Puzzle Challenge](Sliding_number.png)
