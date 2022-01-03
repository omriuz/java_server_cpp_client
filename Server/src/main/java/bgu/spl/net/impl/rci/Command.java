package main.java.bgu.spl.net.impl.rci;

import main.java.bgu.spl.net.impl.BGS.Connections_Impl;
import main.java.bgu.spl.net.impl.BGS.DataBase;

import java.io.Serializable;

public interface Command extends Serializable {

    public void execute(DataBase dataBase, Connections_Impl connections, int id);
}
