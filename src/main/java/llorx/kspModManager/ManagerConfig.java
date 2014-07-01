package llorx.kspModManager;

import java.io.Externalizable;
import java.io.*;

import javax.swing.JSeparator;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

import javax.swing.border.EmptyBorder;

import java.awt.Font;
import java.awt.Dimension;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.lang.ProcessBuilder;

import java.util.ArrayList;
import javax.swing.ImageIcon;

public class ManagerConfig implements Externalizable {
	static final long serialVersionUID = 0;
	
	public static String kspDataFolder = "";
	public static String moduleManagerLink = "http://forum.kerbalspaceprogram.com/threads/55219";
	public static transient String defaultModuleManagerLink = "http://forum.kerbalspaceprogram.com/threads/55219";
	public static boolean excludeUnneededFiles = true;
	public static boolean excludeModuleManagerDll = true;
	public static int locale = -1;
	public static int mainWindowWidth = 500;
	public static int mainWindowHeight = 500;
	
	public static transient boolean localeSelected = false;
	private static transient JCheckBox excludeFilesCheck;
	private static transient JCheckBox excludeMmCheck;
	
	@Override
	public void writeExternal(ObjectOutput out) {
		try {
			out.writeObject(ManagerConfig.kspDataFolder);
			out.writeObject(ManagerConfig.moduleManagerLink);
			out.writeBoolean(ManagerConfig.excludeUnneededFiles);
			out.writeBoolean(ManagerConfig.excludeModuleManagerDll);
			if (ManagerConfig.localeSelected == false) {
				out.writeInt(-1);
			} else {
				out.writeInt(ManagerConfig.locale);
			}
			out.writeInt(mainWindowWidth);
			out.writeInt(mainWindowHeight);
		} catch (Exception e) {
		}
	}

	@Override
	public void readExternal(ObjectInput in) {
		try {
			ManagerConfig.kspDataFolder = (String)in.readObject();
			ManagerConfig.moduleManagerLink = (String)in.readObject();
			ManagerConfig.excludeUnneededFiles = in.readBoolean();
			ManagerConfig.excludeModuleManagerDll = in.readBoolean();
			ManagerConfig.locale = in.readInt();
			ManagerConfig.mainWindowWidth = in.readInt();
			ManagerConfig.mainWindowHeight = in.readInt();
		} catch (Exception e) {
		}
	}
	
	private static void selectLanguage() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		ButtonGroup group = new ButtonGroup();
		
		for (int i = 0; i < Strings.localeNames.length; i++) {
			JRadioButton b = new JRadioButton(Strings.localeNames[i]);
			b.setActionCommand(String.valueOf(i));
			panel.add(b);
			group.add(b);
			if (ManagerConfig.locale == i) {
				group.setSelected(b.getModel(), true);
			}
		}
		
		panel.add(new JSeparator(JSeparator.HORIZONTAL));
		
		JLabel bottom = new JLabel(Strings.get(Strings.CUSTOM_LANGUAGE_INSTRUCTIONS));
		panel.add(bottom);
		
		JOptionPane.showMessageDialog(null, panel, Strings.get(Strings.SELECT_LANGUAGE_TITLE), JOptionPane.PLAIN_MESSAGE);
		
		if (ManagerConfig.locale != Integer.parseInt(group.getSelection().getActionCommand())) {
			ManagerConfig.locale = Integer.parseInt(group.getSelection().getActionCommand());
			ManagerConfig.localeSelected = true;
			JOptionPane.showMessageDialog(null, Strings.get(Strings.LANGUAGE_CHANGED_WARN), Strings.get(Strings.SELECT_LANGUAGE_TITLE), JOptionPane.PLAIN_MESSAGE);
		}
	}
	
	private static JPanel getPanel() {
		JPanel panel = new JPanel(new GridLayout(11,1));
		
		JLabel titleTxt = new JLabel(Strings.get(Strings.CONFIG_MANAGER_TITLE));
		Font font = titleTxt.getFont().deriveFont(titleTxt.getFont().getSize2D()+5.0f);
		titleTxt.setFont(font);
		panel.add(titleTxt);
		
		panel.add(new JSeparator(JSeparator.HORIZONTAL));
		
		ImageIcon lang = new ImageIcon(ManagerConfig.class.getResource("/images/lang.png"));
		JButton changeLanguage = new JButton(Strings.get(Strings.CHANGE_LANGUAGE));
		changeLanguage.setIcon(lang);
		changeLanguage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ManagerConfig.selectLanguage();
			}
		});
		panel.add(changeLanguage);
		
		JButton changeKspDataBut = new JButton(Strings.get(Strings.CHANGE_KSP_FOLDER));
		changeKspDataBut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ManagerConfig.selectKspFolder();
			}
		});
		panel.add(changeKspDataBut);
		
		JButton changeMmLinkBut = new JButton(Strings.get(Strings.CHANGE_MM_LINK));
		changeMmLinkBut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Mod newMm;
				String newUrl;
				do {
					newMm = null;
					newUrl = JOptionPane.showInputDialog(null, Strings.get(Strings.CHANGE_MM_LINK_TEXT), ManagerConfig.moduleManagerLink);
					if (newUrl != null && newUrl.length() > 0) {
						newMm = new Mod("Module Manager dll", newUrl, true);
						if (!newMm.isValid) {
							JOptionPane.showMessageDialog(null, Strings.get(Strings.URL_NOT_VALID), Strings.get(Strings.ERROR_TITLE), JOptionPane.PLAIN_MESSAGE);
						}
					}
				} while ((newMm != null && !newMm.isValid));
				if (newMm != null && newMm.isValid) {
					ManagerConfig.moduleManagerLink = newUrl;
					JOptionPane.showMessageDialog(null, Strings.get(Strings.URL_UPDATED), Strings.get(Strings.UPDATED_TITLE), JOptionPane.PLAIN_MESSAGE);
				}
			}
		});
		panel.add(changeMmLinkBut);
		
		JButton restoreMmLinkBut = new JButton(Strings.get(Strings.RESTORE_MM_LINK));
		restoreMmLinkBut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ManagerConfig.moduleManagerLink = ManagerConfig.defaultModuleManagerLink;
				JOptionPane.showMessageDialog(null, Strings.get(Strings.URL_RESTORED), Strings.get(Strings.UPDATED_TITLE), JOptionPane.PLAIN_MESSAGE);
			}
		});
		panel.add(restoreMmLinkBut);
		
		
		excludeFilesCheck = new JCheckBox(Strings.get(Strings.EXCLUDE_FILES));
		excludeFilesCheck.setSelected(ManagerConfig.excludeUnneededFiles);
		excludeFilesCheck.setBorder(new EmptyBorder(5, 0, 5, 0));
		panel.add(excludeFilesCheck);
		
		panel.add(new JSeparator(JSeparator.HORIZONTAL));
		
		JLabel warnText = new JLabel(Strings.get(Strings.CONFIG_WARNING));
		warnText.setBorder(new EmptyBorder(5, 0, 0, 0));
		panel.add(warnText);
		
		excludeMmCheck = new JCheckBox(Strings.get(Strings.EXCLUDE_MM));
		excludeMmCheck.setSelected(ManagerConfig.excludeModuleManagerDll);
		excludeMmCheck.setBorder(new EmptyBorder(0, 0, 5, 0));
		panel.add(excludeMmCheck);
		
		panel.add(new JSeparator(JSeparator.HORIZONTAL));
		
		return panel;
	}
	
	private static boolean askForKspFolder() {
		try {
			JFileChooser j = new JFileChooser();
			j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int opt = j.showSaveDialog(null);
			if (opt == JFileChooser.APPROVE_OPTION) {
				String path = j.getSelectedFile().getCanonicalPath() + File.separator + "GameData";
				File f = new File(path);
				if (f.exists() && f.isDirectory()) {
					ManagerConfig.kspDataFolder = f.getCanonicalPath();
					return true;
				} else {
					JOptionPane.showMessageDialog(null, Strings.get(Strings.KSP_FOLDER_ERROR), Strings.get(Strings.ERROR_TITLE), JOptionPane.PLAIN_MESSAGE);
				}
			}
		} catch (Exception ex) {
			ErrorLog.log(ex);
		}
		return false;
	}
	
	public static boolean selectKspFolder() {
		boolean ok = false;
		do {
			int reply = JOptionPane.showConfirmDialog(null, Strings.get(Strings.SELECT_KSP_FOLDER), Strings.get(Strings.SELECT_KSP_FOLDER_TITLE), JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
			if (reply == JOptionPane.OK_OPTION) {
				ok = ManagerConfig.askForKspFolder();
			} else {
				return false;
			}
		} while(ok == false);
		return true;
	}
	
	public static void change() {
		JOptionPane.showMessageDialog(null, ManagerConfig.getPanel(), "Config", JOptionPane.PLAIN_MESSAGE);
		ManagerConfig.excludeUnneededFiles = excludeFilesCheck.isSelected();
		ManagerConfig.excludeModuleManagerDll = excludeMmCheck.isSelected();
	}
}