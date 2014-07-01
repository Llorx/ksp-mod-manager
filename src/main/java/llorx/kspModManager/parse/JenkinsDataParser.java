package llorx.kspModManager.parse;

import llorx.kspModManager.Mod;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JenkinsDataParser {
    static void parseJenkinsData(Mod mod, Connection.Response res) {
        try {
            Document doc = res.parse();
            String id = mod.getLink().replace("/lastSuccessfulBuild", "").replace("/", "_");
            mod.setId(id);
            Element el = doc.select("lastBuiltRevision").first();
            if (el != null) {
                el = el.select("SHA1").first();
                if (el != null) {
                    String version = el.text();
                    if (!version.equals("")) {
                        mod.setVersion(version);
                        Elements els = doc.select("artifact");
                        if (els != null) {
                            for (int i = 0; i < els.size(); i++) {
                                el = els.get(i).select("relativePath").first();
                                if (el != null) {
                                    String link = el.text();
                                    if (link.indexOf(".zip") > -1) {
                                        mod.setDownloadLink(mod.getLink() + "/artifact/" + link);
                                        mod.isValid = true;
                                        break;
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