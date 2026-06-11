package toraylife.mappetextras.modules.veyron;

import net.minecraft.util.SoundCategory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class VeyronPlayerState
{
    public int fear;
    public int maxFear = 100;
    public boolean fearEffects = true;
    public float soundMuffle = 0.0F;
    public float speedMultiplier = 1.0F;
    public int visionLevel;
    public int tension;
    public String music = "";
    public final Set<String> zones = new HashSet<String>();
    public final Set<VeyronAction> lockedActions = new HashSet<VeyronAction>();
    public final Map<String, Integer> noises = new HashMap<String, Integer>();
    public final Map<String, String> jumpscares = new HashMap<String, String>();
    public final Map<String, VeyronTimer> timers = new HashMap<String, VeyronTimer>();
    public SoundCategory musicCategory = SoundCategory.MUSIC;
    public long nextAmbientTick;
    public float screenDarkness = 0.0F;
    public float screenBlur = 0.0F;
    public boolean questHud = false;
    public String trackedQuest = "";
    public String activeHud = "";
    public final Map<String, VeyronHud> huds = new HashMap<String, VeyronHud>();
}
