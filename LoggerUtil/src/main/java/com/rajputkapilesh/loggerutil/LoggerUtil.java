package com.rajputkapilesh.loggerutil;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.format.DateFormat;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class LoggerUtil extends AppCompatActivity {
    private static final String h = DateFormat.format("MM-dd-yyyy_hh:mm:ss_AA", System.currentTimeMillis()).toString();
    private static final int STORAGE_PERMISSION_CODE = 1;

    public static void log(Context context, String msg) {
        createNewFile(context, msg);
    }

    private static void createNewFile(Context context, String msg) {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            try {
                File root = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS), getAppLabel(context) + "_Logs");

                if (!root.exists()) {
                    root.mkdirs();
                }

                File filepath = new File(root, "Log_" + h + ".txt"); // file path to save
                FileWriter writer = new FileWriter(filepath, true);

                writer.append(context.getClass().getSimpleName()).append("::").append(msg).append("\n");

                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            requestStoragePermission(context);
        }
    }

    private static void requestStoragePermission(Context context) {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)
                && ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(context)
                    .setTitle("Storage permission needed")
                    .setMessage("Storage permission is required to create and update log files in device storage.")
                    .setPositiveButton("ok", (dialog, which) -> ActivityCompat.requestPermissions((Activity) context,
//                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            STORAGE_PERMISSION_CODE))
                    .setNegativeButton("cancel", (dialog, which) -> dialog.dismiss())
                    .create()
                    .show();
        } else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static String getAppLabel(Context context) {
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = packageManager.getApplicationInfo(context.getApplicationInfo().packageName, 0);
        } catch (final PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return (String) (applicationInfo != null ? packageManager.getApplicationLabel(applicationInfo) : "Unknown");
    }}

