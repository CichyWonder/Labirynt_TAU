package maze;



import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import maze.utils.CompassAlternative;
import maze.utils.Direction;

@RunWith(SpringRunner.class)
@SpringBootTest()
@ContextConfiguration(locations = "test-context.xml")
public class MazeApplicationTests {

	@Autowired
	@Qualifier("CompassAlternative")
	CompassAlternative compass;
	

	@Test
	public void compassTestTurnLeft() {

		 assertThat(compass.getDirectionAhead(), is(Direction.NORTH) );
		 assertThat(compass.getDirectionLeft(), is(Direction.WEST) );
		 assertThat(compass.getDirectionRight(), is(Direction.EAST)  );
		 
		 compass.turnLeft();
		 assertThat(compass.getDirectionAhead(), is(Direction.WEST) );
		 assertThat(compass.getDirectionLeft(), is(Direction.SOUTH) );
		 assertThat(compass.getDirectionRight(), is(Direction.NORTH)  );
		
		 compass.turnLeft();
		 assertThat(compass.getDirectionAhead(), is(Direction.SOUTH) );
		 assertThat(compass.getDirectionLeft(), is(Direction.EAST) );
		 assertThat(compass.getDirectionRight(), is(Direction.WEST)  );
		 
		 compass.turnLeft();
		 assertThat(compass.getDirectionAhead(), is(Direction.EAST) );
		 assertThat(compass.getDirectionLeft(), is(Direction.NORTH) );
		 assertThat(compass.getDirectionRight(), is(Direction.SOUTH)  );
		 
		 compass.turnLeft();
		 assertThat(compass.getDirectionAhead(), is(Direction.NORTH) );
		 assertThat(compass.getDirectionLeft(), is(Direction.WEST) );
		 assertThat(compass.getDirectionRight(), is(Direction.EAST)  );
		 
	}


	@Test
	public void compassTestTurnRight() {
		 assertThat(compass.getDirectionAhead(), is(Direction.NORTH) );
		 assertThat(compass.getDirectionLeft(), is(Direction.WEST) );
		 assertThat(compass.getDirectionRight(), is(Direction.EAST)  );
		 
		 compass.turnRight();
		 assertThat(compass.getDirectionAhead(), is(Direction.EAST) );
		 assertThat(compass.getDirectionLeft(), is(Direction.NORTH) );
		 assertThat(compass.getDirectionRight(), is(Direction.SOUTH)  );
		 
		 compass.turnRight();
		 assertThat(compass.getDirectionAhead(), is(Direction.SOUTH) );
		 assertThat(compass.getDirectionLeft(), is(Direction.EAST) );
		 assertThat(compass.getDirectionRight(), is(Direction.WEST)  );
		 
		 compass.turnRight();
		 assertThat(compass.getDirectionAhead(), is(Direction.WEST) );
		 assertThat(compass.getDirectionLeft(), is(Direction.SOUTH) );
		 assertThat(compass.getDirectionRight(), is(Direction.NORTH)  );
		 
		 compass.turnRight();
		 assertThat(compass.getDirectionAhead(), is(Direction.NORTH) );
		 assertThat(compass.getDirectionLeft(), is(Direction.WEST) );
		 assertThat(compass.getDirectionRight(), is(Direction.EAST)  );
		 
	}
}
