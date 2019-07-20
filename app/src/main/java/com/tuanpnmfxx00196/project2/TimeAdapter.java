package com.tuanpnmfxx00196.project2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Vibrator;
import android.util.SparseBooleanArray;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.ViewHolder>{
    ArrayList<DataTime>arrayList;
    DataTime dataTime;
    Database database;
    Context context;
    boolean checked;
    static AlertInterface alertInterface;
    private SparseBooleanArray itemStateArray= new SparseBooleanArray();
    public TimeAdapter(ArrayList<DataTime> arrayList, Context context, AlertInterface al) {
        this.arrayList = arrayList;
        this.context = context;
        this.alertInterface = al;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        int hourOfDay = arrayList.get(position).hourOfDay;
        int minute = arrayList.get(position).minute;
        dataTime = new DataTime(hourOfDay,minute);
        String description = arrayList.get(position).description.toString();
        viewHolder.timeDisplay.setText(dataTime.Time12H(hourOfDay,minute).toString());
        viewHolder.description.setText(description);
        if(arrayList.get(position).status==1){
            viewHolder.switchAlarm.setChecked(true);
        }
        else{
            viewHolder.switchAlarm.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements com.tuanpnmfxx00196.project2.ViewHolder {
        TextView timeDisplay;
        TextView description;
        Switch switchAlarm;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timeDisplay = (TextView)itemView.findViewById(R.id.timeDisplay);
            description = (TextView)itemView.findViewById(R.id.description);
            switchAlarm = (Switch)itemView.findViewById(R.id.switchAlarm);
            switchAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    AlarmManager[] alarmManagers = new AlarmManager[arrayList.size()];
                    Intent intent [] = new Intent[alarmManagers.length];
                    database = new Database(context);
                    intent [getAdapterPosition()] = new Intent(context,AlarmReceiver2.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                            getAdapterPosition(),intent[getAdapterPosition()],0);
                    if(isChecked){
                        Toast.makeText(context,"ON",Toast.LENGTH_SHORT).show();
                        database.Update(String.valueOf(arrayList.get(getAdapterPosition()).idAlarm),
                                arrayList.get(getAdapterPosition()).hourOfDay,
                                arrayList.get(getAdapterPosition()).minute,
                                arrayList.get(getAdapterPosition()).description,
                                1
                        );
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY,arrayList.
                                get(getAdapterPosition()).getHourOfDay());
                        calendar.set(Calendar.MINUTE,arrayList.
                                get(getAdapterPosition()).getMinute());
                        calendar.set(Calendar.SECOND, 0);
                        alarmManagers[getAdapterPosition()] =
                                (AlarmManager)context.getSystemService(ALARM_SERVICE);
                        alarmManagers[getAdapterPosition()].set(AlarmManager.RTC_WAKEUP,
                                calendar.getTimeInMillis(),pendingIntent);
                    }
                    else{
                        database.Update(String.valueOf(arrayList.get(getAdapterPosition()).idAlarm),
                                arrayList.get(getAdapterPosition()).hourOfDay,
                                arrayList.get(getAdapterPosition()).minute,
                                arrayList.get(getAdapterPosition()).description,
                                0
                        );
                        alarmManagers[getAdapterPosition()] =
                                (AlarmManager)context.getSystemService(ALARM_SERVICE);
                        alarmManagers[getAdapterPosition()].cancel(pendingIntent);
                        Toast.makeText(context,"OFF",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            OnCreateContextMenuListener listener = new OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    MenuItem edit =menu.add(menu.NONE, 1, 1, "Edit alarm");
                    MenuItem delete = menu.add(menu.NONE, 2,2,"Delete alarm");
                    edit.setOnMenuItemClickListener(onChange);
                    delete.setOnMenuItemClickListener(onChange);
                }
            };
            itemView.setOnCreateContextMenuListener(listener);
        }
        private final MenuItem.OnMenuItemClickListener onChange = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 1:
                        Toast.makeText(context, "Choice edit", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(itemView.getContext(), Update.class);
                        intent.putExtra("Hour",arrayList.
                                get(getAdapterPosition()).getHourOfDay());
                        intent.putExtra("Minute",arrayList.
                                get(getAdapterPosition()).getMinute());
                        intent.putExtra("Note",arrayList.
                                get(getAdapterPosition()).getDescription());
                        intent.putExtra("position",arrayList.
                                get(getAdapterPosition()).getIdAlarm());
                        itemView.getContext().startActivity(intent);
                        break;
                    case 2:
                        Toast.makeText(context, "Choice delete:  "+
                                getAdapterPosition(), Toast.LENGTH_SHORT).show();
                        database = new Database(context);
                        database.Delete(String.valueOf(arrayList.get(getAdapterPosition())
                                .getIdAlarm()));
                        arrayList.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(),arrayList.size());
                        break;
                    default:
                        break;
                }
                return false;
            }
        };

        @Override
        public boolean onLongClick(View v) {
            //Toast.makeText(context,getAdapterPosition()+"",Toast.LENGTH_SHORT).show();
            return false;
        }

    }
    public static class AlarmReceiver2 extends BroadcastReceiver {
        Context context;
        Vibrator vibrator;
        @Override
        public void onReceive(final Context context, Intent intent) {
            //intent = new Intent(context,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Toast.makeText(context,"Time Alarm",Toast.LENGTH_SHORT).show();
            alertInterface.showAlertDialog();
            long[] mVibratePattern = new long[]{0, 2000, 1000, 2000};
            vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(mVibratePattern, 0);
        }
    }
}

