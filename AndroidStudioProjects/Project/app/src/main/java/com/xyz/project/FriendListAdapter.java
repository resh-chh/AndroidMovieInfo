package com.xyz.project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Rashmi Chhabria on 1/10/2017.
 */
public class FriendListAdapter extends BaseAdapter {

    Context context;
    ArrayList<Friend> friendList;

    LayoutInflater inflater;

    public FriendListAdapter(Context context, ArrayList<Friend> friendList) {
        this.context = context;
        this.friendList = friendList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return friendList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;

        if (convertView == null) {
            vi = inflater.inflate(R.layout.list_item_friend, null);
        }

        TextView name = (TextView) vi.findViewById(R.id.tvName);
        TextView dob = (TextView) vi.findViewById(R.id.tvDOB);
        TextView phone = (TextView) vi.findViewById(R.id.tvPhone);
        ImageView photo = (ImageView) vi.findViewById(R.id.ivPhoto);

        Friend friend = friendList.get(position);
        name.setText(friend.getName());
        dob.setText(friend.getDob());
        phone.setText(friend.getPhone());

        String photoPath = friend.getPhoto();
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
        photo.setImageBitmap(bitmap);

        return vi;
    }
}
