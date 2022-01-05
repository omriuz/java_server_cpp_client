#ifndef socket_Task_
#define socket_Task_

#include "connectionHandler.h"

using namespace std;

class socketTask{
public:
    socketTask(ConnectionHandler &connectionHandler );
    void operator()();
    short bytesToShort(char* bytesArr);
    bool isAckMessageForLogout(std::string answer);

private:
    ConnectionHandler &handler;
    bool shouldTerminate;

};

#endif