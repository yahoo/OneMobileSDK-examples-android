package com.aol.mobile.sdk.testapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aol.mobile.sdk.controls.ContentControls;
import com.aol.mobile.sdk.testapp.TrackChooserAdapter.Item.Type;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.aol.mobile.sdk.controls.utils.ViewUtils.findView;

public class TrackChooserAdapter extends BaseAdapter {
    @NonNull
    private final List<Item> items = new ArrayList<>();

    public void updateData(@NonNull Context context, @NonNull LinkedList<ContentControls.ViewModel.TrackOptionVM> audioTracks,
                           @NonNull LinkedList<ContentControls.ViewModel.TrackOptionVM> ccTracks) {
        Resources resources = context.getResources();
        int headerColor = resources.getColor(android.R.color.white);
        int trackColor = resources.getColor(android.R.color.black);
        Drawable selectedIcon = resources.getDrawable(android.R.drawable.ic_menu_send);
        Drawable closeIcon = resources.getDrawable(android.R.drawable.ic_menu_close_clear_cancel);

        items.clear();
        if (!audioTracks.isEmpty()) {
            items.add(new Item(headerColor, resources.getString(com.aol.mobile.sdk.controls.R.string.audio_tracks_title)));
            for (ContentControls.ViewModel.TrackOptionVM audioTrack : audioTracks) {
                items.add(new Item(trackColor, audioTrack, selectedIcon, Type.AUDIO, audioTracks.indexOf(audioTrack)));
            }
        }

        if (!ccTracks.isEmpty()) {
            items.add(new Item(headerColor, resources.getString(com.aol.mobile.sdk.controls.R.string.text_tracks_title)));
            for (ContentControls.ViewModel.TrackOptionVM ccTrack : ccTracks) {
                items.add(new Item(trackColor, ccTrack, selectedIcon, Type.CC, ccTracks.indexOf(ccTrack)));
            }
        }

        items.add(new Item(trackColor, resources.getString(com.aol.mobile.sdk.controls.R.string.track_close_title), closeIcon));

        notifyDataSetInvalidated();
    }

    public void select(int index) {
        Item selectedItem = items.get(index);

        if (selectedItem.type == Type.CC || selectedItem.type == Type.AUDIO) {
            for (Item item : items) {
                if (item.type == selectedItem.type) {
                    item.imageVisibility = item == selectedItem ? VISIBLE : INVISIBLE;
                }
            }
        }

        notifyDataSetInvalidated();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Item getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItem(position).imageVisibility != GONE;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), com.aol.mobile.sdk.controls.R.layout.track_item_view, null);
            TextView titleView = findView(convertView, com.aol.mobile.sdk.controls.R.id.track_title);
            ImageView imageView = findView(convertView, com.aol.mobile.sdk.controls.R.id.track_check_image);
            View divider = findView(convertView, com.aol.mobile.sdk.controls.R.id.track_divider);
            convertView.setTag(new ViewHolder(titleView, imageView, divider));
        }

        ViewHolder views = (ViewHolder) convertView.getTag();
        Item item = getItem(position);

        views.image.setVisibility(item.imageVisibility);
        views.image.setImageDrawable(item.imageDrawable);
        views.title.setText(item.text);
        views.title.setTextColor(item.color);
        views.divider.setVisibility(item.hadDivider ? VISIBLE : GONE);

        return convertView;
    }

    private static class ViewHolder {
        @NonNull
        final TextView title;
        @NonNull
        final ImageView image;
        @NonNull
        final View divider;

        private ViewHolder(@NonNull TextView title, @NonNull ImageView image, @NonNull View divider) {
            this.title = title;
            this.image = image;
            this.divider = divider;
        }
    }

    public static class Item {
        @NonNull
        public final Type type;
        public final int index;
        int color;
        int imageVisibility;
        @Nullable
        Drawable imageDrawable;
        @Nullable
        String text;
        boolean hadDivider;

        Item(int color, @Nullable String text) {
            this.color = color;
            this.imageVisibility = GONE;
            this.text = text;
            this.type = Type.TITLE;
            this.index = -1;
        }

        Item(int color, @Nullable String text, @Nullable Drawable drawable) {
            this.color = color;
            this.imageVisibility = VISIBLE;
            this.text = text;
            this.imageDrawable = drawable;
            this.hadDivider = true;
            this.type = Type.CLOSE;
            this.index = -1;
        }

        Item(int color, @NonNull ContentControls.ViewModel.TrackOptionVM track, @NonNull Drawable selectedIcon, @NonNull Type type, int index) {
            this.color = color;
            this.imageVisibility = track.isSelected ? VISIBLE : INVISIBLE;
            this.imageDrawable = selectedIcon;
            this.text = track.title;
            this.type = type;
            this.index = index;
        }

        public enum Type {AUDIO, CC, TITLE, CLOSE}
    }
}
