package com.masterlock.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User {
    private final String email;

    /* renamed from: id */
    private long f199id;
    private final List<Invitation> invitations = new ArrayList();
    private final List<Lock> locks = new ArrayList();
    private final String name;

    public User(String str, String str2) {
        this.name = str;
        this.email = str2;
    }

    public long getId() {
        return this.f199id;
    }

    public void setId(long j) {
        this.f199id = j;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public List<Lock> getLocks() {
        return Collections.unmodifiableList(this.locks);
    }

    public void addLock(Lock lock) {
        this.locks.add(lock);
    }

    public void removeLock(Lock lock) {
        this.locks.remove(lock);
    }

    public List<Invitation> getInvitations() {
        return Collections.unmodifiableList(this.invitations);
    }
}
