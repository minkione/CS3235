package com.masterlock.ble.app.command;

import com.masterlock.api.entity.CommandsResponse;
import com.masterlock.core.Lock;

public class ResetKeysWrapper {
    private CommandsResponse commandsResponse;
    private Lock lock;

    public ResetKeysWrapper(Lock lock2, CommandsResponse commandsResponse2) {
        this.lock = lock2;
        this.commandsResponse = commandsResponse2;
    }

    public Lock getLock() {
        return this.lock;
    }

    public CommandsResponse getCommandsResponse() {
        return this.commandsResponse;
    }
}
