package com.masterlock.ble.app.bus;

import android.content.res.Resources;
import com.masterlock.ble.app.C1075R;

public class UpdateToolbarEvent {
    private int color;
    private Integer statusBarColor;
    private String title;

    public static class Builder {
        private UpdateToolbarEvent event;
        private Resources resources;

        public Builder(Resources resources2) {
            this.resources = resources2;
            this.event = new UpdateToolbarEvent(resources2.getColor(C1075R.color.primary));
        }

        public Builder color(int i) {
            this.event.setColor(this.resources.getColor(i));
            return this;
        }

        public Builder statusBarColor(int i) {
            this.event.setStatusBarColor(Integer.valueOf(this.resources.getColor(i)));
            return this;
        }

        public UpdateToolbarEvent build() {
            return this.event;
        }

        public Builder title(int i) {
            this.event.setTitle(this.resources.getString(i));
            return this;
        }

        public Builder title(String str) {
            this.event.setTitle(str);
            return this;
        }
    }

    public UpdateToolbarEvent(int i) {
        this(i, null);
    }

    public UpdateToolbarEvent(int i, String str) {
        this(i, null, null);
    }

    public UpdateToolbarEvent(int i, Integer num, String str) {
        this.color = i;
        this.statusBarColor = num;
        this.title = str;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int i) {
        this.color = i;
    }

    public Integer getStatusBarColor() {
        return this.statusBarColor;
    }

    public void setStatusBarColor(Integer num) {
        this.statusBarColor = num;
    }

    public boolean hasTitle() {
        return this.title != null;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }
}
