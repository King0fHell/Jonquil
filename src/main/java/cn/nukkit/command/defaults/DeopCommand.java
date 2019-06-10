package cn.nukkit.command.defaults;

import cn.nukkit.IPlayer;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.TextFormat;

public class DeopCommand extends VanillaCommand {
    public DeopCommand(String name) {
        super(name, "Takes the specified player's operator status", "/deop <player>");
        this.setPermission("nukkit.command.op.take");
        this.commandParameters.put("default", new CommandParameter[]{
                new CommandParameter("player", CommandParamType.TARGET, false)
        });
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("Usage: " + this.usageMessage);

            return false;
        }

        String playerName = args[0];
        IPlayer player = sender.getServer().getOfflinePlayer(playerName);
        player.setOp(false);

        if (player instanceof Player) {
            ((Player) player).sendMessage(TextFormat.GRAY + "De-opped!");
        }

        Command.broadcastCommandMessage(sender, "De-opped " + player.getName());

        return true;
    }
}
