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
import javax.swing.JPanel;
import javax.swing.JFrame;
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

public class Browser {
	WebEngine webEngine;
	Worker webWorker;

	int width = 0;
	int height = 0;
	
	public String lastClick = "";
	public String downloadFile = "";
	
	JLabel loading;
	
	int dots = 0;
	JDialog dialog;
	
	public void show(String url) {
		Platform.setImplicitExit(false);
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
		
		
		final JButton jButton = new JButton("<< Back");
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
	
	private void initFX(final JFXPanel fxPanel, String url) {

		Group group = new Group();
		Scene scene = new Scene(group);
		fxPanel.setScene(scene);

		WebView webView = new WebView();

		group.getChildren().add(webView);
		webView.setMinSize(width, height);
		webView.setMaxSize(width, height);
		webView.setContextMenuEnabled(false);
		
			// Obtain the webEngine to navigate
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
						int fileType = Http.fileType(lastClick);
						if (fileType != Http.HTML) {
							if (fileType == Http.ZIP_EXTENSION) {
								int reply = JOptionPane.showConfirmDialog(null, "Selected:\n" + lastClick + "\nAre you sure?", "Sure?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
								if (reply == JOptionPane.YES_OPTION) {
									downloadFile = lastClick;
									Platform.runLater(new Runnable(){
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
								JOptionPane.showMessageDialog(null, "This file is not a zip file. Not supported by Mod Manager right now. Select another one.", "File not supported", JOptionPane.PLAIN_MESSAGE);
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
		
		webEngine.load(url);
	}
}