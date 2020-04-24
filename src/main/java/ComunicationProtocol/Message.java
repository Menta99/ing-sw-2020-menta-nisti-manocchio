package ComunicationProtocol;

import java.io.Serializable;

public abstract class Message implements Serializable {
    private CommandType commandType = CommandType.COMMUNICATION;

    public CommandType getCommandType() {
        return commandType;
    }

    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }
}
