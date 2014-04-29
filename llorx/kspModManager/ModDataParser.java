package llorx.kspModManager;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import java.io.*;
import java.util.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Connection.Response;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JOptionPane;
import javax.swing.ButtonGroup;
import javax.swing.BoxLayout;

import java.awt.Font;
import java.awt.Desktop;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.net.URI;

import java.lang.Thread;

public class ModDataParser {
	public static void parseModData(Mod mod, Response res) {
		switch(mod.getType()) {
			case Mod.TYPE_SPACEPORT:
				parseSpaceportData(mod, res);
				break;
			case Mod.TYPE_KSPFORUM:
				parseKspForumData(mod, res);
				break;
			case Mod.TYPE_JENKINS:
				parseJenkinsData(mod, res);
				break;
			case Mod.TYPE_GITHUB:
				parseGitHubData(mod, res);
				break;
			case Mod.TYPE_BITBUCKET:
				parseBitBucketData(mod, res);
				break;
			case Mod.TYPE_DROPBOX_FOLDER:
				parseDropboxFolderData(mod, res);
				break;
			default:
				parseDefaultData(mod);
				break;
		}
	}
	
	private static void parseSpaceportData(Mod mod, Response res) {
		try {
			Document doc = res.parse();
			Element el = doc.select("input[name=addonid]").first();
			if (el != null) {
				String id = el.attr("value");
				if (id.length() > 0) {
					mod.setId(id);
					mod.setDownloadLink(getSpaceportDownloadUrl(mod.getUnprefixedId()));
					mod.setVersion(mod.getDownloadLink());
					int vIndex = mod.getVersion().indexOf("?f=uploads/");
					if (vIndex > -1) {
						mod.setVersion(mod.getVersion().substring(vIndex+11));
					} else {
						vIndex = mod.getVersion().indexOf("?f=");
						if (vIndex > -1) {
							mod.setVersion(mod.getVersion().substring(vIndex+3));
						}
					}
					mod.isValid = true;
				}
			}
		} catch (Exception e) {
		}
	}
	
	private static void parseKspForumData(Mod mod, Response res) {
		try {
			Document doc = res.parse();
			Element idElement = doc.select("input[name=t]").first();
			if (idElement != null) {
				String id = idElement.attr("value");
				mod.setId(id);
				String serverDate = res.header("Date");
				java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
				Date d = format.parse(serverDate);
				Element time = doc.select("div[id=footer_time]").first();
				String gmt = "GMT-5";
				if (time != null) {
					String tim = time.text();
					int index = tim.indexOf("GMT");
					if (index > -1) {
						int index2 = tim.indexOf(".", index);
						if (index2 > -1) {
							gmt = tim.substring(index, index2).replace(" ", "");
						}
					}
				}
				Calendar date = Calendar.getInstance(TimeZone.getTimeZone(gmt));
				date.setTime(d);
				
				Element posts = doc.select("ol[id=posts]").first();
				if (posts != null) {
					Element post = posts.select("li[id^=post_]").first();
					if (post != null) {
						Element edited = post.select("blockquote[class*=lastedited]").first();
						if (edited != null) {
							String editedDate = edited.text();
							int index = editedDate.indexOf(";");
							if (index > -1) {
								index = index + 1;
								int index2 = editedDate.indexOf(".", index);
								if (index2 > -1) {
									editedDate = editedDate.substring(index, index2).trim();
									
									
									String[] suffixes =
										//  0     1     2     3     4     5     6     7     8     9
										 { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
									    //  10    11    12    13    14    15    16    17    18    19
										   "th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
									    //  20    21    22    23    24    25    26    27    28    29
										   "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
									    //  30    31
										   "th", "st" };
									String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
									
									int day = date.get(Calendar.DAY_OF_MONTH);
									int month = date.get(Calendar.MONTH);
									editedDate = editedDate.replace("Today", day + suffixes[day] + " " + monthNames[month] + " " + date.get(Calendar.YEAR));
									date.add(Calendar.DATE, -1);
									day = date.get(Calendar.DAY_OF_MONTH);
									month = date.get(Calendar.MONTH);
									editedDate = editedDate.replace("Yesterday", day + suffixes[day] + " " + monthNames[month] + " " + date.get(Calendar.YEAR));
									mod.setVersion(editedDate + " GMT-5");
								}
							}
						} else {
							mod.setVersion("First post not edited. Version 0.");
						}
						if (!mod.getVersion().equals("")) {
							mod.isValid = true;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static void parseJenkinsData(Mod mod, Response res) {
		
	}
	private static void parseGitHubData(Mod mod, Response res) {
		try {
			Document doc = res.parse();
			Element el = doc.select("a[class=js-current-repository js-repo-home-link]").first();
			if (el != null) {
				String id = el.attr("href");
				id.replace("/", "_");
				if (id.substring(0,1).equals("_")) {
					id = id.substring(1);
				}
				if (id.length() > 0) {
					el = doc.select("local-time").first();
					if (el != null) {
						String version = el.attr("datetime");
						mod.setVersion(version);
						el = doc.select("ul[class=release-downloads]").first();
						if (el != null) {
							el = el.select("a[class=button primary]").first();
							if (el != null) {
								String download = el.attr("abs:href");
								mod.setDownloadLink(download);
								mod.isValid = true;
							}
						}
					}
				}
			}
		} catch (Exception e) {
		}
	}
	private static void parseBitBucketData(Mod mod, Response res) {
		try {
			Document doc = res.parse();
			Element el = doc.select("a[class=repo-link]").first();
			if (el != null) {
				String id = el.attr("href");
				id.replace("/", "_");
				if (id.substring(0,1).equals("_")) {
					id = id.substring(1);
				}
				if (id.length() > 0) {
					mod.setId(id);
					Element table = doc.select("table[id=uploaded-files]").first();
					if (table != null) {
						Element lastDownload = table.select("tr[id^=download-]").first();
						if (lastDownload != null) {
							Element data = lastDownload.select("td[class=date]").first();
							if (data != null) {
								data = data.select("time").first();
								if (data != null) {
									String version = data.attr("datetime");
									mod.setVersion(version);
									Element dlink = lastDownload.select("td[class=name]").first();
									if (dlink != null) {
										dlink = dlink.select("a").first();
										if (dlink != null) {
											String link = dlink.attr("abs:href");
											if (!link.equals("")) {
												mod.setDownloadLink(link);
												mod.isValid = true;
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
		}
	}
	private static void parseDropboxFolderData(Mod mod, Response res) {
		
	}
	private static void parseDefaultData(Mod mod) {
		
	}
	
	public static String getDownloadLink(Mod mod) {
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
							links = post.select("a[href*=.zip]");
						}
					}
					break;
				default:
					links = doc.select("a[href*=.zip]");
					break;
			}
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
			JLabel titleLabel = new JLabel(mod.getName());
			JLabel testLabel;
			if (links != null && links.size() > 1) {
				testLabel = new JLabel("Multiple links detected. What file do you want to install?");
			} else if (links != null && links.size() == 1) {
				testLabel = new JLabel("This file was detected, do you want to install it?");
			} else {
				testLabel = new JLabel("No files detected. Download a file manually from the website.");
			}
			Font font = testLabel.getFont();
			Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize()+2);
			titleLabel.setFont(boldFont);
			testLabel.setFont(boldFont);
			panel.add(titleLabel);
			panel.add(testLabel);
			ButtonGroup group = new ButtonGroup();
			boolean firstSelected = false;
			String downloadLink = "";
			Browser browser = new Browser();
			if (links != null && links.size() > 0) {
				for (int i = 0; i < links.size(); i++) {
					JRadioButton b = new JRadioButton(links.get(i).attr("href"));
					b.setActionCommand(links.get(i).attr("href"));
					panel.add(b);
					group.add(b);
					if (firstSelected == false) {
						firstSelected = true;
						group.setSelected(b.getModel(), true);
					}
				}
				int reply = JOptionPane.showOptionDialog(null, panel, "Install file", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[]{"Download selected file", "Open website and download file from there"}, null);
				if (reply == JOptionPane.YES_OPTION) {
					downloadLink = group.getSelection().getActionCommand();
				} else {
					browser.show(mod.getLink());
				}
			} else {
				JOptionPane.showOptionDialog(null, panel, "Install file", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[]{"Open website and download file from there"}, null);
				browser.show(mod.getLink());
			}
			if (!browser.downloadFile.equals("")) {
				downloadLink = browser.downloadFile;
			}
			return downloadLink;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static String getSpaceportDownloadUrl(String id) {
		try {
			URL url = new URL("http://kerbalspaceport.com/wp/wp-admin/admin-ajax.php");
			
			URLConnection urlConn = url.openConnection();
			urlConn.setDoInput(true);
			urlConn.setDoOutput(true);
			urlConn.setUseCaches(false);
			urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			urlConn.setRequestProperty("User-Agent", "LlorxKspModManager");
			urlConn.setRequestProperty("Origin", "http://kerbalspaceport.com");
			urlConn.setRequestProperty("Referer", "http://kerbalspaceport.com/?p=" + id);
			
			DataOutputStream printout = new DataOutputStream (urlConn.getOutputStream());
			String content = "addonid=" + id + "&send=" + URLEncoder.encode("Download Now!", "UTF-8") + "&action=downloadfileaddon";
			printout.writeBytes(content);
			printout.flush();
			printout.close();
			
			BufferedReader input = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
			String str;
			String data = "";
			while (null != ((str = input.readLine()))) {
				data = data + str;
			}
			input.close();
			return data;
		} catch (Exception e) {
		}
		return null;
	}
}