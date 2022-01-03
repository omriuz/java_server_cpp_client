package main.java.bgu.spl.net.impl.BGS.Commands;

import main.java.bgu.spl.net.impl.BGS.Connections_Impl;
import main.java.bgu.spl.net.impl.BGS.DataBase;
import main.java.bgu.spl.net.impl.rci.Command;

public class LogoutCommand implements Command {
    @Override
    public void execute(DataBase dataBase, Connections_Impl connections, int connectionId) {
        boolean toReturn = dataBase.logOut(connectionId);
        connections.send(connectionId,toReturn ? new AckMessage(): new ErrorMessage());
    }
}
