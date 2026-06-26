package toraylife.mappetextras.modules.veyron;

import mchorse.mappet.api.triggers.blocks.EventTriggerBlock;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.entities.EntityNpc;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import toraylife.mappetextras.modules.veyron.network.PacketVeyronOverlay;
import toraylife.mappetextras.network.Dispatcher;

import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VeyronManager
{
    private static final Map<UUID, VeyronPlayerState> PLAYERS = new HashMap<UUID, VeyronPlayerState>();
    private static final Map<UUID, VeyronStalkerState> STALKERS = new HashMap<UUID, VeyronStalkerState>();

    public static VeyronPlayerState player(EntityPlayerMP player)
    {
        UUID uuid = player.getUniqueID();
        VeyronPlayerState state = PLAYERS.get(uuid);

        if (state == null)
        {
            state = new VeyronPlayerState();
            PLAYERS.put(uuid, state);
        }

        return state;
    }

    public static VeyronStalkerState stalker(EntityNpc npc)
    {
        UUID uuid = npc.getUniqueID();
        VeyronStalkerState state = STALKERS.get(uuid);

        if (state == null)
        {
            state = new VeyronStalkerState();
            state.home = npc.getPosition();
            STALKERS.put(uuid, state);
        }

        return state;
    }

    public static void addNoise(EntityPlayerMP player, String type, int power)
    {
        VeyronPlayerState state = player(player);
        state.noises.put(type, power);
    }

    public static void removePlayer(UUID uuid)
    {
        PLAYERS.remove(uuid);
    }

    public static void removeStalker(UUID uuid)
    {
        STALKERS.remove(uuid);
    }

    /**
     * Server side tick: decays accumulated player noise and drives the stalker AI.
     */
    public static void serverTick(MinecraftServer server)
    {
        if (server == null)
        {
            return;
        }

        for (VeyronPlayerState state : PLAYERS.values())
        {
            Iterator<Map.Entry<String, Integer>> it = state.noises.entrySet().iterator();

            while (it.hasNext())
            {
                Map.Entry<String, Integer> entry = it.next();
                int value = entry.getValue() - 1;

                if (value <= 0)
                {
                    it.remove();
                }
                else
                {
                    entry.setValue(value);
                }
            }
        }

        if (STALKERS.isEmpty())
        {
            return;
        }

        for (Map.Entry<UUID, VeyronStalkerState> entry : STALKERS.entrySet())
        {
            VeyronStalkerState state = entry.getValue();
            EntityNpc npc = findNpc(server, entry.getKey());

            if (npc == null || npc.isDead)
            {
                continue;
            }

            if (state.enabled)
            {
                tickStalker(server, npc, state);
            }
        }
    }

    private static void tickStalker(MinecraftServer server, EntityNpc npc, VeyronStalkerState state)
    {
        long now = npc.world.getTotalWorldTime();
        EntityPlayerMP target = resolveTarget(server, npc, state);
        boolean see = target != null && canSee(npc, target, state);

        if (see)
        {
            state.lastKnown = target.getPosition();
            state.lastSeenTick = now;
            state.mode = "chase";

            if (!state.seeing)
            {
                state.seeing = true;
                state.aware = true;
                fireTrigger(npc, target, state, "see");
            }

            if (npc.getDistance(target) <= state.catchRadius)
            {
                fireTrigger(npc, target, state, "catch");
            }

            moveTo(npc, target.posX, target.posY, target.posZ, state.speed);
        }
        else
        {
            state.seeing = false;

            if (target != null && state.trackNoise && noiseLevel(target) > 0 && npc.getDistance(target) <= state.hearingRadius)
            {
                state.lastKnown = target.getPosition();
                state.lastHeardTick = now;

                if (!state.aware)
                {
                    state.aware = true;
                    state.mode = "search";
                    fireTrigger(npc, target, state, "hear");
                }
            }

            long lastContact = Math.max(state.lastSeenTick, state.lastHeardTick);

            if (state.aware && lastContact > 0 && now - lastContact >= state.forgetTime)
            {
                state.aware = false;
                state.mode = "idle";
                state.lastKnown = null;
                fireTrigger(npc, target, state, "lose");
            }

            if (state.lastKnown != null)
            {
                moveTo(npc, state.lastKnown.getX() + 0.5D, state.lastKnown.getY(), state.lastKnown.getZ() + 0.5D, state.speed);
            }
        }
    }

    private static void moveTo(EntityNpc npc, double x, double y, double z, double speed)
    {
        if (speed > 0.0D)
        {
            npc.getNavigator().tryMoveToXYZ(x, y, z, speed);
        }
    }

    private static int noiseLevel(EntityPlayerMP player)
    {
        int max = 0;

        for (int value : player(player).noises.values())
        {
            if (value > max)
            {
                max = value;
            }
        }

        return max;
    }

    private static EntityPlayerMP resolveTarget(MinecraftServer server, EntityNpc npc, VeyronStalkerState state)
    {
        if (state.target != null)
        {
            return server.getPlayerList().getPlayerByUUID(state.target);
        }

        return (EntityPlayerMP) npc.world.getClosestPlayerToEntity(npc, state.visionRadius);
    }

    private static EntityNpc findNpc(MinecraftServer server, UUID uuid)
    {
        for (WorldServer world : server.worlds)
        {
            net.minecraft.entity.Entity entity = world.getEntityFromUuid(uuid);

            if (entity instanceof EntityNpc)
            {
                return (EntityNpc) entity;
            }
        }

        return null;
    }

    private static void fireTrigger(EntityNpc npc, EntityPlayerMP player, VeyronStalkerState state, String event)
    {
        String id = state.triggers.get(event);

        if (id == null || id.isEmpty())
        {
            return;
        }

        new EventTriggerBlock(id).trigger(new DataContext(npc, player));
    }

    /**
     * Fires per-stalker zone triggers for stalkers currently targeting the given player.
     */
    public static void onZoneChange(EntityPlayerMP player, String zone, boolean entered)
    {
        if (zone == null || zone.isEmpty())
        {
            return;
        }

        String event = (entered ? "enter:" : "exit:") + zone;
        UUID uuid = player.getUniqueID();
        MinecraftServer server = player.world.getMinecraftServer();

        for (Map.Entry<UUID, VeyronStalkerState> entry : STALKERS.entrySet())
        {
            VeyronStalkerState state = entry.getValue();

            if (!state.enabled || !uuid.equals(state.target) || !state.triggers.containsKey(event))
            {
                continue;
            }

            EntityNpc npc = server == null ? null : findNpc(server, entry.getKey());

            if (npc != null)
            {
                new EventTriggerBlock(state.triggers.get(event)).trigger(new DataContext(npc, player));
            }
        }
    }

    public static boolean isLocked(EntityPlayerMP player, VeyronAction action)
    {
        return action != null && player(player).lockedActions.contains(action);
    }

    public static boolean canSee(EntityNpc npc, EntityPlayerMP player, VeyronStalkerState state)
    {
        double distance = npc.getDistance(player);

        if (distance > state.visionRadius)
        {
            return false;
        }

        Vec3d look = npc.getLookVec();
        Vec3d toPlayer = new Vec3d(player.posX - npc.posX, player.posY + player.getEyeHeight() - (npc.posY + npc.getEyeHeight()), player.posZ - npc.posZ).normalize();
        double dot = look.dotProduct(toPlayer);
        double angle = Math.toDegrees(Math.acos(Math.max(-1.0D, Math.min(1.0D, dot))));

        return angle <= state.visionAngle / 2.0D && npc.canEntityBeSeen(player);
    }

    public static void playSound(EntityPlayerMP player, SoundEvent event, double x, double y, double z, float volume, float pitch)
    {
        playSound(player, event, SoundCategory.AMBIENT, x, y, z, volume, pitch);
    }

    public static void playSound(EntityPlayerMP player, SoundEvent event, SoundCategory category, double x, double y, double z, float volume, float pitch)
    {
        player.world.playSound(null, x, y, z, event, category, volume, pitch);
    }

    public static void stopSound(EntityPlayerMP player, String sound, SoundCategory category)
    {
        PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());

        buffer.writeString(category == null ? "" : category.getName());
        buffer.writeString(sound == null ? "" : sound);

        player.connection.sendPacket(new SPacketCustomPayload("MC|StopSound", buffer));
    }

    public static void syncOverlay(EntityPlayerMP player)
    {
        Dispatcher.sendTo(new PacketVeyronOverlay(writeOverlay(player(player))), player);
    }

    public static NBTTagCompound writeOverlay(VeyronPlayerState state)
    {
        NBTTagCompound root = new NBTTagCompound();
        NBTTagCompound huds = new NBTTagCompound();

        root.setFloat("Darkness", state.screenDarkness);
        root.setFloat("Blur", state.screenBlur);
        root.setBoolean("QuestHUD", state.questHud);
        root.setString("TrackedQuest", state.trackedQuest == null ? "" : state.trackedQuest);
        root.setString("ActiveHud", state.activeHud == null ? "" : state.activeHud);

        for (Map.Entry<String, VeyronHud> hudEntry : state.huds.entrySet())
        {
            NBTTagCompound hudTag = new NBTTagCompound();
            NBTTagCompound elements = new NBTTagCompound();
            VeyronHud hud = hudEntry.getValue();

            hudTag.setBoolean("Visible", hud.visible);
            hudTag.setInteger("X", hud.x);
            hudTag.setInteger("Y", hud.y);
            hudTag.setInteger("Gap", hud.gap);

            for (Map.Entry<String, VeyronHudElement> elementEntry : hud.elements.entrySet())
            {
                VeyronHudElement element = elementEntry.getValue();
                NBTTagCompound elementTag = new NBTTagCompound();

                elementTag.setString("Type", element.type);
                elementTag.setString("Text", element.text);
                elementTag.setString("Icon", element.icon);
                elementTag.setBoolean("Visible", element.visible);
                elementTag.setInteger("Color", element.color);
                elementTag.setInteger("Background", element.background);
                elementTag.setInteger("X", element.x);
                elementTag.setInteger("Y", element.y);
                elementTag.setInteger("Width", element.width);
                elementTag.setInteger("Height", element.height);
                elementTag.setDouble("Value", element.value);
                elementTag.setDouble("Max", element.max);
                elementTag.setInteger("Order", element.order);
                elements.setTag(elementEntry.getKey(), elementTag);
            }

            hudTag.setTag("Elements", elements);
            huds.setTag(hudEntry.getKey(), hudTag);
        }

        root.setTag("Huds", huds);

        return root;
    }
}
