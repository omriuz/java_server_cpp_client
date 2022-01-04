package bgu.spl.net.impl.rci;

import bgu.spl.net.impl.BGS.Connections_Impl;
import bgu.spl.net.impl.BGS.DataBase;

public interface Command extends Communication {

    public void execute(DataBase dataBase, Connections_Impl connections, int id);

}
