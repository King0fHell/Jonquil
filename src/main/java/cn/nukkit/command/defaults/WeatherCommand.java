package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.Level;

public class WeatherCommand extends VanillaCommand {

    private java.util.Random rand = new java.util.Random();

    public WeatherCommand(String name) {
        super(name, "Sets the weather in current world", "/weather <clear|rain|thunder> [duration in seconds]");
        this.setPermission("nukkit.command.weather");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                new CommandParameter("clear|rain|thunder", CommandParamType.STRING, false),
                new CommandParameter("duration in seconds", CommandParamType.INT, true)
        });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        if (args.length == 0 || args.length > 2) {
            sender.sendMessage("Usage: " + this.usageMessage);
            return false;
        }

        String weather = args[0];
        Level level;
        int seconds;
        if (args.length > 1) {
            try {
                seconds = Integer.parseInt(args[1]);
            } catch (Exception e) {
                sender.sendMessage("Usage: " + this.usageMessage);
                return true;
            }
        } else {
            seconds = 600 * 20;
        }

        if (sender instanceof Player) {
            level = ((Player) sender).getLevel();
        } else {
            level = sender.getServer().getDefaultLevel();
        }

        switch (weather) {
            case "clear":
                level.setRaining(false);
                level.setThundering(false);
                level.setRainTime(seconds * 20);
                level.setThunderTime(seconds * 20);
                Command.broadcastCommandMessage(sender, "Changing to clear weather");
                return true;
            case "rain":
                level.setRaining(true);
                level.setRainTime(seconds * 20);
                Command.broadcastCommandMessage(sender, "Changing to rainy weather");
                return true;
            case "thunder":
                level.setThundering(true);
                level.setRainTime(seconds * 20);
                level.setThunderTime(seconds * 20);
                Command.broadcastCommandMessage(sender, "Changing to rain and thunder");
                return true;
            default:
                sender.sendMessage("commands.weather.usage" + this.usageMessage);
                return false;
        }

    }
}
