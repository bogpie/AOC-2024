package D17;

import D16.*;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.*;

import static D16.Direction.*;
import static D16.Type.*;

public class D17 {
    public void main() {
        ArrayList<Long> registers = new ArrayList<>(Collections.nCopies(3, 0L));
        ArrayList<Long> program = new ArrayList<>();
        List<String> splitOutput = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(new File("src/D17/input.txt"));

            // Read the three registers, which are in format Register X: 0
            for (int i = 0; i < 3; i++) {
                String line = scanner.nextLine();
                registers.set(i, Long.parseLong(line.split(" ")[2]));
            }

            // Skip the empty line
            scanner.nextLine();

            // Read the program, in the format Program: 0,1,2,...
            String valuesString = scanner.nextLine().split(" ")[1];

            program = new ArrayList<>(Arrays.stream(valuesString.split(",")).map(Long::parseLong).toList());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println(registers);
        System.out.println(program);

        long pointer = 0;

        while (pointer < program.size()) {
            long instructionCode = program.get((int) pointer);
            long literal = program.get((int) pointer + 1);

            Instruction instruction = Instruction.fromValue(instructionCode);

            long combo;
            if (literal <= 3) {
                combo = literal;
            } else if (literal == 4) {
                combo = registers.get(0);
            } else if (literal == 5) {
                combo = registers.get(1);
            } else if (literal == 6) {
                combo = registers.get(2);
            } else {
                throw new IllegalArgumentException("Reserved argument code");
            }

            switch (instruction) {
                // Division, REG_A / 2 ^ combo -> REG_A
                case ADV -> {
                    long result = registers.get(0) / (1L << combo);
                    registers.set(0, result);
                    pointer += 2;
                }

                // Bitwise XOR, REG_B ^ literal -> REG_B
                case BXL -> {
                    registers.set(
                            1,
                            registers.get(1) ^ literal
                    );
                    pointer += 2;
                }

                //  Modulo 8, COMBO % 8 -> REG_B
                case BST -> {
                    registers.set(1, combo % 8);
                    pointer += 2;
                }

                // Jump if REG_A not zero to literal
                case JNZ -> {
                    if (registers.get(0) != 0) {
                        pointer = literal;
                    } else {
                        pointer += 2;
                    }
                }

                // XOR, REG_B ^ REG_C -> REG_B
                case BXC -> {
                    registers.set(
                            1,
                            registers.get(1) ^ registers.get(2)
                    );
                    pointer += 2;
                }

                // Output, combo MOD 8 -> OUTPUT
                case OUT -> {
                    splitOutput.add(String.valueOf(combo % 8));
                    pointer += 2;
                }

                // Division B, REG_A / 2 ^ combo -> REG_B
                case BDV -> {
                    long result = registers.get(0) / (1L << combo);
                    registers.set(1, result);

                    pointer += 2;
                }

                // Division C, REG_A / 2 ^ combo -> REG_C
                case CDV -> {
                    long result = registers.get(0) / (1L << combo);
                    registers.set(2, result);

                    pointer += 2;
                }
            }
            System.out.println("Registers: " + registers);
            System.out.println("Pointer: " + pointer);

        }
        System.out.println(splitOutput);
        System.out.println(String.join(",", splitOutput));
        System.out.println(String.join("", splitOutput));
    }
}