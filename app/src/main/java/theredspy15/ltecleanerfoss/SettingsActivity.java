/*
 *  Copyright 2018 TheRedSpy15
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package theredspy15.ltecleanerfoss;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import com.fxn.stash.Stash;
import com.sdsmdg.tastytoast.TastyToast;

public class SettingsActivity extends AppCompatActivity {

    CheckBox apkCheckBox;
    CheckBox tmpCheckBox;
    CheckBox emptyCheckBox;
    CheckBox logCheckBox;
    CheckBox cacheCheckBox;
    ListView listView;

    BaseAdapter adapter;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        apkCheckBox = findViewById(R.id.apkBox);
        tmpCheckBox = findViewById(R.id.tmpBox);
        emptyCheckBox = findViewById(R.id.emptyFolderBox);
        logCheckBox = findViewById(R.id.logBox);
        cacheCheckBox = findViewById(R.id.cacheBox);
        listView = findViewById(R.id.whitelistView);

        adapter = new ArrayAdapter<>(SettingsActivity.this,R.layout.custom_textview,MainActivity.whiteList);
        listView.setAdapter(adapter);

        apkCheckBox.setChecked(Stash.getBoolean("deleteAPKs",false));
        tmpCheckBox.setChecked(Stash.getBoolean("deleteTmp",true));
        emptyCheckBox.setChecked(Stash.getBoolean("deleteEmpty",true));
        logCheckBox.setChecked(Stash.getBoolean("deleteLog",true));
        cacheCheckBox.setChecked(Stash.getBoolean("deleteCache",true));
    }

    /**
     * Saves all settings to the shared preferences file
     * @param view view that is clicked
     */
    public final void save(View view) {

        Stash.put("deleteAPKs",apkCheckBox.isChecked());
        Stash.put("deleteEmpty",emptyCheckBox.isChecked());
        Stash.put("deleteLog",logCheckBox.isChecked());
        Stash.put("deleteCache",cacheCheckBox.isChecked());
        Stash.put("deleteTmp",tmpCheckBox.isChecked());
        Stash.put("whiteList",MainActivity.whiteList);

        TastyToast.makeText(this,"Saved",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS).show();
    }

    public final void viewPrivacyPolicy(View view) {

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://cdn.rawgit.com/TheRedSpy15/LTECleanerFOSS/d9522c76/privacy_policy.html"));
        startActivity(browserIntent);
    }

    /**
     * Starts the main activity
     * @param view the view that is clicked
     */
    public final void back(View view) {

        Intent randomIntent = new Intent(this, MainActivity.class);
        startActivity(randomIntent);
    }

    public final void addToWhitelist(View view) {

        final EditText input = new EditText(SettingsActivity.this);

        new AlertDialog.Builder(SettingsActivity.this)
                .setTitle(R.string.add_to_whitelist)
                .setMessage(R.string.enter_file_name)
                .setView(input)
                .setPositiveButton(R.string.add, (dialog, whichButton) -> MainActivity.whiteList.add(String.valueOf(input.getText())))
                .setNegativeButton(R.string.cancel, (dialog, whichButton) -> { }).show();
    }

    public final void resetWhitelist(View view) {

        MainActivity.whiteList.clear();

        runOnUiThread(() -> {
            adapter.notifyDataSetChanged();
            listView.invalidateViews();
            listView.refreshDrawableState();
        });

        MainActivity.setUpWhiteListAndFilter(false);
    }
}
