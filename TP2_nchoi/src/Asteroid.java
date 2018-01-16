import objectdraw.*;
import java.awt.*;

//Name: Nicole Choi
//Comments:
//Extra Credit:
// - Changed image of Asteroid to Stag
// - Customized left/right Stag image depending on direction of movement

/**
 * An Asteroid is an ActiveObject that appears as a Stag. The Stags move at
 * unique, randomized speeds and directions. The Stags die if they come into
 * contact with a sageshot and end the game if they touch Cecil.
 */
public class Asteroid extends ActiveObject {
	// Constants
	// Random generators
	private static final int SMALLSIZE = 20; 
	private static final int BIGSIZE = 50; 
	private static final int SLOWPOS = 1; 
	private static final int FASTPOS = 3; 
	private static final int SLOWNEG = -1; 
	private static final int FASTNEG = -3; 
	
	// Screen
	private static final int CANVAS_EDGE1 = 0;
	private static final int CANVAS_EDGE2 = 400;
	private static final int HALF_CANVAS = 200;
	
	// Movement
	private static final int PAUSE = 100;

	// Instance Variables
	// The Stag
	private VisibleImage stag;
	private Image stagPicLeft;
	private Image stagPicRight;

	// Stag characteristics
	private int speedX;
	private int speedY;
	private Boolean stagLife;
	private int xLoc;
	private int yLoc;
	private int size;
	private int randomX; // decides starting xLoc

	// Other classes passed in as parameters
	private SpaceShip cecil;
	private ScoreKeeper scorekeeper;
	Asteroid[] theArray;

	// Generate stag speed, size and starting location
	private RandomIntGenerator stagSizer;
	private RandomIntGenerator stagSpeederPos;
	private RandomIntGenerator stagSpeederNeg;
	private RandomIntGenerator stagSpeederBoth;
	private RandomIntGenerator stagX;
	private RandomIntGenerator stagY;

	/**
	 * Creates a randomly sized Stag at a random off-screen location.
	 * 
	 * @param array ... array of Stags
	 * @param sageHen ... Cecil figure
	 * @param scoreKeep ... the scorekeeper
	 * @param stagLeft ... a left traveling Stag
	 * @param stagRight ... a right traveling Stag
	 * @param canvas ... Drawing Canvas
	 */
	public Asteroid(Asteroid[] array, SpaceShip sageHen, ScoreKeeper scoreKeep, Image stagLeft, Image stagRight,
			DrawingCanvas canvas) {
		// Assign variables to parameters
		theArray = array;
		cecil = sageHen;
		scorekeeper = scoreKeep;
		stagPicLeft = stagLeft;
		stagPicRight = stagRight;
		
		// Set up random generators for size, speed and starting location
		stagSizer = new RandomIntGenerator(SMALLSIZE, BIGSIZE);
		stagSpeederPos = new RandomIntGenerator(SLOWPOS, FASTPOS);
		stagSpeederNeg = new RandomIntGenerator(FASTNEG, SLOWNEG);
		stagSpeederBoth = new RandomIntGenerator(FASTNEG, FASTPOS);
		stagX = new RandomIntGenerator(0, 1);
		stagY = new RandomIntGenerator(CANVAS_EDGE1, CANVAS_EDGE2);
		
		// Randomizes Stag characteristics: size, speed, starting location
		size = stagSizer.nextValue();

		// Randomizes Stag starting location at right or left edge of screen and
		// sets speed so that Stag moves onto screen accordingly
		randomX = stagX.nextValue();
		if (randomX == 0) {
			xLoc = 0 - size;
			speedX = stagSpeederPos.nextValue();
			speedY = stagSpeederBoth.nextValue();
			// Makes sure Stag doesn't freeze off-screen
			while (speedY == 0) {
				speedY = stagSpeederBoth.nextValue();
			}

		}
		if (randomX == 1) {
			xLoc = CANVAS_EDGE2;
			speedX = stagSpeederNeg.nextValue();
			speedY = stagSpeederBoth.nextValue();
			// Makes sure Stag doesn't freeze off-screen
			while (speedY == 0) {
				speedY = stagSpeederBoth.nextValue();
			}
		}
		yLoc = stagY.nextValue();

		// Creates Stag
		stag = new VisibleImage(stagPicLeft, xLoc, yLoc, canvas);
		stag.setSize(size, size);
		stagLife = true;

		// Depending on Stag location, sets correct image
		if (stag.getX() < HALF_CANVAS) {
			stag.setImage(stagPicRight);
		} else {
			stag.setImage(stagPicLeft);
		}

		start();
	}

	/**
	 * Determines if Cecil has hit a Stag or not.
	 * 
	 * @param cecil
	 *            ... Cecil
	 * @return true if Cecil image touches a Stag.
	 */
	public boolean overlapShip(SpaceShip cecil) {
		return cecil.overlap(stag);
	}

	/**
	 * Determines if a sageshot has hit a Stag or not.
	 * 
	 * @param sageshot
	 *            ... Sageshot
	 * @return true if sageshot "47" touches a Stag.
	 */
	public boolean overlapBullet(Text sageshot) {
		return stag.overlaps(sageshot);
	}

	/**
	 * Freezes Stag and removes it from the canvas.
	 */
	public void destroy() {
		stagLife = false;
		stag.removeFromCanvas();
	}

	/**
	 * run ... active object main loop
	 * 
	 * Moves the Stag across the screen unless it is hit by a sageshot or goes
	 * off-screen. If hit by a sageshot, the Stag is removed. If it goes
	 * off-screen, the Stag wraps around the screen to reappear.
	 */
	public void run() {
		while (stagLife) {
			// If Stag hits Cecil, ends game
			if (overlapShip(cecil)) {
				cecil.stopShoot();
				scorekeeper.gameOver();
				cecil.kill();
			}
			// Checks if Stag ever goes off-screen and moves Stag
			// to opposite edge of canvas if so.
			if (stag.getX() < CANVAS_EDGE1 - stag.getWidth()) {
				stag.moveTo(CANVAS_EDGE2 + stag.getWidth(), stag.getY());

			}
			if (stag.getX() > CANVAS_EDGE2) {
				stag.moveTo(CANVAS_EDGE1 - stag.getWidth(), stag.getY());

			}
			if (stag.getY() < CANVAS_EDGE1 - stag.getHeight()) {
				stag.moveTo(stag.getX(), CANVAS_EDGE2);

			}
			if (stag.getY() > CANVAS_EDGE2) {
				stag.moveTo(stag.getX(), CANVAS_EDGE1);

			}
			// Moves stag in random direction.
			stag.move(speedX, speedY);
			pause(PAUSE);
		}
	}

}
