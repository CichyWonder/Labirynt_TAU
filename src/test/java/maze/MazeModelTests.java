package maze;



import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import maze.model.Cell;
import maze.model.Maze;
import maze.utils.CellType;
import maze.utils.Direction;
import maze.utils.MazeStats;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(locations = "test-context.xml")
public class MazeModelTests {

	@Autowired
	@Qualifier("Maze")
	Maze maze;

	@Autowired
	@Qualifier("MazeStats")
	MazeStats mazeStats;
    

	@Before
	public void setup() {
	    maze.load("Maze1.txt");
	}
	
	@Test 
	public void loadGoodMap() {
		 
		 assertThat(mazeStats.getMazeColCount(), is(15) );
		 assertThat(mazeStats.getMazeRowCount(), is(15) );


		 Cell topLeftCell = maze.getCell(0,0);
		 assertThat( topLeftCell.getCellInDirection(Direction.NORTH).isUnknown()  , is(true));
		 assertThat( topLeftCell.getCellInDirection(Direction.WEST).isUnknown()   , is(true));
		 assertThat( topLeftCell.getCellInDirection(Direction.EAST).getCellType() , is(CellType.WALL));
		 assertThat( topLeftCell.getCellInDirection(Direction.SOUTH).getCellType(), is(CellType.WALL));
         

         Cell bottomRightCell = maze.getCell(mazeStats.getMazeColCount()-1,mazeStats.getMazeRowCount()-1);
         assertThat( bottomRightCell.getCellInDirection(Direction.NORTH).getCellType(), is(CellType.WALL));
         assertThat( bottomRightCell.getCellInDirection(Direction.WEST).getCellType() , is(CellType.WALL));
         assertThat( bottomRightCell.getCellInDirection(Direction.EAST).isUnknown()   , is(true));
         assertThat( bottomRightCell.getCellInDirection(Direction.SOUTH).isUnknown()  , is(true));
         

         Cell startCell = maze.getStartCell();
         assertThat( startCell.getCellInDirection(Direction.NORTH).getCellType(), is(CellType.WALL));
         assertThat( startCell.getCellInDirection(Direction.WEST).getCellType() , is(CellType.WALL));
         assertThat( startCell.getCellInDirection(Direction.EAST).getCellType() , is(CellType.PATH));
         assertThat( startCell.getCellInDirection(Direction.SOUTH).getCellType(), is(CellType.WALL));
         
	}
	
	
}

