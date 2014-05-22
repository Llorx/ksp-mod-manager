package llorx.kspModManager;

import java.awt.Dimension;
import java.awt.Point;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.Toolkit;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import javafx.beans.value.*;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;

import org.jsoup.Connection.Response;

import javax.swing.JOptionPane;

import java.util.regex.*;

import java.util.Date;

public class Browser {
	WebEngine webEngine;
	Worker webWorker;

	int width = 0;
	int height = 0;
	
	public String lastClick = "";
	public String downloadFile = "";
	public boolean modReloaded = false;
	
	JLabel loading;
	
	int dots = 0;
	JDialog dialog;
	
	Mod mod;
	
	public void show(String url) {
		show(url, null);
	}
	
	public void show(String url, Mod m) {
		Platform.setImplicitExit(false);
		mod = m;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = (int)screenSize.getWidth();
		height = (int)screenSize.getHeight();
		
		if (width > 1024 || width == 0) {
			width = 1024;
		}
		
		if (height > 768 || height == 0) {
			height = 768;
		}
		
		dialog = new JDialog();
		
		dialog.getContentPane().setLayout(null);
		
		JFXPanel fxPanel = new JFXPanel();
		
		loading = new JLabel("", JLabel.CENTER);
		dialog.add(loading);
		loading.setSize(new Dimension(150, 30));
		loading.setLocation(new Point((width/2)-(loading.getWidth()/2), (height/2)-(loading.getHeight()/2)));
		loading.setOpaque(true);
		loading.setVisible(false);
		
		LineBorder line = new LineBorder(Color.darkGray, 1);
		loading.setBorder(line);
		
		final JButton jButton = new JButton("<< " + Strings.get(Strings.BROWSER_BACK));
		dialog.add(jButton);
		jButton.setSize(new Dimension(100, 27));
		jButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						webEngine.executeScript("history.back()");
					}
				});
			}
		});
		
		
		dialog.add(fxPanel);
		dialog.setModal(true);
		
		fxPanel.setSize(new Dimension(width, height));
		
		dialog.getContentPane().setPreferredSize(new Dimension(width, height));
		dialog.pack();
		dialog.setResizable(false);
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				initFX(fxPanel, url);
			}
		});
		
		Point middle = new Point((int)screenSize.getWidth() / 2, (int)screenSize.getHeight() / 2);
		Point newLocation = new Point(middle.x - (dialog.getWidth() / 2), 
									  middle.y - (dialog.getHeight() / 2));
		dialog.setLocation(newLocation);
		
		dialog.setVisible(true);
	}
	
	private void checkLinkChange() {
		String[][] patterns = {{
			"CurseForge",
			"(.*)(kerbal.curseforge.com\\/)([^\\/]*)(\\/)(\\d*)([^\\/]*)"
		},{
			"Curse",
			"(.*)(curse.com\\/)([^\\/]*)(\\/kerbal\\/)(\\d*)([^\\/]*)"
		},{
			"GitHub",
			"(.*)(github.com\\/)([^\\/]*)(\\/[^\\/]*)(\\/releases)"
		}};
		String link = lastClick;
		for (String[] p: patterns) {
			String name = p[0];
			String pat = p[1];
			if (link.matches(pat+"(.*)")) {
				Pattern pattern = Pattern.compile(pat);
				Matcher matcher = pattern.matcher(link);
				if (matcher.find()) {
					link = matcher.group(0);
					int reply = JOptionPane.showConfirmDialog(null, Strings.get(Strings.LINK_DETECTED_ASK).replace("%SERVERNAME%", name), Strings.get(Strings.LINK_DETECTED_TITLE), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (reply == JOptionPane.YES_OPTION) {
						modReloaded = true;
						mod.reloadMod(link);
						mod.setLastDate(new Date());
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								webWorker.cancel();
								SwingUtilities.invokeLater(new Runnable(){
									@Override public void run() {
										dialog.dispose();
									}
								});
							}
						});
					}
				}
			}
		}
	}
	
	private void initFX(JFXPanel fxPanel, String url) {

		Group group = new Group();
		Scene scene = new Scene(group);
		fxPanel.setScene(scene);

		WebView webView = new WebView();

		group.getChildren().add(webView);
		webView.setMinSize(width, height);
		webView.setMaxSize(width, height);
		webView.setContextMenuEnabled(false);
		
		webEngine = webView.getEngine();
		webWorker = webEngine.getLoadWorker();
		
		webWorker.stateProperty().addListener(new ChangeListener<State>() {
			@Override
			public void changed(ObservableValue ov, State oldState, State newState) {
				if (newState == State.SUCCEEDED || newState == State.CANCELLED || newState == State.FAILED) {
					loading.setVisible(false);
				} else if (newState == State.RUNNING) {
					loading.setVisible(true);
					dots = 0;
					try {
						if (mod != null) {
							checkLinkChange();
						}
						int fileType = Http.fileType(lastClick);
						if (modReloaded == false && fileType != Http.HTML) {
							if (fileType == Http.ZIP_EXTENSION) {
								int reply = JOptionPane.showConfirmDialog(null, Strings.get(Strings.LINK_SELECTED_ASK).replace("%LINKCLICKED%", lastClick), Strings.get(Strings.SURE_ASK), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
								if (reply == JOptionPane.YES_OPTION) {
									downloadFile = lastClick;
									Platform.runLater(new Runnable() {
										@Override
										public void run() {
											webWorker.cancel();
											SwingUtilities.invokeLater(new Runnable(){
												@Override public void run() {
													dialog.dispose();
												}
											});
										}
									});
								}
							} else {
								JOptionPane.showMessageDialog(null, Strings.get(Strings.FILE_NOT_SUPPORTED), Strings.get(Strings.FILE_NOT_SUPPORTED_TITLE), JOptionPane.PLAIN_MESSAGE);
							}
						}
					} catch (Exception e) {
					}
				}
			}
		});
		
		webWorker.progressProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue ov, Number oldState, Number newState) {
				String dotsString = "";
				for (int i = 0; i < 15; i++) {
					if (i == dots) {
						dotsString = dotsString + "|";
					} else {
						dotsString = dotsString + "-";
					}
				}
				dots++;
				if (dots >= 15) {
					dots = 0;
				}
				loading.setText(dotsString);
			}
		});
		
		webEngine.locationProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observableValue, String oldLoc, String newLoc) {
				lastClick = newLoc;
			}
		});
		
		webEngine.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.131 Safari/537.36");
		webEngine.load(url);
	}
}