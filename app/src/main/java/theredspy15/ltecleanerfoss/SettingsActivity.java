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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;

import com.sdsmdg.tastytoast.TastyToast;

public class SettingsActivity extends AppCompatActivity {

    CheckBox apkCheckBox;
    CheckBox tmpCheckBox;
    CheckBox emptyCheckBox;
    CheckBox logCheckBox;
    CheckBox cacheCheckBox;
    CheckBox clipboardCheckBox;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

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
        clipboardCheckBox = findViewById(R.id.clipboardBox);

        preferences = getSharedPreferences("userPrefs",MODE_PRIVATE);
        editor = preferences.edit();

        apkCheckBox.setChecked(preferences.getBoolean("deleteAPKs",false));
        tmpCheckBox.setChecked(preferences.getBoolean("deleteTmp",true));
        emptyCheckBox.setChecked(preferences.getBoolean("deleteEmpty",true));
        logCheckBox.setChecked(preferences.getBoolean("deleteLog",true));
        cacheCheckBox.setChecked(preferences.getBoolean("deleteCache",true));
        clipboardCheckBox.setChecked(preferences.getBoolean("deleteClipboard",false));
    }

    public final void save(View view) {

        editor.putBoolean("deleteAPKs",apkCheckBox.isChecked());
        editor.putBoolean("deleteEmpty",emptyCheckBox.isChecked());
        editor.putBoolean("deleteLog",logCheckBox.isChecked());
        editor.putBoolean("deleteCache",cacheCheckBox.isChecked());
        editor.putBoolean("deleteClipboard",clipboardCheckBox.isChecked());
        editor.putBoolean("deleteTmp",tmpCheckBox.isChecked());

        editor.apply();

        TastyToast.makeText(this,"Saved",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS).show();

        Intent randomIntent = new Intent(this, MainActivity.class);
        startActivity(randomIntent);
    }

    public final void back(View view) {

        Intent randomIntent = new Intent(this, MainActivity.class);
        startActivity(randomIntent);
    }
}
