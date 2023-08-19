package dev.schlaubi.lavakord.interop

import dev.arbjerg.lavalink.protocol.v4.Track
import dev.schlaubi.lavakord.audio.Event
import dev.schlaubi.lavakord.audio.EventSource
import dev.schlaubi.lavakord.audio.player.Player
import kotlinx.coroutines.CoroutineScope
import java.time.Duration
import java.util.concurrent.CompletableFuture
import kotlin.coroutines.CoroutineContext

/**
 * Wrapper class for [Player] which replaces suspending functions by using [CompletableFuture].
 *
 * @see Player
 * @property playingTrack the currently playing [Track]
 * @property paused whether the playback is currently paused or not
 * @property volume the current volume of this player
 * @property position the position of the current song the player is at (-1 if [playingTrack] is null)
 */
public class JavaPlayer(internal val suspendingPlayer: Player) : CoroutineScope, JavaEventSource<Event> {
    override val coroutineContext: CoroutineContext
        get() = suspendingPlayer.coroutineScope.coroutineContext
    override val suspendingEventSource: EventSource<Event>
        get() = suspendingPlayer
    public val playingTrack: Track?
        get() = suspendingPlayer.playingTrack
    public val paused: Boolean
        get() = suspendingPlayer.paused
    public val volume: Int
        get() = suspendingPlayer.volume
    public val position: Long
        get() = suspendingPlayer.position


    /**
     * Changes the currently playing track to [track].
     */
    public fun playTrack(track: Track): CompletableFuture<Void> = playTrack(track.encoded)

    /**
     * Changes the currently playing track to [track].
     *
     * @param track the lavalink encoded track
     */
    public fun playTrack(track: String): CompletableFuture<Void> =
        run { suspendingPlayer.playTrack(track) }

    /**
     * Stops playback of the currently playing track.
     */
    public fun stopTrack(): CompletableFuture<Void> = run { suspendingPlayer.stopTrack() }

    /**
     * Pauses/unpauses playback of the current track.
     *
     * @param doPause whether the playback should be paused or un-paused
     */
    public fun pause(doPause: Boolean = true): CompletableFuture<Void> = run { suspendingPlayer.pause(doPause) }

    /**
     * Unpauses the playback
     *
     * @see pause
     */
    public fun unPause(): CompletableFuture<Void> = pause(false)

    /**
     * Seeks to a specific [position] in the current playback.
     */
    public fun seekTo(position: Duration): CompletableFuture<Void> = seekTo(position.toMillis())

    /**
     * Seeks to a specific [position] in the current playback.
     *
     * @param position the position in the track in milliseconds
     */
    public fun seekTo(position: Long): CompletableFuture<Void> = run { suspendingPlayer.seekTo(position) }

    /**
     * Creates an [EqualizerBuilder] to update equalizer config.
     * **This does not update the equalizers. To update equalizers please call [EqualizerBuilder.apply]**
     */
    public fun updateEqualizer(): EqualizerBuilder = EqualizerBuilder(suspendingPlayer.equalizers.toMutableMap())
}
