// DO NOT EDIT!! - This code has been generated by QueryUtilityProcessor
// Edit this file instead lavasrc/src/commonMain/kotlin/QueryUtils.kt
@file:Suppress(names = arrayOf("IncorrectFormatting", "INVISIBLE_REFERENCE"))

package dev.schlaubi.lavakord.plugins.lavasrc

import dev.arbjerg.lavalink.protocol.v4.LoadResult
import dev.schlaubi.lavakord.`internal`.QueryBuilder
import dev.schlaubi.lavakord.`internal`.query
import dev.schlaubi.lavakord.`internal`.taggedQuery
import dev.schlaubi.lavakord.audio.Node
import dev.schlaubi.lavakord.audio.player.PlayOptions
import dev.schlaubi.lavakord.audio.player.Player
import dev.schlaubi.lavakord.rest.loadItem
import kotlin.contracts.InvocationKind.EXACTLY_ONCE
import kotlin.contracts.contract

/**
 * Performs a track search and plays the result using [Spotify](https://spotify.com).
 */
public suspend fun Player.searchAndPlayUsingSpotify(
    query: String,
    options: SpotifySearchQueryBuilder = SpotifySearchQueryBuilder.Default,
    playOptionsBuilder: PlayOptions.() -> Unit = {},
) {
    contract { callsInPlace(playOptionsBuilder, EXACTLY_ONCE) }
    searchAndPlayTrack("spsearch:" + options.toQuery(query), playOptionsBuilder)
}

/**
 * Performs a track search using [Spotify](https://spotify.com).
 */
public suspend inline fun Node.searchUsingSpotify(query: String,
        builder: SpotifySearchQueryBuilder.() -> Unit = {}): LoadResult {
    contract { callsInPlace(builder, EXACTLY_ONCE) }
    val options = SpotifySearchQueryBuilder().apply(builder)
    return loadItem("spsearch:" + options.toQuery(query))
}

/**
 * Builder for Spotify search queries.
 */
@Suppress(names = arrayOf("MemberVisibilityCanBePrivate"))
public class SpotifySearchQueryBuilder : QueryBuilder {
    /**
     * Searches for results from a specific arist (Only works for album, artists and tracks)
     */
    public var artist: String? = null

    /**
     * Searches for results from within a specific range (eg.1955-1960) (Only works for album,
     * artists and tracks)
     */
    public var year: String? = null

    /**
     * Searches for results containing a specific track (like Albums, Artists)
     */
    public var track: String? = null

    /**
     * Searches for albums with a specific upc
     */
    public var upc: String? = null

    /**
     * Searches for a specific track by it's isrc
     */
    public var isrc: String? = null

    /**
     * Searches for a specific track by it's isrc
     */
    public var tag: Tag? = null

    @PublishedApi
    @Suppress(names = arrayOf("INVISIBLE_MEMBER"))
    internal fun toQuery(query: String): String = taggedQuery("" to query, "artist" to artist,
            "year" to year, "track" to track, "upc" to upc, "isrc" to isrc, "tag" to tag)

    /**
     * Type of [Tag].
     */
    public enum class Tag(
        /**
         * The value used in queries
         */
        public val `value`: String,
    ) {
        /**
         * The `tag:new` filter will return albums released in the past two weeks
         */
        NEW("new"),
        /**
         * The `tag:hipster` can be used to return only albums with the lowest 10% popularity.
         */
        HIPSTER("hipster"),
        ;

        override fun toString(): String = value
    }

    public companion object {
        /**
         * An instance of the builder with default values
         */
        public val Default: SpotifySearchQueryBuilder = SpotifySearchQueryBuilder()
    }
}

/**
 * Creates a new [SpotifySearchQueryBuilder] and applies [builder] to it
 */
public inline fun spotifySearchQuery(builder: SpotifySearchQueryBuilder.() -> Unit):
        SpotifySearchQueryBuilder {
    contract { callsInPlace(builder, EXACTLY_ONCE) }
    return SpotifySearchQueryBuilder().apply(builder)
}

/**
 * Performs a track search using [Spotify](https://spotify.com).
 *
 * @param seedArtists A comma separated list of
 * [Spotify IDs](https://developer.spotify.com/documentation/web-api/concepts/spotify-uris-ids) for
 * seed artists
 * @param seedGenres A comma separated list of any genres in the set of
 * [available genre seeds](https://developer.spotify.com/documentation/web-api/reference/get-recommendations#available-genre-seeds)
 * @param seedTracks A comma separated list of
 * [Spotify IDs](https://developer.spotify.com/documentation/web-api/concepts/spotify-uris-ids) for
 * seed tracks
 */
public suspend inline fun Node.recommendUsingSpotify(
    seedArtists: String,
    seedGenres: String,
    seedTracks: String,
    builder: SpotifyRecommendQueryBuilder.() -> Unit = {},
): LoadResult {
    contract { callsInPlace(builder, EXACTLY_ONCE) }
    val options = SpotifyRecommendQueryBuilder().apply(builder)
    return loadItem("sprec:" + options.toQuery(seedArtists, seedGenres, seedTracks))
}

/**
 * Builder for Spotify recommend queries.
 */
@Suppress(names = arrayOf("MemberVisibilityCanBePrivate"))
public class SpotifyRecommendQueryBuilder : QueryBuilder {
    /**
     * The target size of the list of recommended tracks. For seeds with unusually small pools or
     * when highly restrictive filtering is applied, it may be impossible to generate the requested
     * number of recommended tracks. Debugging information for such cases is available in the response.
     * Default: 20. Minimum: 1. Maximum: 100.
     */
    public var limit: Int? = null

    /**
     * For each tunable track attribute, a hard floor on the selected track attribute’s value can be
     * provided. See tunable track attributes below for the list of available options. For example,
     * `min_tempo=140` would restrict results to only those tracks with a tempo of greater than 140
     * beats per minute.
     */
    public var minAcousticness: Double? = null

    /**
     * For each tunable track attribute, a hard ceiling on the selected track attribute’s value can
     * be provided. See tunable track attributes below for the list of available options. For example,
     * `max_instrumentalness=0.35` would filter out most tracks that are likely to be instrumental.
     */
    public var maxAcousticness: Double? = null

    /**
     * For each of the tunable track attributes (below) a target value may be provided. Tracks with
     * the attribute values nearest to the target values will be preferred. For example, you might
     * request `target_energy=0.6` and `target_danceability=0.8`. All target values will be weighed
     * equally in ranking results.
     */
    public var targetAcousticness: Double? = null

    /**
     * For each tunable track attribute, a hard floor on the selected track attribute’s value can be
     * provided. See tunable track attributes below for the list of available options. For example,
     * `min_tempo=140` would restrict results to only those tracks with a tempo of greater than 140
     * beats per minute.
     */
    public var minDanceability: Double? = null

    /**
     * For each tunable track attribute, a hard ceiling on the selected track attribute’s value can
     * be provided. See tunable track attributes below for the list of available options. For example,
     * `max_instrumentalness=0.35` would filter out most tracks that are likely to be instrumental.
     */
    public var maxDanceability: Double? = null

    /**
     * For each of the tunable track attributes (below) a target value may be provided. Tracks with
     * the attribute values nearest to the target values will be preferred. For example, you might
     * request `target_energy=0.6` and `target_danceability=0.8`. All target values will be weighed
     * equally in ranking results.
     */
    public var targetDanceability: Double? = null

    /**
     * For each tunable track attribute, a hard floor on the selected track attribute’s value can be
     * provided. See tunable track attributes below for the list of available options. For example,
     * `min_tempo=140` would restrict results to only those tracks with a tempo of greater than 140
     * beats per minute.
     */
    public var minDurationMs: Int? = null

    /**
     * For each tunable track attribute, a hard ceiling on the selected track attribute’s value can
     * be provided. See tunable track attributes below for the list of available options. For example,
     * `max_instrumentalness=0.35` would filter out most tracks that are likely to be instrumental.
     */
    public var maxDurationMs: Int? = null

    /**
     * Target duration of the track (ms)
     */
    public var targetDurationMs: Int? = null

    /**
     * For each tunable track attribute, a hard floor on the selected track attribute’s value can be
     * provided. See tunable track attributes below for the list of available options. For example,
     * `min_tempo=140` would restrict results to only those tracks with a tempo of greater than 140
     * beats per minute.
     */
    public var minEnergy: Double? = null

    /**
     * For each tunable track attribute, a hard ceiling on the selected track attribute’s value can
     * be provided. See tunable track attributes below for the list of available options. For example,
     * `max_instrumentalness=0.35` would filter out most tracks that are likely to be instrumental.
     */
    public var maxEnergy: Double? = null

    /**
     * For each of the tunable track attributes (below) a target value may be provided. Tracks with
     * the attribute values nearest to the target values will be preferred. For example, you might
     * request `target_energy=0.6` and `target_danceability=0.8`. All target values will be weighed
     * equally in ranking results.
     */
    public var targetEnergy: Double? = null

    /**
     * For each tunable track attribute, a hard floor on the selected track attribute’s value can be
     * provided. See tunable track attributes below for the list of available options. For example,
     * `min_tempo=140` would restrict results to only those tracks with a tempo of greater than 140
     * beats per minute.
     */
    public var minInstrumentalness: Double? = null

    /**
     * For each tunable track attribute, a hard ceiling on the selected track attribute’s value can
     * be provided. See tunable track attributes below for the list of available options. For example,
     * `max_instrumentalness=0.35` would filter out most tracks that are likely to be instrumental.
     */
    public var maxInstrumentalness: Double? = null

    /**
     * For each of the tunable track attributes (below) a target value may be provided. Tracks with
     * the attribute values nearest to the target values will be preferred. For example, you might
     * request `target_energy=0.6` and `target_danceability=0.8`. All target values will be weighed
     * equally in ranking results.
     */
    public var targetInstrumentalness: Double? = null

    /**
     * For each tunable track attribute, a hard floor on the selected track attribute’s value can be
     * provided. See tunable track attributes below for the list of available options. For example,
     * `min_tempo=140` would restrict results to only those tracks with a tempo of greater than 140
     * beats per minute.
     */
    public var minKey: Int? = null

    /**
     * For each tunable track attribute, a hard ceiling on the selected track attribute’s value can
     * be provided. See tunable track attributes below for the list of available options. For example,
     * `max_instrumentalness=0.35` would filter out most tracks that are likely to be instrumental.
     */
    public var maxKey: Int? = null

    /**
     * For each of the tunable track attributes (below) a target value may be provided. Tracks with
     * the attribute values nearest to the target values will be preferred. For example, you might
     * request `target_energy=0.6` and `target_danceability=0.8`. All target values will be weighed
     * equally in ranking results.
     */
    public var targetKey: Int? = null

    /**
     * For each tunable track attribute, a hard floor on the selected track attribute’s value can be
     * provided. See tunable track attributes below for the list of available options. For example,
     * `min_tempo=140` would restrict results to only those tracks with a tempo of greater than 140
     * beats per minute.
     */
    public var minLiveness: Double? = null

    /**
     * For each tunable track attribute, a hard ceiling on the selected track attribute’s value can
     * be provided. See tunable track attributes below for the list of available options. For example,
     * `max_instrumentalness=0.35` would filter out most tracks that are likely to be instrumental.
     */
    public var maxLiveness: Double? = null

    /**
     * For each of the tunable track attributes (below) a target value may be provided. Tracks with
     * the attribute values nearest to the target values will be preferred. For example, you might
     * request `target_energy=0.6` and `target_danceability=0.8`. All target values will be weighed
     * equally in ranking results.
     */
    public var targetLiveness: Double? = null

    /**
     * For each tunable track attribute, a hard floor on the selected track attribute’s value can be
     * provided. See tunable track attributes below for the list of available options. For example,
     * `min_tempo=140` would restrict results to only those tracks with a tempo of greater than 140
     * beats per minute.
     */
    public var minLoudness: Double? = null

    /**
     * For each tunable track attribute, a hard ceiling on the selected track attribute’s value can
     * be provided. See tunable track attributes below for the list of available options. For example,
     * `max_instrumentalness=0.35` would filter out most tracks that are likely to be instrumental.
     */
    public var maxLoudness: Double? = null

    /**
     * For each of the tunable track attributes (below) a target value may be provided. Tracks with
     * the attribute values nearest to the target values will be preferred. For example, you might
     * request `target_energy=0.6` and `target_danceability=0.8`. All target values will be weighed
     * equally in ranking results.
     */
    public var targetLoudness: Double? = null

    /**
     * For each tunable track attribute, a hard floor on the selected track attribute’s value can be
     * provided. See tunable track attributes below for the list of available options. For example,
     * `min_tempo=140` would restrict results to only those tracks with a tempo of greater than 140
     * beats per minute.
     */
    public var minMode: Int? = null

    /**
     * For each tunable track attribute, a hard ceiling on the selected track attribute’s value can
     * be provided. See tunable track attributes below for the list of available options. For example,
     * `max_instrumentalness=0.35` would filter out most tracks that are likely to be instrumental.
     */
    public var maxMode: Int? = null

    /**
     * For each of the tunable track attributes (below) a target value may be provided. Tracks with
     * the attribute values nearest to the target values will be preferred. For example, you might
     * request `target_energy=0.6` and `target_danceability=0.8`. All target values will be weighed
     * equally in ranking results.
     */
    public var targetMode: Int? = null

    /**
     * For each tunable track attribute, a hard floor on the selected track attribute’s value can be
     * provided. See tunable track attributes below for the list of available options. For example,
     * `min_tempo=140` would restrict results to only those tracks with a tempo of greater than 140
     * beats per minute.
     */
    public var minPopularity: Int? = null

    /**
     * For each tunable track attribute, a hard ceiling on the selected track attribute’s value can
     * be provided. See tunable track attributes below for the list of available options. For example,
     * `max_instrumentalness=0.35` would filter out most tracks that are likely to be instrumental.
     */
    public var maxPopularity: Int? = null

    /**
     * For each of the tunable track attributes (below) a target value may be provided. Tracks with
     * the attribute values nearest to the target values will be preferred. For example, you might
     * request `target_energy=0.6` and `target_danceability=0.8`. All target values will be weighed
     * equally in ranking results.
     */
    public var targetPopularity: Int? = null

    /**
     * For each tunable track attribute, a hard floor on the selected track attribute’s value can be
     * provided. See tunable track attributes below for the list of available options. For example,
     * `min_tempo=140` would restrict results to only those tracks with a tempo of greater than 140
     * beats per minute.
     */
    public var minSpeechiness: Double? = null

    /**
     * For each tunable track attribute, a hard ceiling on the selected track attribute’s value can
     * be provided. See tunable track attributes below for the list of available options. For example,
     * `max_instrumentalness=0.35` would filter out most tracks that are likely to be instrumental.
     */
    public var maxSpeechiness: Double? = null

    /**
     * For each of the tunable track attributes (below) a target value may be provided. Tracks with
     * the attribute values nearest to the target values will be preferred. For example, you might
     * request `target_energy=0.6` and `target_danceability=0.8`. All target values will be weighed
     * equally in ranking results.
     */
    public var targetSpeechiness: Double? = null

    /**
     * For each tunable track attribute, a hard floor on the selected track attribute’s value can be
     * provided. See tunable track attributes below for the list of available options. For example,
     * `min_tempo=140` would restrict results to only those tracks with a tempo of greater than 140
     * beats per minute.
     */
    public var minTempo: Double? = null

    /**
     * For each tunable track attribute, a hard ceiling on the selected track attribute’s value can
     * be provided. See tunable track attributes below for the list of available options. For example,
     * `max_instrumentalness=0.35` would filter out most tracks that are likely to be instrumental.
     */
    public var maxTempo: Double? = null

    /**
     * Target tempo (BPM)
     */
    public var targetTempo: Double? = null

    /**
     * For each tunable track attribute, a hard floor on the selected track attribute’s value can be
     * provided. See tunable track attributes below for the list of available options. For example,
     * `min_tempo=140` would restrict results to only those tracks with a tempo of greater than 140
     * beats per minute.
     */
    public var minTimeSignature: Int? = null

    /**
     * For each tunable track attribute, a hard ceiling on the selected track attribute’s value can
     * be provided. See tunable track attributes below for the list of available options. For example,
     * `max_instrumentalness=0.35` would filter out most tracks that are likely to be instrumental.
     */
    public var maxTimeSignature: Int? = null

    /**
     * For each of the tunable track attributes (below) a target value may be provided. Tracks with
     * the attribute values nearest to the target values will be preferred. For example, you might
     * request `target_energy=0.6` and `target_danceability=0.8`. All target values will be weighed
     * equally in ranking results.
     */
    public var targetTimeSignature: Int? = null

    /**
     * For each tunable track attribute, a hard floor on the selected track attribute’s value can be
     * provided. See tunable track attributes below for the list of available options. For example,
     * `min_tempo=140` would restrict results to only those tracks with a tempo of greater than 140
     * beats per minute.
     */
    public var minValence: Double? = null

    /**
     * For each tunable track attribute, a hard ceiling on the selected track attribute’s value can
     * be provided. See tunable track attributes below for the list of available options. For example,
     * `max_instrumentalness=0.35` would filter out most tracks that are likely to be instrumental.
     */
    public var maxValence: Double? = null

    /**
     * For each of the tunable track attributes (below) a target value may be provided. Tracks with
     * the attribute values nearest to the target values will be preferred. For example, you might
     * request `target_energy=0.6` and `target_danceability=0.8`. All target values will be weighed
     * equally in ranking results.
     */
    public var targetValence: Double? = null

    @PublishedApi
    @Suppress(names = arrayOf("INVISIBLE_MEMBER"))
    internal fun toQuery(
        seedArtists: String,
        seedGenres: String,
        seedTracks: String,
    ): String = query("seed_artists" to seedArtists, "seed_genres" to seedGenres,
            "seed_tracks" to seedTracks, "limit" to limit, "min_acousticness" to minAcousticness,
            "max_acousticness" to maxAcousticness, "target_acousticness" to targetAcousticness,
            "min_danceability" to minDanceability, "max_danceability" to maxDanceability,
            "target_danceability" to targetDanceability, "min_duration_ms" to minDurationMs,
            "max_duration_ms" to maxDurationMs, "target_duration_ms" to targetDurationMs,
            "min_energy" to minEnergy, "max_energy" to maxEnergy, "target_energy" to targetEnergy,
            "min_instrumentalness" to minInstrumentalness,
            "max_instrumentalness" to maxInstrumentalness,
            "target_instrumentalness" to targetInstrumentalness, "min_key" to minKey,
            "max_key" to maxKey, "target_key" to targetKey, "min_liveness" to minLiveness,
            "max_liveness" to maxLiveness, "target_liveness" to targetLiveness,
            "min_loudness" to minLoudness, "max_loudness" to maxLoudness,
            "target_loudness" to targetLoudness, "min_mode" to minMode, "max_mode" to maxMode,
            "target_mode" to targetMode, "min_popularity" to minPopularity,
            "max_popularity" to maxPopularity, "target_popularity" to targetPopularity,
            "min_speechiness" to minSpeechiness, "max_speechiness" to maxSpeechiness,
            "target_speechiness" to targetSpeechiness, "min_tempo" to minTempo,
            "max_tempo" to maxTempo, "target_tempo" to targetTempo,
            "min_time_signature" to minTimeSignature, "max_time_signature" to maxTimeSignature,
            "target_time_signature" to targetTimeSignature, "min_valence" to minValence,
            "max_valence" to maxValence, "target_valence" to targetValence)

    public companion object {
        /**
         * An instance of the builder with default values
         */
        public val Default: SpotifyRecommendQueryBuilder = SpotifyRecommendQueryBuilder()
    }
}

/**
 * Creates a new [SpotifyRecommendQueryBuilder] and applies [builder] to it
 */
public inline fun spotifyRecommendQuery(builder: SpotifyRecommendQueryBuilder.() -> Unit):
        SpotifyRecommendQueryBuilder {
    contract { callsInPlace(builder, EXACTLY_ONCE) }
    return SpotifyRecommendQueryBuilder().apply(builder)
}
