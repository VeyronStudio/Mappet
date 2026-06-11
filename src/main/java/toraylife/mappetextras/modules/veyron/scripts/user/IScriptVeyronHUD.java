package toraylife.mappetextras.modules.veyron.scripts.user;

public interface IScriptVeyronHUD
{
    public IScriptVeyronHUD show(String id);
    public IScriptVeyronHUD hide(String id);
    public IScriptVeyronHUD use(String id);
    public boolean isVisible(String id);
    public IScriptVeyronHUD clear(String id);
    public IScriptVeyronHUD setLayerPosition(String id, int x, int y);
    public IScriptVeyronHUD setLayerGap(String id, int gap);
    public IScriptVeyronHUD setText(String id, String text);
    public IScriptVeyronHUD setProgress(String id, double value, double max);
    public IScriptVeyronHUD setIcon(String id, String texture);
    public IScriptVeyronHUD setPosition(String id, int x, int y);
    public IScriptVeyronHUD setSize(String id, int width, int height);
    public IScriptVeyronHUD setColor(String id, int color);
    public IScriptVeyronHUD setBackground(String id, int color);
    public IScriptVeyronHUD setVisible(String id, boolean visible);
    public IScriptVeyronHUD remove(String id);
}
