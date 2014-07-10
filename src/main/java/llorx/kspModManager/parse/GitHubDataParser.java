package llorx.kspModManager.parse;

import llorx.kspModManager.Http;
import llorx.kspModManager.Mod;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class GitHubDataParser {
    static void parseData(Mod mod, Connection.Response res) {
        try {
            res = Http.get(mod.getLink());
            Document doc = res.parse();
            Element el = doc.select("a[class=js-current-repository js-repo-home-link]").first();
            if (el != null) {
                mod.setLink(el.attr("abs:href") + "/releases/latest");
                res = Http.get(mod.getLink());
                doc = res.parse();
                String id = el.attr("href");
                id = id.replace("/", "_");
                if (id.substring(0, 1).equals("_")) {
                    id = id.substring(1);
                }
                if (id.length() > 0) {
                    mod.setId(id);
                    el = doc.select("time").first();
                    if (el == null) {
                        el = doc.select("local-time").first();
                    }
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
}