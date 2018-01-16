import objectdraw.*;

import java.applet.AudioClip;
import java.awt.*;

//Name: Nicole Choi
//Comments:
//Extra Credit:
// - Changed SpaceShip object into Cecil image
// - Changed gun to a wand in Cecil's hand
// - Cecil freely moves in the direction wand is pointing
// - Cecil and wand wrap around if they go off-screen
// - Cecil's death is animated
// - Added sound effect to when Cecil dies

/**
 * A SpaceShip is an ActiveObject that appears as Cecil with a wand. The wand
 * rotates and Cecil can move in the direction of the wand. Cecil dies if it
 * comes into contact with a Stag or runs out of "47" sageshots.
 */
public class SpaceShip extends ActiveObject {
	// Constants
	// For Cecil and wand
	private static final int CECILWIDTH = 60;
	private static final int CECILHEIGHT = 90;
	private static final double ANGLE = Math.PI / 12;
	private static final int WANDLENGTH = 15;
	private static final int WANDX_OFFSET = 50;
	private static final int WANDY_OFFSET = 10; 
	private static final int CECIL_DESIZE = 2;
	
	// Screen
	private static final int CANVASEDGE1 = 0;
	private static final int CANVASEDGE2 = 400;
	private static final Location CANVASMID = new Location(200, 200);
	
	// Cecil movement
	private static final int SPEEDMULTIPLIER = 5;
	private static final int PAUSE = 600;

	// Instance variables
	// Cecil components
	private VisibleImage cecilSage;
	private Image sagehen;
	private Line cecilWand;

	// Refers to Cecil
	private Location cecilCenter; // location of Cecil's center
	private double theta; // angle between wand starting position and
								// current position
	private boolean life; // keeps track of whether or not Cecil has been hit
	private boolean move; // keeps track of whether or not Cecil can move
	private boolean wandLife; // keeps track of whether or not wand can shoot

	private AudioClip lose; // plays when Cecil dies

	/**
	 * Create a Cecil with a wand in the center of the game.
	 * 
	 * @param lost ... the noise played when Cecil dies
	 * @param cecil ... the image for Cecil
	 * @param canvas ... Drawing Canvas
	 */
	public SpaceShip(AudioClip lost, Image cecil, DrawingCanvas canvas) {
		// Assigns parameters to variables
		lose = lost;
		sagehen = cecil;

		// Draws Cecil in correct location
		cecilSage = new VisibleImage(sagehen, CANVASMID, canvas);
		cecilSage.setSize(CECILWIDTH, CECILHEIGHT);
		Location cecilStart = new Location(CANVASMID.getX() - (cecilSage.getWidth() / 2),
				CANVASMID.getY() - (cecilSage.getHeight() / 2));
		cecilSage.moveTo(cecilStart);
		cecilCenter = new Location(cecilSage.getX() + WANDX_OFFSET, cecilSage.getY() + (cecilSage.getHeight() / 2) + WANDY_OFFSET);

		// Draws wand in correct location
		Location gunEnd = new Location(cecilCenter.getX() + (WANDLENGTH * (Math.cos(0))),
				cecilCenter.getY() - (WANDLENGTH * (Math.sin(0))));
		cecilWand = new Line(cecilCenter, gunEnd, canvas);
		cecilWand.setColor(Color.blue);

		// Defines all variables
		// Booleans for Cecil life, Cecil movement and wand life
		life = true;
		move = true;
		wandLife = true;
		// Angle
		theta = 0;

		start();
	}

	/**
	 * Ends the game by removing Cecil, the wand and playing a sound.
	 */
	public void kill() {
		// Plays losing sound, updates life boolean and removes wand
		lose.play();
		cecilWand.removeFromCanvas();
		life = false;
		// Freezes Cecil and reduces them in size until they disappear
		for (int i = 0; i < CECIL_DESIZE; i++) {
			move = false;
			cecilSage.setSize(cecilSage.getWidth() / 2, cecilSage.getHeight() / 2);
			Location newMiddle = new Location(cecilSage.getX() + cecilSage.getWidth() / 2,
					cecilSage.getY() + cecilSage.getHeight() / 2);
			cecilSage.moveTo(newMiddle);
			pause(PAUSE);
		}
		cecilSage.removeFromCanvas();
	}

	/**
	 * Returns the angle the wand is currently at (between original position and
	 * current position) for the correct movement of the sageshot in the Bullet
	 * class.
	 * 
	 * @return angle wand is currently at
	 */
	public double getTheta() {
		return theta;
	}

	/**
	 * Keeps wand in Cecil's hand while Cecil moves.
	 */
	public void reCenter() {
		// Gets updated location of Cecil's center and moves wand to it
		cecilCenter = new Location(cecilSage.getX() + WANDX_OFFSET, cecilSage.getY() + (cecilSage.getHeight() / 2) + WANDY_OFFSET);
		cecilWand.moveTo(cecilCenter);
	}

	/**
	 * Rotates the wand to the left.
	 */
	public void rotateLeft() {
		// Moves the end of wand by correct angle only if Cecil is alive
		if (life) {
			theta = theta + ANGLE;
			cecilWand.setEnd(cecilCenter.getX() + (WANDLENGTH * (Math.cos(theta))),
					cecilCenter.getY() - (WANDLENGTH * (Math.sin(theta))));
		}
	}

	/**
	 * Rotates the wand to the right.
	 */
	public void rotateRight() {
		// Moves the end of wand by correct angle only if Cecil is alive
		if (life) {
			theta = theta - ANGLE;
			cecilWand.setEnd(cecilCenter.getX() + (WANDLENGTH * (Math.cos(theta))),
					cecilCenter.getY() - (WANDLENGTH * (Math.sin(theta))));
		}
	}

	/**
	 * Determine if Cecil has hit an asteroid or not.
	 * 
	 * @param asteroid ... Stag
	 * @return true if Cecil image touches an asteroid.
	 */
	public boolean overlap(VisibleImage asteroid) {
		return cecilSage.overlaps(asteroid);
	}

	/**
	 * Get the current location of the wand end.
	 * 
	 * @return Location of wand end.
	 */
	public Location getGunEnd() {
		return cecilWand.getEnd();
	}

	/**
	 * Checks if the wand is still alive.
	 * 
	 * @return true if wand (Cecil) has not died.
	 */
	public boolean wandLife() {
		return wandLife;
	}

	/**
	 * Sets the wand's life to false so it can no longer shoot.
	 * 
	 * @return false.
	 */
	public void stopShoot() {
		wandLife = false;
	}

	/**
	 * Moves Cecil in the same direction as wand and wraps Cecil around if they
	 * go off-screen.
	 */
	public void moveUp() {
		// Checks if Cecil is able to move
		if (move) {
			// Checks if Cecil ever goes off-screen and moves Cecil (and wand)
			// to opposite edge of canvas (if so). 
			if (cecilSage.getX() < CANVASEDGE1 - cecilSage.getWidth()) {
				cecilSage.moveTo(CANVASEDGE2 + cecilSage.getWidth(), cecilSage.getY());
				cecilWand.moveTo(cecilSage.getLocation());

			}
			if (cecilSage.getX() > CANVASEDGE2) {
				cecilSage.moveTo(CANVASEDGE1 - cecilSage.getWidth(), cecilSage.getY());
				cecilWand.moveTo(cecilSage.getLocation());

			}
			if (cecilSage.getY() < CANVASEDGE1 - cecilSage.getHeight()) {
				cecilSage.moveTo(cecilSage.getX(), CANVASEDGE2);
				cecilWand.moveTo(cecilSage.getLocation());

			}
			if (cecilSage.getY() > CANVASEDGE2) {
				cecilSage.moveTo(cecilSage.getX(), CANVASEDGE1);
				cecilWand.moveTo(cecilSage.getLocation());
			}
			// Moves Cecil and wand together in the direction wand is pointing.
			cecilSage.move(SPEEDMULTIPLIER * Math.cos(theta), SPEEDMULTIPLIER * -Math.sin(theta));
			reCenter();
		}
	}

}
