public class Main {
        public static void main(String[] args) {
        String[] boards = {"0 1",
                           "1 0",
                           "0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40",
                           "0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 35 26 27 28 29 30 31 32 33 34 25 36 37 38 39 40",
                           "7 5 4|0 3 2|8 1 6",
                           "7 5 4|0 3 2|8 6 1",
                           "6 4 7|8 5 0|3 2 1",
                           "6 7 5 1|2 10 4 11|9 3 8 0",
                           "1 6 4 15|5 10 0 13|9 11 3 7|12 8 2 14",
                           "1 9 0 12 10 5 6|8 11 2 3 4 13 7",
                           "1 2 3 12 5 6|7 8 9 4 11 17|13 14 15 0 10 16",
                           "29 7 14 9 11 16|23 17 24 22 18 20|5 3 21 13 27 15|6 4 12 1 19 28|26 10 8 25 2 0",
                           "2 3 8 4 14 13 6|16 9 17 0 10 5 7|1 11 15 18 12 19 20",
                           "1 2 23 5 21 14 7 6 16|10 11 3 22 15 35 9 34 0|28 20 13 31 4 33 26 17 8|29 19 12 32 30 24 18 27 25"};
        int successCounter = 0;
        for (String boardString : boards) {
            boolean success = searchOnce(boardString);
            if (success) {
                successCounter++;
            }
        }
        System.out.println("Able to solve " + successCounter + " ot of " + boards.length + " games.");
    }

    private static boolean searchOnce(String boardString) {
        Search search = new Search();
        Thread t = new Thread(() -> search.search(boardString));
        t.start();
        try {
            t.join(60000);
        } catch (InterruptedException ex) {
        }
        boolean succeeded = false;
        if (t.isAlive()) {
            t.stop();
            System.out.println("Timout occurred...");
        } else {
            int status = search.getStatus();
            switch (status) {
                case Search.SOLVED:
                    System.out.println("Solution length: " + search.getResult().size());
                    System.out.println(search.getResult());
                    succeeded = true;
                    break;
                case Search.UNSOLVABLE:
                    System.out.println("Unsolvable game...");
                    break;
                case Search.OUT_OF_MEMORY:
                    System.out.println("Out of memory while searching...");
                    break;
            }
        }
        System.out.println("Number of expanded nodes: " + search.getExpandedNodes());
        System.out.println("----------------------------------------------------------------------");
        return succeeded;
    }
}
