package ComunicationProtocol;

/**
 * Enumeration of different types of messages
 */
public enum CommandType {
    NAME,//nickname
    FIRST,//playerNum
    COORDINATES,//position
    NUMBER,//god choice by the player
    ANSWER,//use power
    GOD,//in game god choice
    CLOSE,//game closure
    UPDATE,//map update
    COMMUNICATION;//simple print, no answer
}
