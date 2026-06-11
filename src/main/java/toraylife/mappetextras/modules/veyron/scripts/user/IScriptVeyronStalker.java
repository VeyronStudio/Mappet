package toraylife.mappetextras.modules.veyron.scripts.user;

import mchorse.mappet.api.scripts.user.entities.IScriptPlayer;

public interface IScriptVeyronStalker
{
    public IScriptVeyronStalker enable(boolean enabled);
    public boolean isEnabled();
    public IScriptVeyronStalker setTarget(IScriptPlayer player);
    public IScriptVeyronStalker clearTarget();
    public String getMode();
    public IScriptVeyronStalker setMode(String mode);
    public IScriptVeyronStalker setSpeed(double speed);
    public IScriptVeyronStalker trackNoise(boolean enabled);
    public IScriptVeyronStalker trackLight(boolean enabled);
    public IScriptVeyronStalker trackFootprints(boolean enabled);
    public IScriptVeyronStalker setVisionRadius(double radius);
    public IScriptVeyronStalker setVisionAngle(double angle);
    public IScriptVeyronStalker setHearingRadius(double radius);
    public IScriptVeyronStalker setForgetTime(int ticks);
    public boolean canSee(IScriptPlayer player);
    public IScriptVeyronStalker onSeePlayer(String script);
    public IScriptVeyronStalker onHearNoise(String script);
    public IScriptVeyronStalker onLosePlayer(String script);
    public IScriptVeyronStalker onCatchPlayer(String script);
    public IScriptVeyronStalker onEnterZone(String zone, String script);
    public IScriptVeyronStalker onExitZone(String zone, String script);
    public String getTrigger(String event);
    public IScriptVeyronStalker reset();
}
