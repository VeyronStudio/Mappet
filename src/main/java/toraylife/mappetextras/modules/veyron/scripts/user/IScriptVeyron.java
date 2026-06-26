package toraylife.mappetextras.modules.veyron.scripts.user;

public interface IScriptVeyron
{
    /**
     * Returns the player's current fear value.
     *
     * <pre>{@code
     *    function main(c)
     *    {
     *        c.send("Fear: " + c.getSubject().getHorror().getFear());
     *    }
     * }</pre>
     *
     * @return the current fear amount.
     */
    public int getFear();

    /**
     * Sets the player's fear, clamped between 0 and the configured maximum,
     * and re-applies the matching visual/audio fear effects.
     *
     * <pre>{@code
     *    function main(c)
     *    {
     *        c.getSubject().getHorror().setFear(60);
     *    }
     * }</pre>
     *
     * @param value the new fear amount (clamped to 0..maxFear).
     */
    public IScriptVeyron setFear(int value);

    /**
     * Adds the given amount to the player's current fear (negative values reduce it).
     *
     * @param value amount to add to fear.
     */
    public IScriptVeyron addFear(int value);

    /**
     * Returns the configured maximum fear value.
     *
     * @return the maximum fear.
     */
    public int getMaxFear();

    /**
     * Sets the maximum fear value (minimum of 1) and re-clamps current fear.
     *
     * @param value the new maximum fear (values below 1 are treated as 1).
     */
    public IScriptVeyron setMaxFear(int value);

    /**
     * Toggles whether fear automatically applies visual/audio effects such as
     * nausea, blindness and slowness.
     *
     * @param enabled true to enable automatic fear effects.
     */
    public IScriptVeyron setFearEffects(boolean enabled);

    /**
     * Forces a minimum visual distortion level regardless of the current fear,
     * clamped between 0 (none) and 4 (strongest).
     *
     * @param level distortion level in the 0..4 range.
     */
    public IScriptVeyron setVisionLevel(int level);

    /**
     * Sets a sound-muffle factor for the player, clamped between 0 (normal) and
     * 1 (fully muffled).
     *
     * @param value muffle factor in the 0..1 range.
     */
    public IScriptVeyron setSoundMuffle(float value);

    /**
     * Sets the player's walking speed multiplier (minimum 0.05) and applies it
     * immediately. 1.0 is the default vanilla speed.
     *
     * <pre>{@code
     *    function main(c)
     *    {
     *        // Slow the player down to half speed
     *        c.getSubject().getHorror().setSpeedMultiplier(0.5);
     *    }
     * }</pre>
     *
     * @param value speed multiplier (clamped to a minimum of 0.05).
     */
    public IScriptVeyron setSpeedMultiplier(float value);

    /**
     * Returns the player's current tension value.
     *
     * @return the current tension (0..100).
     */
    public int getTension();

    /**
     * Sets the player's tension value, clamped between 0 and 100. Tension is a
     * free-form counter you can read from your own scripts (for example to drive
     * dynamic music or events).
     *
     * @param value the new tension (clamped to 0..100).
     */
    public IScriptVeyron setTension(int value);

    /**
     * Marks the player as being inside the named zone. Entering a zone also
     * fires the matching {@code onEnterZone} triggers of stalkers targeting
     * this player.
     *
     * @param zone zone name.
     */
    public IScriptVeyron setZone(String zone);

    /**
     * Removes the player from the named zone. Leaving a zone also fires the
     * matching {@code onExitZone} triggers of stalkers targeting this player.
     *
     * @param zone zone name.
     */
    public IScriptVeyron clearZone(String zone);

    /**
     * Returns whether the player is currently inside the named zone.
     *
     * @param zone zone name.
     * @return true if the player is in that zone.
     */
    public boolean isInZone(String zone);

    /**
     * Locks an action so the player can no longer perform it. The event that
     * would normally happen is cancelled.
     *
     * <p>Valid actions: {@code break}, {@code place}, {@code attack},
     * {@code interact}, {@code open_door}.</p>
     *
     * <pre>{@code
     *    function main(c)
     *    {
     *        c.getSubject().getHorror().lockAction("break").lockAction("attack");
     *    }
     * }</pre>
     *
     * @param action action name (case-insensitive); unknown names are ignored.
     */
    public IScriptVeyron lockAction(String action);

    /**
     * Unlocks a previously locked action.
     *
     * @param action action name (case-insensitive).
     */
    public IScriptVeyron unlockAction(String action);

    /**
     * Unlocks all actions for the player.
     */
    public IScriptVeyron clearLockedActions();

    /**
     * Returns whether the given action is currently locked.
     *
     * @param action action name (case-insensitive).
     * @return true if the action is locked.
     */
    public boolean isActionLocked(String action);

    /**
     * Sets a named noise level for the player. Noise decays over time and can be
     * heard by stalkers that track noise.
     *
     * @param type noise type name.
     * @param power noise strength (clamped to a minimum of 0).
     */
    public IScriptVeyron setNoise(String type, int power);

    /**
     * Returns the current level of the named noise.
     *
     * @param type noise type name.
     * @return the noise level, or 0 if there is none.
     */
    public int getNoise(String type);

    /**
     * Removes the named noise immediately.
     *
     * @param type noise type name.
     */
    public IScriptVeyron clearNoise(String type);

    /**
     * Registers a jumpscare id associated with a condition name, so it can be
     * looked up later from your scripts.
     *
     * @param id jumpscare identifier.
     * @param condition condition name used as the lookup key.
     */
    public IScriptVeyron jumpscare(String id, String condition);

    /**
     * Returns the jumpscare id registered for the given condition.
     *
     * @param condition condition name.
     * @return the jumpscare id, or null if none is registered.
     */
    public String getJumpscare(String condition);

    /**
     * Removes the jumpscare registered for the given condition.
     *
     * @param condition condition name.
     */
    public IScriptVeyron clearJumpscare(String condition);

    /**
     * Plays an ambient sound at the player's position. Falls back to the cave
     * ambient sound if the sound id is unknown.
     *
     * @param sound sound event id (for example "minecraft:entity.ghast.scream").
     * @param volume playback volume.
     * @param pitch playback pitch.
     */
    public IScriptVeyron playAmbient(String sound, float volume, float pitch);

    /**
     * Plays a sound at a random horizontal point around the player, between the
     * given minimum and maximum distance.
     *
     * @param sound sound event id.
     * @param minDistance minimum distance from the player.
     * @param maxDistance maximum distance from the player.
     * @param volume playback volume.
     * @param pitch playback pitch.
     */
    public IScriptVeyron playRandomNear(String sound, double minDistance, double maxDistance, float volume, float pitch);

    /**
     * Plays looping/background music for the player using the music sound
     * category. Any previously playing music track is stopped first.
     *
     * <pre>{@code
     *    function main(c)
     *    {
     *        c.getSubject().getHorror().playMusic("mymod:music.chase");
     *    }
     * }</pre>
     *
     * @param sound sound event id.
     */
    public IScriptVeyron playMusic(String sound);

    /**
     * Stops the currently playing music for the player.
     */
    public IScriptVeyron stopMusic();

    /**
     * Switches the current music to a new track.
     *
     * <p>Note: positional sounds cannot be smoothly cross-faded, so this
     * currently switches the track immediately and the {@code ticks} argument is
     * reserved for future use.</p>
     *
     * @param sound sound event id of the new track.
     * @param ticks reserved fade duration in ticks.
     */
    public IScriptVeyron fadeMusic(String sound, int ticks);

    /**
     * Darkens the player's screen with a black overlay, clamped between 0
     * (none) and 1 (fully black). Synced to the client immediately.
     *
     * @param value darkness amount in the 0..1 range.
     */
    public IScriptVeyron setScreenDarkness(float value);

    /**
     * Applies a blur post-effect to the player's screen, clamped between 0
     * (none) and 8 (strongest). Synced to the client immediately.
     *
     * @param value blur amount in the 0..8 range.
     */
    public IScriptVeyron setScreenBlur(float value);

    /**
     * Clears both the screen darkness and blur effects.
     */
    public IScriptVeyron clearScreenEffects();

    /**
     * Sends a chat message to the player (client-side appearance only).
     *
     * @param message text to display.
     */
    public IScriptVeyron fakeMessage(String message);

    /**
     * Shows a large title on the player's screen.
     *
     * @param title title text.
     */
    public IScriptVeyron fakeTitle(String title);

    /**
     * Shows a subtitle under the current title on the player's screen.
     *
     * @param subtitle subtitle text.
     */
    public IScriptVeyron fakeSubtitle(String subtitle);

    /**
     * Shows a title and subtitle with custom timing.
     *
     * @param title title text.
     * @param subtitle subtitle text.
     * @param fadeIn fade-in time in ticks.
     * @param stay on-screen time in ticks.
     * @param fadeOut fade-out time in ticks.
     */
    public IScriptVeyron fakeTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut);

    /**
     * Shows a message in the action bar (above the hotbar).
     *
     * @param message action bar text.
     */
    public IScriptVeyron fakeActionBar(String message);

    /**
     * Toggles the Veyron quest HUD on or off for the player.
     *
     * @param enabled true to show the quest HUD.
     */
    public IScriptVeyron showQuestHUD(boolean enabled);

    /**
     * Enables the quest HUD and tracks the given quest by name.
     *
     * @param questId quest name to track.
     */
    public IScriptVeyron trackQuest(String questId);

    /**
     * Clears the currently tracked quest (the HUD falls back to the first
     * visible quest, if any).
     */
    public IScriptVeyron clearTrackedQuest();

    /**
     * Starts (or restarts) a named countdown timer with a duration in ticks
     * (20 ticks = 1 second).
     *
     * <pre>{@code
     *    function main(c)
     *    {
     *        // 10 second timer
     *        c.getSubject().getHorror().startTimer("escape", 200);
     *    }
     * }</pre>
     *
     * @param id timer id.
     * @param ticks duration in ticks.
     */
    public IScriptVeyron startTimer(String id, int ticks);

    /**
     * Starts (or restarts) a named countdown timer with a duration in seconds.
     *
     * @param id timer id.
     * @param seconds duration in seconds.
     */
    public IScriptVeyron startTimerSeconds(String id, int seconds);

    /**
     * Adds (or subtracts, with negative values) ticks to an existing timer.
     *
     * @param id timer id.
     * @param ticks ticks to add.
     */
    public IScriptVeyron addTimer(String id, int ticks);

    /**
     * Pauses a running timer, freezing its remaining time.
     *
     * @param id timer id.
     */
    public IScriptVeyron pauseTimer(String id);

    /**
     * Resumes a paused timer.
     *
     * @param id timer id.
     */
    public IScriptVeyron resumeTimer(String id);

    /**
     * Stops and removes a timer.
     *
     * @param id timer id.
     */
    public IScriptVeyron stopTimer(String id);

    /**
     * Returns whether a timer with the given id exists.
     *
     * @param id timer id.
     * @return true if the timer exists.
     */
    public boolean hasTimer(String id);

    /**
     * Returns the remaining time of a timer in ticks.
     *
     * @param id timer id.
     * @return remaining ticks, or 0 if the timer does not exist.
     */
    public int getTimer(String id);

    /**
     * Returns whether a timer exists and has run out.
     *
     * @param id timer id.
     * @return true if the timer exists and its remaining time is 0.
     */
    public boolean isTimerFinished(String id);

    /**
     * Returns the remaining time of a timer formatted as {@code mm:ss}.
     *
     * @param id timer id.
     * @return formatted remaining time.
     */
    public String formatTimer(String id);

    /**
     * Resets the player's entire Veyron state: fear, tension, vision, speed,
     * zones, locked actions, noises, jumpscares, music, timers, screen effects
     * and HUDs.
     */
    public IScriptVeyron reset();

    /**
     * Counts how many items of the given type the player currently has in
     * their main inventory, including the hotbar and the off-hand slot.
     *
     * <pre>{@code
     *    function main(c)
     *    {
     *        var horror = c.getSubject().getHorror();
     *        c.send("Batteries: " + horror.countItem("minecraft:redstone"));
     *    }
     * }</pre>
     *
     * @param item registry name of the item (for example "minecraft:redstone").
     * @return the total amount of matching items, or 0 if there are none or the name is invalid.
     */
    public int countItem(String item);

    /**
     * Checks whether the player has at least the given amount of an item.
     *
     * <pre>{@code
     *    function main(c)
     *    {
     *        var horror = c.getSubject().getHorror();
     *
     *        if (horror.hasItem("minecraft:redstone", 3))
     *        {
     *            c.send("You have enough batteries to keep the flashlight on.");
     *        }
     *    }
     * }</pre>
     *
     * @param item registry name of the item.
     * @param count minimum amount required (values below 1 are treated as 1).
     * @return true if the player holds at least that many.
     */
    public boolean hasItem(String item, int count);

    /**
     * Removes up to the given amount of an item from the player's inventory
     * (main inventory, hotbar and off-hand) and returns how many were actually
     * taken.
     *
     * <pre>{@code
     *    function main(c)
     *    {
     *        var horror = c.getSubject().getHorror();
     *        var used = horror.removeItem("minecraft:redstone", 1);
     *
     *        c.send("Consumed " + used + " battery.");
     *    }
     * }</pre>
     *
     * @param item registry name of the item.
     * @param count maximum amount to remove.
     * @return the amount actually removed (less than {@code count} if the player did not have enough).
     */
    public int removeItem(String item, int count);
}
