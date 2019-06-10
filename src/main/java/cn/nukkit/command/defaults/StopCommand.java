package cn.nukkit.command.defaults;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class StopCommand extends VanillaCommand {

    public StopCommand(String name) {
        super(name, "Stop the server", "/stop");
        this.setPermission("nukkit.command.stop");
        this.commandParameters.clear();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        Command.broadcastCommandMessage(sender, "Stopping the server");

        sender.getServer().shutdown();

        return true;
    }
}
