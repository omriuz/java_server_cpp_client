#ifndef keyBoard_Task_
#define keyBoard_Task_

#include "connectionHandler.h"
#include <boost/property_tree/json_parser.hpp>
#include <boost/property_tree/ptree.hpp>
#include <iostream>
#include <sstream>
#include <bits/stdc++.h>


using namespace std;

class keyBoardTask{
public:
    keyBoardTask(ConnectionHandler &connectionHandler );
    void operator()();
    std::string editMessage(std::string rawMsg);
    void shortToBytes(short num, char* bytesArr);
    std::string toJsonRegister(std::string rawMessage);
    std::string toJsonLogin(std::string rawMessage);
    std::string toJsonLogout(std::string rawMessage);
    std::string toJsonFollow(std::string rawMessage);
    std::string toJsonPost(std::string rawMessage);
    std::string toJsonPM(std::string rawMessage);
    std::string toJsonLogStat(std::string rawMessage);
    std::string toJsonStat(std::string rawMessage);
    std::string toJsonBlock(std::string rawMessage);

private:
    ConnectionHandler &handler;
    bool shouldTerminate;
    short opCode;

};

#endif