package at.rpisec.client.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import at.rpisec.client.R;
import at.rpisec.client.adapter.ImageAdapter;
import at.rpisec.client.util.RpiSecConstants;

/**
 * @author Philipp Wurm <philipp.wurm@gmail.com>.
 */

public class IncidentsActivity extends AppCompatActivity {

    private static final String DEBUG_FCM_TAG = "IncidentsActivity";
    private List<File> images = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidents);

        ensureImageDirExists();

        GridView gridview = (GridView) findViewById(R.id.incidentsGridView);
        images = findAllImagesInPath();
        gridview.setAdapter(new ImageAdapter(this, images));

        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                images = findAllImagesInPath();
                ((ImageAdapter) gridview.getAdapter()).setImageData(images);
                ((ImageAdapter) gridview.getAdapter()).notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    showImage(position);
                } catch (Exception e) {
                    Log.e(DEBUG_FCM_TAG, e.getMessage());
                }
            }
        });
    }

    private void showImage(int index) {
        Intent showImage = new Intent(Intent.ACTION_VIEW);
        Uri data = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".fileprovider", images.get(index));

        showImage.setDataAndType(data, RpiSecConstants.RPI_SEC_IMAGE_DATATYPE);
        showImage.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(showImage);
    }

    private void ensureImageDirExists() {
        final File baseDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!(baseDir != null && baseDir.exists()))
            if (baseDir != null) {
                baseDir.mkdirs();
            }
    }

    private List<File> findAllImagesInPath() {
        List<File> inFiles = new ArrayList<>();
        try {
            Queue<File> files = new LinkedList<>();

            File baseDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            if ((baseDir != null && baseDir.exists()) && baseDir.canRead()) {
                files.addAll(Arrays.asList(baseDir.listFiles()));
                while (!files.isEmpty()) {
                    File file = files.remove();
                    if (file.isDirectory()) {
                        files.addAll(Arrays.asList(file.listFiles()));
                    } else {
                        inFiles.add(file);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Collections.reverse(inFiles);

        return inFiles;
    }

    @Override
    protected void onResume() {
        super.onResume();
        images = findAllImagesInPath();
    }
}
