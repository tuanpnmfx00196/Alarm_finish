//package com.tuanpnmfxx00196.project2;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Vibrator;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AlertDialog;
//
//public class AlarmReceiver extends BroadcastReceiver {
//    Context context;
//    Vibrator vibrator;
//    @Override
//    public void onReceive(final Context context, Intent intent) {
//        //intent = new Intent(context,MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        Toast.makeText(context,"Time Alarm",Toast.LENGTH_SHORT).show();
//        long[] mVibratePattern = new long[]{0, 2000, 1000, 2000};
//        vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
//        vibrator.vibrate(mVibratePattern, 0);
//    }
//
//}
//
