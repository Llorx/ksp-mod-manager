package llorx.kspModManager.parse;

import llorx.kspModManager.Http;
import llorx.kspModManager.Mod;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class KspForumDataParser {
    static void parseData(Mod mod, Connection.Response res) {
        try {
            int index = mod.getLink().indexOf("-");
            if (index > -1) {
                mod.setLink(mod.getLink().substring(0, index));
            }
            res = Http.get(mod.getLink());
            Document doc = res.parse();
            Element idElement = doc.select("input[name=searchthreadid]").first();
            if (idElement == null) {
                idElement = doc.select("input[name=t]").first();
            }
            if (idElement != null) {
                String id = idElement.attr("value");
                mod.setId(id);
                String serverDate = res.header("Date");
                SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
                Date d = format.parse(serverDate);
                Element time = doc.select("div[id=footer_time]").first();
                String gmt = "GMT-5";
                if (time != null) {
                    String tim = time.text();
                    index = tim.indexOf("GMT");
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
                            index = editedDate.indexOf(";");
                            if (index > -1) {
                                index = index + 1;
                                int index2 = editedDate.indexOf(".", index);
                                if (index2 > -1) {
                                    editedDate = editedDate.substring(index, index2).trim();


                                    String[] suffixes =
                                            //  0     1     2     3     4     5     6     7     8     9
                                            {"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                                                    //  10    11    12    13    14    15    16    17    18    19
                                                    "th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
                                                    //  20    21    22    23    24    25    26    27    28    29
                                                    "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                                                    //  30    31
                                                    "th", "st"};
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
                            mod.setVersion("First version (Post not edited)");
                        }
                        if (!mod.getVersion().equals("")) {
                            mod.isValid = true;
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
    }
}