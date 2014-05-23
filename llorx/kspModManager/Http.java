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
				Connection.Response res = Jsoup.connect(link).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.131 Safari/537.36").method(Connection.Method.GET).followRedirects(false).execute();
				String newUrl = parseLocation(res.header("Location"), link);
				if (newUrl != null) {
					return Http.get(newUrl);
				} else {
					return res;
				}
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
					String filename = Http.parseFileHeader(conn.getHeaderField("Content-Disposition"), link, null);
					if (filename != null) {
						filename.toLowerCase();
						if (filename.endsWith(".zip")) {
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
	
	public static String parseFileHeader(String header, String link, String def) {
		String filename = null;
		if (header != null && header.indexOf("=") != -1) {
			filename = header.split("=")[1];
			if (filename.indexOf(";") > -1) {
				filename = header.split(";")[0];
			}
			int index = filename.indexOf("\"");
			if (index > -1) {
				int index2 = filename.indexOf("\"", index+1);
				if (index2 > -1) {
					filename = filename.substring(index+1, index2);
				}
			}
			index = filename.indexOf("'");
			if (index > -1) {
				int index2 = filename.indexOf("'", index+1);
				if (index2 > -1) {
					filename = filename.substring(index+1, index2);
				}
			}
		} else {
			int index = link.lastIndexOf("/");
			if (index > -1) {
				String f = link.substring(index+1);
				index = f.lastIndexOf("?");
				int index2 = f.lastIndexOf("#");
				if (index > -1) {
					if (index2 > -1 && index2 < index) {
						index = index2;
					}
					f = f.substring(0, index);
				} else if (index2 > -1) {
					f = f.substring(0, index2);
				}
				filename = f;
			}
		}
		if (filename == null) {
			filename = def;
		}
		filename = filename.replace("\\", "_");
		filename = filename.replace("/", "_");
		filename = filename.replace("\"", "");
		filename = filename.replace("'", "");
		return filename;
	}
	
	public static String parseLocation(String newLoc, String link) {
		if (newLoc != null) {
			if (newLoc.startsWith("//")) {
				newLoc = link.substring(0, link.indexOf("//")) + newLoc;
			} else if (newLoc.indexOf("://") == -1) {
				if (newLoc.startsWith("/")) {
					newLoc = link.substring(0, link.indexOf("/", link.indexOf("//")+2)) + newLoc;
				} else {
					newLoc = link.substring(0, link.lastIndexOf("/")+1) + newLoc;
				}
			}
		}
		return newLoc;
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
				conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.131 Safari/537.36");
				String newUrl = parseLocation(conn.getHeaderField("Location"), link);
				if (newUrl != null && newUrl.length() > 0) {
					return Http.getConnection(newUrl);
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
				} else if (link.indexOf("box.com/") > -1) {
					Element d = doc.select("ul[data-module=header-shared]").first();
					if (d != null) {
						String sharedName = d.attr("data-sharedname");
						String fileId = d.attr("data-fileid");
						if (!sharedName.equals("") && !fileId.equals("")) {
							System.out.println(Http.parseLocation("/index.php?rm=box_download_shared_file&shared_name="+sharedName+"&file_id=f_"+fileId, link));
							return Http.parseLocation("/index.php?rm=box_download_shared_file&shared_name="+sharedName+"&file_id=f_"+fileId, link);
						}
					}
				}
			} catch (Exception e) {
			}
		}
		return null;
	}
}