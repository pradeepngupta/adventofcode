package in.pradeep;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class HistorianHysteria {
    public static void main(String[] args) {

        // Read the actual input from a file
        Scanner scanner = new Scanner(Objects.requireNonNull(HistorianHysteria.class.getResourceAsStream("/historian_hysteria_day1_input.txt")));

        // Parse the input
        List<List<Integer>> lists = parseInput(scanner);
        List<Integer> leftList = lists.get(0);
        List<Integer> rightList = lists.get(1);

        HistorianHysteria historianHysteria = new HistorianHysteria();

        // Calculate and print the total distance
        int totalDistance = historianHysteria.calculateTotalDistance(leftList, rightList);
        log.info("Actual puzzle total distance: {}", totalDistance);

        int similarityScore = historianHysteria.calculateSimilarityScore(leftList, rightList);
        log.info("Actual puzzle similarityScore: {}", similarityScore);
    }

    public int calculateSimilarityScore(List<Integer> leftList, List<Integer> rightList) {
        int similarityScore = 0;

        for (int l : leftList) {
            similarityScore += l * Collections.frequency(rightList, l);
        }

        return similarityScore;
    }

    public int calculateTotalDistance(List<Integer> leftList, List<Integer> rightList) {
        // sort the lists
        Collections.sort(leftList);
        Collections.sort(rightList);

        int totalDistance = 0;
        int minCount = Math.min(leftList.size(), rightList.size());
        int maxCount = Math.max(leftList.size(), rightList.size());

        for (int i = 0; i < minCount; i++) {
            totalDistance += Math.abs(leftList.get(i) - rightList.get(i));
        }

        if (minCount < maxCount) {
            List<Integer> list = leftList;
            if (minCount == leftList.size()) {
                list = rightList;
            }
            for (int i = minCount; i < maxCount; i++) {
                totalDistance += Math.abs(list.get(i));
            }
        }

        return totalDistance;
    }

    private static List<List<Integer>> parseInput(Scanner scanner) {
        List<Integer> leftList = new ArrayList<>();
        List<Integer> rightList = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] numbers = line.trim().split("\\s+");
            leftList.add(Integer.parseInt(numbers[0]));
            rightList.add(Integer.parseInt(numbers[1]));
        }
        scanner.close();

        List<List<Integer>> result = new ArrayList<>();
        result.add(leftList);
        result.add(rightList);
        return result;
    }
}
