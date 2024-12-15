package D15;

import misc.Misc;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

import static D15.Type.*;


public class D15 {

    public void main() {

        ArrayList<String> mapLines = new ArrayList<>();
        char[][] area = new char[0][0];
        StringBuilder movesBuilder = new StringBuilder();
        String moves = "";

        Point robot = new Point(0, 0);

        try {
            File data = new File("src/D15/input.txt");
            Scanner scanner = new Scanner(data);
            int idLine = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.equals("")) {
                    break;
                }
                mapLines.add(line);
            }

            area = new char[mapLines.size()][mapLines.get(0).length()];
            for (var line : mapLines) {
                area[idLine] = line.toCharArray();
                idLine++;
            }

            while (scanner.hasNextLine()) {
                movesBuilder.append(scanner.nextLine());
            }
            moves = movesBuilder.toString();
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        }


        for (int idLine = 0; idLine < area.length; idLine++) {
            for (int idChar = 0; idChar < area[idLine].length; idChar++) {
                if (area[idLine][idChar] == ROBOT.getValue()) {
                    robot = new Point(idLine, idChar);
                }
            }
        }

        System.out.println(robot);

        for (var move : moves.toCharArray()) {
//            System.out.println(move);
            attemptMove(area, robot, move, true);
//            printArea(area);
        }


        Long sum = 0L;
        for (int idLine = 0; idLine < area.length; idLine++) {
            for (int idChar = 0; idChar < area[idLine].length; idChar++) {
                if (area[idLine][idChar] == BOX.getValue()) {
                    sum += 100L * idLine + idChar;
                }
            }
        }

        System.out.println(sum);
    }

    private static void printArea(char[][] area) {
        for (var row : area) {
            for (var cell : row) {
                System.out.print(cell);
            }
            System.out.println();
        }
        System.out.println("\n");
    }

    private boolean attemptMove(char[][] area, Point object, char move, boolean isRobot) {
        Point possibleMove = new Point();

        if (move == NORTH.getValue()) {
            possibleMove = new Point(object.x - 1, object.y);
        } else if (move == Type.EAST.getValue()) {
            possibleMove = new Point(object.x, object.y + 1);
        } else if (move == Type.SOUTH.getValue()) {
            possibleMove = new Point(object.x + 1, object.y);
        } else if (move == Type.WEST.getValue()) {
            possibleMove = new Point(object.x, object.y - 1);
        }

        char possibleMoveChar = area[possibleMove.x][possibleMove.y];
        if (possibleMoveChar == WALL.getValue()) {
            return false;
        }
        if (possibleMoveChar == EMPTY.getValue()) {
            area[object.x][object.y] = EMPTY.getValue();
            area[possibleMove.x][possibleMove.y] = isRobot ? ROBOT.getValue() : BOX.getValue();
            object.setLocation(possibleMove);
            return true;
        }

        if (possibleMoveChar == BOX.getValue()) {
            var boxPossibleMove = new Point(possibleMove.x, possibleMove.y);
            var objectPossibleMove = new Point(possibleMove.x, possibleMove.y);

            // Move box
            boolean hasMoved = attemptMove(area, boxPossibleMove, move, false);

            // Move object
            if (hasMoved) {
                area[object.x][object.y] = EMPTY.getValue();
                area[objectPossibleMove.x][objectPossibleMove.y] = isRobot ? ROBOT.getValue() : BOX.getValue();
                object.setLocation(possibleMove);
                return true;
            }
        }

        return false;
    }

}
