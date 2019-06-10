package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;

public class PlayerKickEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    public enum Reason {
        NEW_CONNECTION,
        KICKED_BY_ADMIN,
        NOT_WHITELISTED,
        IP_BANNED,
        NAME_BANNED,
        INVALID_PVE,
        LOGIN_TIMEOUT,
        SERVER_FULL,
        FLYING_DISABLED,
        UNKNOWN;

        @Override
        public String toString() {
            return this.name();
        }
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    protected String quitMessage;

    protected final Reason reason;
    protected final String reasonString;

    @Deprecated
    public PlayerKickEvent(Player player, Reason reason, String reasonString, String quitMessage) {
        this.player = player;
        this.quitMessage = quitMessage;
        this.reason = reason;
        this.reasonString = reason.name();
    }

    public String getReason() {
        return reasonString;
    }

    public Reason getReasonEnum() {
        return this.reason;
    }

    public String getQuitMessage() {
        return quitMessage;
    }

    @Deprecated
    public void setQuitMessage(String quitMessage) {
        this.quitMessage = quitMessage;
    }

}
