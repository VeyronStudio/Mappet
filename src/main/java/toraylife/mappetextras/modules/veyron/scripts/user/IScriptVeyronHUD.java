package toraylife.mappetextras.modules.veyron.scripts.user;

public interface IScriptVeyronHUD
{
    /**
     * Makes the given HUD layer visible and marks it as the active layer that
     * subsequent element calls operate on. Creates the layer if it doesn't exist.
     *
     * <pre>{@code
     *    function main(c)
     *    {
     *        var hud = c.getSubject().getHud();
     *        hud.show("stats").setText("title", "Sanity");
     *    }
     * }</pre>
     *
     * @param id layer id (empty/null uses "default").
     */
    public IScriptVeyronHUD show(String id);

    /**
     * Hides the given HUD layer without removing its elements.
     *
     * @param id layer id.
     */
    public IScriptVeyronHUD hide(String id);

    /**
     * Marks the given layer as the active one (creating it if needed) without
     * changing its visibility. Following element calls operate on this layer.
     *
     * @param id layer id.
     */
    public IScriptVeyronHUD use(String id);

    /**
     * Returns whether the given HUD layer is currently visible.
     *
     * @param id layer id.
     * @return true if the layer is visible.
     */
    public boolean isVisible(String id);

    /**
     * Removes all elements from the given HUD layer.
     *
     * @param id layer id.
     */
    public IScriptVeyronHUD clear(String id);

    /**
     * Sets the on-screen position of a HUD layer (its top-left origin).
     *
     * @param id layer id.
     * @param x x position in pixels.
     * @param y y position in pixels.
     */
    public IScriptVeyronHUD setLayerPosition(String id, int x, int y);

    /**
     * Sets the vertical gap used when auto-stacking new elements in a layer.
     *
     * @param id layer id.
     * @param gap gap in pixels (minimum 0).
     */
    public IScriptVeyronHUD setLayerGap(String id, int gap);

    /**
     * Sets the text of a text element on the active layer, creating it as a
     * text element if it doesn't exist.
     *
     * <pre>{@code
     *    function main(c)
     *    {
     *        c.getSubject().getHud().use("stats").setText("clock", "23:58");
     *    }
     * }</pre>
     *
     * @param id element id.
     * @param text text to display.
     */
    public IScriptVeyronHUD setText(String id, String text);

    /**
     * Sets the value and maximum of a progress-bar element on the active layer,
     * creating it as a progress element if it doesn't exist.
     *
     * <pre>{@code
     *    function main(c)
     *    {
     *        c.getSubject().getHud().use("stats").setProgress("sanity", 40, 100);
     *    }
     * }</pre>
     *
     * @param id element id.
     * @param value current value.
     * @param max maximum value (values of 0 or less are treated as 1).
     */
    public IScriptVeyronHUD setProgress(String id, double value, double max);

    /**
     * Sets the texture of an icon element on the active layer, creating it as an
     * icon element if it doesn't exist.
     *
     * @param id element id.
     * @param texture texture resource location (for example "mymod:textures/gui/icon.png").
     */
    public IScriptVeyronHUD setIcon(String id, String texture);

    /**
     * Sets the position of an element relative to its layer's origin.
     *
     * @param id element id.
     * @param x x offset in pixels.
     * @param y y offset in pixels.
     */
    public IScriptVeyronHUD setPosition(String id, int x, int y);

    /**
     * Sets the size of an element (used by progress bars and icons).
     *
     * @param id element id.
     * @param width width in pixels (minimum 1).
     * @param height height in pixels (minimum 1).
     */
    public IScriptVeyronHUD setSize(String id, int width, int height);

    /**
     * Sets the main color of an element (text color, progress fill or icon tint)
     * as an ARGB integer.
     *
     * @param id element id.
     * @param color ARGB color.
     */
    public IScriptVeyronHUD setColor(String id, int color);

    /**
     * Sets the background color of an element (for example the empty part of a
     * progress bar) as an ARGB integer.
     *
     * @param id element id.
     * @param color ARGB color.
     */
    public IScriptVeyronHUD setBackground(String id, int color);

    /**
     * Toggles the visibility of a single element on the active layer.
     *
     * @param id element id.
     * @param visible true to show the element.
     */
    public IScriptVeyronHUD setVisible(String id, boolean visible);

    /**
     * Removes a single element from the active layer.
     *
     * @param id element id.
     */
    public IScriptVeyronHUD remove(String id);
}
