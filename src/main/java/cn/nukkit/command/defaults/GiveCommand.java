package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.item.Item;
import cn.nukkit.utils.TextFormat;

public class GiveCommand extends VanillaCommand {
    public GiveCommand(String name) {
        super(name, "Gives the specified player a certain amount of items", "/give <player> <item[:damage]> [amount] [tags ...]");
        this.setPermission("nukkit.command.give");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                new CommandParameter("player", CommandParamType.TARGET, false),
                new CommandParameter("item", false, CommandParameter.ENUM_TYPE_ITEM_LIST),
                new CommandParameter("amount", CommandParamType.INT, true),
                new CommandParameter("meta", CommandParamType.INT, true),
                new CommandParameter("tags...", CommandParamType.RAWTEXT, true)
        });
        this.commandParameters.put("toPlayerById", new CommandParameter[]{
                new CommandParameter("player", CommandParamType.TARGET, false),
                new CommandParameter("item ID", CommandParamType.INT, false),
                new CommandParameter("amount", CommandParamType.INT, true),
                new CommandParameter("tags...", CommandParamType.RAWTEXT, true)
        });
        this.commandParameters.put("toPlayerByIdMeta", new CommandParameter[]{
                new CommandParameter("player", CommandParamType.TARGET, false),
                new CommandParameter("item ID:meta", CommandParamType.RAWTEXT, false),
                new CommandParameter("amount", CommandParamType.INT, true),
                new CommandParameter("tags...", CommandParamType.RAWTEXT, true)
        });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage("Usage: " + this.usageMessage);

            return true;
        }

        Player player = sender.getServer().getPlayer(args[0]);
        Item item;

        try {
            item = Item.fromString(args[1]);
        } catch (Exception e) {
            sender.sendMessage("Usage: " + this.usageMessage);
            return true;
        }

        try {
            item.setCount(Integer.parseInt(args[2]));
        } catch (Exception e) {
            item.setCount(item.getMaxStackSize());
        }

        if (player != null) {
            if (item.getId() == 0) {
                sender.sendMessage(TextFormat.RED + "%commands.give.item.notFound" + args[1]);
                return true;
            }
            player.getInventory().addItem(item.clone());
        } else {
            sender.sendMessage(TextFormat.RED + "%commands.generic.player.notFound");

            return true;
        }
        Command.broadcastCommandMessage(sender, "Given "+ player.getName() + " * " + item.getName() + " (" + item.getId() + ":" + item.getDamage() + ")" + " to " + item.getCount());
        return true;
    }
}
