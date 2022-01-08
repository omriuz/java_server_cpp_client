package bgu.spl.net.impl.BGS;



public interface Command extends Communication {

    public void execute(DataBase dataBase, Connections_Impl connections, int id);

}
