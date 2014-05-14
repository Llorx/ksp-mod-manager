package llorx.kspModManager;

import java.io.Serializable;
import java.io.File;

import javax.swing.JSeparator;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.JCheckBox;

import javax.swing.border.EmptyBorder;

import java.awt.Font;
import java.awt.Dimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.lang.ProcessBuilder;

import java.util.ArrayList;

public class ManagerConfig implements Serializable {
	static final long serialVersionUID = -1060044609638745786L;
	
	public String kspDataFolder = "";
	public String moduleManagerLink = "http://forum.kerbalspaceprogram.com/threads/55219";
	public transient String defaultModuleManagerLink = "http://forum.kerbalspaceprogram.com/threads/55219";
	public boolean excludeUnneededFiles = true;
	public boolean excludeModuleManagerDll = true;
	
	private transient JCheckBox excludeFilesCheck;
	private transient JCheckBox excludeMmCheck;
	
	public transient Main main;
	
	public ManagerConfig(Main main) {
		this.main = main;
	}
	
	private JPanel getPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		JLabel titleTxt = new JLabel("KSP Mod Manager config");
		Font font = titleTxt.getFont().deriveFont(titleTxt.getFont().getSize2D()+5.0f);
		titleTxt.setFont(font);
		panel.add(titleTxt);
		
		panel.add(new JSeparator(JSeparator.HORIZONTAL));
		
		JButton changeKspDataBut = new JButton("Change KSP installation folder");
		changeKspDataBut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectKspFolder();
			}
		});
		changeKspDataBut.setMaximumSize(new Dimension(300, 30));
		panel.add(changeKspDataBut);
		
		JButton changeMmLinkBut = new JButton("Change Module Manager download link");
		changeMmLinkBut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Mod newMm;
				String newUrl;
				do {
					newMm = null;
					newUrl = JOptionPane.showInputDialog(null, "Paste the URL to download Module Manager dll", moduleManagerLink);
					if (newUrl != null && newUrl.length() > 0) {
						newMm = new Mod("Module Manager dll", newUrl, true);
						if (!newMm.isValid) {
							main.alertBox(null, "That URL was not valid.");
						}
					}
				} while ((newMm != null && !newMm.isValid));
				if (newMm != null && newMm.isValid) {
					moduleManagerLink = newUrl;
					main.alertBox(null, "URL updated.");
				}
			}
		});
		changeMmLinkBut.setMaximumSize(new Dimension(300, 30));
		panel.add(changeMmLinkBut);
		
		JButton restoreMmLinkBut = new JButton("Restore Module Manager download link");
		restoreMmLinkBut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moduleManagerLink = defaultModuleManagerLink;
				main.alertBox(null, "URL restored.");
			}
		});
		restoreMmLinkBut.setMaximumSize(new Dimension(300, 30));
		panel.add(restoreMmLinkBut);
		
		
		excludeFilesCheck = new JCheckBox("Exclude unneeded files (Sources, Readmes, etc...)");
		excludeFilesCheck.setSelected(excludeUnneededFiles);
		excludeFilesCheck.setBorder(new EmptyBorder(5, 0, 5, 0));
		panel.add(excludeFilesCheck);
		
		panel.add(new JSeparator(JSeparator.HORIZONTAL));
		
		JLabel warnText = new JLabel("Only change this if you know what you are doing:");
		warnText.setBorder(new EmptyBorder(5, 0, 0, 0));
		panel.add(warnText);
		
		excludeMmCheck = new JCheckBox("Exclude Module Manager dll's from other mods");
		excludeMmCheck.setSelected(excludeModuleManagerDll);
		excludeMmCheck.setBorder(new EmptyBorder(0, 0, 5, 0));
		panel.add(excludeMmCheck);
		
		panel.add(new JSeparator(JSeparator.HORIZONTAL));
		
		return panel;
	}
	
	private boolean askForKspFolder() {
		try {
			JFileChooser j = new JFileChooser();
			j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int opt = j.showSaveDialog(main);
			if (opt == JFileChooser.APPROVE_OPTION) {
				String path = j.getSelectedFile().getCanonicalPath() + File.separator + "GameData";
				File f = new File(path);
				if (f.exists() && f.isDirectory()) {
					kspDataFolder = f.getCanonicalPath();
					return true;
				} else {
					main.alertBox(null, "This does not seems a KSP main installation folder.");
				}
			}
		} catch (Exception ex) {
			ErrorLog.log(ex);
		}
		return false;
	}
	
	public boolean selectKspFolder() {
		boolean ok = false;
		do {
			int reply = JOptionPane.showConfirmDialog(null, "Please, select your KSP main installation folder.", "KSP main folder", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
			if (reply == JOptionPane.OK_OPTION) {
				ok = askForKspFolder();
			} else {
				return false;
			}
		} while(ok == false);
		return true;
	}
	
	public void change() {
		JOptionPane.showMessageDialog(null, getPanel(), "Config", JOptionPane.PLAIN_MESSAGE);
		this.excludeUnneededFiles = excludeFilesCheck.isSelected();
		this.excludeModuleManagerDll = excludeMmCheck.isSelected();
	}
	
	public void checkVersion() {
		boolean updateFound = false;
		String LMMversion = "v0.1.7.1alpha";
		try {
			Document doc = Http.get("http://forum.kerbalspaceprogram.com/threads/78861").parse();
			Element title = doc.select("span[class=threadtitle]").first();
			if (title != null) {
				String v = title.text();
				int index = v.lastIndexOf("-");
				if (index > -1) {
					v = v.substring(index+1).trim();
					if (!LMMversion.equals(v)) {
						int reply = JOptionPane.showConfirmDialog(null, "New update found. Install it now?", "New update", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
						if (reply == JOptionPane.YES_OPTION) {
							JOptionPane.showMessageDialog(null, "Updating... do not touch anything", "Updating", JOptionPane.PLAIN_MESSAGE);
							updateFound = true;
							Element posts = doc.select("ol[id=posts]").first();
							if (posts != null) {
								Element post = posts.select("li[id^=post_]").first();
								if (post != null) {
									Element linkEl = post.select("a[href*=.zip]").first();
									if (linkEl != null) {
										String link = linkEl.attr("href");
										if (link.length() > 0) {
											String filename = main.downloadFile(link, null);
											if (filename != null) {
												File f = new File("temp" + File.separator + filename);
												boolean error = false;
												try {
													Zip.extract("temp" + File.separator + filename, "temp" + File.separator + "LMMupdate");
												} catch (Exception e) {
													ErrorLog.log(e);
													error = true;
												}
												if (error == false) {
													
													final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
													
													final ProcessBuilder builder = new ProcessBuilder(javaBin, "-jar", "temp" + File.separator + "LMMupdate" + File.separator + "LlorxKspModManager.jar", "-u");
													builder.start();
													
													System.exit(0);
													
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			ErrorLog.log(e);
		}
		if (updateFound == true) {
			JOptionPane.showMessageDialog(null, "Error updating. Download LMM manually.", "Error", JOptionPane.PLAIN_MESSAGE);
		}
	}
}