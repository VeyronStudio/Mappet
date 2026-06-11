package toraylife.mappetextras.modules.veyron.scripts.code;

import mchorse.mappet.api.scripts.user.entities.IScriptPlayer;
import mchorse.mappet.entities.EntityNpc;
import net.minecraft.entity.player.EntityPlayerMP;
import toraylife.mappetextras.modules.veyron.VeyronManager;
import toraylife.mappetextras.modules.veyron.VeyronStalkerState;
import toraylife.mappetextras.modules.veyron.scripts.user.IScriptVeyronStalker;

public class ScriptVeyronStalker implements IScriptVeyronStalker
{
    private final EntityNpc npc;
    private final VeyronStalkerState state;

    public ScriptVeyronStalker(EntityNpc npc)
    {
        this.npc = npc;
        this.state = VeyronManager.stalker(npc);
    }

    public IScriptVeyronStalker enable(boolean enabled) { this.state.enabled = enabled; return this; }
    public boolean isEnabled() { return this.state.enabled; }
    public IScriptVeyronStalker setTarget(IScriptPlayer player) { this.state.target = player == null ? null : player.getMinecraftPlayer().getUniqueID(); return this; }
    public IScriptVeyronStalker clearTarget() { this.state.target = null; return this; }
    public String getMode() { return this.state.mode; }
    public IScriptVeyronStalker setMode(String mode) { this.state.mode = mode == null ? "idle" : mode; return this; }
    public IScriptVeyronStalker setSpeed(double speed) { this.state.speed = Math.max(0.0D, speed); return this; }
    public IScriptVeyronStalker trackNoise(boolean enabled) { this.state.trackNoise = enabled; return this; }
    public IScriptVeyronStalker trackLight(boolean enabled) { this.state.trackLight = enabled; return this; }
    public IScriptVeyronStalker trackFootprints(boolean enabled) { this.state.trackFootprints = enabled; return this; }
    public IScriptVeyronStalker setVisionRadius(double radius) { this.state.visionRadius = Math.max(0.0D, radius); return this; }
    public IScriptVeyronStalker setVisionAngle(double angle) { this.state.visionAngle = Math.max(0.0D, Math.min(360.0D, angle)); return this; }
    public IScriptVeyronStalker setHearingRadius(double radius) { this.state.hearingRadius = Math.max(0.0D, radius); return this; }
    public IScriptVeyronStalker setForgetTime(int ticks) { this.state.forgetTime = Math.max(0, ticks); return this; }
    public boolean canSee(IScriptPlayer player) { EntityPlayerMP mp = player == null ? null : player.getMinecraftPlayer(); return mp != null && VeyronManager.canSee(this.npc, mp, this.state); }
    public IScriptVeyronStalker onSeePlayer(String script) { return this.trigger("see", script); }
    public IScriptVeyronStalker onHearNoise(String script) { return this.trigger("hear", script); }
    public IScriptVeyronStalker onLosePlayer(String script) { return this.trigger("lose", script); }
    public IScriptVeyronStalker onCatchPlayer(String script) { return this.trigger("catch", script); }
    public IScriptVeyronStalker onEnterZone(String zone, String script) { return this.trigger("enter:" + zone, script); }
    public IScriptVeyronStalker onExitZone(String zone, String script) { return this.trigger("exit:" + zone, script); }
    public String getTrigger(String event) { return this.state.triggers.get(event); }
    public IScriptVeyronStalker reset() { this.state.enabled = false; this.state.target = null; this.state.mode = "idle"; this.state.triggers.clear(); return this; }

    private IScriptVeyronStalker trigger(String event, String script)
    {
        if (script == null || script.isEmpty()) this.state.triggers.remove(event);
        else this.state.triggers.put(event, script);
        return this;
    }
}
