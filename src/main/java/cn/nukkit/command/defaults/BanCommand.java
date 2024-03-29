package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.event.player.PlayerKickEvent;

public class BanCommand extends VanillaCommand {

    public BanCommand(String name) {
        super(name, "Prevents the specified player from using this server", "/ban <name> [reason ...]");
        this.setPermission("nukkit.command.ban.player");
        this.commandParameters.clear();
        this.commandParameters.put("default",
                new CommandParameter[]{
                        new CommandParameter("player", CommandParamType.TARGET, false),
                        new CommandParameter("reason", CommandParamType.STRING, true)
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

        sender.getServer().getNameBans().addBan(name, reason, null, sender.getName());

        Player player = sender.getServer().getPlayerExact(name);
        if (player != null) {
            player.kick(PlayerKickEvent.Reason.NAME_BANNED, !reason.isEmpty() ? "Banned by admin. Reason: " + reason : "Banned by admin");
        }

        Command.broadcastCommandMessage(sender, "Banned player " + (player != null ? player.getName() : name));

        return true;
    }
}
