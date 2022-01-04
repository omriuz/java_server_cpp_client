package bgu.spl.net.impl.BGS.CommandsAndMessages;

import bgu.spl.net.impl.BGS.Connections_Impl;
import bgu.spl.net.impl.BGS.DataBase;
import bgu.spl.net.impl.rci.Command;

public class LogoutCommand implements Command {
    private final int opCode;

    public LogoutCommand(int opCode) {
        this.opCode = 3;
    }

    @Override
    public void execute(DataBase dataBase, Connections_Impl connections, int connectionId) {
        boolean toReturn = dataBase.logOut(connectionId);
        connections.send(connectionId,toReturn ? new AckMessage(opCode): new ErrorMessage(opCode));
    }
}
