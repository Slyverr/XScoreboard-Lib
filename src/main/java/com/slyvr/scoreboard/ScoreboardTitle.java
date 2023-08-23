package com.slyvr.scoreboard;

import com.google.common.base.Preconditions;
import com.slyvr.scoreboard.utils.ScoreboardUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a scoreboard-title.
 *
 * @since 1.0.0
 */
public final class ScoreboardTitle {

    private final List<String> titles;
    private final long wait;

    /**
     * Constructs a new scoreboard-title.
     *
     * @param titles The list containing the names.
     * @param wait   The time to wait by the scoreboard before updating to the next title.
     *
     * @throws NullPointerException     If the given titles-list or a title is null.
     * @throws IllegalArgumentException If the given titles-list is empty!
     */
    public ScoreboardTitle(@NotNull List<String> titles, long wait) {
        Preconditions.checkNotNull(titles, "Scoreboard's titles-list cannot be null!");
        Preconditions.checkArgument(!titles.isEmpty(), "Scoreboard's titles-list cannot be empty!");

        List<String> result = new ArrayList<>(titles.size());
        for (String title : titles) {
            if (title == null)
                throw new NullPointerException("Scoreboard's title cannot be null!");

            if (title.length() > ScoreboardUtils.MAXIMUM_SCOREBOARD_TITLE_LENGTH)
                throw new IllegalArgumentException("Scoreboard's title cannot be longer than " + ScoreboardUtils.MAXIMUM_SCOREBOARD_TITLE_LENGTH + "!");

            result.add(title);
        }

        this.titles = result;
        this.wait = result.size() > 1 ? Math.max(wait, 5) : -1;
    }

    /**
     * Gets a copy of the titles-list of this scoreboard-title.
     *
     * @return A copy of the titles-list of this scoreboard-title
     */
    @NotNull
    public List<String> getTitles() {
        return new ArrayList<>(titles);
    }

    /**
     * Gets the title at the specified index.
     *
     * @param index The index of the title.
     *
     * @return The title at the specified index, or null if none
     */
    @Nullable
    public String getTitle(int index) {
        return index >= 0 && index < titles.size() ? titles.get(index) : null;
    }

    /**
     * Gets the time to wait by the scoreboard before updating to the next title.
     *
     * @return The time to wait by the scoreboard, or -1 if there's only 1 title
     */
    public long getUpdateTicks() {
        return wait;
    }

    /**
     * Gets whether the title can be updated. This checks if the waiting time is not -1.
     *
     * @return True if the title can be updated, otherwise false
     */
    public boolean shouldUpdate() {
        return wait >= 5;
    }

    /**
     * Gets the number of titles stored.
     *
     * @return The number of titles stored
     */
    public int size() {
        return titles.size();
    }

}