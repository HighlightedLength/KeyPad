package net.highlightedsign.keypadkit.models

enum class DesiredConnectionState {
    Disconnected,   // - for when the user actually doesn't want to be connected
    Idle,           // - for when initialization and when the connection attempt failed
                    //   so that the manager doesn't do anything
    Connected       // - for when the user wants to be connected
}