import objectdraw.*;

import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

//Name: Nicole Choi
//Comments: 
//Extra Credit:
// - Added display for sageshot limit
// - Added customized backgrounds
// - Added customized background music
// - Added ability to change background via button clicks
// - Added a "final score" display when game ends
// - Added a game over message

/**
 * A ScoreKeeper displays the player's score, how many bullets they have left
 * and a game over message. It also creates specific changeable backgrounds and
 * background music.
 */
public class ScoreKeeper implements ActionListener {
	// Constants
	// Game over message
	private static final int SIZE = 20; // font size
	private static final int BKG_X = 38;
	private static final int BKG_Y = 155;
	private static final int BKG_WIDTH = 330;
	private static final int BKG_HEIGHT = 60;
	private static final int CHIRP_X = 50;
	private static final int CHIRP_Y = 162;
	private static final int NEXTTIME_X = 75;
	private static final int NEXTTIME_Y = 182;

	// Background dimensions
	private static final int SIZE1 = 400;
	private static final int SIZE2 = 500;
	private static final int SIZE3 = 600;
	private static final int SIZE4 = 380;
	private static final int SCRIPPSX = -100;
	private static final int SCRIPPSY = -20;

	// Misc.
	private static final int SCORE = 10; // number of points for every Stag hit
	private static final int MAX_SAGESHOTS = 47; // maximum number of sageshots
	private static final int NUM_ROWS = 3; // number of rows in south panel 
	private static final int NUM_COLUMNS = 5; // number of columns in button row

	// Instance Variables
	// Score display and sageshot count
	private JLabel scoreDisplay;
	private JLabel sageshotsLeft;

	// Buttons to change background
	private JButton claremontM;
	private JButton pamona;
	private JButton patzer;
	private JButton scrapps;
	private JButton hmadd;

	// Game over message
	private Text endGame1;
	private Text endGame2;
	private FilledRect bkgText;
	private FramedRect bkgTextFrame;

	// Backgrounds
	private VisibleImage pomona;
	private VisibleImage cmc;
	private VisibleImage pitzer;
	private VisibleImage scripps;
	private VisibleImage hmudd;
	private Location generalBkgLoc;
	private Location scrippsLoc; 

	// Background music
	private AudioClip cmcMus;
	private AudioClip poMus;
	private AudioClip piMus;
	private AudioClip scrMus;
	private AudioClip hMus;

	// Other class passed in as parameter
	private AsteroidsGame theGame;

	// Etc
	private Container contentPanel;
	private int score; // actual score
	private int numSageshots; // remaining bullets

	/**
	 * Creates the score display, sageshot count, backgrounds and background
	 * music.
	 * 
	 * @param game ... the AsteroidsGame being played
	 * @param cMusic ... background music played at CMC
	 * @param poMusic ... default background music played at Pomona
	 * @param piMusic ... background music played at Pitzer
	 * @param scrMusic ... background music played at Scripps
	 * @param hMusic ... background music played at Harvey Mudd
	 * @param pomonaBkg ... Pomona background image
	 * @param cmcBkg ... CMC background image
	 * @param pitzBkg ... Pitzer background image
	 * @param scrippsBkg ... Scripps background image
	 * @param hmuddBkg ... Harvey Mudd background image
	 * @param contentPanel ... content pane
	 * @param canvas ... Drawing Canvas
	 */
	public ScoreKeeper(AsteroidsGame game, AudioClip cMusic, AudioClip poMusic, AudioClip piMusic, AudioClip scrMusic,
			AudioClip hMusic, Image pomonaBkg, Image cmcBkg, Image pitzBkg, Image scrippsBkg, Image hmuddBkg,
			Container contentPanel, DrawingCanvas canvas) {
		// Assign parameters to variables
		theGame = game;

		// Define variables
		numSageshots = MAX_SAGESHOTS;
		score = 0;

		// Set up bottom panel to hold score
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(NUM_ROWS, 1));

		// Sets up sub-panel for background change buttons
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, NUM_COLUMNS));

		// Construct score and sageshots left display
		sageshotsLeft = new JLabel("You have " + numSageshots + " sageshots left.", SwingConstants.CENTER);
		scoreDisplay = new JLabel("Score: " + score, SwingConstants.CENTER);

		// Creates buttons for background changes and adds them to sub-panel
		pamona = new JButton("Pomona");
		claremontM = new JButton("CMC");
		patzer = new JButton("Pitzer");
		scrapps = new JButton("Scripps");
		hmadd = new JButton("HMC");
		buttonPanel.add(pamona);
		buttonPanel.add(claremontM);
		buttonPanel.add(patzer);
		buttonPanel.add(scrapps);
		buttonPanel.add(hmadd);

		// Set up backgrounds
		generalBkgLoc = new Location(0, 0); // Location for most backgrounds
		scrippsLoc = new Location(SCRIPPSX, SCRIPPSY);
		pomona = new VisibleImage(pomonaBkg, generalBkgLoc, canvas);
		pomona.setSize(SIZE1, SIZE1);
		pomona.sendToBack();
		cmc = new VisibleImage(cmcBkg, generalBkgLoc, canvas);
		cmc.setSize(SIZE2, SIZE1);
		cmc.hide();
		pitzer = new VisibleImage(pitzBkg, generalBkgLoc, canvas);
		pitzer.setSize(SIZE3, SIZE4);
		pitzer.hide();
		scripps = new VisibleImage(scrippsBkg, scrippsLoc, canvas);
		scripps.setSize(SIZE3, SIZE1);
		scripps.hide();
		hmudd = new VisibleImage(hmuddBkg, generalBkgLoc, canvas);
		hmudd.setSize(SIZE3, SIZE1);
		hmudd.hide();

		// Create background music and set default
		cmcMus = cMusic;
		poMus = poMusic;
		piMus = piMusic;
		scrMus = scrMusic;
		hMus = hMusic;
		poMus.play();

		// Creates game over display and hides it
		bkgText = new FilledRect(BKG_X, BKG_Y, BKG_WIDTH, BKG_HEIGHT, canvas);
		bkgText.setColor(Color.blue);
		bkgText.hide();
		bkgTextFrame = new FramedRect(BKG_X, BKG_Y, BKG_WIDTH, BKG_HEIGHT, canvas);
		bkgTextFrame.setColor(Color.white);
		bkgTextFrame.hide();
		endGame1 = new Text("CHIRP CHIRP MOTHERCLUCKER,", CHIRP_X, CHIRP_Y, canvas);
		endGame2 = new Text("BETTER LUCK NEXT TIME!", NEXTTIME_X, NEXTTIME_Y, canvas);
		endGame1.setFontSize(SIZE);
		endGame1.setColor(Color.ORANGE);
		endGame1.hide();
		endGame2.setFontSize(SIZE);
		endGame2.setColor(Color.ORANGE);
		endGame2.hide();

		// Add score display, sageshot count and background buttons to screen
		bottomPanel.add(sageshotsLeft);
		bottomPanel.add(scoreDisplay);
		bottomPanel.add(buttonPanel);

		// Set this class to respond to button clicks
		claremontM.addActionListener(this);
		pamona.addActionListener(this);
		patzer.addActionListener(this);
		scrapps.addActionListener(this);
		hmadd.addActionListener(this);

		// Add components to content pane
		this.contentPanel = contentPanel;
		this.contentPanel.add(bottomPanel, BorderLayout.SOUTH);
	}

	/**
	 * Adds 10 points to the score and update display.
	 */
	public void increment() {
		// increments score and updates display accordingly
		score = score + SCORE;
		scoreDisplay.setText("Score: " + score);
	}

	/**
	 * Ends the game by displaying final score, game over message and making
	 * buttons unclickable.
	 */
	public void gameOver() {
		// Shows final score, game over message and makes buttons null
		scoreDisplay.setText("Final Score: " + score);
		bkgText.show();
		bkgTextFrame.show();
		endGame1.show();
		endGame2.show();
		claremontM = null;
		pamona = null;
		patzer = null;
		scrapps = null;
		hmadd = null;
	}

	/**
	 * Updates sageshot counter.
	 */
	public void bulletShot() {
		numSageshots--;
		sageshotsLeft.setText("You have " + numSageshots + " sageshots left.");
	}

	/**
	 * Updates sageshot counter.
	 * 
	 * @param e
	 *            ... button click
	 */
	public void actionPerformed(ActionEvent e) {
		// Checks button pressed and changes background image and music
		// accordingly; also resets focus in AsteroidsGame class to continue
		// keyboard functions.
		if (e.getSource().equals(claremontM)) {
			theGame.requestFocus();
			cmc.show(); // correct background, hides all others
			pomona.hide();
			pitzer.hide();
			scripps.hide();
			hmudd.hide();
			cmcMus.play(); // correct music, stops all others
			poMus.stop();
			piMus.stop();
			scrMus.stop();
			hMus.stop();
		} else if (e.getSource().equals(pamona)) {
			theGame.requestFocus();
			pomona.show(); // correct background, hides all others
			pitzer.hide();
			cmc.hide();
			hmudd.hide();
			scripps.hide();
			poMus.play(); // correct music, stops all others
			piMus.stop();
			scrMus.stop();
			hMus.stop();
			cmcMus.stop();
		} else if (e.getSource().equals(patzer)) {
			theGame.requestFocus();
			pitzer.show(); // correct background, hides all others
			cmc.hide();
			pomona.hide();
			hmudd.hide();
			scripps.hide();
			piMus.play(); // correct music, stops all others
			cmcMus.stop();
			poMus.stop();
			scrMus.stop();
			hMus.stop();
		} else if (e.getSource().equals(scrapps)) {
			theGame.requestFocus();
			scripps.show(); // correct background, hides all others
			cmc.hide();
			pomona.hide();
			hmudd.hide();
			pitzer.hide();
			scrMus.play(); // correct music, stops all others
			piMus.stop();
			cmcMus.stop();
			poMus.stop();
			hMus.stop();
		} else if (e.getSource().equals(hmadd)) {
			theGame.requestFocus();
			hmudd.show(); // correct background, hides all others
			scripps.hide();
			cmc.hide();
			pomona.hide();
			pitzer.hide();
			hMus.play(); // correct music, stops all others
			piMus.stop();
			cmcMus.stop();
			poMus.stop();
			scrMus.stop();
		}
	}

}