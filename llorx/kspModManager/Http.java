package llorx.kspModManager;

import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URL;
import java.net.HttpURLConnection;

public class Http {
	public static Connection.Response get(String link) {
		int tryouts = 0;
		while(tryouts < 10) {
			tryouts++;
			try {
				return Jsoup.connect(link).method(Connection.Method.GET).followRedirects(true).execute();
			} catch (Exception e) {
			}
		}
		return null;
	}
	
	public static boolean isFile(String link) {
		HttpURLConnection conn = Http.getConnection(link);
		if (conn != null) {
			String type = conn.getHeaderField("Content-Type");
			return (type.indexOf("application/") > -1);
		}
		return false;
	}
	
	public static HttpURLConnection getConnection(String link) {
		int tryouts = 0;
		while(tryouts < 10) {
			tryouts++;
			try {
				URL website = new URL(link);
				HttpURLConnection conn = (HttpURLConnection)website.openConnection();
				conn.addRequestProperty("User-Agent", "LlorxKspModManager");
				int status = conn.getResponseCode();
				boolean redirect = false;
				if (status != HttpURLConnection.HTTP_OK) {
					if (status == HttpURLConnection.HTTP_MOVED_TEMP
						|| status == HttpURLConnection.HTTP_MOVED_PERM
							|| status == HttpURLConnection.HTTP_SEE_OTHER)
					redirect = true;
				}
				if (redirect) {
					String newUrl = conn.getHeaderField("Location");
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
					Element d = doc.select("id[default_content_download_button]").first();
					if (d != null) {
						String dlink = d.attr("href");
						if (!dlink.equals("")) {
							return dlink;
						}
					}
				}
			} catch (Exception e) {
			}
		}
		return null;
	}
}