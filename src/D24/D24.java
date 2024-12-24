package D24;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class D24 {
    public void main() {
        HashMap<String, Boolean> wires = new HashMap<>();
        var outputToConnection = new HashMap<String, Connection>();

        scanProblemInput(wires, outputToConnection);
        System.out.println(wires);
        System.out.println(outputToConnection);

        ArrayList<String> bitNamesZ = new ArrayList<>();
        String bits = doPartOne(wires, outputToConnection, bitNamesZ);
        System.out.println(bits);
        Long decimal = Long.parseLong(bits, 2);
        System.out.println(decimal);

        // Find faulty gates for ripple carry adder
        System.out.println();
        ArrayList<String> faultyOutputs = new ArrayList<>();

        // 1. If the output of a gate is z, then the operation has to be XOR unless it is the last bit.
        ArrayList<String> faultyOutputs1 = new ArrayList<>();
        findFaultyOutputsRule1(outputToConnection, bitNamesZ, faultyOutputs1);

        // 2. If the output of a gate is NOT Z and the inputs are NOT x, y, then it has to be AND / OR, but not XOR
        ArrayList<String> faultyOutputs2 = new ArrayList<>();
        findFaultyOutputsRule2(outputToConnection, faultyOutputs2);

        // 3. If you have a XOR gate with inputs x, y, there must be another XOR gate with this gate as an input.
        ArrayList<String> faultyOutputs3 = new ArrayList<>();
        findFaultyOutputsRule3(outputToConnection, faultyOutputs3);


        faultyOutputs.addAll(faultyOutputs1);
        faultyOutputs.addAll(faultyOutputs2);

        var distinctFaultyOutputs = faultyOutputs.stream().distinct().toList();
        var sortedFaultyOutputs = distinctFaultyOutputs.stream().sorted().toList();

        System.out.println(sortedFaultyOutputs);

        System.out.println(sortedFaultyOutputs.toString()
                .replace("[", "")
                .replace("]", "")
                .replace(", ", ","));

    }

    private void findFaultyOutputsRule3(
            HashMap<String, Connection> outputToConnection,
            ArrayList<String> faultyOutputs3) {
        var connectionsForXORWithXY = outputToConnection
                .entrySet().stream()
                .filter(entry -> entry.getValue().gate().value().equals("XOR"))
                .filter(entry -> entry.getValue().input1().startsWith("x") && entry.getValue().input2().startsWith("y"));
        System.out.println(connectionsForXORWithXY);
    }

    private static void findFaultyOutputsRule2(
            HashMap<String, Connection> outputToConnection,
            ArrayList<String> faultyOutputs2
    ) {
        var connectionsForOutputNotZAndInputsNotXY = outputToConnection
                .entrySet()
                .stream()
                .filter(entry -> !entry.getKey().startsWith("z"))
                .filter(
                        entry -> !(
                                entry.getValue().input1().startsWith("x") && entry.getValue().input2().startsWith("y") ||
                                        entry.getValue().input1().startsWith("y") && entry.getValue().input2().startsWith("x")
                        )
                )
                .toList();

        var faultyGatesRule2 = connectionsForOutputNotZAndInputsNotXY
                .stream()
                .filter(entry -> entry.getValue().gate().value().equals("XOR"))
                .toList();

        faultyOutputs2.addAll(faultyGatesRule2.stream().map(Map.Entry::getKey).toList());
    }

    private static void findFaultyOutputsRule1(
            HashMap<String, Connection> outputToConnection,
            ArrayList<String> bitNamesZ, ArrayList<String> faultyOutputs1
    ) {
        var lastBitName = bitNamesZ.get(bitNamesZ.size() - 1);
        var connectionsForZ = outputToConnection
                .entrySet()
                .stream()
                .filter(entry -> bitNamesZ.contains(entry.getKey()))
                .toList();

        var faultyGatesRule1 = connectionsForZ
                .stream()
                .filter(
                        entry -> !entry.getKey().equals(lastBitName)
                )
                .filter(
                        entry -> entry.getValue().gate().value().equals("XOR")
                )
                .toList();

        faultyOutputs1.addAll(faultyGatesRule1.stream().map(Map.Entry::getKey).toList());
    }

    private String doPartOne(HashMap<String, Boolean> wires,
                             HashMap<String, Connection> outputToConnection,
                             ArrayList<String> bitNamesZ) {
        String startsWithString = "z";
        bitNamesZ.addAll(
                wires.keySet().stream()
                        .filter(name -> name.startsWith(startsWithString))
                        .sorted(String::compareTo)
                        .toList()
        );

        // Go from output to input, and memoize the value of each wire
        StringBuilder bitsBuilderZ = new StringBuilder();

        for (var bitName : bitNamesZ) {
            Character bit = getBitValue(bitName, outputToConnection, wires);
            bitsBuilderZ.insert(0, bit);
        }
        return bitsBuilderZ.toString();
    }

    private static void scanProblemInput(HashMap<String, Boolean> wires,
                                         HashMap<String, Connection> outputToConnection) {
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

                outputToConnection.put(output, connection);
                wires.put(output, null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void additionalPrints(HashMap<String, Boolean> wires, HashMap<String, Connection> outputToConnection, String bits) {
        StringBuilder bitsBuilderX = new StringBuilder();
        var bitNamesX = wires
                .keySet()
                .stream().filter(name -> name.startsWith("x")).sorted(String::compareTo).toList();
        for (var bitName : bitNamesX) {
            Character bit = getBitValue(bitName, outputToConnection, wires);
            bitsBuilderX.insert(0, bit);
        }
        var decimalX = Long.parseLong(bitsBuilderX.toString(), 2);

        StringBuilder bitsBuilderY = new StringBuilder();
        var bitNamesY = wires
                .keySet()
                .stream().filter(name -> name.startsWith("y")).sorted(String::compareTo).toList();
        for (var bitName : bitNamesY) {
            Character bit = getBitValue(bitName, outputToConnection, wires);
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
            HashMap<String, Connection> outputToConnection,
            HashMap<String, Boolean> wires
    ) {
        if (wires.get(bitName) != null) {
            return wires.get(bitName) ? '1' : '0';
        }

        var connection = outputToConnection.get(bitName);
        var inputValue1 = getBitValue(connection.input1(), outputToConnection, wires);
        var inputValue2 = getBitValue(connection.input2(), outputToConnection, wires);
        var outputValue = connection.gate().getOutput(inputValue1 == '1', inputValue2 == '1');
        wires.put(bitName, outputValue);
        return outputValue ? '1' : '0';
    }
}
