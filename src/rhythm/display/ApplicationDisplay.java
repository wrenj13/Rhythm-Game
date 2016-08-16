package rhythm.display;


import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import rhythm.game.MusicReader;
import rhythm.game.RhythmGame;
import rhythm.game.Song;
import rhythm.game.User;

public class ApplicationDisplay {

	private static User user;
	private static Song song;
	private static String username = "";
	private static String password = "";
	
	/**
	 * A function that opens a webpage in the users default browser, given a URI
	 * 
	 * http://stackoverflow.com/questions/10967451/open-a-link-in-browser-with-java-button
	 * 
	 * @param uri
	 */
	public static void openWebpage(URI uri) {
		Desktop desktop = Desktop.isDesktopSupported() ? 
				Desktop.getDesktop() : null;
		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				desktop.browse(uri);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * A wrapper function to open a webpage, given a URL.
	 * 
	 * http://stackoverflow.com/questions/10967451/open-a-link-in-browser-with-java-button
	 * 
	 * @param url
	 */
	public static void openWebpage(URL url) {
		try {
			openWebpage(url.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets up the initial login screen.
	 * 
	 * @param panel
	 */
	private static void addLoginScreen(JPanel panel) {

		panel.setLayout(null);

		JLabel userLabel = new JLabel("User");
		userLabel.setBounds(10, 10, 80, 25);
		panel.add(userLabel);

		JTextField userText = new JTextField(20);
		userText.setBounds(100, 10, 160, 25);
		panel.add(userText);

		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(10, 40, 80, 25);
		panel.add(passwordLabel);

		JPasswordField passwordText = new JPasswordField(20);
		passwordText.setBounds(100, 40, 160, 25);
		panel.add(passwordText);

		JButton loginButton = new JButton("login");
		loginButton.setBounds(10, 80, 80, 25);
		panel.add(loginButton);

		JButton registerButton = new JButton("register");
		registerButton.setBounds(180, 80, 80, 25);
		panel.add(registerButton);

		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				username = userText.getText();
				password = new String(passwordText.getPassword());
				
				// Tempory code to instantiate a user.
				// TODO: Query database for the username
				user = new User(username);
				user.addPoints(100);
				Song tsOrig = MusicReader.createYouBelongWithMeTS();
				user.purchaseSong(tsOrig);
				user.purchaseSong(tsOrig);
				user.purchaseSong(tsOrig);
				user.purchaseSong(tsOrig);
				user.purchaseSong(tsOrig);
				user.purchaseSong(MusicReader.createGee());
				Song easyTS = new Song(MusicReader.createEasyVersion(tsOrig.getEvents()), tsOrig.getTitle() + ": Easy", tsOrig.getArtist(), tsOrig.getSongPath(), tsOrig.getBpm(), tsOrig.getPoints() / 2);
				user.purchaseSong(easyTS);
				Song doubleTS = new Song(MusicReader.populateDouble(tsOrig.getEvents()), tsOrig.getTitle() + ": Expert", tsOrig.getArtist(), tsOrig.getSongPath(), tsOrig.getBpm(), tsOrig.getPoints() * 2);
				user.purchaseSong(doubleTS);
				
				panel.removeAll();
				panel.setLayout(new BorderLayout());
				setMainMenu(panel);
				panel.updateUI();
			}
		});

		registerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String urlStr = "http://web.engr.illinois.edu/~davidfu2/renjay/registration.php";
				URL url = null;
				try {
					url = new URL(urlStr);
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				openWebpage(url);
			}
		});
	}

	/**
	 * Adds the main screen buttons and their actionlisteners.
	 * 
	 * @param panel
	 */
	public static void setMainMenu(JPanel panel) {

		// Create the Song panel
		JPanel buttonPanel = new JPanel(new GridLayout(0, 1));
		
		for (Song s : user.getSongs()) {
			JButton songButton = new JButton(s.getTitle());
			songButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					song = s;
				}
			});
			buttonPanel.add(songButton);
		}		
		JScrollPane buttonScroll = new JScrollPane(buttonPanel);

		JButton playButton = new JButton("Play Song!");
		playButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				if (song == null) {
					JOptionPane.showMessageDialog(panel,
                            "Please select a song",
                            "No song selected!",
                            JOptionPane.ERROR_MESSAGE);
				}
				else {
					RhythmGame game = new RhythmGame(song);
				}
			}
		});

		JPanel playPanel = new JPanel();
		playPanel.add(playButton);

		panel.add(buttonScroll, BorderLayout.WEST);
		panel.add(playButton, BorderLayout.EAST);
	}
	
	public static void main(String[] args) {
		JPanel panel = new JPanel(new BorderLayout());
		addLoginScreen(panel);
		JFrame frame = new JFrame("Welcome to This Healthy Beat!");
		frame.setSize(300, 200);
		frame.setLocation(500, 280);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setFocusable(true);
		frame.add(panel);
		frame.setVisible(true);
	}
}
