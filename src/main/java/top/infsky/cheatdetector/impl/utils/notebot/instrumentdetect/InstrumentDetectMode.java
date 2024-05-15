/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package top.infsky.cheatdetector.impl.utils.notebot.instrumentdetect;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.NoteBlock;

@Getter
public enum InstrumentDetectMode {
    BlockState(((noteBlock, blockPos) -> noteBlock.getValue(NoteBlock.INSTRUMENT))),
    BelowBlock(((noteBlock, blockPos) -> {
        assert Minecraft.getInstance().level != null;
        return Minecraft.getInstance().level.getBlockState(blockPos.below()).instrument();
    }));

    private final InstrumentDetectFunction instrumentDetectFunction;

    InstrumentDetectMode(InstrumentDetectFunction instrumentDetectFunction) {
        this.instrumentDetectFunction = instrumentDetectFunction;
    }

}
