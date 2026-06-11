package toraylife.mappetextras.modules.veyron.scripts.code;

import net.minecraft.entity.player.EntityPlayerMP;
import toraylife.mappetextras.modules.veyron.VeyronHud;
import toraylife.mappetextras.modules.veyron.VeyronHudElement;
import toraylife.mappetextras.modules.veyron.VeyronManager;
import toraylife.mappetextras.modules.veyron.VeyronPlayerState;
import toraylife.mappetextras.modules.veyron.scripts.user.IScriptVeyronHUD;

public class ScriptVeyronHUD implements IScriptVeyronHUD
{
    private final EntityPlayerMP player;
    private final VeyronPlayerState state;

    public ScriptVeyronHUD(EntityPlayerMP player)
    {
        this.player = player;
        this.state = VeyronManager.player(player);
    }

    @Override
    public IScriptVeyronHUD show(String id)
    {
        VeyronHud hud = this.getHud(id);
        hud.visible = true;
        this.state.activeHud = id == null ? "" : id;
        this.sync();

        return this;
    }

    @Override
    public IScriptVeyronHUD hide(String id)
    {
        VeyronHud hud = this.getHud(id);
        hud.visible = false;

        if (id != null && id.equals(this.state.activeHud))
        {
            this.state.activeHud = "";
        }

        this.sync();

        return this;
    }

    @Override
    public IScriptVeyronHUD use(String id)
    {
        this.getHud(id);
        this.state.activeHud = id == null ? "" : id;
        this.sync();

        return this;
    }

    @Override
    public boolean isVisible(String id)
    {
        return this.getHud(id).visible;
    }

    @Override
    public IScriptVeyronHUD clear(String id)
    {
        this.getHud(id).elements.clear();
        this.sync();

        return this;
    }

    @Override
    public IScriptVeyronHUD setLayerPosition(String id, int x, int y)
    {
        VeyronHud hud = this.getHud(id);

        hud.x = x;
        hud.y = y;
        this.sync();

        return this;
    }

    @Override
    public IScriptVeyronHUD setLayerGap(String id, int gap)
    {
        this.getHud(id).gap = Math.max(0, gap);
        this.sync();

        return this;
    }

    @Override
    public IScriptVeyronHUD setText(String id, String text)
    {
        VeyronHudElement element = this.getElement(id, "text");
        element.text = text == null ? "" : text;
        this.sync();

        return this;
    }

    @Override
    public IScriptVeyronHUD setProgress(String id, double value, double max)
    {
        VeyronHudElement element = this.getElement(id, "progress");
        element.value = value;
        element.max = max <= 0 ? 1.0D : max;
        this.sync();

        return this;
    }

    @Override
    public IScriptVeyronHUD setIcon(String id, String texture)
    {
        VeyronHudElement element = this.getElement(id, "icon");
        element.icon = texture == null ? "" : texture;
        this.sync();

        return this;
    }

    @Override
    public IScriptVeyronHUD setPosition(String id, int x, int y)
    {
        VeyronHudElement element = this.getElement(id, null);
        element.x = x;
        element.y = y;
        this.sync();

        return this;
    }

    @Override
    public IScriptVeyronHUD setSize(String id, int width, int height)
    {
        VeyronHudElement element = this.getElement(id, null);
        element.width = Math.max(1, width);
        element.height = Math.max(1, height);
        this.sync();

        return this;
    }

    @Override
    public IScriptVeyronHUD setColor(String id, int color)
    {
        this.getElement(id, null).color = color;
        this.sync();

        return this;
    }

    @Override
    public IScriptVeyronHUD setBackground(String id, int color)
    {
        this.getElement(id, null).background = color;
        this.sync();

        return this;
    }

    @Override
    public IScriptVeyronHUD setVisible(String id, boolean visible)
    {
        this.getElement(id, null).visible = visible;
        this.sync();

        return this;
    }

    @Override
    public IScriptVeyronHUD remove(String id)
    {
        this.getHud(this.getActiveHudId()).elements.remove(id);
        this.sync();

        return this;
    }

    private VeyronHudElement getElement(String id, String type)
    {
        VeyronHud hud = this.getHud(this.getActiveHudId());
        VeyronHudElement element = hud.elements.get(id);

        if (element == null)
        {
            element = new VeyronHudElement();
            element.type = type == null ? "text" : type;
            element.order = hud.elements.size();
            element.y = element.order * hud.gap;
            if ("icon".equals(element.type))
            {
                element.width = 16;
                element.height = 16;
            }
            hud.elements.put(id, element);
        }
        else if (type != null)
        {
            element.type = type;
        }

        return element;
    }

    private VeyronHud getHud(String id)
    {
        String key = id == null || id.isEmpty() ? "default" : id;
        VeyronHud hud = this.state.huds.get(key);

        if (hud == null)
        {
            hud = new VeyronHud();
            this.state.huds.put(key, hud);
        }

        return hud;
    }

    private String getActiveHudId()
    {
        if (this.state.activeHud == null || this.state.activeHud.isEmpty())
        {
            this.state.activeHud = "default";
        }

        return this.state.activeHud;
    }

    private void sync()
    {
        VeyronManager.syncOverlay(this.player);
    }
}
