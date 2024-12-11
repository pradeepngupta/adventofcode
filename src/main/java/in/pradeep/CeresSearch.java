package in.pradeep;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CeresSearch {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        // Read the actual input from a file
//        Scanner scanner = new Scanner(Objects.requireNonNull(CeresSearch.class.getResourceAsStream("/ceres_search_day4_input.txt")));
//                while (scanner.hasNextLine()) {
//            String line = scanner.nextLine();
//            list.add(line);
//        }
//        scanner.close();

        String str = """
                MMMSXXMASM
                MSAMXMSMSA
                AMXSXMAAMM
                MSAMASMSMX
                XMASAMXAMM
                XXAMMXXAMA
                SMSMSASXSS
                SAXAMASAAA
                MAMMMXMMMM
                MXMXAXMASX
                """;

        list = new ArrayList<>(Arrays.asList(str.split("\n")));
//
//        System.out.println(list);
        String lookup = "XMAS";

        char[][] grid = list.stream().map(String::toCharArray).toArray(char[][]::new);
        int count = countLookup(grid, lookup);
        System.out.println("The number of times " + lookup + " occurs is: " + count);
    }

    private static int countLookup(char[][] grid, String lookup) {
        int count = 0;
        int rows = grid.length;
        int cols = grid[0].length;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (grid[row][col] == lookup.charAt(0)) {
                    count += searchAllDirections(grid, lookup, row, col);
                }
            }
        }

        return count;
    }

    private static int searchAllDirections(char[][] grid, String lookup, int row, int col) {
        int count = 0;
        int[][] directions = {
                {0, 1}, {1, 0}, {1, 1}, {1, -1},  // right, down, diagonal down-right, diagonal down-left
                {0, -1}, {-1, 0}, {-1, -1}, {-1, 1}  // left, up, diagonal up-left, diagonal up-right
        };

        for (int[] dir : directions) {
            if (checkDirection(grid, lookup, row, col, dir[0], dir[1])) {
                count++;
            }
        }

        return count;
    }

    private static boolean checkDirection(char[][] grid, String lookup, int row, int col, int dRow, int dCol) {
        int rows = grid.length;
        int cols = grid[0].length;

        for (int i = 0; i < lookup.length(); i++) {
            int newRow = row + i * dRow;
            int newCol = col + i * dCol;

            if (newRow < 0 || newRow >= rows || newCol < 0 || newCol >= cols ||
                    grid[newRow][newCol] != lookup.charAt(i)) {
                return false;
            }
        }

        return true;
    }


}
