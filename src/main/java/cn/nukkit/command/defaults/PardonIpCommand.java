package cn.nukkit.command.defaults;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;

import java.util.regex.Pattern;

public class PardonIpCommand extends VanillaCommand {

    public PardonIpCommand(String name) {
        super(name, "Allows the specified IP address to use this server", "/pardon-ip <address>");
        this.setPermission("nukkit.command.unban.ip");
        this.setAliases(new String[]{"unbanip", "unban-ip", "pardonip"});
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                new CommandParameter("ip")
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

        String value = args[0];

        if (Pattern.matches("^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$", value)) {
            sender.getServer().getIPBans().remove(value);
            sender.getServer().getNetwork().unblockAddress(value);

            Command.broadcastCommandMessage(sender, "Unbanned IP address " + value);
        } else {

            sender.sendMessage("You have entered an invalid IP address");
        }

        return true;
    }
}
