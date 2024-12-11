package in.pradeep;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class CeresSearch2 {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        // Read the actual input from a file
        Scanner scanner = new Scanner(Objects.requireNonNull(CeresSearch.class.getResourceAsStream("/ceres_search_day4_input.txt")));
                while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            list.add(line);
        }
        scanner.close();

        String lookup = "MAS";
        char[][] grid = list.stream().map(String::toCharArray).toArray(char[][]::new);

        CeresSearch2 ceresSearch2 = new CeresSearch2();
        int count = ceresSearch2.countLookup(grid, lookup);
        log.info("The number of times {} occurs is: {}", lookup, count);
    }

    public int countLookup(char[][] grid, String lookup) {
        int count = 0;
        int rows = grid.length;
        int cols = grid[0].length;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (grid[row][col] == lookup.charAt(1)) {
                    count += searchXMAS(grid, lookup, row, col);
                }
            }
        }

        return count;
    }

    private int searchXMAS(char[][] grid, String lookup, int row, int col) {
        int count = 0;

        int[][] directions = {
                {-1, -1}, {-1, 1},  // left, up, diagonal up-left, diagonal up-right
                {1, 1}, {1, -1},  // right, down, diagonal down-right, diagonal down-left
        };

        for (int i = 0; i < directions.length; i++) {
            int[] dir1 = directions[i];

            for (int j = i; j < directions.length; j++) {
                int[] dir2 = directions[j];

                if (dir1[0] == -dir2[0] || dir1[1] == -dir2[1]) {
                    boolean found = checkMAS1(grid, lookup, row, col, dir1, dir2);
                    if(found) count++;
                }
            }
        }


        return count;
    }

    private boolean checkMAS1(char[][] grid, String lookup, int row, int col, int[] dir1, int[] dir2) {
        boolean found1 = checkMAS(grid, lookup, row, col, dir1[0], dir1[1]);
        if (found1) {
            return checkMAS(grid, lookup, row, col, dir2[0], dir2[1]);
        }
        return false;
    }

    private boolean checkMAS(char[][] grid, String lookup, int row, int col, int dRow, int dCol) {
        int rows = grid.length;
        int cols = grid[0].length;

        // Check for 'M'
        int newRow = row - dRow;
        int newCol = col - dCol;
        if (newRow < 0 || newRow >= rows || newCol < 0 || newCol >= cols) {
            return false;
        }

        if (grid[newRow][newCol] == lookup.charAt(0)) {
            // 'A' is already checked in the center

            // Check for 'S'
            newRow = row + dRow;
            newCol = col + dCol;
            if (newRow < 0 || newRow >= rows || newCol < 0 || newCol >= cols) {
                return false;
            }
            return grid[newRow][newCol] == lookup.charAt(2);
        }

        return false;
    }

}
