package cn.nukkit.command.defaults;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.TextFormat;

public class WhitelistCommand extends VanillaCommand {

    public WhitelistCommand(String name) {
        super(name, "Manages the list of players allowed to use this server", "/whitelist <on|off|list|add|remove|reload>");
        this.setPermission(
                "nukkit.command.whitelist.reload;" +
                        "nukkit.command.whitelist.enable;" +
                        "nukkit.command.whitelist.disable;" +
                        "nukkit.command.whitelist.list;" +
                        "nukkit.command.whitelist.add;" +
                        "nukkit.command.whitelist.remove"
        );
        this.commandParameters.clear();
        this.commandParameters.put("1arg", new CommandParameter[]{
                new CommandParameter("on|off|list|reload", CommandParamType.STRING, false)
        });
        this.commandParameters.put("2args", new CommandParameter[]{
                new CommandParameter("add|remove", CommandParamType.STRING, false),
                new CommandParameter("player", CommandParamType.TARGET, false)
        });
    }


    @Override
    @SuppressWarnings("deprecation")
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        if (args.length == 0 || args.length > 2) {
            sender.sendMessage("Usage: " + this.usageMessage);
            return true;
        }

        if (args.length == 1) {
            if (this.badPerm(sender, args[0].toLowerCase())) {
                return false;
            }
            switch (args[0].toLowerCase()) {
                case "reload":
                    sender.getServer().reloadWhitelist();
                    Command.broadcastCommandMessage(sender,"Reloaded the whitelist");

                    return true;
                case "on":
                    sender.getServer().setPropertyBoolean("white-list", true);
                    Command.broadcastCommandMessage(sender,"Turned on the whitelist");

                    return true;
                case "off":
                    sender.getServer().setPropertyBoolean("white-list", false);
                    Command.broadcastCommandMessage(sender,"Turned off the whitelist");

                    return true;
                case "list":
                    String result = "";
                    int count = 0;
                    for (String player : sender.getServer().getWhitelist().getAll().keySet()) {
                        result += player + ", ";
                        ++count;
                    }
                    sender.sendMessage("There are " + count + " (out of " + count + " seen) whitelisted players:");
                    sender.sendMessage(result.length() > 0 ? result.substring(0, result.length() - 2) : "");

                    return true;

                case "add":
                    sender.sendMessage("Usage: /whitelist add <player>");
                    return true;

                case "remove":
                    sender.sendMessage("Usage: /whitelist remove <player>");
                    return true;
            }
        } else if (args.length == 2) {
            if (this.badPerm(sender, args[0].toLowerCase())) {
                return false;
            }
            switch (args[0].toLowerCase()) {
                case "add":
                    sender.getServer().getOfflinePlayer(args[1]).setWhitelisted(true);
                    Command.broadcastCommandMessage(sender, "Added " + args[1] + " to the whitelist");

                    return true;
                case "remove":
                    sender.getServer().getOfflinePlayer(args[1]).setWhitelisted(false);
                    Command.broadcastCommandMessage(sender, "Removed "+ args[1] + " from the whitelist");

                    return true;
            }
        }

        return true;
    }

    private boolean badPerm(CommandSender sender, String perm) {
        if (!sender.hasPermission("nukkit.command.whitelist" + perm)) {
            sender.sendMessage(TextFormat.RED + "You do not have permission to use this command");

            return true;
        }

        return false;
    }
}
