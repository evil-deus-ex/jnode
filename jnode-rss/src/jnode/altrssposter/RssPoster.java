package jnode.altrssposter;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import jnode.logger.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Manjago (kirill@temnenkov.com)
 */
public final class RssPoster {

    private static final Logger logger = Logger
            .getLogger(RssPoster.class);

    // simple test
    public static void main(String[] args) throws FileNotFoundException {
        String fileName = "/temp/data.txt";
        Watermarks watermarks = new Watermarks(fileName);
        System.out.println(getText("http://flibusta.net/polka/show/all/rss", watermarks, 4));
    }

    private static String fill(StringBuilder sb, String url, String watermark, int limit) {
        String lastWatermark = null;
        try {
            List<SyndEntry> entries = new ArrayList<SyndEntry>();
            lastWatermark = load(entries, url, watermark, limit);
            print(sb, entries);
        } catch (Exception e) {
            logger.l2("Some error happens while parsing " + url, e);
        }
        return lastWatermark;
    }

    public static StringBuilder getText(String url, Watermarks watermarks, int limit) throws FileNotFoundException {

        StringBuilder sb = new StringBuilder();
        String lastWatermark = null;
        final String watermark = watermarks.readValue(url);

        try {
            lastWatermark = fill(sb, url, watermark, limit);
        } catch (Exception e) {
            logger.l2("Some error happens while parsing " + url, e);
        }

        if (lastWatermark != null) {
            watermarks.storeValue(url, lastWatermark);
        }
        return sb;
    }

    private static String load(List<SyndEntry> entries, String url, String watermark, int limit) throws IOException, FeedException {
        String lastWatermark = null;
        SyndFeedInput feedInput = new SyndFeedInput();

        SyndFeed feed = feedInput.build(new XmlReader(new URL(url)));

        int max = limit > 0 ? Math.min(feed.getEntries().size(), limit) : feed.getEntries().size();
        for (int i = 0; i < max; ++i) {
            SyndEntry entry = (SyndEntry) feed.getEntries().get(i);

            if (entry.getUri().equals(watermark)) {
                break;
            }

            if (lastWatermark == null) {
                lastWatermark = entry.getUri();
            }

            entries.add(entry);
        }

        return lastWatermark;
    }

    private static void print(StringBuilder sb, List<SyndEntry> entries) {
        for (int i = entries.size() - 1; i >= 0; --i) {
            SyndEntry entry = entries.get(i);

            sb.append("*");
            sb.append(Cleaner.clean(entry.getTitle(), true));
            sb.append("*");
            sb.append("\n\n");
            sb.append(Cleaner.clean(entry.getDescription().getValue(), false));
            sb.append("\nRead more: ");
            sb.append(entry.getLink());
            sb.append("\n----------------------------------------------------------------------\n\n");
        }
    }

}
