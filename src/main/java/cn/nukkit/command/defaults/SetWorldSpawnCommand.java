package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;

import java.text.DecimalFormat;

public class SetWorldSpawnCommand extends VanillaCommand {
    public SetWorldSpawnCommand(String name) {
        super(name, "Sets a worlds's spawn point. If no coordinates are specified, the player's coordinates will be used.", "/setworldspawn [<x> <y> <z>]");
        this.setPermission("nukkit.command.setworldspawn");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                new CommandParameter("blockPos", CommandParamType.POSITION, true)
        });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        Level level;
        Vector3 pos;
        if (args.length == 0) {
            if (sender instanceof Player) {
                level = ((Player) sender).getLevel();
                pos = ((Player) sender).round();
            } else {
                sender.sendMessage("You can only perform this command as a player");
                return true;
            }
        } else if (args.length == 3) {
            level = sender.getServer().getDefaultLevel();
            try {
                pos = new Vector3(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
            } catch (NumberFormatException e1) {
                sender.sendMessage("Usage: " + this.usageMessage);
                return true;
            }
        } else {
            sender.sendMessage("Usage: " + this.usageMessage);
            return true;
        }
        level.setSpawnLocation(pos);
        DecimalFormat round2 = new DecimalFormat("##0.00");
        Command.broadcastCommandMessage(sender, "Set the world spawn point to (" + round2.format(pos.x) + "," + round2.format(pos.y) + "," + round2.format(pos.z) + ")");
        return true;
    }
}
