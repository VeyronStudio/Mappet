package toraylife.mappetextras.modules.veyron;

public enum VeyronAction
{
    BREAK,
    PLACE,
    ATTACK,
    INTERACT,
    OPEN_DOOR;

    public static VeyronAction fromString(String action)
    {
        if (action == null)
        {
            return null;
        }

        try
        {
            return VeyronAction.valueOf(action.trim().toUpperCase());
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
