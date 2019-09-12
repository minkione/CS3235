package com.masterlock.ble.app.tape;

import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import com.google.gson.Gson;
import com.squareup.tape.FileObjectQueue;
import com.squareup.tape.ObjectQueue;
import com.squareup.tape.TaskQueue;
import java.io.File;
import java.io.IOException;

public class UploadTaskQueue extends TaskQueue<UploadTask> {
    private static final String QUEUE_FILENAME = "upload_task_queue";
    /* access modifiers changed from: private */
    public final Context mContext;

    public UploadTaskQueue(ObjectQueue<UploadTask> objectQueue, Context context) {
        super(objectQueue);
        this.mContext = context;
        if (size() > 0) {
            startService();
        }
    }

    public static UploadTaskQueue create(Context context, Gson gson) {
        try {
            return new UploadTaskQueue(new FileObjectQueue(new File(context.getFilesDir(), QUEUE_FILENAME), new GsonConverter(gson, UploadTask.class)), context);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create file queue.", e);
        }
    }

    public void startService() {
        if (VERSION.SDK_INT >= 26) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    UploadTaskQueue.this.mContext.startForegroundService(new Intent(UploadTaskQueue.this.mContext, UploadTaskService.class));
                }
            });
            return;
        }
        Context context = this.mContext;
        context.startService(new Intent(context, UploadTaskService.class));
    }

    public void add(UploadTask uploadTask) {
        super.add(uploadTask);
        startService();
    }

    public void remove() {
        super.remove();
    }
}
