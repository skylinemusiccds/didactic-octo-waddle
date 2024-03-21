package com.universe.audioflare.viewModel

import android.app.Application
import android.graphics.drawable.GradientDrawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.offline.Download
import com.universe.audioflare.common.DownloadState
import com.universe.audioflare.common.SELECTED_LANGUAGE
import com.universe.audioflare.data.dataStore.DataStoreManager
import com.universe.audioflare.data.db.entities.AlbumEntity
import com.universe.audioflare.data.db.entities.SongEntity
import com.universe.audioflare.data.model.browse.album.AlbumBrowse
import com.universe.audioflare.data.model.browse.album.Track
import com.universe.audioflare.data.repository.MainRepository
import com.universe.audioflare.service.test.download.DownloadUtils
import com.universe.audioflare.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private var dataStoreManager: DataStoreManager,
    private val mainRepository: MainRepository,
    application: Application
): AndroidViewModel(application) {
    @Inject
    lateinit var downloadUtils: DownloadUtils

    var gradientDrawable: MutableLiveData<GradientDrawable> = MutableLiveData()
    private var _loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var loading: MutableStateFlow<Boolean> = _loading

    private var _albumBrowse: MutableStateFlow<Resource<AlbumBrowse>?> = MutableStateFlow(null)
    val albumBrowse: StateFlow<Resource<AlbumBrowse>?> = _albumBrowse

    private var _browseId: MutableStateFlow<String?> = MutableStateFlow(null)
    val browseId: StateFlow<String?> = _browseId

    private var _albumEntity: MutableStateFlow<AlbumEntity?> = MutableStateFlow(null)
    val albumEntity: StateFlow<AlbumEntity?> = _albumEntity

    private var _liked: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var liked: MutableStateFlow<Boolean> = _liked
    private var regionCode: String? = null
    private var language: String? = null

    init {
        regionCode = runBlocking { dataStoreManager.location.first() }
        language = runBlocking { dataStoreManager.getString(SELECTED_LANGUAGE).first() }
    }

    fun updateBrowseId(browseId: String){
        _browseId.value = browseId
    }
    fun browseAlbum(browseId: String){
        loading.value = true
        viewModelScope.launch {
            mainRepository.getAlbumData(browseId).collect { values ->
                _albumBrowse.value = values
            }
            withContext(Dispatchers.Main){
                loading.value = false
            }
        }
    }

    fun insertAlbum(albumEntity: AlbumEntity){
        viewModelScope.launch {
            mainRepository.insertAlbum(albumEntity)
            mainRepository.getAlbum(albumEntity.browseId).collect{ values ->
                _liked.value = values.liked
                val list = values.tracks
                var count = 0
                list?.forEach { track ->
                    mainRepository.getSongById(track).collect { song ->
                        if (song != null) {
                            if (song.downloadState == DownloadState.STATE_DOWNLOADED) {
                                count++
                            }
                        }
                    }
                }
                if (count == list?.size) {
                    updateAlbumDownloadState(albumEntity.browseId, DownloadState.STATE_DOWNLOADED)
                }
                else {
                    updateAlbumDownloadState(albumEntity.browseId, DownloadState.STATE_NOT_DOWNLOADED)
                }
                mainRepository.getAlbum(albumEntity.browseId).collect { album ->
                    _albumEntity.value = values
                }
            }
        }
    }

    fun getAlbum(browseId: String){
        viewModelScope.launch {
            mainRepository.getAlbum(browseId).collect{ values ->
                _liked.value = values.liked
                val list = values.tracks
                var count = 0
                list?.forEach { track ->
                    mainRepository.getSongById(track).collect { song ->
                        if (song != null) {
                            if (song.downloadState == DownloadState.STATE_DOWNLOADED) {
                                count++
                            }
                        }
                    }
                }
                if (count == list?.size) {
                    updateAlbumDownloadState(browseId, DownloadState.STATE_DOWNLOADED)
                }
                else {
                    updateAlbumDownloadState(browseId, DownloadState.STATE_NOT_DOWNLOADED)
                }
                mainRepository.getAlbum(browseId).collect { album ->
                    _albumEntity.value = album
                }
            }
        }
    }

    fun updateAlbumLiked(liked: Boolean, browseId: String){
        viewModelScope.launch {
            val tempLiked = if(liked) 1 else 0
            mainRepository.updateAlbumLiked(browseId, tempLiked)
            mainRepository.getAlbum(browseId).collect{ values ->
                _albumEntity.value = values
                _liked.value = values.liked
            }
        }
    }
    val albumDownloadState: MutableStateFlow<Int> = MutableStateFlow(DownloadState.STATE_NOT_DOWNLOADED)


    private fun updateAlbumDownloadState(browseId: String, state: Int) {
        viewModelScope.launch {
            mainRepository.getAlbum(browseId).collect { album ->
                _albumEntity.value = album
                mainRepository.updateAlbumDownloadState(browseId, state)
                albumDownloadState.value = state
            }
        }
    }

    fun checkAllSongDownloaded(list: ArrayList<Track>) {
        viewModelScope.launch {
            var count = 0
            list.forEach { track ->
                mainRepository.getSongById(track.videoId).collect { song ->
                    if (song != null) {
                        if (song.downloadState == DownloadState.STATE_DOWNLOADED) {
                            count++
                        }
                    }
                }
            }
            if (count == list.size) {
                updateAlbumDownloadState(browseId.value!!, DownloadState.STATE_DOWNLOADED)
            }
            mainRepository.getAlbum(browseId.value!!).collect { album ->
                if (albumEntity.value?.downloadState != album.downloadState) {
                    _albumEntity.value = album
                }
            }
        }
    }
    val listJob: MutableStateFlow<ArrayList<SongEntity>> = MutableStateFlow(arrayListOf())

    fun updatePlaylistDownloadState(id: String, state: Int) {
        viewModelScope.launch {
            mainRepository.getAlbum(id).collect { playlist ->
                _albumEntity.value = playlist
                mainRepository.updateAlbumDownloadState(id, state)
                albumDownloadState.value = state
            }
        }
    }
    fun updateDownloadState(videoId: String, state: Int) {
        viewModelScope.launch {
            mainRepository.updateDownloadState(videoId, state)
        }
    }

    @UnstableApi
    fun getDownloadStateFromService(videoId: String) {
        viewModelScope.launch {
            val downloadState = downloadUtils.getDownload(videoId).stateIn(viewModelScope)
            downloadState.collect { down ->
                if (down != null) {
                    when (down.state) {
                        Download.STATE_COMPLETED -> {
                            mainRepository.getSongById(videoId).collect{ song ->
                                if (song?.downloadState != DownloadState.STATE_DOWNLOADED) {
                                    mainRepository.updateDownloadState(videoId, DownloadState.STATE_DOWNLOADED)
                                }
                            }
                        }
                        Download.STATE_FAILED -> {
                            mainRepository.getSongById(videoId).collect {song ->
                                if (song?.downloadState != DownloadState.STATE_NOT_DOWNLOADED) {
                                    mainRepository.updateDownloadState(videoId, DownloadState.STATE_NOT_DOWNLOADED)
                                }
                            }
                        }
                        Download.STATE_DOWNLOADING -> {
                            mainRepository.getSongById(videoId).collect{ song ->
                                if (song?.downloadState != DownloadState.STATE_DOWNLOADING) {
                                    mainRepository.updateDownloadState(videoId, DownloadState.STATE_DOWNLOADING)
                                }
                            }
                        }
                        Download.STATE_QUEUED -> {
                            mainRepository.getSongById(videoId).collect{ song ->
                                if (song?.downloadState != DownloadState.STATE_NOT_DOWNLOADED) {
                                    mainRepository.updateDownloadState(videoId, DownloadState.STATE_NOT_DOWNLOADED)
                                }
                            }
                        }

                        else -> {
                            Log.d("Check Downloaded", "Not Downloaded")
                        }
                    }
                }
            }
        }
    }

    private var _listTrack: MutableStateFlow<List<SongEntity>?> = MutableStateFlow(null)
    var listTrack: StateFlow<List<SongEntity>?> = _listTrack

    fun getListTrack(tracks: List<String>?) {
        viewModelScope.launch {
            mainRepository.getSongsByListVideoId(tracks!!).collect { values ->
                _listTrack.value = values
            }
        }
    }

    private var _listTrackForDownload: MutableStateFlow<List<SongEntity>?> = MutableStateFlow(null)
    var listTrackForDownload: StateFlow<List<SongEntity>?> = _listTrackForDownload

    fun getListTrackForDownload(tracks: List<String>?) {
        viewModelScope.launch {
            mainRepository.getSongsByListVideoId(tracks!!).collect { values ->
                _listTrackForDownload.value = values
            }
        }
    }

    fun clearAlbumBrowse() {
        _albumBrowse.value = null
        _albumEntity.value = null
    }

    fun getLocation() {
        regionCode = runBlocking { dataStoreManager.location.first() }
        language = runBlocking { dataStoreManager.getString(SELECTED_LANGUAGE).first() }
    }

    fun insertSong(songEntity: SongEntity) {
        viewModelScope.launch {
            mainRepository.insertSong(songEntity).collect {
                println("Insert Song $it")
            }
        }
    }

    @UnstableApi
    fun downloadFullAlbumState(browseId: String) {
        viewModelScope.launch {
            downloadUtils.downloads.collect { download ->
                albumDownloadState.value =
                    if (listJob.value.all { download[it.videoId]?.state == Download.STATE_COMPLETED }) {
                        mainRepository.updateAlbumDownloadState(
                            browseId,
                            DownloadState.STATE_DOWNLOADED
                        )
                        DownloadState.STATE_DOWNLOADED
                    } else if (listJob.value.all {
                            download[it.videoId]?.state == Download.STATE_QUEUED
                                    || download[it.videoId]?.state == Download.STATE_DOWNLOADING
                                    || download[it.videoId]?.state == Download.STATE_COMPLETED
                        }) {
                        mainRepository.updateAlbumDownloadState(
                            browseId,
                            DownloadState.STATE_DOWNLOADING
                        )
                        DownloadState.STATE_DOWNLOADING
                    } else {
                        mainRepository.updateAlbumDownloadState(browseId, DownloadState.STATE_NOT_DOWNLOADED)
                        DownloadState.STATE_NOT_DOWNLOADED
                    }
            }
        }
    }
}