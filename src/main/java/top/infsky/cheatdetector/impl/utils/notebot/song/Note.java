/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package top.infsky.cheatdetector.impl.utils.notebot.song;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;

import java.util.Objects;

@Setter
@Getter
public class Note {

    private NoteBlockInstrument instrument;
    private int noteLevel;

    public Note(NoteBlockInstrument instrument, int noteLevel) {
        this.instrument = instrument;
        this.noteLevel = noteLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return instrument == note.instrument && noteLevel == note.noteLevel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(instrument, noteLevel);
    }

    @Override
    public String toString() {
        return "Note{" +
            "instrument=" + getInstrument() +
            ", noteLevel=" + getNoteLevel() +
            '}';
    }
}
