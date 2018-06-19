package theredspy15.ltecleanerfoss;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sdsmdg.tastytoast.TastyToast;

import net.alhazmy13.catcho.library.Catcho;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import in.codeshuffle.typewriterview.TypeWriterView;

public class MainActivity extends AppCompatActivity {

    List<File> foundFiles;
    int amountRemoved = 0;

    TypeWriterView typeWriterView;
    LinearLayout fileListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // catches app crashes and creates error reports
        Catcho.Builder(this)
                .recipients("hjadar15@protonmail.com")
                .build();

        typeWriterView = findViewById(R.id.typeWriterView);
        fileListView = findViewById(R.id.fileListView);

        setUpTypeWriter();
        requestWriteExternalPermission();
    }

    /**
     * Runs search and delete on background thread
     */
    public final void clean(View view) {

        new Thread(this::searchAndDeleteFiles).start();
    }

    /**
     * Searches entire device, adds all files to a list, then a for each loop filters
     * out files for deletion
     */
    private void searchAndDeleteFiles() {

        Looper.prepare();

        amountRemoved = 0;

        // forward slash for whole device
        String path = Environment.getExternalStorageDirectory().toString() + "/";
        File directory = new File(path);
        foundFiles = getListFiles(directory);

        for (File file : foundFiles)
            if (file.getName().contains(".tmp") || file.getName().contains(".log") || file.getName().contains("cache")) {

                // creating and adding a text view to the scroll view with path to file
                ++amountRemoved;
                TextView textView = new TextView(MainActivity.this);
                textView.setTextColor(Color.WHITE);
                textView.setText(file.getAbsolutePath());

                // adding to scroll view
                runOnUiThread(() -> fileListView.addView(textView));

                // deletion & error message
                String errorMessage = getResources().getString(R.string.error_when_deleting);
                if (!file.delete()) TastyToast.makeText(
                        MainActivity.this, errorMessage, TastyToast.LENGTH_LONG, TastyToast.ERROR
                ).show();
            }

        // No files found
        String errorMessage = getResources().getString(R.string.no_files_found);
        if (amountRemoved == 0) TastyToast.makeText(
                MainActivity.this, errorMessage, TastyToast.LENGTH_LONG, TastyToast.CONFUSING
        ).show();

        Looper.loop();
    }

    /**
     * Used to generate a list of all files on device
     * @param parentDir where to start searching from
     * @return List of all files on device
     */
    List<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) { // folder
                inFiles.addAll(getListFiles(file));
            } else {
                inFiles.add(file); // file
            }
        }
        return inFiles;
    }

    /**
     * Sets up the type writer style text view
     */
    private void setUpTypeWriter() {

        String text = getResources().getString(R.string.lte_cleaner);
        typeWriterView.setDelay(1);
        typeWriterView.setWithMusic(false);
        typeWriterView.animateText(text);
    }

    /**
     * Request write permission
     */
    public void requestWriteExternalPermission() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
    }

    /**
     * Handles the whether the user grants permission. Closes app on deny
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 // Granted
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) break;
                else System.exit(0); // Permission denied
                break;
        }
    }
}
