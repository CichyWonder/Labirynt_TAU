package maze.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import maze.exception.MazeException;
import maze.utils.CellType;
import maze.utils.Direction;
import maze.utils.MazeStats;

@Component
@Scope("prototype")
public class MazeCreator {
    private boolean startFound = false;
    private boolean finishFound = false;

    @Autowired
    @Qualifier("mazeStats")
    MazeStats stats;


    private List<String> readMapFile(String fileName) {
        List<String> mazeContent = new ArrayList<>();

        // read file into stream, try-with-resources
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEachOrdered(r -> {
                String row = ((String) r).toUpperCase();

                // use first line to determine overall maze width
                if (stats.getMazeRowCount() == 0) {
                    stats.setMazeColCount(row.length());
                }

                // check for consistent row length
                if (row.length() != stats.getMazeColCount()) {
                    throw new IllegalStateException(String.format("Error ln.%s, Maze width %s, exceeds limit of %s",
                            stats.getMazeRowCount(), row.length(), stats.getMazeColCount()));

                }

                // Check for validate Maze syntax
                if (!StringUtils.containsOnly(row, "XSF "))
                    throw new IllegalStateException(
                            String.format("Error ln.%s, Invalid syntax detected.", stats.getMazeRowCount()));

                // Check for 'Start'
                if (StringUtils.contains(row, "S")) {
                    if (startFound) {
                        throw new IllegalStateException(
                                String.format("Error Ln %s, Duplicate Start 'S' located.", stats.getMazeRowCount()));
                    } else {
                        startFound = true;

                    }
                }
                // Check for 'Finish'
                if (StringUtils.contains(row, "F")) {
                    if (finishFound) {
                        throw new IllegalStateException(
                                String.format("Error Ln %s, Duplicate Finish 'F' located.", stats.getMazeRowCount()));
                    } else {
                        finishFound = true;

                    }
                }

                mazeContent.add(row);
                stats.incrementMazeRowCount();
            });

            if (!startFound || !finishFound) {
                throw new IllegalStateException("Error, Maze must contain a 'S'tart and a 'F'inish.");
            }

            return mazeContent;
        } catch (IOException e) {
            throw new IllegalStateException(String.format("Error, reading Maze map file: %s", fileName));

        }
    }


    private Cell[] parseMazeMap(List<String> mazeMapRows) {
        int i = 0;

        Cell[] mazeCells = new Cell[(stats.getMazeColCount() * stats.getMazeRowCount())];
        int rowIndex = 0;

        for (String row : mazeMapRows) {
            int colIndex = 0;
            for (char c : row.toCharArray()) {

                // create a cell of the correct type (Path, wall etc..)
                Cell newCell = new Cell(CellType.valueOfSymbol(c), rowIndex, colIndex);


                connectCell(newCell, mazeCells);

                mazeCells[i++] = newCell;

                // save the Maze entry point & Update stats
                if (newCell.isStart())   stats.setStartCell(newCell);
                if (newCell.isUnknown()) stats.incrementUnknownCount();
                if (newCell.isWall())    stats.incrementWallCount();
                if (newCell.isPath())    stats.incrementPathCount();

                colIndex++;
            }

            rowIndex++;

        }

        return mazeCells;
    }


    private void connectCell(Cell newCell, Cell[] cellList) {
        // Resolve relationship to the cell north of this cell
        try {
            if (newCell.getRowIndex() >= 0) {
                Cell northCell = getCell(newCell.getColIndex(), newCell.getRowIndex() - 1, cellList);
                // exchange references bidirectionally
                // newCell.connectNorth( northCell);
                newCell.setCellInDirection(Direction.NORTH, northCell);
                // northCell.connectSouth(newCell);
                northCell.setCellInDirection(Direction.SOUTH, newCell);
            }
        } catch (MazeException ma) {
            // Do nothing - the default is Unknown.
        }

        // Resolve relationship to the cell West of this cell.
        try {
            if (newCell.getColIndex() >= 0) {
                Cell westCell = getCell(newCell.getColIndex() - 1, newCell.getRowIndex(), cellList);
                // exchange references bidirectionally
                // newCell.connectWest( westCell);
                // westCell.connectEast(newCell);
                newCell.setCellInDirection(Direction.WEST, westCell);
                westCell.setCellInDirection(Direction.EAST, newCell);
            }
        } catch (MazeException ma) {
            // Do nothing - the default is Unknown.
        }

    }


    private Cell getCell(int colNumber, int rowNumber, Cell[] cellList) {
        try {
            int computedArrayIndex = rowNumber * stats.getMazeRowCount() + colNumber;
            return cellList[computedArrayIndex];

        } catch (ArrayIndexOutOfBoundsException aiobe) {
            throw new MazeException(
                    String.format("Co-ordinate col:%s, row:%s exceeds maze bounds.", colNumber, rowNumber));
        }
    }

    public Cell[] createMaze(String fileName) {
        return parseMazeMap(readMapFile(fileName));

    }
}
