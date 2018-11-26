package i.am.edgedeveloper.mmmcalculator;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.provider.CalendarContract;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import i.am.edgedeveloper.mmmcalculator.databinding.SingleMavroBinding;

/**
 * Created by OPEYEMI OLORUNLEKE on 6/7/2017.
 */

public class MavroAdapter extends RecyclerView.Adapter<MavroAdapter.MavroHolder> {

    private static final String TAG = "Mavro Adapter";
    public List<Mavro> mMavros = new ArrayList<>();
    private Context mContext;
    private DateFormat dateFormat;


    public MavroAdapter(Context context) {
        mContext = context;
        dateFormat = new SimpleDateFormat("MMM d yyyy");

    }

    @Override
    public MavroHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        SingleMavroBinding binding = DataBindingUtil.inflate(inflater, R.layout.single_mavro, parent, false);
        return new MavroHolder(binding);
    }

    @Override
    public void onBindViewHolder(MavroHolder holder, int position) {
        holder.bind(mMavros.get(position));
    }

    @Override
    public int getItemCount() {
        return mMavros.size();
    }

    public void addMavros(List<Mavro> mavros) {
        mMavros = mavros;
        notifyDataSetChanged();
    }

    public class MavroHolder extends RecyclerView.ViewHolder {
        private SingleMavroBinding vBinding;
        private Mavro vMavro;


        public MavroHolder(SingleMavroBinding binding) {
            super(binding.getRoot());
            vBinding = binding;
            binding.addToCalendar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Date endTime = vMavro.getEndTime();
                    Date beginTime = vMavro.getBeginTime();

                    Log.e(TAG, "onClick: " + dateFormat.format(endTime.getTime()));
                    Log.e(TAG, "onClick: " + dateFormat.format(beginTime.getTime()));
                    Log.e(TAG, "onClick: " + dateFormat.format(endTime));


                    // -----------------------------

                    try {

                        Intent intent = new Intent(Intent.ACTION_INSERT);

                        intent.setData(CalendarContract.Events.CONTENT_URI);
                        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, endTime.getTime());
                        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTime() + 24 * 60 * 60 * 1000);
                        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
                        intent.putExtra(CalendarContract.Events.TITLE, "Get Help of " + vMavro.getMoney());
                        intent.putExtra(CalendarContract.Events.DESCRIPTION, "This is a Reminder to Get Help on " + vMavro.getDate() + " - You Requested to Provided Help on " + dateFormat.format(beginTime.getTime()));
                        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "http://www.mmmoffice.com");

                        mContext.startActivity(intent);

                        // -----------------------------


                        Intent intent1 = new Intent(Intent.ACTION_INSERT);

                        intent1.setData(CalendarContract.Events.CONTENT_URI);
                        intent1.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTime());
                        intent1.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, beginTime.getTime() + 24 * 60 * 60 * 1000);
                        intent1.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
                        intent1.putExtra(CalendarContract.Events.TITLE, "Provided Help of " + vMavro.getMoney());
                        intent1.putExtra(CalendarContract.Events.DESCRIPTION, "You Requested to Provided Help on " + dateFormat.format(beginTime.getTime()));
                        intent1.putExtra(CalendarContract.Events.EVENT_LOCATION, "http://www.mmmoffice.com");

                        mContext.startActivity(intent1);
                    }catch (Exception e){
                        Snackbar.make(vBinding.getRoot(), "ERROR, Could Not Open Calendar",Snackbar.LENGTH_LONG).show();
                    }

                    // -----------------------------
                }
            });
        }

        public void bind(Mavro mavro) {
            vMavro = mavro;
            vBinding.setMavro(mavro);
            vBinding.executePendingBindings();

        }
    }
}
