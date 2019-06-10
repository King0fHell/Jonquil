package cn.nukkit.command.defaults;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import co.aikar.timings.Timings;
import co.aikar.timings.TimingsExport;

public class TimingsCommand extends VanillaCommand {

    public TimingsCommand(String name) {
        super(name, "Records timings to see performance of the server.", "/timings <reset|report/paste|on|off|verbon|verboff>");
        this.setPermission("nukkit.command.timings");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                new CommandParameter("on|off|paste")
        });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("Usage: " + usageMessage);
            return true;
        }

        String mode = args[0].toLowerCase();

        if (mode.equals("on")) {
            Timings.setTimingsEnabled(true);
            Timings.reset();
            sender.sendMessage("Enabled timings and reset");
            return true;
        } else if (mode.equals("off")) {
            Timings.setTimingsEnabled(false);
            sender.sendMessage("Disable timings");
            return true;
        }

        if (!Timings.isTimingsEnabled()) {
            sender.sendMessage("Please enable timings by typing /timings on");
            return true;
        }

        switch (mode) {
            case "verbon":
                sender.sendMessage("Enabled verbose timings");
                Timings.setVerboseEnabled(true);
                break;
            case "verboff":
                sender.sendMessage("Disabled verbose timings");
                Timings.setVerboseEnabled(true);
                break;
            case "reset":
                Timings.reset();
                sender.sendMessage("Timings reset");
                break;
            case "report":
            case "paste":
                TimingsExport.reportTimings(sender);
                break;
        }
        return true;
    }
}

