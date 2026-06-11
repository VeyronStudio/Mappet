package toraylife.mappetextras.modules.veyron;

public class VeyronTimer
{
    public long endTick;
    public int pausedTicks;
    public boolean paused;

    public void start(long now, int ticks)
    {
        this.paused = false;
        this.pausedTicks = Math.max(0, ticks);
        this.endTick = now + this.pausedTicks;
    }

    public int getRemaining(long now)
    {
        if (this.paused)
        {
            return Math.max(0, this.pausedTicks);
        }

        return (int) Math.max(0, this.endTick - now);
    }

    public void add(long now, int ticks)
    {
        if (this.paused)
        {
            this.pausedTicks = Math.max(0, this.pausedTicks + ticks);
        }
        else
        {
            this.endTick += ticks;
        }
    }

    public void pause(long now)
    {
        if (this.paused)
        {
            return;
        }

        this.pausedTicks = this.getRemaining(now);
        this.paused = true;
    }

    public void resume(long now)
    {
        if (!this.paused)
        {
            return;
        }

        this.endTick = now + this.pausedTicks;
        this.paused = false;
    }
}
