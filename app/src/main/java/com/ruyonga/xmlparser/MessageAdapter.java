package com.ruyonga.xmlparser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.ruyonga.xmlparser.sugarorms.savemessage;

import java.util.List;
import java.util.Random;

/**
 * Created by ruyonga on 04/05/16.
 */
public class MessageAdapter extends BaseAdapter {

    List<savemessage> mgs;
    Context cont;
    public static LayoutInflater inflater = null;

    public MessageAdapter(List<savemessage> ms, Context context) {
        this.mgs = ms;
        this.cont = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mgs.size();
    }

    @Override
    public Object getItem(int position) {
        return mgs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.message_item, null);
        TextView message = (TextView) vi.findViewById(R.id.Notimessage);
        ImageView user = (ImageView) vi.findViewById(R.id.setting_pro);

        /**
         * Set the notification Data
         */
        message.setText(mgs.get(position).message);
        String msg = mgs.get(position).message;


        int[] androidColors = cont.getResources().getIntArray(R.array.androidcolors);
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];


        /**
         * Set the that multiple colors
         */

        String ap = "FUND";
        String cha = null;
        if (msg == null) {
            cha = String.valueOf(ap.charAt(0));
        } else {
            cha = String.valueOf(msg.charAt(0));
        }
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(cha.toUpperCase(), randomAndroidColor);
        user.setImageDrawable(drawable);


        return vi;
    }
}
