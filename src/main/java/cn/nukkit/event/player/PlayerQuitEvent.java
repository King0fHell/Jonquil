package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.HandlerList;

public class PlayerQuitEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    protected String quitMessage;
    protected boolean autoSave = true;
    protected String reason;


    public PlayerQuitEvent(Player player, String quitMessage) {
        this(player, quitMessage, true);
    }

    public PlayerQuitEvent(Player player, String quitMessage, boolean autoSave) {
        this(player, quitMessage, autoSave, "No reason");
    }

    public PlayerQuitEvent(Player player, String quitMessage, boolean autoSave, String reason) {
        this.player = player;
        this.quitMessage = quitMessage;
        this.autoSave = autoSave;
        this.reason = reason;
    }

    public String getQuitMessage() {
        return quitMessage;
    }

    public boolean getAutoSave() {
        return this.autoSave;
    }

    public void setAutoSave() {
        this.setAutoSave(true);
    }

    public void setAutoSave(boolean autoSave) {
        this.autoSave = autoSave;
    }

    public String getReason() {
        return reason;
    }
}
