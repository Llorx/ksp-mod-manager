package llorx.kspModManager.parse;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import java.io.*;

import llorx.kspModManager.Browser;
import llorx.kspModManager.Http;
import llorx.kspModManager.Mod;
import llorx.kspModManager.Strings;
import llorx.kspModManager.ErrorLog;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Connection.Response;
/*import org.slf4j.Logger;
import org.slf4j.LoggerFactory;*/

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JOptionPane;
import javax.swing.ButtonGroup;
import javax.swing.BoxLayout;

import java.awt.Font;

public class ModDataParser {
    //private static Logger LOGGER = LoggerFactory.getLogger(ModDataParser.class);

    public static void parseModData(Mod mod, Response res) {
		switch(mod.getType()) {
			case Mod.TYPE_SPACEPORT:
                SpaceportParser.parseSpaceportData(mod, res);
				break;
			case Mod.TYPE_KSPFORUM:
                KspForumDataParser.parseKspForumData(mod, res);
				break;
			case Mod.TYPE_JENKINS:
                JenkinsDataParser.parseJenkinsData(mod, res);
				break;
			case Mod.TYPE_GITHUB:
                GitHubDataParser.parseGitHubData(mod, res);
				break;
			case Mod.TYPE_BITBUCKET:
                BitBucketDataParser.parseBitBucketData(mod, res);
				break;
			case Mod.TYPE_DROPBOX_FOLDER:
				parseDropboxFolderData(mod, res);
				break;
			case Mod.TYPE_CURSEFORGE:
                CurseForgeDataParser.parseCurseForgeData(mod, res);
				break;
			case Mod.TYPE_CURSE:
                CurseDataParser.parseCurseData(mod, res);
				break;
			default:
				parseDefaultData(mod);
				break;
		}
	}

    private static void parseDropboxFolderData(Mod mod, Response res) {
		
	}
	private static void parseDefaultData(Mod mod) {
		
	}
	
	public static String getDownloadLink(Mod mod) {
		if (!mod.getDownloadLink().equals("")) {
			return mod.getDownloadLink();
		}
		try {
			Response res = Http.get(mod.getLink());
			Document doc = res.parse();
			Elements links = null;
			
			switch(mod.getType()) {
				case Mod.TYPE_KSPFORUM:
					Element posts = doc.select("ol[id=posts]").first();
					if (posts != null) {
						Element post = posts.select("li[id^=post_]").first();
						if (post != null) {
							links = post.select("a[href*=.zip],a[href*=mediafire.com/],a[href*=cubby.com/],a[href*=.box.com/],a[href*=/box.com/]");
						}
					}
					break;
				default:
					links = doc.select("a[href*=.zip],a[href*=mediafire.com/],a[href*=cubby.com/],a[href*=.box.com/],a[href*=/box.com/]");
					break;
			}
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
			JLabel titleLabel = new JLabel(mod.getName());
			JLabel testLabel;
			if (links != null && links.size() > 1) {
				testLabel = new JLabel(Strings.get(Strings.MULTIPLE_LINKS_ASK));
			} else if (links != null && links.size() == 1) {
				testLabel = new JLabel(Strings.get(Strings.ONE_LINK_DETECTED));
			} else {
				testLabel = new JLabel(Strings.get(Strings.NO_LINK_DETECTED));
			}
			Font font = testLabel.getFont();
			Font boldFont1 = new Font(font.getFontName(), Font.BOLD, font.getSize()+2);
			Font boldFont2 = new Font(font.getFontName(), Font.BOLD, font.getSize()+1);
			titleLabel.setFont(boldFont1);
			testLabel.setFont(boldFont2);
			panel.add(titleLabel);
			panel.add(testLabel);
			ButtonGroup group = new ButtonGroup();
			boolean firstSelected = false;
			String downloadLink = "";
			Browser browser = new Browser();
			if (links != null && links.size() > 0) {
				for (int i = 0; i < links.size(); i++) {
					String txt = links.get(i).attr("href");
					if (!links.get(i).text().startsWith("http:") && !links.get(i).text().startsWith("https:")) {
						txt = links.get(i).text() + " - " + txt;
					}
					JRadioButton b = new JRadioButton(txt);
					b.setActionCommand(links.get(i).attr("href"));
					panel.add(b);
					group.add(b);
					if (firstSelected == false) {
						firstSelected = true;
						group.setSelected(b.getModel(), true);
					}
				}
				int reply = JOptionPane.showOptionDialog(null, panel, Strings.get(Strings.INSTALL_FILE_TITLE), JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[]{Strings.get(Strings.DOWNLOAD_SELECTED_BUTTON), Strings.get(Strings.OPEN_BROWSER_DOWNLOAD)}, null);
				if (reply == JOptionPane.YES_OPTION) {
					downloadLink = group.getSelection().getActionCommand();
				} else {
					browser.show(mod.getLink(), mod);
				}
			} else {
				JOptionPane.showOptionDialog(null, panel, Strings.get(Strings.INSTALL_FILE_TITLE), JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[]{Strings.get(Strings.OPEN_BROWSER_DOWNLOAD)}, null);
				browser.show(mod.getLink(), mod);
			}
			if (browser.downloadFile != null) {
				downloadLink = browser.downloadFile.getURL().toExternalForm();
			}
			if (browser.modReloaded == true) {
				downloadLink = getDownloadLink(mod);
			}
			return downloadLink;
		} catch (Exception e) {
			//LOGGER.error("Error when trying to find download link", e);
			ErrorLog.log(e);
		}
		return "";
	}

}