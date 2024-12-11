package in.pradeep;

import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MullItOver {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(Objects.requireNonNull(RedNosedReports.class.getResourceAsStream("/mull_it_over_day3_input.txt")));

        StringBuilder stringBuilder = new StringBuilder();
        while(scanner.hasNextLine())

        {
            String line = scanner.nextLine();
            stringBuilder.append(line);
        }
        scanner.close();

        String corruptedMemory = stringBuilder.toString(); //"xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))";

        int result = processCorruptedMemory(corruptedMemory);
        System.out.println("Sum of multiplication results: " + result);

        result = processCorruptedMemoryForAccuracy(corruptedMemory);
        System.out.println("Sum of multiplication results (accuracy): " + result);
    }

    private static int processCorruptedMemoryForAccuracy(String corruptedMemory) {
        int sum = 0;
        boolean enabled = true;

        Pattern mulPattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
        Pattern doPattern = Pattern.compile("do");
        Pattern dontPattern = Pattern.compile("don't");

        Matcher mulMatcher = mulPattern.matcher(corruptedMemory);
        Matcher doMatcher = doPattern.matcher(corruptedMemory);
        Matcher dontMatcher = dontPattern.matcher(corruptedMemory);

        int lastPosition = 0;

        while (lastPosition < corruptedMemory.length()) {
            int mulPos = mulMatcher.find(lastPosition) ? mulMatcher.start() : corruptedMemory.length();
            int doPos = doMatcher.find(lastPosition) ? doMatcher.start() : corruptedMemory.length();
            int dontPos = dontMatcher.find(lastPosition) ? dontMatcher.start() : corruptedMemory.length();

            int nextPos = Math.min(mulPos, Math.min(doPos, dontPos));
            if(nextPos == corruptedMemory.length())
                break;

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
                enabled = true;
                lastPosition = doMatcher.end();
            }
        }
        return sum;

    }

    private static int processCorruptedMemory(String corruptedMemoryString) {
        int sum = 0;
        Pattern pattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
        Matcher matcher = pattern.matcher(corruptedMemoryString);

        while (matcher.find()) {
            //System.out.println(matcher.toMatchResult().group());
            int x = Integer.parseInt(matcher.toMatchResult().group(1));
            int y = Integer.parseInt(matcher.toMatchResult().group(2));
            sum += x * y;
        }

        return sum;
    }


}
