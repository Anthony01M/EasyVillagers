package xyz.berrystudios.easyvillagers.util.misc;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import xyz.berrystudios.easyvillagers.EasyVillagers;

import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class MessageUtil {
    @Getter
    private final Random random = new Random();

    private final char[] colors = "0123456789abcdef".toCharArray();

    /**
     * Get a random int
     */
    public int randomInt(int bound) {
        return random.nextInt(bound);
    }

    /**
     * Format a string with colors to its color code
     *
     * @param string string to format
     * @return formatted string
     */
    public String format(String string) {
        string = formatWithNoColor(string);
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    /**
     * Translate hex codes to color codes (#FFFFFF or #ffffff work)
     *
     * @param message message to translate
     * @return translated message
     */
    private String translateHexCodes(String message) {
        final String hexPattern = "#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{8})";
        Matcher matcher = Pattern.compile(hexPattern).matcher(message);
        StringBuffer sb = new StringBuffer(message.length());
        while (matcher.find()) {
            String hex = matcher.group(1);
            net.md_5.bungee.api.ChatColor color = net.md_5.bungee.api.ChatColor.of("#" + hex);
            matcher.appendReplacement(sb, color.toString());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * Format a string with colors to without color code
     *
     * @param string string to format
     * @return formatted string without color code
     */
    public String formatWithNoColor(String string) {
        return string.replace("%prefix%", EasyVillagers.getInstance().getCfile().prefix);
    }

    /**
     * Get a random color
     */
    public String randomColor() {
        return "&" + colors[getRandom().nextInt(colors.length)];
    }

    /**
     * Get a random object from a list
     */
    public <T> T randomObject(List<T> list) {
        return list.get(randomInt(list.size()));
    }
}
