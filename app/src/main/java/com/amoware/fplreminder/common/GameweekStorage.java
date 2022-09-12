package com.amoware.fplreminder.common;

import static com.amoware.fplreminder.common.Constants.tagger;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.amoware.fplreminder.gameweek.Gameweek;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Created by amoware on 2022-09-06.
 */
public class GameweekStorage {
    @Nullable
    private final Context mContext;

    private static final String FILE_NAME = "storage.txt";

    public GameweekStorage(@Nullable Context context) {
        mContext = context;
    }

    @Nullable
    public List<Gameweek> readGameweeksFromFile() {
        return GameweekParser.toGameweeks(readFileContent());
    }

    @Nullable
    private String readFileContent() {
        if (mContext == null) {
            Log.e(tagger(getClass()), "readFileContent: context is null");
            return null;
        }

        try {
            return readStringFromInputStream(mContext.openFileInput(FILE_NAME));
        } catch (FileNotFoundException e) {
            Log.w(tagger(getClass()), "readFileContent: file not found", e);
        } catch (IOException e) {
            Log.e(tagger(getClass()), "readFileContent: cannot read file", e);
        }

        return null;
    }

    @Nullable
    private String readStringFromInputStream(@Nullable InputStream inputStream) throws IOException {
        if (inputStream == null) {
            Log.w(tagger(getClass()), "readStringFromInputStream: inputStream is null");
            return null;
        }

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String receiveString;
        StringBuilder stringBuilder = new StringBuilder();

        while ((receiveString = bufferedReader.readLine()) != null) {
            stringBuilder.append(receiveString).append("\n");
        }

        inputStream.close();
        return stringBuilder.toString().trim();
    }

    public void writeGameweeksToFile(@Nullable List<Gameweek> gameweeks) {
        if (mContext == null) {
            Log.e(tagger(getClass()), "writeGameweeksToFile: context is null");
            return;
        }

        if (gameweeks == null || gameweeks.size() == 0) {
            Log.w(tagger(getClass()), "writeGameweeksToFile: gameweeks is empty");
            return;
        }

        String data = GameweekParser.toString(gameweeks);
        if (StringUtil.isStringEmpty(data)) {
            Log.w(tagger(getClass()), "writeGameweeksToFile: content to write is empty");
            return;
        }

        try {
            OutputStream outputStream = mContext.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            Log.d(tagger(getClass()), "writeGameweeksToFile: gameweeks written to file");
        } catch (IOException e) {
            Log.e(tagger(getClass()), "writeGameweeksToFile: file write failed", e);
        }
    }
}
