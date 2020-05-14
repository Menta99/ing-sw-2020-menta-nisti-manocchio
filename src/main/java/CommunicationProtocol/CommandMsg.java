package CommunicationProtocol;

import CommunicationProtocol.SantoriniInfo.Info;

import java.io.Serializable;

/**
 * Immutable Class for messages from Server to Client
 */
public class CommandMsg implements Message, Serializable {
    private final CommandType commandType;
    private final Info info;

    /**
     * Constructor of the Class
     * @param commandType type of message
     * @param info Info to send to the Client
     */
    public CommandMsg(CommandType commandType, Info info){
        this.commandType = commandType;
        this.info = info;
    }

    @Override
    public CommandType getCommandType() {
        return commandType;
    }

    public Info getInfo() {
        return info;
    }
}
