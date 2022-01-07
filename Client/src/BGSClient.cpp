#include <stdlib.h>
#include <connectionHandler.h>
#include <iostream>
#include <thread>
#include <keyBoardTask.h>
#include <socketTask.h>

using boost::asio::ip::tcp;


int main (int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);
    
    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }
	
    std::thread keyBoardThread = std::thread(keyBoardTask(connectionHandler));
    std::thread socketThread = std::thread(socketTask(connectionHandler));
    
    //TODO: make sure to close the threads correctly
    socketThread.join();
    //TODO: check thow to close the thread in right way
    keyBoardThread.~thread();
    // keyBoardThread.join();
    
    return 0;
}
