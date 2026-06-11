package toraylife.mappetextras.modules.veyron;

import java.util.LinkedHashMap;
import java.util.Map;

public class VeyronHud
{
    public boolean visible = true;
    public int x = 12;
    public int y = 12;
    public int gap = 12;
    public final Map<String, VeyronHudElement> elements = new LinkedHashMap<String, VeyronHudElement>();
}
