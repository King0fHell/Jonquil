package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.event.player.PlayerKickEvent;
import cn.nukkit.utils.TextFormat;

public class KickCommand extends VanillaCommand {

    public KickCommand(String name) {
        super(name, "Removes the specified player from the server", "/kick <player> [reason ...]");
        this.setPermission("nukkit.command.kick");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                new CommandParameter("player", CommandParamType.TARGET, false),
                new CommandParameter("reason", true)
        });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage("Usage: " + this.usageMessage);
            return false;
        }

        String name = args[0];

        String reason = "";
        for (int i = 1; i < args.length; i++) {
            reason += args[i] + " ";
        }

        if (reason.length() > 0) {
            reason = reason.substring(0, reason.length() - 1);
        }

        Player player = sender.getServer().getPlayer(name);
        if (player != null) {
            player.kick(PlayerKickEvent.Reason.KICKED_BY_ADMIN, reason);
            if (reason.length() >= 1) {
                Command.broadcastCommandMessage(sender, "Kicked "  + player.getName() + " from the game: '" + reason + "'");
            } else {
                Command.broadcastCommandMessage(sender, "Kicked " + player.getName() + " from the game");
            }
        } else {
            sender.sendMessage(TextFormat.RED + "That player cannot be found");
        }

        return true;
    }
}
