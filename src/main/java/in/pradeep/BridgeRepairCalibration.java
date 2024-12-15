package in.pradeep;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

@Slf4j
public class BridgeRepairCalibration {
    public static void main(String[] args) {
        // Read the actual input from a file
        List<CalibreEquation> calibreEquations = getCalibreEquationListInput();
        BridgeRepairCalibration bridgeRepairCalibration = new BridgeRepairCalibration();
        long result = bridgeRepairCalibration.evaluateCalibrationResult(calibreEquations, false);
        log.info("the total calibration result {}", result);
        long result2 = bridgeRepairCalibration.evaluateCalibrationResult(calibreEquations, true);
        log.info("the total calibration result with allow-concatenation {}", result2);
    }

    private long evaluateCalibrationResult(List<CalibreEquation> calibreEquations, boolean allowConcatenation) {
        long result = 0;
        for (CalibreEquation calibreEquation : calibreEquations) {
            if (canProduceTestValue(calibreEquation, allowConcatenation))
                result += calibreEquation.result;
        }

        return result;
    }

    private boolean canProduceTestValue(CalibreEquation calibreEquation, boolean allowConcatenation) {
        List<Long> longList = calibreEquation.operands;
        long[] operands = new long[longList.size()];
        for (int i = 0; i < longList.size(); i++) {
            operands[i] = longList.get(i);
        }

        return tryOperators(operands, 0, operands[0], calibreEquation.result, allowConcatenation);
    }

    private boolean tryOperators(long[] numbers, int index, long currentValue, long testValue, boolean allowConcatenation) {
        if (index == numbers.length - 1) {
            return currentValue == testValue;
        }

        // Try addition
        if (tryOperators(numbers, index + 1, currentValue + numbers[index + 1], testValue, allowConcatenation)) {
            return true;
        }
        // Try multiplication
        if(tryOperators(numbers, index + 1, currentValue * numbers[index + 1], testValue, allowConcatenation)) {
            return true;
        }
        if(allowConcatenation) {
            // Try concatenation
            return tryOperators(numbers, index + 1, Long.parseLong(currentValue + String.valueOf(numbers[index + 1])), testValue, true);
        }
        return false;
    }

    private static List<CalibreEquation> getCalibreEquationListInput() {
        Scanner scanner = new Scanner(Objects.requireNonNull(BridgeRepairCalibration.class.getResourceAsStream("/bridge_repair_day7_input.txt")));
        List<CalibreEquation> calibreEquations = new ArrayList<>();
        while (scanner.hasNextLine()) {
            CalibreEquation calibreEquation = new CalibreEquation();
            String line = scanner.nextLine();
            String[] splits = line.split(":");
            calibreEquation.setResult(Long.parseLong(splits[0]));
            String[] opSplits = splits[1].split(" ");
            for (String opSplit : opSplits) {
                if (!opSplit.trim().isEmpty())
                    calibreEquation.getOperands().add(Long.valueOf(opSplit));
            }
            calibreEquations.add(calibreEquation);
        }
        scanner.close();
        return calibreEquations;
    }

    @Getter
    @Setter
    @ToString
    private static class CalibreEquation {
        long result;
        List<Long> operands = new ArrayList<>();

    }

}
