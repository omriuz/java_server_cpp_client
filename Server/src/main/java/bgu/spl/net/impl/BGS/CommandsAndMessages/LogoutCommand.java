package bgu.spl.net.impl.BGS.CommandsAndMessages;

import bgu.spl.net.impl.BGS.Connections_Impl;
import bgu.spl.net.impl.BGS.DataBase;
import bgu.spl.net.impl.rci.Command;

public class LogoutCommand implements Command {
    private int opCode;

    public LogoutCommand(int opCode) {
        
    }

    @Override
    public void execute(DataBase dataBase, Connections_Impl connections, int connectionId) {
        this.opCode = 3;
        boolean toReturn = dataBase.logOut(connectionId);
        connections.send(connectionId,toReturn ? new AckMessage(opCode): new ErrorMessage(opCode));
    }
}
