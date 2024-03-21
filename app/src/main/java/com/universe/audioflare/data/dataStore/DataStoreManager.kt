package com.universe.audioflare.data.dataStore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.media3.common.Player
import com.universe.audioflare.common.SELECTED_LANGUAGE
import com.universe.audioflare.common.SPONSOR_BLOCK
import com.universe.audioflare.common.SUPPORTED_LANGUAGE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.universe.audioflare.common.QUALITY as COMMON_QUALITY

class DataStoreManager
    @Inject
    constructor(private val settingsDataStore: DataStore<Preferences>) {
        val location: Flow<String> =
            settingsDataStore.data.map { preferences ->
                preferences[LOCATION] ?: "VN"
            }

        suspend fun setLocation(location: String) {
            withContext(Dispatchers.IO) {
                settingsDataStore.edit { settings ->
                    settings[LOCATION] = location
                }
            }
        }

        val quality: Flow<String> =
            settingsDataStore.data.map { preferences ->
                preferences[QUALITY] ?: COMMON_QUALITY.items[0].toString()
            }

        suspend fun setQuality(quality: String) {
            withContext(Dispatchers.IO) {
                settingsDataStore.edit { settings ->
                    settings[QUALITY] = quality
                }
            }
        }

        val language: Flow<String> =
            settingsDataStore.data.map { preferences ->
                preferences[stringPreferencesKey(SELECTED_LANGUAGE)] ?: SUPPORTED_LANGUAGE.codes.first()
            }

        fun getString(key: String): Flow<String?> {
            return settingsDataStore.data.map { preferences ->
                preferences[stringPreferencesKey(key)]
            }
        }

        suspend fun putString(
            key: String,
            value: String,
        ) {
            settingsDataStore.edit { settings ->
                settings[stringPreferencesKey(key)] = value
            }
        }

        val loggedIn: Flow<String> =
            settingsDataStore.data.map { preferences ->
                preferences[LOGGED_IN] ?: FALSE
            }

        val cookie: Flow<String> =
            settingsDataStore.data.map { preferences ->
                preferences[COOKIE] ?: ""
            }

        suspend fun setCookie(cookie: String) {
            withContext(Dispatchers.IO) {
                settingsDataStore.edit { settings ->
                    settings[COOKIE] = cookie
                }
            }
        }

        suspend fun setLoggedIn(logged: Boolean) {
            withContext(Dispatchers.IO) {
                if (logged) {
                    settingsDataStore.edit { settings ->
                        settings[LOGGED_IN] = TRUE
                    }
                } else {
                    settingsDataStore.edit { settings ->
                        settings[LOGGED_IN] = FALSE
                    }
                }
            }
        }

        val normalizeVolume: Flow<String> =
            settingsDataStore.data.map { preferences ->
                preferences[NORMALIZE_VOLUME] ?: FALSE
            }

        suspend fun setNormalizeVolume(normalize: Boolean) {
            withContext(Dispatchers.IO) {
                if (normalize) {
                    settingsDataStore.edit { settings ->
                        settings[NORMALIZE_VOLUME] = TRUE
                    }
                } else {
                    settingsDataStore.edit { settings ->
                        settings[NORMALIZE_VOLUME] = FALSE
                    }
                }
            }
        }

        val skipSilent: Flow<String> =
            settingsDataStore.data.map { preferences ->
                preferences[SKIP_SILENT] ?: FALSE
            }

        suspend fun setSkipSilent(skip: Boolean) {
            withContext(Dispatchers.IO) {
                if (skip) {
                    settingsDataStore.edit { settings ->
                        settings[SKIP_SILENT] = TRUE
                    }
                } else {
                    settingsDataStore.edit { settings ->
                        settings[SKIP_SILENT] = FALSE
                    }
                }
            }
        }

        val saveStateOfPlayback: Flow<String> =
            settingsDataStore.data.map { preferences ->
                preferences[SAVE_STATE_OF_PLAYBACK] ?: FALSE
            }

        suspend fun setSaveStateOfPlayback(save: Boolean) {
            withContext(Dispatchers.IO) {
                if (save) {
                    settingsDataStore.edit { settings ->
                        settings[SAVE_STATE_OF_PLAYBACK] = TRUE
                    }
                } else {
                    settingsDataStore.edit { settings ->
                        settings[SAVE_STATE_OF_PLAYBACK] = FALSE
                    }
                }
            }
        }

        val shuffleKey: Flow<String> =
            settingsDataStore.data.map { preferences ->
                preferences[SHUFFLE_KEY] ?: FALSE
            }
        val repeatKey: Flow<String> =
            settingsDataStore.data.map { preferences ->
                preferences[REPEAT_KEY] ?: REPEAT_MODE_OFF
            }

        suspend fun recoverShuffleAndRepeatKey(
            shuffle: Boolean,
            repeat: Int,
        ) {
            withContext(Dispatchers.IO) {
                if (shuffle) {
                    settingsDataStore.edit { settings ->
                        settings[SHUFFLE_KEY] = TRUE
                    }
                } else {
                    settingsDataStore.edit { settings ->
                        settings[SHUFFLE_KEY] = FALSE
                    }
                }
                settingsDataStore.edit { settings ->
                    settings[REPEAT_KEY] =
                        when (repeat) {
                            Player.REPEAT_MODE_ONE -> REPEAT_ONE
                            Player.REPEAT_MODE_ALL -> REPEAT_ALL
                            Player.REPEAT_MODE_OFF -> REPEAT_MODE_OFF
                            else -> REPEAT_MODE_OFF
                        }
                }
            }
        }

        val saveRecentSongAndQueue: Flow<String> =
            settingsDataStore.data.map { preferences ->
                preferences[SAVE_RECENT_SONG] ?: FALSE
            }

        suspend fun setSaveRecentSongAndQueue(save: Boolean) {
            withContext(Dispatchers.IO) {
                if (save) {
                    settingsDataStore.edit { settings ->
                        settings[SAVE_RECENT_SONG] = TRUE
                    }
                } else {
                    settingsDataStore.edit { settings ->
                        settings[SAVE_RECENT_SONG] = FALSE
                    }
                }
            }
        }

        val recentMediaId =
            settingsDataStore.data.map { preferences ->
                preferences[RECENT_SONG_MEDIA_ID_KEY] ?: ""
            }
        val recentPosition =
            settingsDataStore.data.map { preferences ->
                preferences[RECENT_SONG_POSITION_KEY] ?: "0"
            }

        suspend fun saveRecentSong(
            mediaId: String,
            position: Long,
        ) {
            Log.w("saveRecentSong", "$mediaId $position")
            withContext(Dispatchers.IO) {
                settingsDataStore.edit { settings ->
                    settings[RECENT_SONG_MEDIA_ID_KEY] = mediaId
                    settings[RECENT_SONG_POSITION_KEY] = position.toString()
                }
            }
        }

        val playlistFromSaved =
            settingsDataStore.data.map { preferences ->
                preferences[FROM_SAVED_PLAYLIST] ?: ""
            }

        suspend fun setPlaylistFromSaved(playlist: String) {
            withContext(Dispatchers.IO) {
                settingsDataStore.edit { settings ->
                    settings[FROM_SAVED_PLAYLIST] = playlist
                }
            }
        }

        val sendBackToGoogle =
            settingsDataStore.data.map { preferences ->
                preferences[SEND_BACK_TO_GOOGLE] ?: FALSE
            }

        suspend fun setSendBackToGoogle(send: Boolean) {
            withContext(Dispatchers.IO) {
                if (send) {
                    settingsDataStore.edit { settings ->
                        settings[SEND_BACK_TO_GOOGLE] = TRUE
                    }
                } else {
                    settingsDataStore.edit { settings ->
                        settings[SEND_BACK_TO_GOOGLE] = FALSE
                    }
                }
            }
        }

        val sponsorBlockEnabled =
            settingsDataStore.data.map { preferences ->
                preferences[SPONSOR_BLOCK_ENABLED] ?: FALSE
            }

        suspend fun setSponsorBlockEnabled(enabled: Boolean) {
            withContext(Dispatchers.IO) {
                if (enabled) {
                    settingsDataStore.edit { settings ->
                        settings[SPONSOR_BLOCK_ENABLED] = TRUE
                    }
                } else {
                    settingsDataStore.edit { settings ->
                        settings[SPONSOR_BLOCK_ENABLED] = FALSE
                    }
                }
            }
        }

        suspend fun getSponsorBlockCategories(): ArrayList<String> {
            val list: ArrayList<String> = arrayListOf()
            for (category in SPONSOR_BLOCK.list) {
                if (getString(category.toString()).first() == TRUE) list.add(category.toString())
            }
            return list
        }

        suspend fun setSponsorBlockCategories(categories: ArrayList<String>) {
            withContext(Dispatchers.IO) {
                Log.w("setSponsorBlockCategories", categories.toString())
                for (category in categories) {
                    settingsDataStore.edit { settings ->
                        settings[stringPreferencesKey(category)] = TRUE
                    }
                }
            }
        }

        val enableTranslateLyric =
            settingsDataStore.data.map { preferences ->
                preferences[USE_TRANSLATION_LANGUAGE] ?: TRUE
            }

        suspend fun setEnableTranslateLyric(enable: Boolean) {
            withContext(Dispatchers.IO) {
                if (enable) {
                    settingsDataStore.edit { settings ->
                        settings[USE_TRANSLATION_LANGUAGE] = TRUE
                    }
                } else {
                    settingsDataStore.edit { settings ->
                        settings[USE_TRANSLATION_LANGUAGE] = FALSE
                    }
                }
            }
        }

        val lyricsProvider =
            settingsDataStore.data.map { preferences ->
                preferences[LYRICS_PROVIDER] ?: MUSIXMATCH
            }

        suspend fun setLyricsProvider(provider: String) {
            withContext(Dispatchers.IO) {
                settingsDataStore.edit { settings ->
                    settings[LYRICS_PROVIDER] = provider
                }
            }
        }

        val musixmatchLoggedIn =
            settingsDataStore.data.map { preferences ->
                preferences[MUSIXMATCH_LOGGED_IN] ?: FALSE
            }

        suspend fun setMusixmatchLoggedIn(loggedIn: Boolean) {
            withContext(Dispatchers.IO) {
                if (loggedIn) {
                    settingsDataStore.edit { settings ->
                        settings[MUSIXMATCH_LOGGED_IN] = TRUE
                    }
                } else {
                    settingsDataStore.edit { settings ->
                        settings[MUSIXMATCH_LOGGED_IN] = FALSE
                    }
                }
            }
        }

        val translationLanguage =
            settingsDataStore.data.map { preferences ->
                preferences[TRANSLATION_LANGUAGE] ?: if (language.first().length >= 2) {
                    language.first()
                        .substring(0..1)
                } else {
                    "en"
                }
            }

        suspend fun setTranslationLanguage(language: String) {
            withContext(Dispatchers.IO) {
                settingsDataStore.edit { settings ->
                    settings[TRANSLATION_LANGUAGE] = language
                }
            }
        }

        val musixmatchCookie =
            settingsDataStore.data.map { preferences ->
                preferences[MUSIXMATCH_COOKIE] ?: "%7B%22tokens%22%3A%7B%22musixmatch-artists-v2.0%22%3A%22240321883340310880f1f2a8daed127bf3a1d0c930b19a296e4079%22%2C%22mxm-backoffice-v1.0%22%3A%222403212e55f8d7e9b8249f3d76f9232d8f378592afcdc8863619f2%22%2C%22musixmatch-podcasts-v2.0%22%3A%22240321aa11e3ae275f5956184bc7fc77dc70c0730682326f172b8d%22%2C%22musixmatch-podcasts-v2.0-pp%22%3A%222403215d762f73d14189b1e298c50d19d5ac378baa4222ecb74afe%22%2C%22mxm-pro-web-v1.0%22%3A%22240321b85b79c0905152a3742bb982f811e1ef958ae784ee2ae500%22%2C%22mxm-com-v1.0%22%3A%22240321d1fd3fe4c61b6edd6413d0689c5ea4f8137480cd9d52d037%22%2C%22musixmatch-publishers-v2.0%22%3A%22240321a27d3ce44f6014dafd53adee7fe2e56e3810adb097435046%22%2C%22web-desktop-app-v1.0%22%3A%2224032123b5248930a50920c42e5f7d05f8c42f7c7db137277d7418%22%2C%22community-app-v1.0%22%3A%22240321663d8469b0cbd5af41c83b9dead608bbe2b99db0142db21d%22%2C%22mxm-proxy-manager-v1.0%22%3A%222403217448e4d039e5cb32c75fa57a1354e03e1bb86417a31a0296%22%2C%22mxm-studio-v1.0%22%3A%22240321f5650edac76d2fff9e11cdc9d5ff941260b7a759ca5b95c1%22%2C%22mxm-account-v1.0%22%3A%2224032167b9d015fca84038a14f1cad38a29d55a4326a55f818ed47%22%7D%2C%22version%22%3A1%7D"
            }

        suspend fun setMusixmatchCookie(cookie: String) {
            withContext(Dispatchers.IO) {
                settingsDataStore.edit { settings ->
                    settings[MUSIXMATCH_COOKIE] = cookie
                }
            }
        }

        val maxSongCacheSize =
            settingsDataStore.data.map { preferences ->
                preferences[MAX_SONG_CACHE_SIZE] ?: -1
            }

        suspend fun setMaxSongCacheSize(size: Int) {
            withContext(Dispatchers.IO) {
                settingsDataStore.edit { settings ->
                    settings[MAX_SONG_CACHE_SIZE] = size
                }
            }
        }

        val watchVideoInsteadOfPlayingAudio =
            settingsDataStore.data.map { preferences ->
                preferences[WATCH_VIDEO_INSTEAD_OF_PLAYING_AUDIO] ?: FALSE
            }

        suspend fun setWatchVideoInsteadOfPlayingAudio(watch: Boolean) {
            withContext(Dispatchers.IO) {
                if (watch) {
                    settingsDataStore.edit { settings ->
                        settings[WATCH_VIDEO_INSTEAD_OF_PLAYING_AUDIO] = TRUE
                    }
                } else {
                    settingsDataStore.edit { settings ->
                        settings[WATCH_VIDEO_INSTEAD_OF_PLAYING_AUDIO] = FALSE
                    }
                }
            }
        }

        val videoQuality =
            settingsDataStore.data.map { preferences ->
                preferences[VIDEO_QUALITY] ?: "720p"
            }

        suspend fun setVideoQuality(quality: String) {
            withContext(Dispatchers.IO) {
                settingsDataStore.edit { settings ->
                    settings[VIDEO_QUALITY] = quality
                }
            }
        }

        val spdc =
            settingsDataStore.data.map { preferences ->
                preferences[SPDC] ?: ""
            }

        suspend fun setSpdc(spdc: String) {
            withContext(Dispatchers.IO) {
                settingsDataStore.edit { settings ->
                    settings[SPDC] = spdc
                }
            }
        }

        val spotifyLyrics: Flow<String> =
            settingsDataStore.data.map { preferences ->
                preferences[SPOTIFY_LYRICS] ?: TRUE
            }

        suspend fun setSpotifyLyrics(spotifyLyrics: Boolean) {
            withContext(Dispatchers.IO) {
                if (spotifyLyrics) {
                    settingsDataStore.edit { settings ->
                        settings[SPOTIFY_LYRICS] = TRUE
                    }
                } else {
                    settingsDataStore.edit { settings ->
                        settings[SPOTIFY_LYRICS] = FALSE
                    }
                }
            }
        }

        val spotifyCanvas: Flow<String> =
            settingsDataStore.data.map { preferences ->
                preferences[SPOTIFY_CANVAS] ?: TRUE
            }

        suspend fun setSpotifyCanvas(spotifyCanvas: Boolean) {
            withContext(Dispatchers.IO) {
                if (spotifyCanvas) {
                    settingsDataStore.edit { settings ->
                        settings[SPOTIFY_CANVAS] = TRUE
                    }
                } else {
                    settingsDataStore.edit { settings ->
                        settings[SPOTIFY_CANVAS] = FALSE
                    }
                }
            }
        }

        val spotifyPersonalToken: Flow<String> =
            settingsDataStore.data.map { preferences ->
                preferences[SPOTIFY_PERSONAL_TOKEN] ?: ""
            }

        suspend fun setSpotifyPersonalToken(token: String) {
            withContext(Dispatchers.IO) {
                settingsDataStore.edit { settings ->
                    settings[SPOTIFY_PERSONAL_TOKEN] = token
                }
            }
        }

        val spotifyPersonalTokenExpires: Flow<Long> =
            settingsDataStore.data.map { preferences ->
                preferences[SPOTIFY_PERSONAL_TOKEN_EXPIRES] ?: 0
            }

        suspend fun setSpotifyPersonalTokenExpires(expires: Long) {
            withContext(Dispatchers.IO) {
                settingsDataStore.edit { settings ->
                    settings[SPOTIFY_PERSONAL_TOKEN_EXPIRES] = expires
                }
            }
        }

        val spotifyClientToken: Flow<String> =
            settingsDataStore.data.map { preferences ->
                preferences[SPOTIFY_CLIENT_TOKEN] ?: ""
            }

        suspend fun setSpotifyClientToken(token: String) {
            withContext(Dispatchers.IO) {
                settingsDataStore.edit { settings ->
                    settings[SPOTIFY_CLIENT_TOKEN] = token
                }
            }
        }

        val homeLimit: Flow<Int> =
            settingsDataStore.data.map { preferences ->
                preferences[HOME_LIMIT] ?: 5
            }

        suspend fun setHomeLimit(limit: Int) {
            withContext(Dispatchers.IO) {
                settingsDataStore.edit { settings ->
                    settings[HOME_LIMIT] = limit
                }
            }
        }

        val chartKey =
            settingsDataStore.data.map { preferences ->
                preferences[CHART_KEY] ?: "IN"
            }

        suspend fun setChartKey(key: String) {
            withContext(Dispatchers.IO) {
                settingsDataStore.edit { settings ->
                    settings[CHART_KEY] = key
                }
            }
        }

        companion object Settings {
            val COOKIE = stringPreferencesKey("cookie")
            val LOGGED_IN = stringPreferencesKey("logged_in")
            val LOCATION = stringPreferencesKey("location")
            val QUALITY = stringPreferencesKey("quality")
            val NORMALIZE_VOLUME = stringPreferencesKey("normalize_volume")
            val SKIP_SILENT = stringPreferencesKey("skip_silent")
            val SAVE_STATE_OF_PLAYBACK = stringPreferencesKey("save_state_of_playback")
            val SAVE_RECENT_SONG = stringPreferencesKey("save_recent_song")
            val RECENT_SONG_MEDIA_ID_KEY = stringPreferencesKey("recent_song_media_id")
            val RECENT_SONG_POSITION_KEY = stringPreferencesKey("recent_song_position")
            val SHUFFLE_KEY = stringPreferencesKey("shuffle_key")
            val REPEAT_KEY = stringPreferencesKey("repeat_key")
            val SEND_BACK_TO_GOOGLE = stringPreferencesKey("send_back_to_google")
            val FROM_SAVED_PLAYLIST = stringPreferencesKey("from_saved_playlist")
            val MUSIXMATCH_LOGGED_IN = stringPreferencesKey("musixmatch_logged_in")
            val YOUTUBE = "youtube"
            val MUSIXMATCH = "musixmatch"
            val LYRICS_PROVIDER = stringPreferencesKey("lyrics_provider")
            val TRANSLATION_LANGUAGE = stringPreferencesKey("translation_language")
            val USE_TRANSLATION_LANGUAGE = stringPreferencesKey("use_translation_language")
            val MUSIXMATCH_COOKIE = stringPreferencesKey("musixmatch_cookie")
            const val RESTORE_LAST_PLAYED_TRACK_AND_QUEUE_DONE = "RestoreLastPlayedTrackAndQueueDone"
            val SPONSOR_BLOCK_ENABLED = stringPreferencesKey("sponsor_block_enabled")
            val MAX_SONG_CACHE_SIZE = intPreferencesKey("maxSongCacheSize")
            val WATCH_VIDEO_INSTEAD_OF_PLAYING_AUDIO =
                stringPreferencesKey("watch_video_instead_of_playing_audio")
            val VIDEO_QUALITY = stringPreferencesKey("video_quality")
            val SPDC = stringPreferencesKey("sp_dc")
            val SPOTIFY_LYRICS = stringPreferencesKey("spotify_lyrics")
            val SPOTIFY_CANVAS = stringPreferencesKey("spotify_canvas")
            val SPOTIFY_PERSONAL_TOKEN = stringPreferencesKey("spotify_personal_token")
            val SPOTIFY_PERSONAL_TOKEN_EXPIRES = longPreferencesKey("spotify_personal_token_expires")
            val SPOTIFY_CLIENT_TOKEN = stringPreferencesKey("spotify_client_token")
            val HOME_LIMIT = intPreferencesKey("home_limit")
            val CHART_KEY = stringPreferencesKey("chart_key")
            const val REPEAT_MODE_OFF = "REPEAT_MODE_OFF"
            const val REPEAT_ONE = "REPEAT_ONE"
            const val REPEAT_ALL = "REPEAT_ALL"
            const val TRUE = "TRUE"
            const val FALSE = "FALSE"
        }
    }
