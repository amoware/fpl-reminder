package com.amoware.fplreminder.model.gameweek;

import com.amoware.fplreminder.gameweek.Gameweek;

import java.util.List;

/**
 * Created by amoware on 2020-02-11.
 */
public interface GameweeksTaskInterface {
    void onGameweeksDownloaded(List<Gameweek> gameweeks);
    void writeBootstrapStaticContentToFile(String content);
}
