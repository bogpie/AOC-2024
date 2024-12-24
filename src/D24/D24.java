package D24;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class D24 {
    public void main() {
        HashMap<String, Boolean> wires = new HashMap<>();
        var listOfOutputToConnection = new ArrayList<HashMap<String, Connection>>();

        scanProblemInput(wires, listOfOutputToConnection);
        System.out.println(wires);
        System.out.println(listOfOutputToConnection);

        String bits = doPartOne(wires, listOfOutputToConnection);
        System.out.println(bits);
        Long decimal = Long.parseLong(bits, 2);
        System.out.println(decimal);

    }

    private String doPartOne(HashMap<String, Boolean> wires,
                             ArrayList<HashMap<String, Connection>> listOfOutputToConnection) {
        String startsWithString = "z";
        var bitNamesZ = wires
                .keySet()
                .stream().filter(name -> name.startsWith(startsWithString)).sorted(String::compareTo).toList();

        // Go from output to input, and memoize the value of each wire
        StringBuilder bitsBuilderZ = new StringBuilder();

        for (var bitName : bitNamesZ) {
            Character bit = getBitValue(bitName, listOfOutputToConnection, wires);
            bitsBuilderZ.insert(0, bit);
        }
        return bitsBuilderZ.toString();
    }

    private static void scanProblemInput(HashMap<String, Boolean> wires,
                                         ArrayList<HashMap<String, Connection>> listOfOutputToConnection) {
        try {
            Scanner scanner = new Scanner(new File("src/D24/input.txt"));

            // Read known wires until empty line
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.isEmpty()) {
                    break;
                }
                String[] parts = line.split(": ");

                wires.put(parts[0], parts[1].equals("1"));
            }

            // Read instructions
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" -> ");

                String input1 = parts[0].split(" ")[0];
                String gateValue = parts[0].split(" ")[1];
                String input2 = parts[0].split(" ")[2];
                String output = parts[1];

                Gate gate = new Gate(gateValue);
                Connection connection = new Connection(input1, input2, gate);

                HashMap<String, Connection> outputToConnection = new HashMap<>();
                outputToConnection.put(output, connection);

                listOfOutputToConnection.add(outputToConnection);

                wires.put(output, null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void additionalPrints(HashMap<String, Boolean> wires, ArrayList<HashMap<String, Connection>> listOfOutputToConnection, String bits) {
        StringBuilder bitsBuilderX = new StringBuilder();
        var bitNamesX = wires
                .keySet()
                .stream().filter(name -> name.startsWith("x")).sorted(String::compareTo).toList();
        for (var bitName : bitNamesX) {
            Character bit = getBitValue(bitName, listOfOutputToConnection, wires);
            bitsBuilderX.insert(0, bit);
        }
        var decimalX = Long.parseLong(bitsBuilderX.toString(), 2);

        StringBuilder bitsBuilderY = new StringBuilder();
        var bitNamesY = wires
                .keySet()
                .stream().filter(name -> name.startsWith("y")).sorted(String::compareTo).toList();
        for (var bitName : bitNamesY) {
            Character bit = getBitValue(bitName, listOfOutputToConnection, wires);
            bitsBuilderY.insert(0, bit);
        }
        var decimalY = Long.parseLong(bitsBuilderY.toString(), 2);
        long actualSum = decimalX + decimalY;
        var actualSumBits = Long.toBinaryString(actualSum);

        System.out.println("Circuit sum bits:  " + bits);
        System.out.println("Actual sum bits:   " + actualSumBits);
    }

    private Character getBitValue(
            String bitName,
            ArrayList<HashMap<String, Connection>> listOfOutputToConnection,
            HashMap<String, Boolean> wires
    ) {
        if (wires.get(bitName) != null) {
            return wires.get(bitName) ? '1' : '0';
        }

        var connection = listOfOutputToConnection
                .stream()
                .filter(outputToConnection -> outputToConnection.containsKey(bitName))
                .map(outputToConnection -> outputToConnection.get(bitName))
                .findFirst()
                .orElseThrow();

        var inputValue1 = getBitValue(connection.input1(), listOfOutputToConnection, wires);
        var inputValue2 = getBitValue(connection.input2(), listOfOutputToConnection, wires);
        var outputValue = connection.gate().getOutput(inputValue1 == '1', inputValue2 == '1');
        wires.put(bitName, outputValue);
        return outputValue ? '1' : '0';
    }
}
