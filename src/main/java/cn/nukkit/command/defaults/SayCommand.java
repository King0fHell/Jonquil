package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.TextFormat;

public class SayCommand extends VanillaCommand {

    public SayCommand(String name) {
        super(name, "Broadcasts the given message as the sender", "/say <text>");
        this.setPermission("nukkit.command.say");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                new CommandParameter("message")
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

        String senderString;
        if (sender instanceof Player) {
            senderString = ((Player) sender).getDisplayName();
        } else if (sender instanceof ConsoleCommandSender) {
            senderString = "Server";
        } else {
            senderString = sender.getName();
        }

        String msg = "";
        for (String arg : args) {
            msg += arg + " ";
        }
        if (msg.length() > 0) {
            msg = msg.substring(0, msg.length() - 1);
        }


        sender.getServer().broadcastMessage(TextFormat.AQUA + senderString + TextFormat.GRAY + " >> " + TextFormat.WHITE + msg);
        return true;
    }
}
