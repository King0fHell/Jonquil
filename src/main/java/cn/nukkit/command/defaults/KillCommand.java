package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause;
import cn.nukkit.level.Level;
import cn.nukkit.utils.TextFormat;

import java.util.StringJoiner;

public class KillCommand extends VanillaCommand {

    public KillCommand(String name) {
        super(name, "Commit suicide or kill other players", "/kill [player]", new String[]{"suicide"});
        this.setPermission("nukkit.command.kill.self;"
                + "nukkit.command.kill.other");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                new CommandParameter("player", CommandParamType.TARGET, true)
        });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        if (args.length >= 2) {
            sender.sendMessage("Usage: " + this.usageMessage);
            return false;
        }
        if (args.length == 1) {
            if (!sender.hasPermission("nukkit.command.kill.other")) {
                sender.sendMessage(TextFormat.RED + "You do not have permission to use this command");
                return true;
            }
            Player player = sender.getServer().getPlayer(args[0]);
            if (player != null) {
                EntityDamageEvent ev = new EntityDamageEvent(player, DamageCause.SUICIDE, 1000);
                sender.getServer().getPluginManager().callEvent(ev);
                if (ev.isCancelled()) {
                    return true;
                }
                player.setLastDamageCause(ev);
                player.setHealth(0);
                Command.broadcastCommandMessage(sender, "Killed " + player.getName());
            } else if (args[0].equals("@e")) {
                StringJoiner joiner = new StringJoiner(", ");
                for (Level level : Server.getInstance().getLevels().values()) {
                    for (Entity entity : level.getEntities()) {
                        if (!(entity instanceof Player)) {
                            joiner.add(entity.getName());
                            entity.close();
                        }
                    }
                }
                String entities = joiner.toString();
                sender.sendMessage("Killed " + (entities.isEmpty() ? "0" : entities));
            } else if (args[0].equals("@s")) {
                if (!sender.hasPermission("nukkit.command.kill.self")) {
                    sender.sendMessage(TextFormat.RED + "You do not have permission to use this command");
                    return true;
                }
                EntityDamageEvent ev = new EntityDamageEvent((Player) sender, DamageCause.SUICIDE, 1000);
                sender.getServer().getPluginManager().callEvent(ev);
                if (ev.isCancelled()) {
                    return true;
                }
                ((Player) sender).setLastDamageCause(ev);
                ((Player) sender).setHealth(0);
                sender.sendMessage("Killed " + sender.getName());
            } else if (args[0].equals("@a")) {
                if (!sender.hasPermission("nukkit.command.kill.other")) {
                    sender.sendMessage(TextFormat.RED + "You do not have permission to use this command");
                    return true;
                }
                for (Level level : Server.getInstance().getLevels().values()) {
                    for (Entity entity : level.getEntities()) {
                        if (entity instanceof Player) {
                            entity.setHealth(0);
                        }
                    }
                }
                sender.sendMessage(TextFormat.GOLD + "Killed all players");
            } else {
                sender.sendMessage(TextFormat.RED + "That player cannot be found");
            }
            return true;
        }
        if (sender instanceof Player) {
            if (!sender.hasPermission("nukkit.command.kill.self")) {
                sender.sendMessage(TextFormat.RED + "You do not have permission to use this command");
                return true;
            }
            EntityDamageEvent ev = new EntityDamageEvent((Player) sender, DamageCause.SUICIDE, 1000);
            sender.getServer().getPluginManager().callEvent(ev);
            if (ev.isCancelled()) {
                return true;
            }
            ((Player) sender).setLastDamageCause(ev);
            ((Player) sender).setHealth(0);
            sender.sendMessage("Killed " + sender.getName());
        } else {
            sender.sendMessage("Usage: " + this.usageMessage);
            return false;
        }
        return true;
    }
}
