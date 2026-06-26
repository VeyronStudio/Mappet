package toraylife.mappetextras.modules.veyron;

import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VeyronStalkerState
{
    public boolean enabled;
    public UUID target;
    public String mode = "idle";
    public boolean trackNoise = true;
    public boolean trackLight = true;
    public boolean trackFootprints = true;
    public double speed = 1.0D;
    public double visionRadius = 14.0D;
    public double visionAngle = 80.0D;
    public double hearingRadius = 20.0D;
    public int forgetTime = 100;
    public double catchRadius = 1.6D;
    public BlockPos home;
    public BlockPos lastKnown;
    public long lastSeenTick;
    public long lastHeardTick;
    public boolean seeing;
    public boolean aware;
    public final Map<String, String> triggers = new HashMap<String, String>();
}
