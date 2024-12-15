package in.pradeep;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class DiskFragmenter {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        // Read the actual input from a file
        Scanner scanner = new Scanner(Objects.requireNonNull(CeresSearch.class.getResourceAsStream("/disk_fragmenter_day9_input.txt")));
        if (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            char[] charArray = line.toCharArray();
            for (char c : charArray) {
                list.add(Integer.parseInt("" + c));
            }
        }
        scanner.close();

        log.info("input {}", list);

        DiskFragmenter diskFragmenter = new DiskFragmenter();
        List<String> diskMap = diskFragmenter.createDiskMap(list);
        log.info("diskMap {}", diskMap);
        List<String> fragmentedDisk = diskFragmenter.fragment(diskMap);
        log.info("fragmented DiskMap {}", fragmentedDisk);

        long checksum = diskFragmenter.calculateChecksum(fragmentedDisk);
        log.info("resulting filesystem checksum is {}", checksum);

        Map<Integer, Integer> filesMap = diskFragmenter.createDiskFilesMap(list);

        List<String> contiguousFragmentedDisk = diskFragmenter.contiguousFragment(diskMap, filesMap);
        log.info("contagious diskmap {}", contiguousFragmentedDisk);
        long checksum2 = diskFragmenter.calculateChecksum(contiguousFragmentedDisk);
        log.info("resulting contagious filesystem checksum is {}", checksum2);
    }


    private long calculateChecksum(List<String> fragmentedDisk) {
        long checksum = 0;
        for (int i = 0; i < fragmentedDisk.size(); i++) {
            if (Objects.equals(fragmentedDisk.get(i), ".")) continue;
            checksum += (long) i * Integer.parseInt(fragmentedDisk.get(i));
        }
        return checksum;
    }

    private List<String> fragment(List<String> diskMap) {
        List<String> fragmentedDiskMap = new ArrayList<>(diskMap);
        for (int i = 0; i < fragmentedDiskMap.size(); i++) {
            if (!fragmentedDiskMap.get(i).equals(".")) {
                continue;
            }
            for (int j = fragmentedDiskMap.size() - 1; j >= i; j--) {
                if (!fragmentedDiskMap.get(j).equals(".")) {
                    Collections.swap(fragmentedDiskMap, i, j);
                    break;
                }
            }
        }
        return fragmentedDiskMap;
    }

    private List<String> contiguousFragment(List<String> diskMap, Map<Integer, Integer> filesMap) {
        List<String> compactedDisk = new ArrayList<>(diskMap);
        for (int fileId = filesMap.size() - 1; fileId >= 0; fileId--) {
            int fileSize = filesMap.get(fileId);
            int currentStart = findFileStart(compactedDisk, fileId);
            if (currentStart == -1) continue;

            int newStart = findFreeSpace(compactedDisk, fileSize, currentStart);
            if (newStart != -1 && newStart < currentStart) {
                moveFile(compactedDisk, currentStart, newStart, fileSize);
            }
        }
        return compactedDisk;
    }

    private void moveFile(List<String> compactedDisk, int currentStart, int newStart, int fileSize) {
        for (int i = 0; i < fileSize; i++) {
            compactedDisk.set(newStart + i, compactedDisk.get(currentStart + i));
            compactedDisk.set(currentStart + i, ".");
        }
    }

    private int findFreeSpace(List<String> compactedDisk, int fileSize, int currentStart) {
        int consecutiveFreeSpace = 0;
        for (int i = 0; i < currentStart; i++) {
            if (compactedDisk.get(i).equals(".")) {
                consecutiveFreeSpace++;
                if (consecutiveFreeSpace == fileSize) {
                    return i - fileSize + 1;
                }
            } else {
                consecutiveFreeSpace = 0;
            }
        }
        return -1;
    }

    private int findFileStart(List<String> compactedDisk, int fileId) {
        for (int i = 0; i < compactedDisk.size(); i++) {
            if (!compactedDisk.get(i).equals(".") && Integer.parseInt(compactedDisk.get(i)) == fileId) {
                return i;
            }
        }
        return -1;
    }

    private List<String> createDiskMap(List<Integer> list) {
        int fileId = 0;
        List<String> diskMap = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            int diskChar = Integer.parseInt("" + list.get(i));
            boolean even = i % 2 == 0;
            for (int j = 0; j < diskChar; j++) {
                if (even) diskMap.add("" + fileId);
                else diskMap.add(".");
            }
            if (even) fileId++;
        }
        return diskMap;
    }

    private Map<Integer, Integer> createDiskFilesMap(List<Integer> list) {
        int fileId = 0;
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            int diskChar = Integer.parseInt("" + list.get(i));
            boolean even = i % 2 == 0;
            if (even) {
                map.put(fileId, diskChar);
                fileId++;
            }
        }
        return map;
    }
}
