package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Level;

public class SaveCommand extends VanillaCommand {

    public SaveCommand(String name) {
        super(name, "Saves the server to disk", "/save-all");
        this.setPermission("nukkit.command.save.perform");
        this.commandParameters.clear();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        Command.broadcastCommandMessage(sender, "Saving...");

        for (Player player : sender.getServer().getOnlinePlayers().values()) {
            player.save();
        }

        for (Level level : sender.getServer().getLevels().values()) {
            level.save(true);
        }

        Command.broadcastCommandMessage(sender, "Saved the world");
        return true;
    }
}
