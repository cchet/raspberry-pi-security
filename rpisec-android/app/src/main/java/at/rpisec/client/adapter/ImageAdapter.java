package at.rpisec.client.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
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
            //imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
            imageView.setLayoutParams(new GridView.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        if(position < _images.size()) {

            String path = _images.get(position).getAbsolutePath();
            if(path != null && !path.isEmpty()) {
                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;
                    Bitmap myBitmap = BitmapFactory.decodeFile(path, options);
                    if (myBitmap != null) {
                        imageView.setImageBitmap(myBitmap);
                    }
                }
                catch (Exception e)
                {
                    Log.e("ImageAdapter", e.getMessage());
                }
            }
        }

        return imageView;
    }
}
