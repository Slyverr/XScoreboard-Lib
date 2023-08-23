package com.slyvr.scoreboard.utils;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.NumberConversions;
import org.jetbrains.annotations.NotNull;

/**
 * Utilities for bukkit-scoreboards.
 *
 * @since 1.0.0
 */
public final class ScoreboardUtils {

    public static final int MAXIMUM_SCOREBOARD_TEXT_LENGTH;
    public static final int MAXIMUM_SCOREBOARD_TITLE_LENGTH;

    private static final int MAXIMUM_SCOREBOARD_TEAM_TEXT_LENGTH;

    static {
        String nms_version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        nms_version = nms_version.replaceAll("[^0-9]", "");

        MAXIMUM_SCOREBOARD_TEXT_LENGTH = NumberConversions.toInt(nms_version) >= 1131 ? 128 : 32;
        MAXIMUM_SCOREBOARD_TITLE_LENGTH = NumberConversions.toInt(nms_version) >= 1131 ? 128 : 32;

        MAXIMUM_SCOREBOARD_TEAM_TEXT_LENGTH = MAXIMUM_SCOREBOARD_TEXT_LENGTH / 2;
    }

    private ScoreboardUtils() {
    }

    /**
     * Creates a new empty bukkit-scoreboard.
     *
     * @param slot  The display-slot for the scoreboard.
     * @param title The display-title for the scoreboard.
     *
     * @return The created bukkit-scoreboard instance
     */
    @NotNull
    public static Scoreboard newScoreboard(@NotNull DisplaySlot slot, @NotNull String title) {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective obj = board.registerNewObjective("Scoreboard", "dummy");
        obj.setDisplayName(title.length() > MAXIMUM_SCOREBOARD_TITLE_LENGTH ? title.substring(0, MAXIMUM_SCOREBOARD_TITLE_LENGTH) : title);
        obj.setDisplaySlot(slot);

        return board;
    }

    /**
     * Sets the given text to the scoreboard-team.
     *
     * @param team The team to sets the text to.
     * @param text The text to set.
     *
     * @throws NullPointerException If the given team or text is null.
     */
    public static void setText(@NotNull Team team, @NotNull String text) {
        Preconditions.checkNotNull(team, "Scoreboard's team cannot be null!");
        Preconditions.checkNotNull(text, "Scoreboard's text cannot be null!");

        int maximum_length = MAXIMUM_SCOREBOARD_TEAM_TEXT_LENGTH;
        int text_length = text.length();

        if (text_length <= maximum_length) {
            team.setPrefix(!text.isEmpty() && text.charAt(text_length - 1) == ChatColor.COLOR_CHAR ? text.substring(0, text_length - 1) : text);
            team.setSuffix("");
            return;
        }

        StringBuilder prefix_builder = new StringBuilder(text.substring(0, maximum_length));
        StringBuilder suffix_builder = new StringBuilder(text.substring(maximum_length));

        int last_index = maximum_length - 1;
        if (prefix_builder.charAt(last_index) == ChatColor.COLOR_CHAR) {
            prefix_builder.deleteCharAt(last_index);
            suffix_builder.deleteCharAt(0);
        }

        String last_color = ChatColor.getLastColors(text.substring(0, maximum_length + 1));
        if (!last_color.isEmpty())
            suffix_builder.insert(0, last_color);

        team.setPrefix(prefix_builder.toString());
        team.setSuffix(suffix_builder.substring(0, Math.min(suffix_builder.length(), maximum_length)));
    }

    /**
     * Checks if the given number is a valid scoreboard's line (Between 1 and 15).
     *
     * @param line The number to check.
     *
     * @return True if the given number is a valid line, otherwise false
     */
    public static boolean isValidLine(int line) {
        return line >= 1 && line <= 15;
    }

}