package cn.nukkit.command.defaults;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;

public class ReloadCommand extends VanillaCommand {

    public ReloadCommand(String name) {
        super(name, "Reloads the server configuration and plugins", "/reload");
        this.setPermission("nukkit.command.reload");
        this.commandParameters.clear();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        Command.broadcastCommandMessage(sender, TextFormat.WHITE + "Reloading..." + TextFormat.WHITE);

        sender.getServer().reload();

        Command.broadcastCommandMessage(sender, TextFormat.GREEN + "Reload complete!" + TextFormat.WHITE);

        return true;
    }
}
