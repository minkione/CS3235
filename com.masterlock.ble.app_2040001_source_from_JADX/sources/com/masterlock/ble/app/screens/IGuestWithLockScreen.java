package com.masterlock.ble.app.screens;

import com.masterlock.core.Country;
import com.masterlock.core.Guest;
import com.masterlock.core.Lock;

public interface IGuestWithLockScreen {
    Country getCountry();

    Guest getGuest();

    Lock getLock();

    void setCountry(Country country);
}
