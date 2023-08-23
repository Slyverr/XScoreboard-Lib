package com.slyvr.scoreboard;

import com.google.common.base.Preconditions;
import com.slyvr.scoreboard.utils.ScoreboardUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a scoreboard.
 *
 * @since 1.0.0
 */
public final class Scoreboard {

    private final org.bukkit.scoreboard.Scoreboard board;
    private final ScoreboardLine[] lines;
    private final ScoreboardTitle title;
    private final AtomicInteger index;
    private DisplaySlot slot;

    /**
     * Constructs a new scorebaord.
     *
     * @param title The title of the scoreboard.
     * @param slot  The display-slot of the scoreboard.
     *
     * @throws NullPointerException If the given title or slot is null.
     */
    public Scoreboard(@NotNull ScoreboardTitle title, @NotNull DisplaySlot slot) {
        Preconditions.checkNotNull(title, "Scoreboard's title cannot be null!");
        Preconditions.checkNotNull(slot, "Scoreboard's display-slot cannot be null!");

        this.lines = new ScoreboardLine[15];
        this.title = title;
        this.slot = slot;

        this.index = new AtomicInteger(0);
        this.board = ScoreboardUtils.newScoreboard(slot, title.getTitle(0));
    }

    /**
     * Constructs a new scoreboard.
     *
     * @param title The title of the scoreboard.
     *
     * @throws NullPointerException If the given title is null.
     */
    public Scoreboard(@NotNull ScoreboardTitle title) {
        this(title, DisplaySlot.SIDEBAR);
    }

    /**
     * Gets the title of this scoreboard.
     *
     * @return The title of this scoreboard
     */
    @NotNull
    public ScoreboardTitle getScoreboardTitle() {
        return title;
    }

    /**
     * Gets the currently displayed title of this scoreboard.
     *
     * @return The currently displayed title
     */
    @NotNull
    public String getCurrentTitle() {
        return board.getObjective("Scoreboard").getDisplayName();
    }

    /**
     * Sets the next title for this scoreboard.
     */
    public void setNextTitle() {
        int current = index.getAndIncrement();
        if (current >= title.size() - 1)
            index.set(0);

        this.board.getObjective("Scoreboard").setDisplayName(title.getTitle(current));
    }

    /**
     * Gets the display-slot of this scoreboard.
     *
     * @return The display-slot of this scoreboard
     */
    @NotNull
    public DisplaySlot getDisplaySlot() {
        return slot;
    }

    /**
     * Sets the display-slot for this scoreboard.
     *
     * @param slot The display-slot to set.
     */
    public void setDisplaySlot(@NotNull DisplaySlot slot) {
        if (slot != null)
            this.slot = slot;
    }

    /**
     * Gets the text at the given line.
     *
     * @param line The line number.
     *
     * @return The text at the given line, or null if none
     */
    @Nullable
    public String getText(int line) {
        if (!ScoreboardUtils.isValidLine(line))
            return null;

        ScoreboardLine sbLine = lines[line - 1];
        return sbLine != null ? sbLine.text : null;
    }

    /**
     * Sets the text at the given line.
     *
     * @param line The line number.
     * @param text The text to set.
     */
    public void setText(int line, @NotNull String text) {
        if (text == null || !ScoreboardUtils.isValidLine(line))
            return;

        ScoreboardLine board_line = lines[line - 1];
        if (board_line == null)
            lines[line - 1] = (board_line = new ScoreboardLine());

        board_line.setText(text, line);
    }

    /**
     * Displays the scoreboard to the given player.
     *
     * @param player The player to display the scoreboard for.
     */
    public void setScoreboard(@NotNull Player player) {
        if (player != null)
            player.setScoreboard(board);
    }

    /**
     * Represents a scoreboard-line.
     *
     * @since 1.0
     */
    private final class ScoreboardLine {

        private String text;
        private Team team;

        /**
         * Constructs a new scoreboard-line.
         */
        public ScoreboardLine() {
        }

        /**
         * Sets the text to display.
         *
         * @param text The text to set.
         */
        public void setText(@NotNull String text, int line) {
            ScoreboardUtils.setText(getTeam(line), text);
        }

        @NotNull
        private Team getTeam(int line) {
            if (team != null)
                return team;

            Objective objective = board.getObjective("Scoreboard");
            String entry = getLineEntry(line);

            this.team = board.registerNewTeam("Line " + line);
            this.team.addEntry(entry);

            objective.getScore(entry).setScore(line);
            return team;
        }

        /**
         * Gets the valid entry to use depending on the line number.
         *
         * @param line The number of the line.
         *
         * @return The entry to use for the given line
         */
        @NotNull
        private String getLineEntry(int line) {
            switch (line) {
                case 1:
                    return ChatColor.WHITE + String.valueOf(ChatColor.RESET);
                case 2:
                    return ChatColor.DARK_BLUE + String.valueOf(ChatColor.RESET);
                case 3:
                    return ChatColor.DARK_GREEN + String.valueOf(ChatColor.RESET);
                case 4:
                    return ChatColor.DARK_AQUA + String.valueOf(ChatColor.RESET);
                case 5:
                    return ChatColor.DARK_RED + String.valueOf(ChatColor.RESET);
                case 6:
                    return ChatColor.DARK_PURPLE + String.valueOf(ChatColor.RESET);
                case 7:
                    return ChatColor.DARK_GRAY + String.valueOf(ChatColor.RESET);
                case 8:
                    return ChatColor.GOLD + String.valueOf(ChatColor.RESET);
                case 9:
                    return ChatColor.GRAY + String.valueOf(ChatColor.RESET);
                case 10:
                    return ChatColor.BLUE + String.valueOf(ChatColor.RESET);
                case 11:
                    return ChatColor.GREEN + String.valueOf(ChatColor.RESET);
                case 12:
                    return ChatColor.AQUA + String.valueOf(ChatColor.RESET);
                case 13:
                    return ChatColor.RED + String.valueOf(ChatColor.RESET);
                case 14:
                    return ChatColor.YELLOW + String.valueOf(ChatColor.RESET);
                case 15:
                    return ChatColor.BLACK + String.valueOf(ChatColor.RESET);
            }

            return String.valueOf(ChatColor.RESET);
        }

    }

}