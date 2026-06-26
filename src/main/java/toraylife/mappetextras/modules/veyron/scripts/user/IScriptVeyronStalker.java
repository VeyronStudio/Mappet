package toraylife.mappetextras.modules.veyron.scripts.user;

import mchorse.mappet.api.scripts.user.entities.IScriptPlayer;

public interface IScriptVeyronStalker
{
    /**
     * Enables or disables the stalker AI for this NPC. While enabled, the NPC is
     * processed every server tick: it tracks its target, chases, searches and
     * fires its triggers.
     *
     * <pre>{@code
     *    function main(c)
     *    {
     *        var stalker = c.getSubject().getStalker();
     *        stalker.enable(true).setTarget(c.getPlayer());
     *    }
     * }</pre>
     *
     * @param enabled true to enable the stalker.
     */
    public IScriptVeyronStalker enable(boolean enabled);

    /**
     * Returns whether the stalker AI is enabled.
     *
     * @return true if enabled.
     */
    public boolean isEnabled();

    /**
     * Sets the player this stalker hunts. If no target is set, the stalker uses
     * the closest player within its vision radius.
     *
     * @param player the target player, or null to clear.
     */
    public IScriptVeyronStalker setTarget(IScriptPlayer player);

    /**
     * Clears the explicit target so the stalker falls back to the nearest player.
     */
    public IScriptVeyronStalker clearTarget();

    /**
     * Returns the current behavior mode ("idle", "search" or "chase").
     *
     * @return the current mode.
     */
    public String getMode();

    /**
     * Sets the behavior mode. The AI also updates this automatically as it
     * gains and loses contact with its target.
     *
     * @param mode mode name (null is treated as "idle").
     */
    public IScriptVeyronStalker setMode(String mode);

    /**
     * Sets the movement speed multiplier used while chasing or searching
     * (1.0 is normal NPC speed). 0 stops the NPC from moving.
     *
     * @param speed speed multiplier (minimum 0).
     */
    public IScriptVeyronStalker setSpeed(double speed);

    /**
     * Toggles whether the stalker can hear and react to player noise within its
     * hearing radius.
     *
     * @param enabled true to enable noise tracking.
     */
    public IScriptVeyronStalker trackNoise(boolean enabled);

    /**
     * Toggles whether the stalker tracks the player by light level.
     *
     * @param enabled true to enable light tracking.
     */
    public IScriptVeyronStalker trackLight(boolean enabled);

    /**
     * Toggles whether the stalker tracks the player by footprints.
     *
     * @param enabled true to enable footprint tracking.
     */
    public IScriptVeyronStalker trackFootprints(boolean enabled);

    /**
     * Sets how far (in blocks) the stalker can see.
     *
     * @param radius vision radius in blocks (minimum 0).
     */
    public IScriptVeyronStalker setVisionRadius(double radius);

    /**
     * Sets the stalker's field of view, in degrees, centered on its facing
     * direction (clamped between 0 and 360).
     *
     * @param angle vision cone angle in degrees.
     */
    public IScriptVeyronStalker setVisionAngle(double angle);

    /**
     * Sets how far (in blocks) the stalker can hear player noise.
     *
     * @param radius hearing radius in blocks (minimum 0).
     */
    public IScriptVeyronStalker setHearingRadius(double radius);

    /**
     * Sets how long (in ticks) the stalker keeps pursuing after losing all
     * contact with the target before giving up and firing {@code onLosePlayer}.
     *
     * @param ticks forget delay in ticks (minimum 0).
     */
    public IScriptVeyronStalker setForgetTime(int ticks);

    /**
     * Returns whether the stalker can currently see the given player, taking
     * distance, field of view and line of sight into account.
     *
     * @param player the player to test.
     * @return true if the player is visible to the stalker.
     */
    public boolean canSee(IScriptPlayer player);

    /**
     * Sets the Mappet event to run when the stalker first sees its target. The
     * event runs with the NPC as the subject and the player as the object.
     *
     * <pre>{@code
     *    function main(c)
     *    {
     *        c.getSubject().getStalker().onSeePlayer("stalker_spotted");
     *    }
     * }</pre>
     *
     * @param script Mappet event id (empty/null removes the trigger).
     */
    public IScriptVeyronStalker onSeePlayer(String script);

    /**
     * Sets the Mappet event to run when the stalker first hears the target's
     * noise. Runs with the NPC as the subject and the player as the object.
     *
     * @param script Mappet event id (empty/null removes the trigger).
     */
    public IScriptVeyronStalker onHearNoise(String script);

    /**
     * Sets the Mappet event to run when the stalker gives up after losing
     * contact. Runs with the NPC as the subject.
     *
     * @param script Mappet event id (empty/null removes the trigger).
     */
    public IScriptVeyronStalker onLosePlayer(String script);

    /**
     * Sets the Mappet event to run when the stalker reaches (catches) its
     * target. Runs with the NPC as the subject and the player as the object.
     *
     * @param script Mappet event id (empty/null removes the trigger).
     */
    public IScriptVeyronStalker onCatchPlayer(String script);

    /**
     * Sets the Mappet event to run when the target enters the named zone. Runs
     * with the NPC as the subject and the player as the object.
     *
     * @param zone zone name.
     * @param script Mappet event id (empty/null removes the trigger).
     */
    public IScriptVeyronStalker onEnterZone(String zone, String script);

    /**
     * Sets the Mappet event to run when the target leaves the named zone. Runs
     * with the NPC as the subject and the player as the object.
     *
     * @param zone zone name.
     * @param script Mappet event id (empty/null removes the trigger).
     */
    public IScriptVeyronStalker onExitZone(String zone, String script);

    /**
     * Returns the Mappet event id registered for the given trigger event.
     *
     * <p>Event keys are "see", "hear", "lose", "catch", "enter:&lt;zone&gt;" and
     * "exit:&lt;zone&gt;".</p>
     *
     * @param event trigger event key.
     * @return the registered event id, or null if none.
     */
    public String getTrigger(String event);

    /**
     * Resets the stalker: disables it, clears its target, sets the mode back to
     * "idle" and removes all triggers.
     */
    public IScriptVeyronStalker reset();
}
