package llorx.kspModManager.parse;

import llorx.kspModManager.Http;
import llorx.kspModManager.Mod;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class BitBucketDataParser {
    static void parseData(Mod mod, Connection.Response res) {
        try {
            res = Http.get(mod.getLink());
            Document doc = res.parse();
            Element linkElement = doc.select("a[id=repo-downloads-link]").first();
            if (linkElement != null) {
                mod.setLink(linkElement.attr("abs:href"));
                res = Http.get(mod.getLink());
                doc = res.parse();
                Element el = doc.select("a[class=repo-link]").first();
                if (el != null) {
                    String id = el.attr("href");
                    id = id.replace("/", "_");
                    if (id.substring(0, 1).equals("_")) {
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
            }
        } catch (Exception e) {
        }
    }
}