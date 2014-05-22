package llorx.kspModManager;

import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URL;
import java.net.HttpURLConnection;

import java.util.Map;
import java.util.List;

public class Http {
	public static final int HTML = -1;
	public static final int ZIP_EXTENSION = 0;
	public static final int OTHER_EXTENSION = 1;

	public static Connection.Response get(String link) {
		int tryouts = 0;
		while(tryouts < 10) {
			tryouts++;
			try {
				return Jsoup.connect(link).userAgent("LlorxKspModManager").method(Connection.Method.GET).followRedirects(true).execute();
			} catch (Exception e) {
			}
		}
		return null;
	}
	
	public static int fileType(String link) {
		HttpURLConnection conn = Http.getConnection(link);
		if (conn != null) {
			String type = conn.getHeaderField("Content-Type");
			if (type.indexOf("application/") > -1) {
				if (type.indexOf("application/zip") > -1 || type.indexOf("application/x-zip-compressed") > -1) {
					return Http.ZIP_EXTENSION;
				} else {
					String header = conn.getHeaderField("Content-Disposition");
					if (header != null && header.indexOf("=") != -1) {
						String filename = header.split("=")[1];
						filename = filename.replace("\\", "_");
						filename = filename.replace("/", "_");
						filename = filename.replace("\"", "");
						if (filename.lastIndexOf(".") > -1 && filename.substring(filename.lastIndexOf(".")).toLowerCase().equals(".zip")) {
							return Http.ZIP_EXTENSION;
						} else {
							return Http.OTHER_EXTENSION;
						}
					} else {
						int index = link.lastIndexOf(".");
						if (index > -1) {
							String ex = link.substring(index);
							index = ex.lastIndexOf("?");
							int index2 = ex.lastIndexOf("#");
							if (index > -1) {
								if (index2 > -1 && index2 < index) {
									index = index2;
								}
								ex = link.substring(0, index);
							} else if (index2 > -1) {
								ex = link.substring(0, index2);
							}
							if (ex.toLowerCase().equals(".zip")) {
								return Http.ZIP_EXTENSION;
							}
						}
						return Http.OTHER_EXTENSION;
					}
				}
			} else {
				return Http.HTML;
			}
		}
		return Http.HTML;
	}
	
	public static HttpURLConnection getConnection(String link) {
		int tryouts = 0;
		while(tryouts < 10) {
			link = link.replace("\\", "/");
			link = link.replace(" ", "%20");
			tryouts++;
			try {
				URL website = new URL(link);
				HttpURLConnection conn = (HttpURLConnection)website.openConnection();
				conn.setInstanceFollowRedirects(false);
				conn.setRequestProperty("User-Agent", "LlorxKspModManager");
				String newUrl = conn.getHeaderField("Location");
				if (newUrl != null && newUrl.length() > 0) {
					if (newUrl.startsWith("//")) {
						newUrl = link.substring(0, link.indexOf("//")) + newUrl;
					}
					return getConnection(newUrl);
				} else {
					return conn;
				}
			} catch (Exception e) {
			}
		}
		return null;
	}
	
	public static String getDownloadLink(String link) {
		Connection.Response res = Http.get(link);
		if (res != null) {
			try {
				Document doc = res.parse();
				if (link.indexOf("mediafire.com/") > -1) {
					Element d = doc.select("div[class=download_link]").first();
					if (d != null) {
						String body = d.html();
						int index = body.indexOf("\"http");
						if (index > -1) {
							index = index + 1;
							int index2 = body.indexOf("\"", index);
							if (index2 > -1) {
								String dlink = body.substring(index, index2);
								return dlink;
							}
						}
					}
				} else if (link.indexOf("dropbox.com/") > -1) {
					Element d = doc.select("a[id=default_content_download_button]").first();
					if (d != null) {
						String dlink = d.attr("href");
						if (!dlink.equals("")) {
							return dlink;
						}
					}
				} else if (link.indexOf("cubby.com/pli/") > -1) {
					return link.replace("/pli/", "/pl/");
				}
			} catch (Exception e) {
			}
		}
		return null;
	}
}