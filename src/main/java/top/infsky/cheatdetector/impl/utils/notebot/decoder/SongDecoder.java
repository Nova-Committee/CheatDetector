/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package top.infsky.cheatdetector.impl.utils.notebot.decoder;

import top.infsky.cheatdetector.impl.modules.common.NoteBot;
import top.infsky.cheatdetector.impl.utils.notebot.song.Song;

import java.io.File;

public abstract class SongDecoder {
    protected NoteBot notebot = (NoteBot) NoteBot.getInstance();

    /**
     * Parse file to a {@link Song} object
     *
     * @param file Song file
     * @return A {@link Song} object
     */
    public abstract Song parse(File file) throws Exception;
}
