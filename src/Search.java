import java.util.*;

public class Search {
    public static final int SOLVED = 0;
    public static final int UNSOLVABLE = 1;
    public static final int OUT_OF_MEMORY = 2;
    public static final int UNSOLVED = 3;

    private int expandedNodes;
    private List<Action> result;
    private int status = UNSOLVED;

    private Node getRoot(String boardString) {
        //Create a new node according to the board string, when there wasn't an action or father node before.
        return new Node(new State(new Board(boardString),null) , null , null);
    }

    public List<Action> search(String boardString) {
        try {
            Node root = getRoot(boardString);

            Queue<Node> frontier = new PriorityQueue<>(Comparator.comparing(Node::heuristicValue));
            Set<State> visited = new HashSet<>();
            frontier.add(root);

            while (!frontier.isEmpty()) {
                Node node = frontier.remove();
                if (node.getState().isGoal()) {
                    result = extractSolution(node);
                    status = SOLVED;
                    return result;
                }
                expandedNodes++;
                Node[] children = node.expand();

                for (Node child : children) {
                    if (!visited.contains(child.getState())) {
                        visited.add(child.getState());
                        frontier.add(child);
                    }
                }
            }
            status = UNSOLVABLE;
            return null;
        } catch (OutOfMemoryError err) {
            status = OUT_OF_MEMORY;
            return null;
        }
    }

    private List<Action> extractSolution(Node node) {
        List<Action> actions = new ArrayList<>();
        while (node != null) {
            actions.add(node.getAction());
            node = node.getParent();
        }
        Collections.reverse(actions);
        actions.remove(0);
        return actions;
    }

    public int getStatus() {
        return status;
    }

    public List<Action> getResult() {
        return result;
    }

    public int getExpandedNodes() {
        return expandedNodes;
    }
}
