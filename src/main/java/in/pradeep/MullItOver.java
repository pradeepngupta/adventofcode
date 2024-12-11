package in.pradeep;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class MullItOver {

    public static final String MUL_PATTERN = "mul\\((\\d{1,3}),(\\d{1,3})\\)";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(Objects.requireNonNull(RedNosedReports.class.getResourceAsStream("/mull_it_over_day3_input.txt")));

        StringBuilder stringBuilder = new StringBuilder();
        while(scanner.hasNextLine())

        {
            String line = scanner.nextLine();
            stringBuilder.append(line);
        }
        scanner.close();

        String corruptedMemory = stringBuilder.toString();

        MullItOver mullItOver = new MullItOver();
        int result = mullItOver.processCorruptedMemory(corruptedMemory);
        log.info("Sum of multiplication results: {}", result);

        result = mullItOver.processCorruptedMemoryForAccuracy(corruptedMemory);
        log.info("Sum of multiplication results (accuracy): {}", result);
    }

    public int processCorruptedMemoryForAccuracy(String corruptedMemory) {


        Pattern mulPattern = Pattern.compile(MUL_PATTERN);
        Pattern doPattern = Pattern.compile("do");
        Pattern dontPattern = Pattern.compile("don't");

        Matcher mulMatcher = mulPattern.matcher(corruptedMemory);
        Matcher doMatcher = doPattern.matcher(corruptedMemory);
        Matcher dontMatcher = dontPattern.matcher(corruptedMemory);

        Result result = new Result(0, true, 0);

        while (result.lastPosition < corruptedMemory.length()) {
            result = processCorruptedMemoryForAccuracyFromLastPosition(corruptedMemory, result, mulMatcher, doMatcher, dontMatcher);
        }
        return result.sum();

    }

    private Result processCorruptedMemoryForAccuracyFromLastPosition(String corruptedMemory, Result result, Matcher mulMatcher, Matcher doMatcher, Matcher dontMatcher) {
        int mulPos = mulMatcher.find(result.lastPosition) ? mulMatcher.start() : corruptedMemory.length();
        int doPos = doMatcher.find(result.lastPosition) ? doMatcher.start() : corruptedMemory.length();
        int dontPos = dontMatcher.find(result.lastPosition) ? dontMatcher.start() : corruptedMemory.length();

        int nextPos = Math.min(mulPos, Math.min(doPos, dontPos));
        if(nextPos == corruptedMemory.length()) {
            return new Result(result.sum, result.enabled, corruptedMemory.length());
        }
        int sum = result.sum;
        int lastPosition;
        boolean enabled = result.enabled;
        if (nextPos == mulPos) {
            if (enabled) {
                int x = Integer.parseInt(mulMatcher.toMatchResult().group(1));
                int y = Integer.parseInt(mulMatcher.toMatchResult().group(2));
                sum += x * y;
            }
            lastPosition = mulMatcher.end();
        } else if (nextPos == dontPos) {
            enabled = false;
            lastPosition = dontMatcher.end();
        } else {
            lastPosition = doMatcher.end();
        }
        return new Result(sum, enabled, lastPosition);
    }

    private record Result(int sum, boolean enabled, int lastPosition) {
    }

    public int processCorruptedMemory(String corruptedMemoryString) {
        int sum = 0;
        Pattern pattern = Pattern.compile(MUL_PATTERN);
        Matcher matcher = pattern.matcher(corruptedMemoryString);

        while (matcher.find()) {
            int x = Integer.parseInt(matcher.toMatchResult().group(1));
            int y = Integer.parseInt(matcher.toMatchResult().group(2));
            sum += x * y;
        }

        return sum;
    }


}
