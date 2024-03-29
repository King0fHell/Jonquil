package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.level.Location;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.utils.TextFormat;

public class TeleportCommand extends VanillaCommand {
    public TeleportCommand(String name) {
        super(name, "Teleports the given player (or yourself) to another player or coordinates", "/tp [target player] <destination player> OR /tp [target player] <x> <y> <z> [<y-rot> <x-rot>]");
        this.setPermission("nukkit.command.teleport");
        this.commandParameters.clear();
        this.commandParameters.put("->Player", new CommandParameter[]{
                new CommandParameter("player", CommandParamType.TARGET, false),
        });
        this.commandParameters.put("Player->Player", new CommandParameter[]{
                new CommandParameter("player", CommandParamType.TARGET, false),
                new CommandParameter("target", CommandParamType.TARGET, false),
        });
        this.commandParameters.put("Player->Pos", new CommandParameter[]{
                new CommandParameter("player", CommandParamType.TARGET, false),
                new CommandParameter("blockPos", CommandParamType.POSITION, false),
        });
        this.commandParameters.put("->Pos", new CommandParameter[]{
                new CommandParameter("blockPos", CommandParamType.POSITION, false),
        });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        if (args.length < 1 || args.length > 6) {
            sender.sendMessage("Usage: " + this.usageMessage);
            return true;
        }
        CommandSender target;
        CommandSender origin = sender;
        if (args.length == 1 || args.length == 3) {
            if (sender instanceof Player) {
                target = sender;
            } else {
                sender.sendMessage("You can only perform this command as a player");
                return true;
            }
            if (args.length == 1) {
                target = sender.getServer().getPlayer(args[0].replace("@s", sender.getName()));
                if (target == null) {
                    sender.sendMessage(TextFormat.RED + "Can't find player " + args[0]);
                    return true;
                }
            }
        } else {
            target = sender.getServer().getPlayer(args[0].replace("@s", sender.getName()));
            if (target == null) {
                sender.sendMessage(TextFormat.RED + "Can't find player " + args[0]);
                return true;
            }
            if (args.length == 2) {
                origin = target;
                target = sender.getServer().getPlayer(args[1].replace("@s", sender.getName()));
                if (target == null) {
                    sender.sendMessage(TextFormat.RED + "Can't find player " + args[1]);
                    return true;
                }
            }
        }
        if (args.length < 3) {
            ((Player) origin).teleport((Player) target, PlayerTeleportEvent.TeleportCause.COMMAND);
            Command.broadcastCommandMessage(sender, "Teleported " + origin.getName() + " to " + target.getName());
            return true;
        } else if (((Player) target).getLevel() != null) {
            int pos;
            if (args.length == 4 || args.length == 6) {
                pos = 1;
            } else {
                pos = 0;
            }
            double x;
            double y;
            double z;
            double yaw;
            double pitch;
            try {
                x = Double.parseDouble(args[pos++].replace("~", "" + ((Player) target).x));
                y = Double.parseDouble(args[pos++].replace("~", "" + ((Player) target).y));
                z = Double.parseDouble(args[pos++].replace("~", "" + ((Player) target).z));
                yaw = ((Player) target).getYaw();
                pitch = ((Player) target).getPitch();
            } catch (NumberFormatException e1) {
                sender.sendMessage("Usage: " + this.usageMessage);
                return true;
            }
            if (y < 0) y = 0;
            if (y > 256) y = 256;
            if (args.length == 6 || (args.length == 5 && pos == 3)) {
                yaw = Integer.parseInt(args[pos++]);
                pitch = Integer.parseInt(args[pos++]);
            }
            ((Player) target).teleport(new Location(x, y, z, yaw, pitch, ((Player) target).getLevel()), PlayerTeleportEvent.TeleportCause.COMMAND);
            Command.broadcastCommandMessage(sender, "Teleported " + origin.getName() + " to " + target.getName() + ", (" + NukkitMath.round(x, 2) + ", " + NukkitMath.round(y, 2) + ", " + NukkitMath.round(z, 2) + ")");
            return true;
        }
        sender.sendMessage("Usage: " + this.usageMessage);
        return true;
    }
}
