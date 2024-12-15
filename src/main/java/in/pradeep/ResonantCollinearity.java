package in.pradeep;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class ResonantCollinearity {
    public static void main(String[] args) {
        List<Antenna> antennas = new ArrayList<>();
        Scanner scanner = new Scanner(Objects.requireNonNull(ResonantCollinearity.class.getResourceAsStream("/resonant_collinearity_day8_input.txt")));
        int lineNo = 0;
        int cols = 0;
        while (scanner.hasNextLine()) {
            lineNo++;
            String line = scanner.nextLine();
            char[] charArray = line.toCharArray();
            cols = charArray.length;
            for (int i = 0; i < charArray.length; i++) {
                antennas.add(new Antenna(lineNo, i, charArray[i]));
            }
        }
        scanner.close();

        ResonantCollinearity resonantCollinearity = new ResonantCollinearity();
        int count = resonantCollinearity.countAntinodes(antennas, lineNo, cols);
        log.info("{} unique locations within the bounds of the map contain an antinode.", count);
    }

    private int countAntinodes(List<Antenna> antennas, int rows, int cols) {
        Map<Character, List<Antenna>> antennaMap = new HashMap<>();
        for (Antenna antenna : antennas) {
            antennaMap.computeIfAbsent(antenna.frequency, k -> new ArrayList<>());
            antennaMap.get(antenna.frequency).add(antenna);
        }

        Set<String> antinodes = new HashSet<>();
        for (Map.Entry<Character, List<Antenna>> entry : antennaMap.entrySet()) {
            List<Antenna> antennaList = entry.getValue();
            if (entry.getKey() == '.' || antennaList.size() < 2) continue;

            for (int i = 0; i < antennaList.size(); i++) {
                Antenna a1 = antennaList.get(i);
                for (int j = i + 1; j < antennaList.size(); j++) {
                    Antenna a2 = antennaList.get(j);
                    findAntinodes(a1, a2, rows, cols, antinodes);
                }
            }
        }

        print(antennas, antinodes);
        return antinodes.size();
    }

    private void print(List<Antenna> antennas, Set<String> antinodes) {
        for (int i = 0; i < antennas.size(); i++) {
            int finalI = i;
            System.out.println();
            List<Antenna> list = antennas.stream().filter(antenna -> antenna.x == finalI).toList();
            for (int j = 0; j < list.size(); j++) {
                if (antinodes.contains(i + "," + j))
                    if (list.get(j).frequency != '.') {
                        System.out.print(list.get(j).frequency);
                    } else {
                        System.out.print("#");
                    }
                else {
                    System.out.print(list.get(j).frequency);
                }
            }
        }
    }

    private void findAntinodes(Antenna a1, Antenna a2, int rows, int cols, Set<String> antinodes) {
        log.info("finding antinodes for antennas {}, {}", a1, a2);
        int diffX = a1.x - a2.x;
        int diffY = a1.y - a2.y;

        int[] antinode1 = new int[]{a2.x - diffX, a2.y - diffY};
        int[] antinode2 = new int[]{a1.x + diffX, a1.y + diffY};

        log.info("found antinodes {}, {}", Arrays.toString(antinode1), Arrays.toString(antinode2));
        if (isInBounds(antinode1[0], antinode1[1], rows, cols)) {
            boolean flag = antinodes.add(antinode1[0] + "," + antinode1[1]);
            log.info("Added Antinodes {} to set - {}", Arrays.toString(antinode1), flag);
        }
        if (isInBounds(antinode2[0], antinode2[1], rows, cols)) {
            boolean flag = antinodes.add(antinode2[0] + "," + antinode2[1]);
            log.info("Added Antinodes {} to set - {}", Arrays.toString(antinode2), flag);
        }
    }

    private boolean isInBounds(int x, int y, int rows, int cols) {
        return x >= 0 && x < cols && y >= 0 && y < rows;
    }

    @ToString
    private static class Antenna {
        int x;
        int y;
        char frequency;

        Antenna(int x, int y, char frequency) {
            this.x = x;
            this.y = y;
            this.frequency = frequency;
        }
    }
}
