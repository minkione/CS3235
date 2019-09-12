package com.masterlock.ble.app.tape;

import android.content.Context;
import android.content.Intent;
import com.google.gson.Gson;
import com.squareup.tape.FileObjectQueue;
import com.squareup.tape.ObjectQueue;
import com.squareup.tape.TaskQueue;
import java.io.File;
import java.io.IOException;

public class ConfirmTaskQueue extends TaskQueue<ConfirmTask> {
    private static final String QUEUE_FILENAME = "confirm_task_queue";
    private final Context mContext;

    public ConfirmTaskQueue(ObjectQueue<ConfirmTask> objectQueue, Context context) {
        super(objectQueue);
        this.mContext = context;
        if (size() > 0) {
            startService();
        }
    }

    public static ConfirmTaskQueue create(Context context, Gson gson) {
        try {
            return new ConfirmTaskQueue(new FileObjectQueue(new File(context.getFilesDir(), QUEUE_FILENAME), new GsonConverter(gson, ConfirmTask.class)), context);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create file queue.", e);
        }
    }

    public void startService() {
        Context context = this.mContext;
        context.startService(new Intent(context, ConfirmTaskService.class));
    }

    public void add(ConfirmTask confirmTask) {
        super.add(confirmTask);
        startService();
    }

    public void remove() {
        super.remove();
    }
}
