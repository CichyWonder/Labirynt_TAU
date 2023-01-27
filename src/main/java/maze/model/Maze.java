package maze.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import maze.exception.MazeException;
import maze.utils.Compass;
import maze.utils.Direction;
import maze.utils.MazeStats;


@Component
@Configuration
@ComponentScan("maze.utils")
@ComponentScan("maze.model")
@Scope("prototype")
public class Maze {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Compass compass;
    @Qualifier("mazeStats")
    @Autowired
    private MazeStats mazeStats;

    @Autowired
    private MazeCreator mazeCreator;

    // the Maze cell data
    private Cell[] mazeCells;

    private Cell currentLocation;

    public void load(String fileName) {
        initMap();

        mazeCells = mazeCreator.createMaze(fileName);
        
        compass.init(Direction.NORTH);

        currentLocation = mazeStats.getStartCell();
        currentLocation.visited();
        logger.info(String.format("Loaded map :%s",fileName));
    }

    public Cell getStartCell() {
        return mazeStats.getStartCell();
    }

    private void initMap() {
        mazeStats.init();
        this.mazeCells = null;

    }

    public Cell getCell(int colNumber, int rowNumber) {
        try {
            return mazeCells[(rowNumber * mazeStats.getMazeRowCount() + colNumber)];
        } catch (ArrayIndexOutOfBoundsException aiobe) {
            throw new MazeException(
                    String.format("Co-ordinate col:%s, row:%s exceeds maze bounds.", colNumber, rowNumber));
        }
    }

    public Cell getCurrentLocation() {
        return currentLocation;
    }

    public String directionAhead() {
        return compass.getDirectionAhead().name();
    }

    /**
     * change the direction we are facing.
     * 
     */
    public void turnLeft() {
        compass.turnLeft();
        mazeStats.incrementUserTurnCount();

    }

    /**
     * change the direction we are facing.
     * 
     */
    public void turnRight() {
        compass.turnRight();
        mazeStats.incrementUserTurnCount();
    }

    /**
     * change our location with the maze.
     * 
     */
    public void moveForward() {
        currentLocation = currentLocation.getCellInDirection(compass.getDirectionAhead());
        currentLocation.visited();
        mazeStats.incrementUserTurnCount();

    }

    /**
     * Can we move forwards ?
     * 
     * @return boolean
     */
    public boolean isForwardAvailable() {
        Cell forwardCell = currentLocation.getCellInDirection(compass.getDirectionAhead());
        return !(forwardCell.isWall() || forwardCell.isUnknown());
    }

    public boolean isFinish() {
        return currentLocation.isFinish();
    }

}
