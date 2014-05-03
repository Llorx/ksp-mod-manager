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

public class ManagerConfig implements Serializable {
	public String kspDataFolder = "";
	public String moduleManagerLink = "http://forum.kerbalspaceprogram.com/threads/55219";
	public String defaultModuleManagerLink = "http://forum.kerbalspaceprogram.com/threads/55219";
	public boolean excludeUnneededFiles = true;
	public boolean excludeModuleManagerDll = true;
	
	private transient JCheckBox excludeFilesCheck;
	private transient JCheckBox excludeMmCheck;
	
	private JPanel getPanel(Main main) {
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
				selectKspFolder(main);
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
					main.moduleManagerMod = newMm;
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
				main.moduleManagerMod = new Mod("Module Manager dll", defaultModuleManagerLink, true);
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
	
	private boolean askForKspFolder(Main main) {
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
			ex.printStackTrace();
		}
		return false;
	}
	
	public boolean selectKspFolder(Main main) {
		boolean ok = false;
		do {
			int reply = JOptionPane.showConfirmDialog(null, "Please, select your KSP main installation folder.", "KSP main folder", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
			if (reply == JOptionPane.OK_OPTION) {
				ok = askForKspFolder(main);
			} else {
				return false;
			}
		} while(ok == false);
		return true;
	}
	
	public void change(Main main) {
		JOptionPane.showMessageDialog(null, getPanel(main), "Config", JOptionPane.PLAIN_MESSAGE);
		this.excludeUnneededFiles = excludeFilesCheck.isSelected();
		this.excludeModuleManagerDll = excludeMmCheck.isSelected();
	}
}