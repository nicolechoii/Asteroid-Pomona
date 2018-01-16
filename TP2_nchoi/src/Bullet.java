import objectdraw.*;

import java.applet.AudioClip;
import java.awt.*;

//Name: Nicole Choi
//Comments:
//Extra Credit:
// - Changed bullets to "47" sageshots. 
// - Added sound effects to when Stag is shot.
// - Added a limit to the number of sageshots available. 
// - End game when maximum number of sageshots is used up. 

/**
 * A Bullet is an ActiveObject that appears as the number 47 called a sageshot.
 * The sageshots come from Cecil's wand and kill the Stags if they come into
 * contact with them.
 */
public class Bullet extends ActiveObject {
	// Constants
	private static final int PAUSE = 5;
	private static final int CANVAS_EDGE1 = 0;
	private static final int CANVAS_EDGE2 = 400;
	private static final int MAX_SAGESHOTS = 47;

	// Instance variables
	// The sageshot
	private Text sageshot;

	// Sageshot characteristics
	private double wandAngle; // angle the sageshot is shot at
	private Location startLoc; // starting location of sageshot
	private Boolean sageshotLife; // checks if sageshot is still on-screen
	private int numSageshots; // number of sageshots fired
	private AudioClip meh; // sound when sageshot hits Stag

	// Other classes passed in as parameters
	private SpaceShip cecil;
	private Asteroid[] theArray;
	private ScoreKeeper scorekeeper;

	// Stag images and canvas
	private Image stagPic;
	private Image stagPic2;
	private DrawingCanvas acanvas;

	/**
	 * Creates a "47" text object which acts as a sageshot.
	 * 
	 * @param stagDie ... the sound played when a Stag is shot
	 * @param numbersageShots ... number of sageshots fired
	 * @param stagArray ... array of Stags
	 * @param stagImage1 ... a left traveling Stag
	 * @param stagImage2 ... a right traveling Stag
	 * @param scoreKeep ... the scorekeeper
	 * @param sageHen ... Cecil figure
	 * @param canvas ... Drawing Canvas
	 */
	public Bullet(AudioClip stagDie, int numbersageshots, Asteroid[] stagArray, Image stagImage1, Image stagImage2,
			ScoreKeeper scoreKeep, SpaceShip sageHen, DrawingCanvas canvas) {
		// Assigns parameters to variables
		cecil = sageHen;
		theArray = stagArray;
		scorekeeper = scoreKeep;
		stagPic = stagImage1;
		stagPic2 = stagImage2;
		acanvas = canvas;
		numSageshots = numbersageshots;
		meh = stagDie;

		// Defines instance variables
		startLoc = cecil.getGunEnd();
		wandAngle = cecil.getTheta();
		sageshotLife = true;

		// Draws the sageshot
		sageshot = new Text("47", startLoc, canvas);
		sageshot.setColor(Color.white);

		start();
	}

	/**
	 * run ... active object main loop
	 * 
	 * Moves the sageshot in the direction wand is pointing until it hits a Stag
	 * or goes off-screen (then is removed). Respawns Stags when they are hit.
	 * Ends the game if number of sageshots is exceeded.
	 */
	public void run() {
		// Checks if maximum number of sageshots has been exceeded
		if (numSageshots < MAX_SAGESHOTS) {
			// Makes sure sageshot stays on canvas only if no Stag has come into
			// contact
			while (sageshotLife) {
				// Checks all Stags to see if they overlap with the sageshot
				for (int i = 0; i < theArray.length; i++) {
					// If a Stag overlaps with the sageshot, the Stag is
					// destroyed with a sound effect. The score is updated, a
					// new Stag is respawned and the sageshot is removed.
					if (theArray[i].overlapBullet(sageshot)) {
						theArray[i].destroy();
						meh.play();
						theArray[i] = new Asteroid(theArray, cecil, scorekeeper, stagPic, stagPic2, acanvas);
						scorekeeper.increment();
						sageshotLife = false;
						// Otherwise the sageshot moves.
					} else {
						sageshot.move(Math.cos(wandAngle), -Math.sin(wandAngle));
						pause(PAUSE);
						// Removes the sageshot if it goes off canvas.
						if (sageshot.getX() > CANVAS_EDGE2 || sageshot.getX() > CANVAS_EDGE2
								|| sageshot.getY() < CANVAS_EDGE1 || sageshot.getY() > CANVAS_EDGE2) {
							sageshotLife = false;
						}
					}

				}
			}
			sageshot.removeFromCanvas();
		} else {
			// Changes the score, removes Cecil and the sageshot to match the
			// game ending.
			cecil.stopShoot();
			scorekeeper.gameOver();
			sageshot.removeFromCanvas();
			cecil.kill();
		}
	}

}