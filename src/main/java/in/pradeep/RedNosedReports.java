package in.pradeep;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.IntStream;

@Slf4j
public class RedNosedReports {
    public static void main(String[] args) {
        // Read the actual input from a file
        Scanner scanner = new Scanner(Objects.requireNonNull(RedNosedReports.class.getResourceAsStream("/red_nosed_report_day2_input.txt")));
        List<List<Integer>> reports = getReports(scanner);

        RedNosedReports redNosedReports = new RedNosedReports();

        int safeReportCount = 0;
        int damplerSafeReportCount = 0;
        for (List<Integer> report: reports)
        {
            if(redNosedReports.isSafe(report)) {
                safeReportCount ++;
            } else if(redNosedReports.canBeMadeSafe(report)) {
                damplerSafeReportCount++;
            }
        }
        log.info("Number of Safe Report: {}, together with DamplerSafe: {}", safeReportCount, safeReportCount + damplerSafeReportCount);
    }

    // Function to check if a report can be made safe by removing one level
    public  boolean canBeMadeSafe(List<Integer> report) {
        for (int i = 0; i < report.size(); i++) {
            int finalI = i;
            List<Integer> modifiedReport =  IntStream.range(0, report.size())
                    .filter(index -> finalI != index)
                    .mapToObj(report::get)
                    .toList();

            if (isSafe(modifiedReport)) {
                return true;
            }
        }
        return false;
    }

    // Function to check if a single report is safe
    public  boolean isSafe(List<Integer> report) {
        if (report.size() < 2) {
            return true; // A report with 0 or 1 level is considered safe
        }

        boolean increasing = report.get(1) > report.get(0);

        for (int i = 1; i < report.size(); i++) {
            int diff = report.get(i) - report.get(i - 1);

            if (increasing && diff <= 0) {
                return false; // Not consistently increasing
            }
            if (!increasing && diff >= 0) {
                return false; // Not consistently decreasing
            }
            if (Math.abs(diff) < 1 || Math.abs(diff) > 3) {
                return false; // Difference not between 1 and 3
            }
        }

        return true;
    }


    private static List<List<Integer>> getReports(Scanner scanner) {
        List<List<Integer>> reports = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] numbers = line.trim().split("\\s+");
            List<Integer> report = new ArrayList<>();

            for(String s: numbers) {
                report.add(Integer.parseInt(s));
            }

            reports.add(report);

        }
        scanner.close();
        return reports;
    }
}
