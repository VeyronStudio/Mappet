package toraylife.mappetextras.modules.veyron.scripts.code;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import toraylife.mappetextras.modules.veyron.VeyronAction;
import toraylife.mappetextras.modules.veyron.VeyronManager;
import toraylife.mappetextras.modules.veyron.VeyronPlayerState;
import toraylife.mappetextras.modules.veyron.VeyronTimer;
import toraylife.mappetextras.modules.veyron.scripts.user.IScriptVeyron;

public class ScriptVeyron implements IScriptVeyron
{
    private final EntityPlayerMP player;
    private final VeyronPlayerState state;

    public ScriptVeyron(EntityPlayerMP player)
    {
        this.player = player;
        this.state = VeyronManager.player(player);
    }

    public int getFear() { return this.state.fear; }
    public IScriptVeyron setFear(int value) { this.state.fear = this.clamp(value, 0, this.state.maxFear); this.applyFearEffects(); return this; }
    public IScriptVeyron addFear(int value) { return this.setFear(this.state.fear + value); }
    public int getMaxFear() { return this.state.maxFear; }
    public IScriptVeyron setMaxFear(int value) { this.state.maxFear = Math.max(1, value); return this.setFear(this.state.fear); }
    public IScriptVeyron setFearEffects(boolean enabled) { this.state.fearEffects = enabled; this.applyFearEffects(); return this; }
    public IScriptVeyron setVisionLevel(int level) { this.state.visionLevel = this.clamp(level, 0, 4); this.applyFearEffects(); return this; }
    public IScriptVeyron setSoundMuffle(float value) { this.state.soundMuffle = Math.max(0.0F, Math.min(1.0F, value)); return this; }
    public IScriptVeyron setSpeedMultiplier(float value) { this.state.speedMultiplier = Math.max(0.05F, value); this.applySpeed(); return this; }
    public int getTension() { return this.state.tension; }
    public IScriptVeyron setTension(int value) { this.state.tension = this.clamp(value, 0, 100); return this; }
    public IScriptVeyron setZone(String zone) { if (zone != null && this.state.zones.add(zone)) VeyronManager.onZoneChange(this.player, zone, true); return this; }
    public IScriptVeyron clearZone(String zone) { if (zone != null && this.state.zones.remove(zone)) VeyronManager.onZoneChange(this.player, zone, false); return this; }
    public boolean isInZone(String zone) { return this.state.zones.contains(zone); }
    public IScriptVeyron lockAction(String action) { VeyronAction a = VeyronAction.fromString(action); if (a != null) this.state.lockedActions.add(a); return this; }
    public IScriptVeyron unlockAction(String action) { VeyronAction a = VeyronAction.fromString(action); if (a != null) this.state.lockedActions.remove(a); return this; }
    public IScriptVeyron clearLockedActions() { this.state.lockedActions.clear(); return this; }
    public boolean isActionLocked(String action) { VeyronAction a = VeyronAction.fromString(action); return a != null && this.state.lockedActions.contains(a); }
    public IScriptVeyron setNoise(String type, int power) { if (type != null) this.state.noises.put(type, Math.max(0, power)); return this; }
    public int getNoise(String type) { Integer value = this.state.noises.get(type); return value == null ? 0 : value; }
    public IScriptVeyron clearNoise(String type) { this.state.noises.remove(type); return this; }
    public IScriptVeyron jumpscare(String id, String condition) { if (id != null && condition != null) this.state.jumpscares.put(condition, id); return this; }
    public String getJumpscare(String condition) { return this.state.jumpscares.get(condition); }
    public IScriptVeyron clearJumpscare(String condition) { this.state.jumpscares.remove(condition); return this; }
    public IScriptVeyron playAmbient(String sound, float volume, float pitch) { this.play(sound, volume, pitch, this.player.posX, this.player.posY, this.player.posZ); return this; }
    public IScriptVeyron playRandomNear(String sound, double minDistance, double maxDistance, float volume, float pitch) { double a = this.player.world.rand.nextDouble() * Math.PI * 2.0D; double d = minDistance + this.player.world.rand.nextDouble() * Math.max(0.0D, maxDistance - minDistance); this.play(sound, volume, pitch, this.player.posX + Math.cos(a) * d, this.player.posY, this.player.posZ + Math.sin(a) * d); return this; }
    public IScriptVeyron playMusic(String sound) { this.stopMusic(); this.state.music = sound == null ? "" : sound; this.play(sound, this.state.musicCategory, 1.0F, 1.0F, this.player.posX, this.player.posY, this.player.posZ); return this; }
    public IScriptVeyron stopMusic() { if (!this.state.music.isEmpty()) { VeyronManager.stopSound(this.player, this.state.music, this.state.musicCategory); this.state.music = ""; } return this; }
    public IScriptVeyron fadeMusic(String sound, int ticks) { return this.playMusic(sound); }
    public IScriptVeyron setScreenDarkness(float value) { this.state.screenDarkness = Math.max(0.0F, Math.min(1.0F, value)); VeyronManager.syncOverlay(this.player); return this; }
    public IScriptVeyron setScreenBlur(float value) { this.state.screenBlur = Math.max(0.0F, Math.min(8.0F, value)); VeyronManager.syncOverlay(this.player); return this; }
    public IScriptVeyron clearScreenEffects() { this.state.screenDarkness = 0.0F; this.state.screenBlur = 0.0F; VeyronManager.syncOverlay(this.player); return this; }
    public IScriptVeyron fakeMessage(String message) { this.player.sendMessage(new net.minecraft.util.text.TextComponentString(message == null ? "" : message)); return this; }
    public IScriptVeyron fakeTitle(String title) { this.player.connection.sendPacket(new net.minecraft.network.play.server.SPacketTitle(net.minecraft.network.play.server.SPacketTitle.Type.TITLE, new net.minecraft.util.text.TextComponentString(title == null ? "" : title))); return this; }
    public IScriptVeyron fakeSubtitle(String subtitle) { this.player.connection.sendPacket(new net.minecraft.network.play.server.SPacketTitle(net.minecraft.network.play.server.SPacketTitle.Type.SUBTITLE, new net.minecraft.util.text.TextComponentString(subtitle == null ? "" : subtitle))); return this; }
    public IScriptVeyron fakeTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) { this.player.connection.sendPacket(new net.minecraft.network.play.server.SPacketTitle(fadeIn, stay, fadeOut)); this.fakeTitle(title); this.fakeSubtitle(subtitle); return this; }
    public IScriptVeyron fakeActionBar(String message) { this.player.connection.sendPacket(new net.minecraft.network.play.server.SPacketTitle(net.minecraft.network.play.server.SPacketTitle.Type.ACTIONBAR, new net.minecraft.util.text.TextComponentString(message == null ? "" : message))); return this; }
    public IScriptVeyron showQuestHUD(boolean enabled) { this.state.questHud = enabled; VeyronManager.syncOverlay(this.player); return this; }
    public IScriptVeyron trackQuest(String questId) { this.state.questHud = true; this.state.trackedQuest = questId == null ? "" : questId; VeyronManager.syncOverlay(this.player); return this; }
    public IScriptVeyron clearTrackedQuest() { this.state.trackedQuest = ""; VeyronManager.syncOverlay(this.player); return this; }
    public IScriptVeyron startTimer(String id, int ticks) { if (id != null) { this.getTimerState(id).start(this.now(), Math.max(0, ticks)); } return this; }
    public IScriptVeyron startTimerSeconds(String id, int seconds) { return this.startTimer(id, seconds * 20); }
    public IScriptVeyron addTimer(String id, int ticks) { if (id != null) { this.getTimerState(id).add(this.now(), ticks); } return this; }
    public IScriptVeyron pauseTimer(String id) { VeyronTimer timer = this.state.timers.get(id); if (timer != null) { timer.pause(this.now()); } return this; }
    public IScriptVeyron resumeTimer(String id) { VeyronTimer timer = this.state.timers.get(id); if (timer != null) { timer.resume(this.now()); } return this; }
    public IScriptVeyron stopTimer(String id) { this.state.timers.remove(id); return this; }
    public boolean hasTimer(String id) { return id != null && this.state.timers.containsKey(id); }
    public int getTimer(String id) { VeyronTimer timer = this.state.timers.get(id); return timer == null ? 0 : timer.getRemaining(this.now()); }
    public boolean isTimerFinished(String id) { return this.hasTimer(id) && this.getTimer(id) <= 0; }
    public String formatTimer(String id) { int ticks = this.getTimer(id); int seconds = ticks / 20; int minutes = seconds / 60; int left = seconds % 60; return String.format("%02d:%02d", minutes, left); }
    public IScriptVeyron reset() { this.state.fear = 0; this.state.tension = 0; this.state.visionLevel = 0; this.state.soundMuffle = 0.0F; this.state.speedMultiplier = 1.0F; this.state.zones.clear(); this.state.lockedActions.clear(); this.state.noises.clear(); this.state.jumpscares.clear(); VeyronManager.stopSound(this.player, this.state.music, this.state.musicCategory); this.state.music = ""; this.state.timers.clear(); this.state.screenDarkness = 0.0F; this.state.screenBlur = 0.0F; this.state.questHud = false; this.state.trackedQuest = ""; this.state.activeHud = ""; this.state.huds.clear(); this.player.removePotionEffect(MobEffects.BLINDNESS); this.player.removePotionEffect(MobEffects.NAUSEA); this.player.removePotionEffect(MobEffects.SLOWNESS); this.applySpeed(); VeyronManager.syncOverlay(this.player); return this; }

    public int countItem(String item)
    {
        Item target = item == null ? null : Item.getByNameOrId(item);

        if (target == null)
        {
            return 0;
        }

        int count = 0;

        for (ItemStack stack : this.player.inventory.mainInventory)
        {
            if (!stack.isEmpty() && stack.getItem() == target)
            {
                count += stack.getCount();
            }
        }

        for (ItemStack stack : this.player.inventory.offHandInventory)
        {
            if (!stack.isEmpty() && stack.getItem() == target)
            {
                count += stack.getCount();
            }
        }

        return count;
    }

    public boolean hasItem(String item, int count)
    {
        return this.countItem(item) >= Math.max(1, count);
    }

    public int removeItem(String item, int count)
    {
        Item target = item == null ? null : Item.getByNameOrId(item);

        if (target == null || count <= 0)
        {
            return 0;
        }

        int remaining = count;

        remaining -= this.removeFrom(this.player.inventory.mainInventory, target, remaining);

        if (remaining > 0)
        {
            remaining -= this.removeFrom(this.player.inventory.offHandInventory, target, remaining);
        }

        int removed = count - remaining;

        if (removed > 0)
        {
            this.player.inventory.markDirty();
            this.player.inventoryContainer.detectAndSendChanges();
        }

        return removed;
    }

    private int removeFrom(java.util.List<ItemStack> list, Item target, int amount)
    {
        int removed = 0;

        for (int i = 0; i < list.size() && removed < amount; i++)
        {
            ItemStack stack = list.get(i);

            if (!stack.isEmpty() && stack.getItem() == target)
            {
                int take = Math.min(stack.getCount(), amount - removed);

                stack.shrink(take);
                removed += take;

                if (stack.isEmpty())
                {
                    list.set(i, ItemStack.EMPTY);
                }
            }
        }

        return removed;
    }

    private void play(String sound, float volume, float pitch, double x, double y, double z)
    {
        this.play(sound, net.minecraft.util.SoundCategory.AMBIENT, volume, pitch, x, y, z);
    }

    private void play(String sound, net.minecraft.util.SoundCategory category, float volume, float pitch, double x, double y, double z)
    {
        SoundEvent event = sound == null ? SoundEvents.AMBIENT_CAVE : SoundEvent.REGISTRY.getObject(new ResourceLocation(sound));
        VeyronManager.playSound(this.player, event == null ? SoundEvents.AMBIENT_CAVE : event, category, x, y, z, volume, pitch);
    }

    private void applyFearEffects()
    {
        if (!this.state.fearEffects)
        {
            return;
        }

        int percent = this.state.maxFear <= 0 ? 0 : this.state.fear * 100 / this.state.maxFear;
        int vision = Math.max(this.state.visionLevel, percent >= 80 ? 3 : percent >= 55 ? 2 : percent >= 30 ? 1 : 0);

        if (vision >= 1)
        {
            this.player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 80, vision - 1, false, false));
        }

        if (vision >= 3)
        {
            this.player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 60, 0, false, false));
        }

        if (percent >= 60)
        {
            this.player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 80, percent >= 85 ? 1 : 0, false, false));
        }

        this.applySpeed();
    }

    private void applySpeed()
    {
        this.player.capabilities.setPlayerWalkSpeed(0.1F * this.state.speedMultiplier);
        this.player.sendPlayerAbilities();
    }

    private int clamp(int value, int min, int max)
    {
        return Math.max(min, Math.min(max, value));
    }

    private long now()
    {
        return this.player.world.getTotalWorldTime();
    }

    private VeyronTimer getTimerState(String id)
    {
        VeyronTimer timer = this.state.timers.get(id);

        if (timer == null)
        {
            timer = new VeyronTimer();
            this.state.timers.put(id, timer);
        }

        return timer;
    }
}
