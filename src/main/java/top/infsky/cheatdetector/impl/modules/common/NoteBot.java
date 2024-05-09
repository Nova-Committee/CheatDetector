package top.infsky.cheatdetector.impl.modules.common;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.cheatdetector.CheatDetector;
import top.infsky.cheatdetector.impl.Module;
import top.infsky.cheatdetector.utils.TRPlayer;
import top.infsky.cheatdetector.utils.TRSelf;
import top.infsky.cheatdetector.impl.modules.ClickGUI;
import top.infsky.cheatdetector.impl.utils.world.PlayerRotation;
import top.infsky.cheatdetector.impl.utils.notebot.NotebotUtils;
import top.infsky.cheatdetector.impl.utils.notebot.decoder.SongDecoders;
import top.infsky.cheatdetector.impl.utils.notebot.instrumentdetect.InstrumentDetectMode;
import top.infsky.cheatdetector.impl.utils.notebot.song.Note;
import top.infsky.cheatdetector.impl.utils.notebot.song.Song;
import top.infsky.cheatdetector.impl.utils.notebot.decoder.SongDecoder;
import top.infsky.cheatdetector.config.Advanced3Config;
import top.infsky.cheatdetector.config.ModuleConfig;
import top.infsky.cheatdetector.utils.LogUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class NoteBot extends Module {
    @Getter
    @Nullable
    private static Module instance = null;

    private CompletableFuture<Song> loadingSongFuture = null;

    private Song song; // Loaded song
    private final Map<Note, BlockPos> noteBlockPositions = new HashMap<>(); // Currently used noteblocks by the song
    private final Multimap<Note, BlockPos> scannedNoteblocks = MultimapBuilder.linkedHashKeys().arrayListValues().build(); // Found noteblocks
    private final List<BlockPos> clickedBlocks = new ArrayList<>();
    private Stage stage = Stage.None;
    private PlayingMode playingMode = PlayingMode.None;
    private boolean isPlaying = false;
    private int currentTick = 0;
    private int ticks = 0;

    private boolean anyNoteblockTuned = false;
    private final Map<BlockPos, Integer> tuneHits = new HashMap<>(); // noteblock -> target hits number

    private int waitTicks = -1;
    private boolean lastEnabled = false;

    public NoteBot(@NotNull TRSelf player) {
        super("NoteBot", player);
        instance = this;
    }

    @Override
    public boolean isDisabled() {
        return !ModuleConfig.noteBotEnabled;
    }

    @Override
    public void _onTick() {
        if (!lastEnabled && !isDisabled()) {
            String baseStringPath = Advanced3Config.noteBotFilePath;
            String stringPath = baseStringPath.charAt(0) == '"' && baseStringPath.charAt(baseStringPath.length() - 1) == '"' ?
                    baseStringPath.substring(1, baseStringPath.length() - 1) : baseStringPath;

            try {
                loadSong(Path.of(stringPath).toFile());
//                tune();
            } catch (NullPointerException ignored) {
            } catch (InvalidPathException e) {
                error("Invalid path: %s".formatted(stringPath));
                stop();
            }
        } else if (lastEnabled && isDisabled()) {
            stop();
        }
        lastEnabled = !isDisabled();
        if (isDisabled()) return;


        // meteor code
        ticks++;
        clickedBlocks.clear();

        if (stage == Stage.WaitingToCheckNoteblocks) {
            waitTicks--;
            if (waitTicks == 0) {
                waitTicks = -1;
                info("Checking noteblocks again...");

                setupTuneHitsMap();
                stage = Stage.Tune;
            }
        }
        else if (stage == Stage.SetUp) {
            scanForNoteblocks();
            if (scannedNoteblocks.isEmpty()) {
                error("Can't find any nearby noteblock!");
                stop();
                return;
            }

            setupNoteblocksMap();
            if (noteBlockPositions.isEmpty()) {
                error("Can't find any valid noteblock to play song.");
                stop();
                return;
            }
            setupTuneHitsMap();
            stage = Stage.Tune;
        }
        else if (stage == Stage.Tune) {
            tune();
        }
        else if (stage == Stage.Playing) {
            if (!isPlaying) return;

            if (TRPlayer.CLIENT.player == null || currentTick > song.getLastTick()) {
                // Stop the song after it is finished
                onSongEnd();
                return;
            }

            if (song.getNotesMap().containsKey(currentTick)) {
                if (TRPlayer.CLIENT.player.getAbilities().instabuild) {
                    error("You need to be in survival mode.");
                    stop();
                    return;
                }
                else onTickPlay();
            }

            currentTick++;
        }
    }


    /**
     * 为了兼容meteor代码
     */
    public void info(String msg1) {
        customMsg(msg1);
    }
    public void info(String msg1, String msg2) {
        customMsg(msg1.formatted(msg2));
    }

    /**
     * 为了兼容meteor代码
     */
    public void warning(String msg1, int msg2) {
        if (!Advanced3Config.noteBotDebug) return;
        customMsg(ChatFormatting.GOLD + msg1.formatted(msg2));
    }
    public void warning(String msg1) {
        if (!Advanced3Config.noteBotDebug) return;
        customMsg(ChatFormatting.GOLD + msg1);
    }

    /**
     * 为了兼容meteor代码
     */
    public void error(String msg1) {
        customMsg(ChatFormatting.DARK_RED + msg1);
    }

    /**
     * Gets an Instrument from Note Map
     *
     * @param inst An instrument
     * @return A new instrument mapped by instrument given in parameters
     */
    @Nullable
    public NoteBlockInstrument getMappedInstrument(@NotNull NoteBlockInstrument inst) {
        if (Advanced3Config.getNoteBotMode() == NotebotUtils.NotebotMode.ExactInstruments) {
            NotebotUtils.OptionalInstrument optionalInstrument = NotebotUtils.OptionalInstrument.None;  // TODO 尚未实现
            return optionalInstrument.toMinecraftInstrument();
        } else {
            return inst;
        }
    }

    /**
     * Loads and plays song
     *
     * @param file Song supported by one of {@link SongDecoder}
     */
    public void loadSong(File file) {
        resetVariables();

        this.playingMode = PlayingMode.Noteblocks;
        if (!loadFileToMap(file, () -> stage = Stage.SetUp)) {
            onSongEnd();
            return;
        }
        ClickGUI.update();
    }

    /**
     * Tunes noteblocks. This method is called per tick.
     */
    private void tune() {
        if (tuneHits.isEmpty()) {
            if (anyNoteblockTuned) {
                anyNoteblockTuned = false;
                waitTicks = Advanced3Config.noteBotCheckNoteblocksAgainDelay;
                stage = Stage.WaitingToCheckNoteblocks;

                info("Delaying check for noteblocks");
            } else {
                stage = Stage.Playing;
                info("Loading done.");
                play();
            }
            return;
        }

        if (ticks < Advanced3Config.noteBotTickDelay) {
            return;
        }

        tuneBlocks();
        ticks = 0;
    }

    private void tuneBlocks() {
        if (player.fabricPlayer == null) {
            CheatDetector.CONFIG_HANDLER.configManager.setValue("noteBotEnabled", false);
            ModuleConfig.noteBotEnabled = false;
        }

        if (Advanced3Config.noteBotSwingArm) {
            player.fabricPlayer.swing(InteractionHand.MAIN_HAND);
        }

        int iterations = 0;
        var iterator = tuneHits.entrySet().iterator();

        // Concurrent tuning :o
        while (iterator.hasNext()){
            var entry = iterator.next();
            BlockPos pos = entry.getKey();
            int hitsNumber = entry.getValue();

            if (Advanced3Config.noteBotAutoRotate)
                if (Advanced3Config.noteBotSilentRotate)
                    PlayerRotation.silentRotate(PlayerRotation.getYaw(pos), PlayerRotation.getPitch(pos), player.currentOnGround);
                else
                    PlayerRotation.rotate(PlayerRotation.getYaw(pos), PlayerRotation.getPitch(pos));
            this.tuneNoteblockWithPackets(pos);

            clickedBlocks.add(pos);

            hitsNumber--;
            entry.setValue(hitsNumber);

            if (hitsNumber == 0) {
                iterator.remove();
            }

            iterations++;

            if (iterations == Advanced3Config.noteBotConcurrentTuneBlocks) return;
        }
    }

    private void tuneNoteblockWithPackets(BlockPos pos) {
        // We don't need to raycast here. Server handles this packet fine
        player.fabricPlayer.connection.send(new ServerboundUseItemOnPacket(InteractionHand.MAIN_HAND, new BlockHitResult(Vec3.atCenterOf(pos), Direction.DOWN, pos, false), 0));

        anyNoteblockTuned = true;
    }

    /**
     * Plays a song after loading and tuning
     */
    public void play() {
        if (player.fabricPlayer == null) return;
        if (player.fabricPlayer.getAbilities().instabuild && playingMode != PlayingMode.Preview) {
            error("You need to be in survival mode.");
        } else if (stage == Stage.Playing) {
            isPlaying = true;
            info("Playing.");
        } else {
            error("No song loaded.");
        }
    }

    /**
     * Loads and plays song directly
     *
     * @param file Song supported by one of {@link SongDecoder}
     * @param callback Callback that is run when song has been loaded
     * @return Success
     */
    public boolean loadFileToMap(File file, Runnable callback) {
        if (!file.exists() || !file.isFile()) {
            error("File not found");
            return false;
        }

        if (!SongDecoders.hasDecoder(file)) {
            error("File is in wrong format. Decoder not found.");
            return false;
        }

        info("Loading song \"%s\".", FilenameUtils.getBaseName(file.getName()));

        // Start loading song
        loadingSongFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return SongDecoders.parse(file);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        loadingSongFuture.completeOnTimeout(null, 60, TimeUnit.SECONDS);

        stage = Stage.LoadingSong;
        long time1 = System.currentTimeMillis();
        loadingSongFuture.whenComplete((song ,ex) -> {
            if (ex == null) {
                // Song is null only when it times out
                if (song == null) {
                    error("Loading song '" + FilenameUtils.getBaseName(file.getName()) + "' timed out.");
                    onSongEnd();
                    return;
                }

                this.song = song;
                long time2 = System.currentTimeMillis();
                long diff = time2 - time1;

                info("Song '" + FilenameUtils.getBaseName(file.getName()) + "' has been loaded to the memory! Took "+diff+"ms");
                callback.run();
            } else {
                if (ex instanceof CancellationException) {
                    error("Loading song '" + FilenameUtils.getBaseName(file.getName()) + "' was cancelled.");
                } else {
                    error("An error occurred while loading song '" + FilenameUtils.getBaseName(file.getName()) + "'. See the logs for more details");
                    LogUtils.LOGGER.error("An error occurred while loading song '" + FilenameUtils.getBaseName(file.getName()) + "'", ex);
                    onSongEnd();
                }
            }
        });
        return true;
    }

    public void onSongEnd() {
        stop();
    }

    public void stop() {
        if (!isDisabled()) info("Stopping.");
        CheatDetector.CONFIG_HANDLER.configManager.setValue("noteBotEnabled", false);
        ModuleConfig.noteBotEnabled = false;
        ClickGUI.update();
    }

    private void resetVariables() {
        if (loadingSongFuture != null) {
            loadingSongFuture.cancel(true);
            loadingSongFuture = null;
        }
        clickedBlocks.clear();
        tuneHits.clear();
        anyNoteblockTuned = false;
        currentTick = 0;
        playingMode = PlayingMode.None;
        isPlaying = false;
        stage = Stage.None;
        song = null;
        noteBlockPositions.clear();
    }

    /**
     * Set up a tune hits map which tells how many times player needs to
     * hit noteblock to obtain desired note level
     */
    private void setupTuneHitsMap() {
        if (TRPlayer.CLIENT.level == null) return;
        tuneHits.clear();

        for (var entry : noteBlockPositions.entrySet()) {
            int targetLevel = entry.getKey().getNoteLevel();
            BlockPos blockPos = entry.getValue();

            BlockState blockState = TRPlayer.CLIENT.level.getBlockState(blockPos);
            int currentLevel = blockState.getValue(NoteBlock.NOTE);

            if (targetLevel != currentLevel) {
                tuneHits.put(blockPos, calcNumberOfHits(currentLevel, targetLevel));
            }
        }
    }

    private static int calcNumberOfHits(int from, int to) {
        if (from > to) {
            return (25 - from) + to;
        } else {
            return to - from;
        }
    }

    /**
     * Scans noteblocks nearby and adds them to the map
     */
    private void scanForNoteblocks() {
        if (TRPlayer.CLIENT.gameMode == null || TRPlayer.CLIENT.level == null || TRPlayer.CLIENT.player == null) return;
        scannedNoteblocks.clear();
        int min = (int) (-TRPlayer.CLIENT.gameMode.getPickRange()) - 2;
        int max = (int) TRPlayer.CLIENT.gameMode.getPickRange() + 2;

        // Scan for noteblocks horizontally
        // 6^3 kek
        for (int y = min; y < max; y++) {
            for (int x = min; x < max; x++) {
                for (int z = min; z < max; z++) {
                    BlockPos pos = TRPlayer.CLIENT.player.blockPosition().offset(x, y + 1, z);

                    BlockState blockState = TRPlayer.CLIENT.level.getBlockState(pos);
                    if (blockState.getBlock() != Blocks.NOTE_BLOCK) continue;

                    // Copied from ServerPlayNetworkHandler#onPlayerInteractBlock
                    Vec3 vec3d2 = Vec3.atCenterOf(pos);
                    double sqDist = TRPlayer.CLIENT.player.getEyePosition().distanceToSqr(vec3d2);
                    if (sqDist > ServerGamePacketListenerImpl.MAX_INTERACTION_DISTANCE) continue;

                    if (!isValidScanSpot(pos)) continue;

                    Note note = NotebotUtils.getNoteFromNoteBlock(blockState, pos, Advanced3Config.getNoteBotMode(), InstrumentDetectMode.BlockState.getInstrumentDetectFunction());
                    scannedNoteblocks.put(note, pos);
                }
            }

        }
    }

    private void onTickPlay() {
        Collection<Note> notes = song.getNotesMap().get(this.currentTick);
        if (!notes.isEmpty()) {

            // Rotate player's head
            if (Advanced3Config.noteBotAutoRotate) {
                Optional<Note> firstNote = notes.stream().findFirst();
                BlockPos firstPos = noteBlockPositions.get(firstNote.get());

                if (firstPos != null) {
                    if (Advanced3Config.noteBotSilentRotate)
                        PlayerRotation.silentRotate(PlayerRotation.getYaw(firstPos), PlayerRotation.getPitch(firstPos), player.currentOnGround);
                    else
                        PlayerRotation.rotate(PlayerRotation.getYaw(firstPos), PlayerRotation.getPitch(firstPos));
                }
            }

            // Swing arm
            if (Advanced3Config.noteBotSwingArm) {
                player.fabricPlayer.swing(InteractionHand.MAIN_HAND);
            }

            // Play notes
            for (Note note : notes) {
                BlockPos pos = noteBlockPositions.get(note);
                if (pos == null) {
                    return;
                }

                if (Advanced3Config.noteBotPolyphonic) {
                    playRotate(pos);
                } else {
                    this.playRotate(pos);
                }
            }
        }
    }

    private void playRotate(BlockPos pos) {
        try {
            player.fabricPlayer.connection.send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, pos, Direction.DOWN, 0));
        } catch (NullPointerException ignored) {
        }
    }

    private boolean isValidScanSpot(BlockPos pos) {
        try (Level level = TRPlayer.CLIENT.level) {
            if (level == null) return false;
            if (level.getBlockState(pos).getBlock() != Blocks.NOTE_BLOCK) return false;
            return level.getBlockState(pos.above()).isAir();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Set up a map of noteblocks positions
     */
    private void setupNoteblocksMap() {
        noteBlockPositions.clear();

        // Modifiable list of unique notes
        List<Note> uniqueNotesToUse = new ArrayList<>(song.getRequirements());
        // A map with noteblocks that have incorrect note level
        Map<NoteBlockInstrument, List<BlockPos>> incorrectNoteBlocks = new HashMap<>();

        // Check if there are already tuned noteblocks
        for (var entry : scannedNoteblocks.asMap().entrySet()) {
            Note note = entry.getKey();
            List<BlockPos> noteblocks = new ArrayList<>(entry.getValue());

            if (uniqueNotesToUse.contains(note)) {
                // Add correct noteblock position to a noteBlockPositions
                noteBlockPositions.put(note, noteblocks.remove(0));
                uniqueNotesToUse.remove(note);
            }

            if (!noteblocks.isEmpty()) {
                // Add excess noteblocks for mapping process [note -> block pos]

                if (!incorrectNoteBlocks.containsKey(note.getInstrument())) {
                    incorrectNoteBlocks.put(note.getInstrument(), new ArrayList<>());
                }

                incorrectNoteBlocks.get(note.getInstrument()).addAll(noteblocks);
            }
        }

        // Map [note -> block pos]
        for (var entry : incorrectNoteBlocks.entrySet()) {
            List<BlockPos> positions = entry.getValue();

            if (Advanced3Config.getNoteBotMode() == NotebotUtils.NotebotMode.ExactInstruments) {
                NoteBlockInstrument inst = entry.getKey();

                List<Note> foundNotes = uniqueNotesToUse.stream()
                        .filter(note -> note.getInstrument() == inst)
                        .collect(Collectors.toList());

                if (foundNotes.isEmpty()) continue;

                for (BlockPos pos : positions) {
                    if (foundNotes.isEmpty()) break;

                    Note note = foundNotes.remove(0);
                    noteBlockPositions.put(note, pos);

                    uniqueNotesToUse.remove(note);
                }
            } else {
                for (BlockPos pos : positions) {
                    if (uniqueNotesToUse.isEmpty()) break;

                    Note note = uniqueNotesToUse.remove(0);
                    noteBlockPositions.put(note, pos);
                }
            }
        }

        if (!uniqueNotesToUse.isEmpty()) {
            for (Note note : uniqueNotesToUse) {
                warning("Missing note: "+note.getInstrument()+", "+note.getNoteLevel());
            }
            warning(uniqueNotesToUse.size()+" missing notes!");
        }
    }

    @Override
    public boolean _onPacketSend(@NotNull Packet<?> basepacket, Connection connection, PacketSendListener listener, CallbackInfo ci) {
        if (isDisabled()) return false;
        if (!Advanced3Config.noteBotSilentRotate) return false;
        if (basepacket instanceof ServerboundMovePlayerPacket packet)
            return PlayerRotation.cancelRotationPacket(packet, connection, listener, ci);
        return false;
    }

    public enum Stage {
        None,
        LoadingSong,
        SetUp,
        Tune,
        WaitingToCheckNoteblocks,
        Playing
    }

    public enum PlayingMode {
        None,
        Preview,
        Noteblocks
    }
}
