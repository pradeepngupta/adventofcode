package in.pradeep;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class GuardGallivant {
    private static final char GUARD = '^';
    private static final char OBSTACLE = '#';

    private static final char EMPTY = '.';
    private static final int[][] DIRECTIONS = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}}; // Up, Right, Down, Left

    public static void main(String[] args) {
        // Read the actual input from a file
        Scanner scanner = new Scanner(Objects.requireNonNull(GuardGallivant.class.getResourceAsStream("/guard_gallivant_day6_input.txt")));

        char[][] map = parseInput(scanner).toArray(new char[0][]);
        log.info("input map");
        for (char[] chars : map) {
            log.info("{}", Arrays.toString(chars));
        }

        GuardGallivant guardGallivant = new GuardGallivant();
        int distinctPositions = guardGallivant.countDistinctPositions(map);
        log.info("distinct positions will the guard visit before leaving the mapped area: {}", distinctPositions);

        int loopPositions = guardGallivant.countLoopPositions(map);
        log.info("there are only {} different positions where a new obstruction would cause the guard to get stuck in a loop", loopPositions);
    }

    private int countLoopPositions(char[][] map) {
        int count = 0;
        int[] guardStart = findGuardPosition(map);

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == EMPTY && (i != guardStart[0] || j != guardStart[1]) && createsLoop(map, i, j)) {
                        count++;
                    }

            }
        }

        return count;
    }

    private boolean createsLoop(char[][] originalMap, int i, int j) {
        char[][] map = copyMap(originalMap);
        map[i][j] = OBSTACLE;

        int[] guardPosition = findGuardPosition(map);
        int direction = 0; // Start facing up
        Set<String> visitedPositions = new HashSet<>();

        while (true) {
            String posKey = guardPosition[0] + "," + guardPosition[1] + "," + direction;
            if (visitedPositions.contains(posKey)) {
                return true; // Loop detected
            }

            if (isOutOfBounds(guardPosition, map.length, map[0].length)) {
                return false; // Guard left the map
            }

            visitedPositions.add(posKey);

            int[] nextPosition = new int[]{
                    guardPosition[0] + DIRECTIONS[direction][0],
                    guardPosition[1] + DIRECTIONS[direction][1]
            };

            if (isOutOfBounds(nextPosition, map.length, map[0].length)) return false; // Guard will leave the map

            if (map[nextPosition[0]][nextPosition[1]] == OBSTACLE) {
                direction = (direction + 1) % 4; // Turn right
            } else {
                guardPosition = nextPosition;
            }
        }
    }

    private char[][] copyMap(char[][] original) {
        char[][] copy = new char[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
    }

    private static List<char[]> parseInput(Scanner scanner) {
        List<char[]> charList = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            charList.add(line.toCharArray());
        }
        scanner.close();
        return charList;
    }

    private int countDistinctPositions(char[][] map) {
        int rows = map.length;
        int cols = map[0].length;
        Set<String> visitedPositions = new HashSet<>();
        int[] guardPosition = findGuardPosition(map);
        int direction = 0; // Start facing up
        int countObstacles = countObstacles(map);
        log.info("Guard Position Initial: {} with direction {}, total Obstacles: {}", Arrays.toString(guardPosition), direction, countObstacles);

        while (!(isOutOfBounds(guardPosition, rows, cols) || visitedPositions.size() == (rows * cols - countObstacles))) {
            log.info("visitedPositions: {}, guardPosition {}", visitedPositions, Arrays.toString(guardPosition));
            visitedPositions.add(guardPosition[0] + "," + guardPosition[1]);
            log.info("Guard Position Visited: {} with direction {}", Arrays.toString(guardPosition), direction);
            int[] nextPosition = new int[]{
                    guardPosition[0] + DIRECTIONS[direction][0],
                    guardPosition[1] + DIRECTIONS[direction][1]
            };
            if (isOutOfBounds(nextPosition, rows, cols)) break;

            if (map[nextPosition[0]][nextPosition[1]] == OBSTACLE) {
                direction = (direction + 1) % 4; // Turn right
            } else {
                guardPosition = nextPosition;
            }

            log.info("nextPosition {} && direction {}", Arrays.toString(nextPosition), direction);
        }

        return visitedPositions.size();
    }

    private boolean isOutOfBounds(int[] position, int rows, int cols) {
        return position[0] < 0 || position[0] >= rows || position[1] < 0 || position[1] >= cols;
    }

    private int[] findGuardPosition(char[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == GUARD) {
                    return new int[]{i, j};
                }
            }
        }
        throw new IllegalArgumentException("Guard not found in the map");
    }

    private static int countObstacles(char[][] map) {
        int count = 0;
        for (char[] row : map) {
            for (char cell : row) {
                if (cell == OBSTACLE) {
                    count++;
                }
            }
        }
        return count;
    }
}
