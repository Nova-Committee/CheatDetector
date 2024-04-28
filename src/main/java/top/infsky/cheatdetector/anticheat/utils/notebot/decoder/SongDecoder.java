/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package top.infsky.cheatdetector.anticheat.utils.notebot.decoder;

import top.infsky.cheatdetector.anticheat.modules.NoteBot;
import top.infsky.cheatdetector.anticheat.utils.notebot.song.Song;

import java.io.File;

public abstract class SongDecoder {
    protected NoteBot notebot = (NoteBot) NoteBot.instance;

    /**
     * Parse file to a {@link Song} object
     *
     * @param file Song file
     * @return A {@link Song} object
     */
    public abstract Song parse(File file) throws Exception;
}
