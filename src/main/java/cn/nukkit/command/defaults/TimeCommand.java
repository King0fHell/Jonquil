package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.Level;
import cn.nukkit.utils.TextFormat;

public class TimeCommand extends VanillaCommand {

    public TimeCommand(String name) {
        super(name, "Changes the time on each world", "/time <set|add> <value> OR /time <start|stop|query>");
        this.setPermission("nukkit.command.time.add;" +
                "nukkit.command.time.set;" +
                "nukkit.command.time.start;" +
                "nukkit.command.time.stop");
        this.commandParameters.clear();
        this.commandParameters.put("1arg", new CommandParameter[]{
                new CommandParameter("start|stop", CommandParamType.STRING, false)
        });
        this.commandParameters.put("2args", new CommandParameter[]{
                new CommandParameter("add|set", CommandParamType.STRING, false),
                new CommandParameter("value", CommandParamType.INT, false)
        });
        this.commandParameters.put("2args_", new CommandParameter[]{
                new CommandParameter("add|set", CommandParamType.STRING, false),
                new CommandParameter("value", CommandParamType.STRING, false)
        });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("Usage: " + this.usageMessage);

            return false;
        }

        if ("start".equals(args[0])) {
            if (!sender.hasPermission("nukkit.command.time.start")) {
                sender.sendMessage(TextFormat.RED + "You do not have permission to use this command");

                return true;
            }
            for (Level level : sender.getServer().getLevels().values()) {
                level.checkTime();
                level.startTime();
                level.checkTime();
            }
            Command.broadcastCommandMessage(sender, "Restarted the time");
            return true;
        } else if ("stop".equals(args[0])) {
            if (!sender.hasPermission("nukkit.command.time.stop")) {
                sender.sendMessage(TextFormat.RED + "You do not have permission to use this command");

                return true;
            }
            for (Level level : sender.getServer().getLevels().values()) {
                level.checkTime();
                level.stopTime();
                level.checkTime();
            }
            Command.broadcastCommandMessage(sender, "Stopped the time");
            return true;
        } else if ("query".equals(args[0])) {
            if (!sender.hasPermission("nukkit.command.time.query")) {
                sender.sendMessage(TextFormat.RED + "You do not have permission to use this command");

                return true;
            }

            Level level;
            if (sender instanceof Player) {
                level = ((Player) sender).getLevel();
            } else {
                level = sender.getServer().getDefaultLevel();
            }
            sender.sendMessage("Time is " + level.getTime());
            return true;
        }


        if (args.length < 2) {
            sender.sendMessage("Usage: " + this.usageMessage);

            return false;
        }

        if ("set".equals(args[0])) {
            if (!sender.hasPermission("nukkit.command.time.set")) {
                sender.sendMessage(TextFormat.RED + "You do not have permission to use this command");

                return true;
            }

            int value;
            if ("day".equals(args[1])) {
                value = Level.TIME_DAY;
            } else if ("night".equals(args[1])) {
                value = Level.TIME_NIGHT;
            } else if ("midnight".equals(args[1])) {
                value = Level.TIME_MIDNIGHT;
            } else if ("noon".equals(args[1])) {
                value = Level.TIME_NOON;
            } else if ("sunrise".equals(args[1])) {
                value = Level.TIME_SUNRISE;
            } else if ("sunset".equals(args[1])) {
                value = Level.TIME_SUNSET;
            } else {
                try {
                    value = Math.max(0, Integer.parseInt(args[1]));
                } catch (Exception e) {
                    sender.sendMessage("Usage: " + this.usageMessage);
                    return true;
                }
            }

            for (Level level : sender.getServer().getLevels().values()) {
                level.checkTime();
                level.setTime(value);
                level.checkTime();
            }
            Command.broadcastCommandMessage(sender, "Set the time to " + value);
        } else if ("add".equals(args[0])) {
            if (!sender.hasPermission("nukkit.command.time.add")) {
                sender.sendMessage(TextFormat.RED + "You do not have permission to use this command");

                return true;
            }

            int value;
            try {
                value = Math.max(0, Integer.parseInt(args[1]));
            } catch (Exception e) {
                sender.sendMessage("Usage: " + this.usageMessage);
                return true;
            }

            for (Level level : sender.getServer().getLevels().values()) {
                level.checkTime();
                level.setTime(level.getTime() + value);
                level.checkTime();
            }
            Command.broadcastCommandMessage(sender, "Added " + value + " to the time");
        } else {
            sender.sendMessage("Usage: " + this.usageMessage);
        }

        return true;
    }
}
