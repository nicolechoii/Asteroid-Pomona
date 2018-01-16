import objectdraw.*;
import java.awt.event.*;
import java.applet.AudioClip;
import java.awt.*;
import javax.swing.*;

//Name: Nicole Choi
//Comments: 
//Extra Credit: 
// - Added sound effects to when the bullet shoots. Note that sounds only work 
// every other time bullets are shot, most likely due to program/Java being 
// unable to handle the sound frequencies. 

/**
 * TP2 - Asteroids
 * 
 * A Pomona version of the Atari classic Asteroids. Instead of a ship and
 * asteroids, Cecil the Sagehen battles off CMC Stags across the campuses by
 * shooting "47" sageshots.
 * 
 * AsteroidsGame class: Instantiates the other classes and receives keyboard
 * events.
 *
 * Suggested screen size: 400 x 450
 */
public class AsteroidsGame extends WindowController implements KeyListener {
	// Constants
	private static final int MAX_STAGS = 10; // Stag array index

	// Instance variables
	// Main game components
	private SpaceShip cecil;
	private ScoreKeeper score;
	private Asteroid[] stagArray;
	private Container contentPane;
	private AsteroidsGame theGame;

	// Images
	private Image sageHen;
	private Image stagLeft;
	private Image stagRight;
	private Image bkg1;
	private Image bkg2;
	private Image bkg3;
	private Image bkg4;
	private Image bkg5; 

	// Sounds
	private AudioClip chirp;
	private AudioClip dead;
	private AudioClip bkgMusic1;
	private AudioClip bkgMusic2;
	private AudioClip bkgMusic3;
	private AudioClip bkgMusic4; 
	private AudioClip bkgMusic5; 
	private AudioClip lose;

	// Sageshot counter
	private int numSageshots;
	
	/**
	 * Initialization method, called when applet starts.
	 * Load all images and sounds. 
	 * Create content pane for GUI elements.
	 * Instantiate the game, Cecil, scorekeeper and array of Stags. 
	 * Arrange to receive events when the arrow keys and space bar are pressed.
	 */
	public void begin() {
		// Load all images
		// Characters
		sageHen = getImage("Pomona.png");
		stagLeft = getImage("stag.png");
		stagRight = getImage("stagother.png");
		// Backgrounds
		bkg1 = getImage("bitmarston.jpg");
		bkg2 = getImage("cube.jpg");
		bkg3 = getImage("Patzer.jpg");
		bkg4 = getImage("Scripps.jpg");
		bkg5 = getImage("HMudd.jpg");

		// Load all sounds
		// Sound effects
		chirp = getAudio("chirp2.au");
		dead = getAudio("swoosh.au");
		lose = getAudio("lose.au");
		// Background music
		bkgMusic1 = getAudio("pomonaMusic.au");
		bkgMusic2 = getAudio("CMC.au");
		bkgMusic3 = getAudio("pitzerMusic.au");
		bkgMusic4 = getAudio("scrippsTune.au");
		bkgMusic5 = getAudio("hMuddTunes.au");

		// Set up content pane for GUI components in ScoreKeeper class
		contentPane = getContentPane();
		contentPane.validate();

		// Creates the game, Cecil, scorekeeper and array of stags
		theGame = this;
		score = new ScoreKeeper(theGame, bkgMusic2, bkgMusic1, bkgMusic3, bkgMusic4, bkgMusic5, bkg1, bkg2, bkg3, bkg4, bkg5, contentPane, canvas);
		cecil = new SpaceShip(lose, sageHen, canvas);
		stagArray = new Asteroid[MAX_STAGS];
		for (int i = 0; i < MAX_STAGS; i++) {
			stagArray[i] = new Asteroid(stagArray, cecil, score, stagLeft, stagRight, canvas);

		}

		// Getting ready to respond to user's key presses
		setFocusable(true);
		requestFocus();
		addKeyListener(this);
		canvas.addKeyListener(this);
	}

	/**
	 * KeyListener event handler for a key having been pressed and
	 * released.
	 * @param e ... event (key that was typed)
	 */
	public void keyTyped(KeyEvent e) {
	}

	/**
	 * KeyListener event handler for a key having been released.
	 * @param e ... event (key that was released)
	 */
	public void keyReleased(KeyEvent e) {
	}

	/**
	 * KeyListener event handler for a key having been pressed.
	 * Handle space bar by telling Cecil to shoot sageshots. 
	 * Handle left and right arrow keys by telling Cecil's wand to rotate left/right respectively.
	 * Handle up arrow key by telling Cecil and wand to move. 
	 * 
	 * @param e ... event (key that was pressed)
	 */
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			// Tells Cecil to shoot only if wand is still alive
			if (cecil.wandLife()) {
				// plays corresponding sound and updates sageshot counters
				chirp.play();
				score.bulletShot();
				numSageshots++;
				new Bullet(dead, numSageshots, stagArray, stagLeft, stagRight, score, cecil, canvas);
			}
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			// Turns wand right
			cecil.rotateRight();
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			// Turns wand left
			cecil.rotateLeft();
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			// Moves Cecil in the direction of the wand
			cecil.moveUp();
		}
	}

	/**
	 * mouse event handler ... request keyboard input
	 * @param pt ... location of mouse press
	 */
	public void onMousePress(Location pt) {
		this.requestFocus();
	}

}
