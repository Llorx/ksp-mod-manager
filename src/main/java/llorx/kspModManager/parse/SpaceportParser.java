package llorx.kspModManager.parse;

import llorx.kspModManager.Http;
import llorx.kspModManager.Mod;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class SpaceportParser {
    static void parseData(Mod mod, Connection.Response res) {
        try {
            res = Http.get(mod.getLink());
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
                        mod.setVersion(mod.getVersion().substring(vIndex + 11));
                    } else {
                        vIndex = mod.getVersion().indexOf("?f=");
                        if (vIndex > -1) {
                            mod.setVersion(mod.getVersion().substring(vIndex + 3));
                        }
                    }
                    mod.isValid = true;
                }
            }
        } catch (Exception e) {
        }
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