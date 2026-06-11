package toraylife.mappetextras.modules.veyron.client;

import mchorse.mappet.api.quests.Quest;
import mchorse.mappet.api.quests.objectives.AbstractObjective;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class VeyronOverlayRenderer extends Gui
{
    private static final ResourceLocation BLUR_SHADER = new ResourceLocation("shaders/post/blur.json");
    private static NBTTagCompound state = new NBTTagCompound();

    public static void setState(NBTTagCompound tag)
    {
        state = tag == null ? new NBTTagCompound() : tag;
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event)
    {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL)
        {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();

        if (mc.player == null || mc.world == null)
        {
            return;
        }

        ScaledResolution resolution = event.getResolution();
        this.renderDarkness(resolution);
        this.renderHuds(mc, resolution);
        this.renderQuestHud(mc, resolution);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase != TickEvent.Phase.END)
        {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();

        if (mc.player == null || mc.world == null)
        {
            this.disableBlur(mc);
            return;
        }

        float blur = state.getFloat("Blur");

        if (blur > 0.001F)
        {
            this.enableBlur(mc, blur);
        }
        else
        {
            this.disableBlur(mc);
        }
    }

    private void renderDarkness(ScaledResolution resolution)
    {
        float darkness = state.getFloat("Darkness");

        if (darkness <= 0.001F)
        {
            return;
        }

        int alpha = Math.max(0, Math.min(255, (int) (darkness * 255.0F)));
        drawRect(0, 0, resolution.getScaledWidth(), resolution.getScaledHeight(), alpha << 24);
    }

    private void renderHuds(Minecraft mc, ScaledResolution resolution)
    {
        NBTTagCompound huds = state.getCompoundTag("Huds");

        for (String hudId : huds.getKeySet())
        {
            NBTTagCompound hud = huds.getCompoundTag(hudId);

            if (!hud.getBoolean("Visible"))
            {
                continue;
            }

            int baseX = hud.getInteger("X");
            int baseY = hud.getInteger("Y");
            List<Map.Entry<String, NBTTagCompound>> elements = new ArrayList<Map.Entry<String, NBTTagCompound>>();

            for (String key : hud.getCompoundTag("Elements").getKeySet())
            {
                elements.add(new java.util.AbstractMap.SimpleEntry<String, NBTTagCompound>(key, hud.getCompoundTag("Elements").getCompoundTag(key)));
            }

            elements.sort(Comparator.comparingInt(a -> a.getValue().getInteger("Order")));

            for (Map.Entry<String, NBTTagCompound> entry : elements)
            {
                NBTTagCompound element = entry.getValue();

                if (!element.getBoolean("Visible"))
                {
                    continue;
                }

                int x = baseX + element.getInteger("X");
                int y = baseY + element.getInteger("Y");
                String type = element.getString("Type");

                if ("text".equals(type))
                {
                    mc.fontRenderer.drawStringWithShadow(element.getString("Text"), x, y, element.getInteger("Color"));
                }
                else if ("progress".equals(type))
                {
                    int width = element.getInteger("Width");
                    int height = element.getInteger("Height");
                    double max = Math.max(1.0D, element.getDouble("Max"));
                    double value = Math.max(0.0D, Math.min(max, element.getDouble("Value")));
                    int fill = (int) Math.round(width * (value / max));

                    drawRect(x, y, x + width, y + height, element.getInteger("Background"));
                    drawRect(x, y, x + fill, y + height, element.getInteger("Color"));
                }
                else if ("icon".equals(type))
                {
                    String path = element.getString("Icon");

                    if (!StringUtils.isBlank(path))
                    {
                        mc.getTextureManager().bindTexture(new ResourceLocation(path));
                        GlStateManager.color(1F, 1F, 1F, 1F);
                        drawModalRectWithCustomSizedTexture(x, y, 0, 0, element.getInteger("Width"), element.getInteger("Height"), element.getInteger("Width"), element.getInteger("Height"));
                    }
                }
            }
        }
    }

    private void renderQuestHud(Minecraft mc, ScaledResolution resolution)
    {
        if (!state.getBoolean("QuestHUD"))
        {
            return;
        }

        ICharacter character = Character.get(mc.player);

        if (character == null)
        {
            return;
        }

        Quest quest = null;
        String tracked = state.getString("TrackedQuest");

        if (!StringUtils.isBlank(tracked))
        {
            quest = character.getQuests().getByName(tracked);
        }

        if (quest == null)
        {
            for (Map.Entry<String, Quest> entry : character.getQuests().quests.entrySet())
            {
                if (entry.getValue().visible)
                {
                    quest = entry.getValue();
                    break;
                }
            }
        }

        if (quest == null)
        {
            return;
        }

        int width = 180;
        int x = resolution.getScaledWidth() - width - 10;
        int y = 50;

        drawRect(x, y, x + width, y + 14, 0xAA000000);
        mc.fontRenderer.drawStringWithShadow(quest.getProcessedTitle(), x + 4, y + 3, 0xFFFFFF);

        y += 18;

        for (AbstractObjective objective : quest.objectives)
        {
            String text = "- " + objective.stringify(mc.player);
            int color = objective.isComplete(mc.player) ? 0xFFFFFF : 0xAAAAAA;

            for (String line : mc.fontRenderer.listFormattedStringToWidth(text, width - 8))
            {
                mc.fontRenderer.drawStringWithShadow(line, x + 4, y, color);
                y += 10;
            }
        }
    }

    private void enableBlur(Minecraft mc, float blur)
    {
        if (!mc.entityRenderer.isShaderActive())
        {
            mc.entityRenderer.loadShader(BLUR_SHADER);
        }

        try
        {
            ShaderGroup group = mc.entityRenderer.getShaderGroup();

            if (group == null)
            {
                return;
            }

            Field field = ShaderGroup.class.getDeclaredField("listShaders");
            field.setAccessible(true);
            List<?> shaders = (List<?>) field.get(group);

            for (Object object : shaders)
            {
                if (object instanceof Shader)
                {
                    Shader shader = (Shader) object;

                    shader.getShaderManager().getShaderUniformOrDefault("Radius").set(blur);
                }
            }
        }
        catch (Exception e)
        {}
    }

    private void disableBlur(Minecraft mc)
    {
        if (mc.entityRenderer.isShaderActive())
        {
            mc.entityRenderer.stopUseShader();
        }
    }
}
