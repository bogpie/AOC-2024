package D15;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static D15.Type.*;


public class D15 {

    public void main() {
        // Part 1
        ArrayList<String> mapLines = new ArrayList<>();
        char[][] area;
        char[][] areaBackup;
        StringBuilder movesBuilder = new StringBuilder();
        String moves = "";

        moves = getMoves(mapLines, movesBuilder, moves);

        int idLine = 0;
        area = new char[mapLines.size()][mapLines.get(0).length()];
        areaBackup = new char[mapLines.size()][mapLines.get(0).length()];
        for (var line : mapLines) {
            area[idLine] = line.toCharArray();
            areaBackup[idLine] = line.toCharArray();
            idLine++;
        }
        doPartOne(area, moves);


        // Part 2
        char[][] area2 = getPartTwoArea(area, areaBackup);
        printArea(area2);

        var bigBoxes = new ArrayList<BigBox>();
        var walls = new ArrayList<Point>();
        Robot robot = new Robot();
        getDataPartTwo(robot, area2, bigBoxes, walls);
        for (var move : moves.toCharArray()) {
//            System.out.println(move);
            attemptMove2(robot, bigBoxes, walls, robot, move);
//            printArea2(area2, bigBoxes, walls, robot);
        }


        Long sum = 0L;
        for (var bigBox : bigBoxes) {
            sum += 100L * bigBox.getLeft().x + bigBox.getLeft().y;
        }

        System.out.println(sum);
    }

    private static void printArea2(char[][] area2, ArrayList<BigBox> bigBoxes, ArrayList<Point> walls, Robot robot) {
        int idLine;
        for (idLine = 0; idLine < area2.length; idLine++) {
            for (int idCol = 0; idCol < area2[idLine].length; idCol++) {
                int finalIdLine = idLine;
                int finalIdCol = idCol;
                if (bigBoxes.stream().anyMatch(box -> box.getLeft().equals(new Point(finalIdLine, finalIdCol)))) {
                    System.out.print(BIG_BOX_LEFT.getValue());
                } else if (bigBoxes.stream().anyMatch(box -> box.getRight().equals(new Point(finalIdLine, finalIdCol)))) {
                    System.out.print(BIG_BOX_RIGHT.getValue());
                } else if (robot.getPoint().equals(new Point(finalIdLine, finalIdCol))) {
                    System.out.print(ROBOT.getValue());
                } else if (walls.contains(new Point(finalIdLine, finalIdCol))) {
                    System.out.print(WALL.getValue());
                } else {
                    System.out.print(EMPTY.getValue());
                }
            }
            System.out.println();
        }
        System.out.println("\n");
    }

    private static void getDataPartTwo(Robot robot, char[][] area2, ArrayList<BigBox> bigBoxes,
                                       ArrayList<Point> walls) {
        int idLine;
        for (idLine = 0; idLine < area2.length; idLine++) {
            for (int idCol = 0; idCol < area2[idLine].length; idCol++) {
                if (area2[idLine][idCol] == BIG_BOX_LEFT.getValue()) {
                    BigBox bigBox = new BigBox(
                            new Point(idLine, idCol),
                            new Point(idLine, idCol + 1)
                    );
                    bigBoxes.add(bigBox);
                }
                if (area2[idLine][idCol] == BIG_BOX_RIGHT.getValue()) {
                    continue;
                }
                if (area2[idLine][idCol] == WALL.getValue()) {
                    Point wall = new Point(
                            idLine,
                            idCol
                    );
                    walls.add(wall);
                }
                if (area2[idLine][idCol] == ROBOT.getValue()) {
                    robot.setPoint(new Point(idLine, idCol));
                }
            }
        }
    }

    private static char[][] getPartTwoArea(char[][] area, char[][] areaBackup) {
        int idLine;
        var area2 = new char[area.length][area[0].length * 2];
        area = areaBackup;
        for (idLine = 0; idLine < area.length; idLine++) {
            for (int idCol = 0; idCol < area[idLine].length; idCol++) {
                if (area[idLine][idCol] == BOX.getValue()) {
                    area2[idLine][idCol * 2] = BIG_BOX_LEFT.getValue();
                    area2[idLine][idCol * 2 + 1] = BIG_BOX_RIGHT.getValue();
                    continue;
                }
                if (area[idLine][idCol] == ROBOT.getValue()) {
                    area2[idLine][idCol * 2] = ROBOT.getValue();
                    area2[idLine][idCol * 2 + 1] = EMPTY.getValue();
                    continue;
                }
                if (area[idLine][idCol] == WALL.getValue()) {
                    area2[idLine][idCol * 2] = WALL.getValue();
                    area2[idLine][idCol * 2 + 1] = WALL.getValue();
                    continue;
                }
                if (area[idLine][idCol] == EMPTY.getValue()) {
                    area2[idLine][idCol * 2] = EMPTY.getValue();
                    area2[idLine][idCol * 2 + 1] = EMPTY.getValue();
                }
            }
        }
        return area2;
    }

    private Point doPartOne(char[][] area, String moves) {
        Point robot = getDataPartTwo(area);
        System.out.println(robot);

        for (var move : moves.toCharArray()) {
            attemptMove(area, robot, move, true);
        }

        Long sum = 0L;
        sum = getFirstPartSum(area, sum);
        System.out.println(sum);
        return robot;
    }

    private boolean attemptMove2(
            Moveable moveable,
            ArrayList<BigBox> bigBoxes,
            ArrayList<Point> walls,
            Robot robot,
            char move
    ) {
        Moveable possibleMove;

        // If we're moving a robot
        if (moveable instanceof Robot) {
            Robot possibleMoveRobot = (Robot) getPossibleMove2(moveable, move);

            // If we hit a wall, stay in place
            if (walls.contains(possibleMoveRobot.getPoint())) {
                return false;
            }

            // Look for a big box in the way
            BigBox bigBox =
                    bigBoxes.stream()
                            .filter(box -> box.getLeft().equals(possibleMoveRobot.getPoint())
                                    || box.getRight().equals(possibleMoveRobot.getPoint()))
                            .findFirst()
                            .orElse(null);

            // If we hit a big box, move the big box
            if (bigBox != null) {
                boolean hasMoved = attemptMove2(bigBox, bigBoxes, walls, robot, move);
                if (!hasMoved) {
                    return false;
                }
            }

            // Move the robot
            robot.setPoint(possibleMoveRobot.getPoint());
        } else if (moveable instanceof BigBox bigBox) {
            // If we're moving a big box
            possibleMove = getPossibleMove2(bigBox, move);
            var possibleMoveBigBox = (BigBox) possibleMove;

            // If we hit any wall, stay in place
            if (walls.contains(possibleMoveBigBox.getLeft())
                    || walls.contains(possibleMoveBigBox.getRight())) {
                return false;
            }

            // If we hit big boxes, attempt to move them
            List<BigBox> bigBoxesInWay =
                    bigBoxes.stream()
                            .filter(
                                    target -> target != bigBox &&
                                            (target.getLeft().equals(possibleMoveBigBox.getLeft())
                                                    || target.getRight().equals(possibleMoveBigBox.getRight())
                                                    || target.getLeft().equals(possibleMoveBigBox.getRight())
                                                    || target.getRight().equals(possibleMoveBigBox.getLeft()))
                            ).toList();


            ArrayList<Boolean> hasMovedArray = new ArrayList<>();

            // Backup the big boxes
            List<BigBox> bigBoxesCopy = bigBoxes.stream()
                    .map(box -> new BigBox(new Point(box.getLeft()), new Point(box.getRight())))
                    .toList();

            for (BigBox bigBoxInWay : bigBoxesInWay) {
                boolean hasMoved = attemptMove2(bigBoxInWay, bigBoxes, walls, robot, move);

                if (!hasMoved) {
                    hasMovedArray.add(false);
                    break;
                }
            }

            // Revert big box changes if we couldn't move the big box in the way
            if (hasMovedArray.contains(false)) {
                bigBoxes.clear();
                for (BigBox box : bigBoxesCopy) {
                    bigBoxes.add(new BigBox(box.getLeft(), box.getRight()));
                }
                return false;
            }

            // We can move the big box (either it's empty or we've moved the big box in the way)
            bigBox.setLeft(possibleMoveBigBox.getLeft());
            bigBox.setRight(possibleMoveBigBox.getRight());
            return true;
        }

        return true;
    }

    private static Long getFirstPartSum(char[][] area, Long sum) {
        int idLine;
        for (idLine = 0; idLine < area.length; idLine++) {
            for (int idChar = 0; idChar < area[idLine].length; idChar++) {
                if (area[idLine][idChar] == BOX.getValue()) {
                    sum += 100L * idLine + idChar;
                }
            }
        }
        return sum;
    }

    private static Point getDataPartTwo(char[][] area) {
        Point robot = new Point();
        int idLine;
        for (idLine = 0; idLine < area.length; idLine++) {
            for (int idChar = 0; idChar < area[idLine].length; idChar++) {
                if (area[idLine][idChar] == ROBOT.getValue()) {
                    robot = new Point(idLine, idChar);
                }
            }
        }
        return robot;
    }

    private static String getMoves(ArrayList<String> mapLines, StringBuilder movesBuilder, String moves) {
        try {
            File data = new File("src/D15/input.txt");
            Scanner scanner = new Scanner(data);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.equals("")) {
                    break;
                }
                mapLines.add(line);
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
        return moves;
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
        Point possibleMove;

        possibleMove = getPossibleMove(object, move);

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

    private static Point getPossibleMove(Point object, char move) {
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
        return possibleMove;
    }

    private static Moveable getPossibleMove2(Moveable moveable, char move) {
        if (moveable instanceof Robot) {
            Point point = getPossibleMove(((Robot) moveable).getPoint(), move);
            return new Robot(point);
        } else {
            BigBox bigBox = (BigBox) moveable;
            Point left = getPossibleMove(bigBox.getLeft(), move);
            Point right = getPossibleMove(bigBox.getRight(), move);
            return new BigBox(left, right);
        }

    }

}
