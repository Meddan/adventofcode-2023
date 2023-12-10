package tbn.aoc2023;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static tbn.aoc2023.Day4.printArray;

public class Day8 {
    public static final Pattern locationPattern = Pattern.compile("[A-Z0-9]+");

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        //part1();
        part2();
    }

    public static void part1() throws FileNotFoundException {
        List<String> lines = readFile("Day8.txt");
        List<String> instructions = getInstructions(lines.get(0));

        lines.remove(0);
        lines.remove(0);

        Map<String, Location> locations = getLocations(lines);

        String currentLocation = "AAA";
        int steps = 0;
        int index = 0;
        while (!currentLocation.equals("ZZZ")) {
            Location location = locations.get(currentLocation);
            steps++;

            if (instructions.get(index).equals("L")) {
                currentLocation = location.l;
            } else {
                currentLocation = location.r;
            }

            index = (index + 1) % instructions.size();
        }

        System.out.println(steps);
    }

    public static void part2() throws FileNotFoundException, InterruptedException {
        List<String> lines = readFile("Day8.txt");
        List<String> instructions = getInstructions(lines.get(0));

        lines.remove(0);
        lines.remove(0);

        Map<String, Location> locations = getLocations(lines);

        String[] currentLocations = getStartingLocations(locations.keySet()).toArray(new String[0]);

        int index = 0;
        int steps = 0;
        Map<String, Set<Integer>> indicesOnEnd = new HashMap<>();

        while (notAtEnd(currentLocations)) {

            steps++;
            for (int i = 0; i < currentLocations.length; i++) {
                Location location = locations.get(currentLocations[i]);
                if (location.name.endsWith("Z")) {
                    Set<Integer> indices;
                    if (indicesOnEnd.containsKey(location.name)) {
                        indices = indicesOnEnd.get(location.name);
                    } else {
                        indices = new HashSet<>();
                        indicesOnEnd.put(location.name, indices);
                    }
                    indices.add(steps);
                }


                if (instructions.get(index).equals("L")) {
                    currentLocations[i] = location.l;
                } else {
                    currentLocations[i] = location.r;
                }


            }
            index = (index + 1) % instructions.size();
            if (steps % 10000000 == 0) {
                //printArray(currentLocations);
                //System.out.println(indicesOnEnd);
                for (Set<Integer> set : indicesOnEnd.values()) {
                    System.out.println("#################");

                    System.out.println(set.stream().map(i -> i / instructions.size()).toList());
                }
            }
        }

        System.out.println(steps);
    }

    public static boolean notAtEnd(String[] currentLocations) {
        return !Arrays.stream(currentLocations).allMatch(s -> s.endsWith("Z"));
    }


    public static List<String> getStartingLocations(Collection<String> strings) {
        List<String> result = new ArrayList<>();
        for (String s : strings) {
            if (s.endsWith("A")) {
                result.add(s);
            }
        }
        return result;
    }


    public static Map<String, Location> getLocations(List<String> lines) {
        Map<String, Location> locations = new HashMap<>();
        for (String line : lines) {
            Location l = getLocation(line);
            locations.put(l.name, l);
        }

        return locations;
    }

    private static Location getLocation(String line) {
        Matcher matcher = locationPattern.matcher(line);

        int i = 0;
        matcher.find(i);
        String name = matcher.group();
        i = matcher.end();
        matcher.find(i);
        String l = matcher.group();
        i = matcher.end();
        matcher.find(i);
        String r = matcher.group();

        return new Location(name, l, r);
    }

    public static List<String> getInstructions(String line) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < line.length(); i++) {
            result.add(String.valueOf(line.charAt(i)));
        }
        return result;
    }

    public static class Location {
        public String name;
        public String l;
        public String r;

        public Location(String name, String l, String r) {
            this.name = name;
            this.l = l;
            this.r = r;
        }

        @Override
        public String toString() {
            return "Location{" +
                    "name='" + name + '\'' +
                    ", l='" + l + '\'' +
                    ", r='" + r + '\'' +
                    '}';
        }
    }

    public static List<String> readFile(String fileName) throws FileNotFoundException {
        File myObj = new File("src/main/resources/" + fileName);
        Scanner myReader = new Scanner(myObj);

        List<String> lines = new ArrayList<>();

        while (myReader.hasNextLine()) {
            String next = myReader.nextLine();
            lines.add(next);
        }

        return lines;
    }
}
