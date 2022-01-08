package bgu.spl.net.impl.BGS;


import bgu.spl.net.srv.Server;


public class TPCMain {
    public static void main(String[] args) {
        DataBase dataBase = new DataBase(); 
        Server.threadPerClient(
                Integer.valueOf(args[0]),
                () -> new RemoteCommandInvocationProtocol (dataBase), //protocol factory
                () -> new ObjectEncoderDecoder() //message encoder decoder factory
        ).serve();
    }
}
