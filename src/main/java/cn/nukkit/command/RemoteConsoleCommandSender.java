package cn.nukkit.command;

public class RemoteConsoleCommandSender extends ConsoleCommandSender {
    private final StringBuilder messages = new StringBuilder();

    @Override
    public void sendMessage(String message) {
        this.messages.append(message.trim()).append("\n");
    }

    public String getMessages() {
        return messages.toString();
    }

    @Override
    public String getName() {
        return "Rcon";
    }
}
