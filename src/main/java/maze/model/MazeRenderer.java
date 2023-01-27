package maze.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import maze.exception.MazeException;
import maze.utils.MazeStats;


@Component
public class MazeRenderer {
    @Autowired
    @Qualifier("mazeStats")
    private MazeStats stats;

    /**
     * Show the Maze
     */
    public String showMaze(Maze maze, boolean includeRouteTaken) {
        int visitCount = 0;

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%nThe Maze%s", includeRouteTaken ? " including users route." : "."));

        for (int r = 0; r < stats.getMazeRowCount(); r++) {

            sb.append(String.format("%n%-5d", r));
            for (int c = 0; c < stats.getMazeColCount(); c++) {
                Cell cell = maze.getCell(c, r);

                if (cell.isNavigable() && includeRouteTaken && cell.isVisited()) {
                    // show users navigation.
                    sb.append(".");
                } else {
                    // show the underlying maze
                    sb.append(cell.getCellType().getType());
                }

                // add dynamic stats
                if (cell.isVisited())
                    visitCount++;

            }

        }

        int pathCount = stats.getPathCount() + 2; // include start and finish.
        sb.append(String.format("%nMaze statistics:%nWalls:%s, Path:%s (Visited:%s, Game moves:%s)",
                stats.getWallCount(), pathCount, visitCount, stats.getGameMoves()));

        return sb.toString();

    }

    /**
     * Locate a specific Cell within the current Maze.
     * 
     * @param cellList
     * @param colNumber
     * @param rowNumber
     * @return
     */
    public Cell getCell(int colNumber, int rowNumber, Cell[] cellList) {
        try {
            int computedArrayIndex = rowNumber * stats.getMazeRowCount() + colNumber;
            return cellList[computedArrayIndex];

        } catch (ArrayIndexOutOfBoundsException aiobe) {
            throw new MazeException(
                    String.format("Co-ordinate col:%s, row:%s exceeds maze bounds.", colNumber, rowNumber));
        }
    }
}
