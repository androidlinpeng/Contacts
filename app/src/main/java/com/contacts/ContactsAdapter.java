package com.contacts;

import android.content.Context;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.uzmap.pkg.uzcore.UZResourcesIDFinder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/14.
 */

public class ContactsAdapter extends BaseAdapter implements SectionIndexer {

    private static final String TAG = "ContactsAdapter";

    private Context context;
    private List<UserInfo> data;

    public ContactsAdapter(Context context, List<UserInfo> data) {
        this.context = context;
        this.data = data;
    }

    public void setDate(List<UserInfo> data){
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public UserInfo getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(context, UZResourcesIDFinder.getResLayoutID("contacts_item_contact_list"), null);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.tvHeader = convertView.findViewById(UZResourcesIDFinder.getResIdID("header"));
            holder.nameTextview = convertView.findViewById(UZResourcesIDFinder.getResIdID("tv_name"));
            holder.iv_avatar = convertView.findViewById(UZResourcesIDFinder.getResIdID("iv_avatar"));
            convertView.setTag(holder);
        }

        UserInfo user = data.get(position);

        holder.tvHeader.setVisibility(View.VISIBLE);
        String name = user.getUsername();
        String icon = user.getIcon();
        String headerLetter = user.getInitialLetter();
        String preHeaderLetter = getPreHeaderLeeter(position);
        if (headerLetter.equals(preHeaderLetter)) {
            holder.tvHeader.setVisibility(View.GONE);
        } else {
            holder.tvHeader.setVisibility(View.VISIBLE);
            holder.tvHeader.setText(headerLetter);
        }
        holder.nameTextview.setText(name);
        Glide.with(context)
                .load(icon)
                .transform(new GlideCircleImage(context))
                .placeholder(UZResourcesIDFinder.getResDrawableID("contacts_ic_head_default"))
                .error(UZResourcesIDFinder.getResDrawableID("contacts_ic_head_default"))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.iv_avatar);
        return convertView;
    }

    private List<String> list;
    private SparseIntArray positionOfSection;
    private SparseIntArray sectionOfPosition;

    @Override
    public Object[] getSections() {
        positionOfSection = new SparseIntArray();
        sectionOfPosition = new SparseIntArray();
        int count = getCount();
        list = new ArrayList<String>();
        list.add("搜");
        positionOfSection.put(0, 0);
        sectionOfPosition.put(0, 0);
        for (int i = 1; i < count; i++) {
            String letter = getItem(i).getInitialLetter();
            int section = list.size() - 1;
            if (list.get(section) != null && !list.get(section).equals(letter)) {
                list.add(letter);
                section++;
                positionOfSection.put(section, i);
            }
            sectionOfPosition.put(i, section);
        }
        return list.toArray(new String[list.size()]);

    }


    public int getPositionForSection(int section) {
        return positionOfSection.get(section) + 1;
    }

    public int getSectionForPosition(int position) {
        return sectionOfPosition.get(position);
    }

    private class ViewHolder {
        ImageView iv_avatar;
        TextView nameTextview, tvHeader;
    }

    private String getPreHeaderLeeter(int position) {
        if (position > 0) {
            UserInfo user = getItem(position - 1);
            return user.getInitialLetter();
        }
        return null;
    }

}
