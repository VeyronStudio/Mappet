package toraylife.mappetextras.modules.veyron;

import net.minecraft.block.BlockDoor;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class VeyronEventHandler
{
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if (event.player.world.isRemote || !(event.player instanceof EntityPlayerMP) || event.phase == TickEvent.Phase.END)
        {
            return;
        }

        EntityPlayerMP player = (EntityPlayerMP) event.player;

        if (player.isSprinting())
        {
            VeyronManager.addNoise(player, "run", Math.max(VeyronManager.player(player).noises.containsKey("run") ? VeyronManager.player(player).noises.get("run") : 0, 8));
        }

        if (player.moveForward != 0 || player.moveStrafing != 0)
        {
            VeyronManager.addNoise(player, "step", Math.max(VeyronManager.player(player).noises.containsKey("step") ? VeyronManager.player(player).noises.get("step") : 0, 3));
        }
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (event.player instanceof EntityPlayerMP)
        {
            VeyronManager.syncOverlay((EntityPlayerMP) event.player);
        }
    }

    @SubscribeEvent
    public void onJump(LivingEvent.LivingJumpEvent event)
    {
        if (!event.getEntityLiving().world.isRemote && event.getEntityLiving() instanceof EntityPlayerMP)
        {
            VeyronManager.addNoise((EntityPlayerMP) event.getEntityLiving(), "jump", 5);
        }
    }

    @SubscribeEvent
    public void onBreak(BlockEvent.BreakEvent event)
    {
        if (!(event.getPlayer() instanceof EntityPlayerMP))
        {
            return;
        }

        EntityPlayerMP player = (EntityPlayerMP) event.getPlayer();

        if (VeyronManager.isLocked(player, VeyronAction.BREAK))
        {
            event.setCanceled(true);
            return;
        }

        VeyronManager.addNoise(player, "break_block", 12);
    }

    @SubscribeEvent
    public void onPlace(BlockEvent.PlaceEvent event)
    {
        if (!(event.getPlayer() instanceof EntityPlayerMP))
        {
            return;
        }

        EntityPlayerMP player = (EntityPlayerMP) event.getPlayer();

        if (VeyronManager.isLocked(player, VeyronAction.PLACE))
        {
            event.setCanceled(true);
            return;
        }

        VeyronManager.addNoise(player, "place_block", 8);
    }

    @SubscribeEvent
    public void onAttack(LivingAttackEvent event)
    {
        if (event.getSource().getTrueSource() instanceof EntityPlayerMP)
        {
            EntityPlayerMP player = (EntityPlayerMP) event.getSource().getTrueSource();

            if (VeyronManager.isLocked(player, VeyronAction.ATTACK))
            {
                event.setCanceled(true);
                return;
            }

            VeyronManager.addNoise(player, "attack", 10);
        }
    }

    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event)
    {
        if (!(event.getEntityPlayer() instanceof EntityPlayerMP))
        {
            return;
        }

        EntityPlayerMP player = (EntityPlayerMP) event.getEntityPlayer();

        if (VeyronManager.isLocked(player, VeyronAction.INTERACT))
        {
            event.setCanceled(true);
            return;
        }

        if (player.world.getBlockState(event.getPos()).getBlock() instanceof BlockDoor)
        {
            if (VeyronManager.isLocked(player, VeyronAction.OPEN_DOOR))
            {
                event.setCanceled(true);
                return;
            }

            VeyronManager.addNoise(player, "door_open", 9);
        }
    }
}
