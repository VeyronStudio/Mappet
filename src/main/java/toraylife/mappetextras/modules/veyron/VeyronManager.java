package toraylife.mappetextras.modules.veyron;

import mchorse.mappet.entities.EntityNpc;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import toraylife.mappetextras.modules.veyron.network.PacketVeyronOverlay;
import toraylife.mappetextras.network.Dispatcher;

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
        player.world.playSound(null, x, y, z, event, SoundCategory.AMBIENT, volume, pitch);
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
