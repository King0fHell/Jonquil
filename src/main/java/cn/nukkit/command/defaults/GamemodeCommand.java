package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.TextFormat;

public class GamemodeCommand extends VanillaCommand {

    public GamemodeCommand(String name) {
        super(name, "Changes the player to a specific game mode", "/gamemode <mode> [player]",
                new String[]{"gm"});
        this.setPermission("nukkit.command.gamemode.survival;" +
                "nukkit.command.gamemode.creative;" +
                "nukkit.command.gamemode.adventure;" +
                "nukkit.command.gamemode.spectator;" +
                "nukkit.command.gamemode.other");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                new CommandParameter("mode", CommandParamType.INT, false),
                new CommandParameter("player", CommandParamType.TARGET, true)
        });
        this.commandParameters.put("byString", new CommandParameter[]{
                new CommandParameter("mode", new String[]{"survival", "s", "creative", "c",
                        "adventure", "a", "spectator", "spc", "view", "v"}),
                new CommandParameter("player", CommandParamType.TARGET, true)
        });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Usage: " + this.usageMessage);
            return false;
        }

        int gameMode = Server.getGamemodeFromString(args[0]);
        if (gameMode == -1) {
            sender.sendMessage("Unknown game mode");
            return true;
        }

        CommandSender target = sender;
        if (args.length > 1) {
            if (sender.hasPermission("nukkit.command.gamemode.other")) {
                target = sender.getServer().getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(TextFormat.RED + "That player cannot be found");
                    return true;
                }
            } else {
                sender.sendMessage(TextFormat.RED + "You do not have permission to use this command");
                return true;
            }
        } else if (!(sender instanceof Player)) {
            sender.sendMessage("Usage: " + this.usageMessage);
            return true;
        }

        if ((gameMode == 0 && !sender.hasPermission("nukkit.command.gamemode.survival")) ||
                (gameMode == 1 && !sender.hasPermission("nukkit.command.gamemode.creative")) ||
                (gameMode == 2 && !sender.hasPermission("nukkit.command.gamemode.adventure")) ||
                (gameMode == 3 && !sender.hasPermission("nukkit.command.gamemode.spectator"))) {
            sender.sendMessage(TextFormat.RED + "You do not have permission to use this command");
            return true;
        }

        if (!((Player) target).setGamemode(gameMode)) {
            sender.sendMessage("Game mode update for " + target.getName() + " failed");
        } else {
            if (target.equals(sender)) {
                Command.broadcastCommandMessage(sender, "Set own game mode to " + Server.getGamemodeString(gameMode));
            } else {
                target.sendMessage("gameMode.changed");
                Command.broadcastCommandMessage(sender, "Set  " + target.getName() + "'s game mode to " + Server.getGamemodeString(gameMode));
            }
        }

        return true;
    }
}
