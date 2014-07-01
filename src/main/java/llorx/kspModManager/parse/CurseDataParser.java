package llorx.kspModManager.parse;

import llorx.kspModManager.Http;
import llorx.kspModManager.Mod;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurseDataParser {
    static void parseCurseData(Mod mod, Connection.Response res) {
        try {
            Pattern pattern = Pattern.compile("(.*)(curse.com\\/)([^\\/]*)(\\/kerbal\\/)(\\d*)([^\\/]*)");
            Matcher matcher = pattern.matcher(mod.getLink());
            if (matcher.find()) {
                mod.setLink(matcher.group(0));
                res = Http.get(mod.getLink());
                Document doc = res.parse();
                int index = mod.getLink().lastIndexOf("/");
                if (index > -1) {
                    String id = "";
                    index = index + 1;
                    int index2 = mod.getLink().indexOf("-", index);
                    int index3 = mod.getLink().indexOf("/", index);
                    if (index3 > -1 && index3 < index2) {
                        index2 = index3;
                    }
                    if (index2 > -1) {
                        id = mod.getLink().substring(index, index2);
                    } else {
                        if (index3 > -1) {
                            index2 = index3;
                            id = mod.getLink().substring(index, index2);
                        } else {
                            id = mod.getLink().substring(index);
                        }
                    }
                    if (id.length() > 0) {
                        mod.setId(id);
                        Element el = doc.select("div[class=download-options]").first();
                        if (el != null) {
                            Element linkEl = el.select("a[href*=download]").first();
                            if (linkEl != null) {
                                String link = linkEl.attr("abs:href");
                                if (link.length() > 0) {
                                    el = doc.select("li[class=updated]").first();
                                    if (el != null) {
                                        el = el.select("abbr").first();
                                        if (el != null) {
                                            String version = el.attr("data-epoch");
                                            if (version.length() > 0) {
                                                mod.setVersion(version);
                                                res = Http.get(link);
                                                doc = res.parse();
                                                el = doc.select("a[class=download-link]").first();
                                                if (el != null) {
                                                    link = el.attr("abs:data-href");
                                                    if (link.length() > 0) {
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
            }
        } catch (Exception e) {
        }
    }
}