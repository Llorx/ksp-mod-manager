package llorx.kspModManager.parse;

import llorx.kspModManager.Http;
import llorx.kspModManager.Mod;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class KerbalSpacePartsDataParser {
    static void parseData(Mod mod, Connection.Response res) {
        try {
            res = Http.get(mod.getLink());
            Document doc = res.parse();
            Element el = doc.select("a[itemprop=downloadUrl]").first();
            if (el != null) {
				String downloadLink = el.attr("abs:href");
				mod.setDownloadLink(downloadLink);
				int index = downloadLink.indexOf("kerbal-space-parts.com/");
				if (index > -1) {
					index = index + 23;
					downloadLink = downloadLink.substring(index);
					index = downloadLink.lastIndexOf("/");
					if (index > -1) {
						String id = downloadLink.substring(0, index);
						if (id.length() > 0) {
							id = id.replace("/", "_");
							mod.setId(id);
							el = doc.select("span[itemprop=version]").first();
							if (el != null) {
								String version = el.text();
								if (version.length() > 0) {
									version = version + downloadLink.substring(index);
									mod.setVersion(version);
									mod.isValid = true;
								}
							}
						}
					}
				}
            }
        } catch (Exception e) {
        }
    }
}