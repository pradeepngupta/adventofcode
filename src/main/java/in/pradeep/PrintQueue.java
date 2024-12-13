package in.pradeep;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class PrintQueue {
    public static void main(String[] args) {
        // Read the actual input from a file
        Scanner scanner = new Scanner(Objects.requireNonNull(PrintQueue.class.getResourceAsStream("/print_queue_day5_input.txt")));

        // Parse the input
        PrintQueueRecord printQueueRecord = PrintQueue.parseInput(scanner);

        PrintQueue printQueue = new PrintQueue();
        List<List<Integer>> validPrintQueues = printQueue.findValidPrintQueues(printQueueRecord);
        log.info("validPrintQueues: {}", validPrintQueues);
        int sum = 0;
        for (List<Integer> validPrintQueue : validPrintQueues) {
            sum += validPrintQueue.get(validPrintQueue.size() / 2);
        }
        log.info("Add up the middle page number from valid print queue updates {}", sum);

    }

    private List<List<Integer>> findValidPrintQueues(PrintQueueRecord printQueueRecord) {
        List<PagesPriorityOrder> pagesPriorityOrder = printQueueRecord.pagesPriorityOrder;
        Map<Integer, List<PagesPriorityOrder>> pagesInPriorityOrder = mappedPriorityOrder(pagesPriorityOrder);
        log.info("pagesInPriorityOrder: {}", pagesInPriorityOrder);
        log.info("printQueue {}", printQueueRecord.printQueues);
        List<List<Integer>> validPrintQueues = new ArrayList<>();
        for (List<Integer> printQueue : printQueueRecord.printQueues) {
            boolean valid = isValid(printQueue, pagesInPriorityOrder);
            log.info("PrinterQueue {} -> isValid: {}", printQueue, valid);
            if (valid)
                validPrintQueues.add(printQueue);
        }

        return validPrintQueues;
    }

    private Map<Integer, List<PagesPriorityOrder>> mappedPriorityOrder(List<PagesPriorityOrder> pagesPriorityOrder) {
        Map<Integer, List<PagesPriorityOrder>> map = new HashMap<>();

        for (PagesPriorityOrder pagesPriorityOrder1 : pagesPriorityOrder) {
            map.computeIfAbsent(pagesPriorityOrder1.first, k -> new ArrayList<>());
            map.computeIfAbsent(pagesPriorityOrder1.second, k -> new ArrayList<>());
            map.get(pagesPriorityOrder1.first).add(pagesPriorityOrder1);
            map.get(pagesPriorityOrder1.second).add(pagesPriorityOrder1);
        }
        return map;
    }

    private boolean isValid(List<Integer> printQueue, Map<Integer, List<PagesPriorityOrder>> pagesInPriorityOrder) {

        for (int i = 0; i < printQueue.size(); i++) {
            boolean validIndex = isValidIndex(i, printQueue, pagesInPriorityOrder);
            log.info("validIndex -> index {} in printerQueue -> {}", i, validIndex);
            if (validIndex) continue;
            return false;
        }

        return true;
    }

    private boolean isValidIndex(int index, List<Integer> printQueue, Map<Integer, List<PagesPriorityOrder>> pagesInPriorityOrder) {
        if (index == 0) return true;

        for (int i = 0; i < index; i++) {
            int firstValue = printQueue.get(i);
            int secondValue = printQueue.get(index);
            if (isInValidOrder(firstValue, secondValue, pagesInPriorityOrder)) continue;
            return false;
        }
        return true;
    }

    private boolean isInValidOrder(int firstValue, int secondValue, Map<Integer, List<PagesPriorityOrder>> pagesInPriorityOrder) {
        List<PagesPriorityOrder> firstList = pagesInPriorityOrder.get(firstValue);
        List<PagesPriorityOrder> secondList = pagesInPriorityOrder.get(secondValue);

        List<PagesPriorityOrder> common = firstList.stream().filter(secondList::contains).toList();

        if (common.isEmpty()) return true;

        for (PagesPriorityOrder pagesPriorityOrder : common) {
            if (pagesPriorityOrder.first == firstValue && pagesPriorityOrder.second == secondValue) continue;
            return false;
        }
        return true;
    }


    private static PrintQueueRecord parseInput(Scanner scanner) {
        List<PagesPriorityOrder> pagesPriorityOrder = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (!line.contains("|")) break;
            String[] split = line.trim().split("\\|");
            pagesPriorityOrder.add(new PagesPriorityOrder(Integer.parseInt(split[0]), Integer.parseInt(split[1])));
        }

        List<List<Integer>> printQueues = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] split = line.trim().split(",");
            if (split.length == 0) break;

            List<Integer> printQueue = new ArrayList<>();
            for (String s : split) {
                printQueue.add(Integer.valueOf(s));
            }
            printQueues.add(printQueue);
        }

        scanner.close();
        return new PrintQueueRecord(pagesPriorityOrder, printQueues);
    }

    private record PrintQueueRecord(List<PagesPriorityOrder> pagesPriorityOrder, List<List<Integer>> printQueues) {
    }

    private record PagesPriorityOrder (int first, int second) {

    }

}

