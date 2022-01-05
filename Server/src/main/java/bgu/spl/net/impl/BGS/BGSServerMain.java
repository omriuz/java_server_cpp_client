package bgu.spl.net.impl.BGS;


import bgu.spl.net.srv.Server;


public class BGSServerMain {
    public static void main(String[] args) {
        DataBase dataBase = new DataBase(); //one shared object

// you can use any server...
        Server.threadPerClient(
                7777, //port
                () -> new RemoteCommandInvocationProtocol (dataBase), //protocol factory
                () -> new ObjectEncoderDecoder() //message encoder decoder factory
        ).serve();
    }
}
