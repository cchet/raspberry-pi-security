package at.rpisec.client.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Philipp Wurm <philipp.wurm@gmail.com>.
 */

public class ImageAdapter extends BaseAdapter {

    private Context _context;
    private List<File> _images = new ArrayList<>();

    public ImageAdapter(Context context, List<File> imageFiles) {
        this._context = context;
        this._images = imageFiles;
    }

    public void setImageData(List<File> imageFiles) {
        this._images = imageFiles;
    }

    @Override
    public int getCount() {
        return _images.size();
    }

    @Override
    public Object getItem(int position) {
        if (position >= _images.size() - 1)
            return null;

        return _images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(_context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        Bitmap myBitmap = BitmapFactory.decodeFile(_images.get(position).getAbsolutePath());
        imageView.setImageBitmap(myBitmap);

        return imageView;
    }
}
