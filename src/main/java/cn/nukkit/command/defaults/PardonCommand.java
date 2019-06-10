package cn.nukkit.command.defaults;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;

public class PardonCommand extends VanillaCommand {

    public PardonCommand(String name) {
        super(name, "Allows the specified player to use this server", "/pardon-ip <address>");
        this.setPermission("nukkit.command.unban.player");
        this.setAliases(new String[]{"unban"});
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                new CommandParameter("player", CommandParamType.TARGET, false)
        });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("Usage: " + this.usageMessage);

            return false;
        }

        sender.getServer().getNameBans().remove(args[0]);

        Command.broadcastCommandMessage(sender, "Unbanned player " + args[0]);

        return true;
    }
}
