package com.universe.kotlinytmusicscraper.pages

import com.universe.kotlinytmusicscraper.models.Album
import com.universe.kotlinytmusicscraper.models.AlbumItem
import com.universe.kotlinytmusicscraper.models.Artist
import com.universe.kotlinytmusicscraper.models.ArtistItem
import com.universe.kotlinytmusicscraper.models.MusicResponsiveListItemRenderer
import com.universe.kotlinytmusicscraper.models.MusicTwoRowItemRenderer
import com.universe.kotlinytmusicscraper.models.PlaylistItem
import com.universe.kotlinytmusicscraper.models.SongItem
import com.universe.kotlinytmusicscraper.models.YTItem
import com.universe.kotlinytmusicscraper.models.oddElements

data class RelatedPage(
    val songs: List<SongItem>,
    val albums: List<AlbumItem>,
    val artists: List<ArtistItem>,
    val playlists: List<PlaylistItem>,
) {
    companion object {
        fun fromMusicResponsiveListItemRenderer(renderer: MusicResponsiveListItemRenderer): SongItem? {
            return SongItem(
                id = renderer.playlistItemData?.videoId ?: return null,
                title =
                    renderer.flexColumns.firstOrNull()
                        ?.musicResponsiveListItemFlexColumnRenderer?.text?.runs?.firstOrNull()
                        ?.text ?: return null,
                artists =
                    renderer.flexColumns.getOrNull(1)?.musicResponsiveListItemFlexColumnRenderer?.text?.runs?.oddElements()
                        ?.map {
                            Artist(
                                name = it.text,
                                id = it.navigationEndpoint?.browseEndpoint?.browseId,
                            )
                        } ?: return null,
                album =
                    renderer.flexColumns.getOrNull(2)?.musicResponsiveListItemFlexColumnRenderer?.text?.runs?.firstOrNull()
                        ?.let {
                            Album(
                                name = it.text,
                                id = it.navigationEndpoint?.browseEndpoint?.browseId ?: return null,
                            )
                        },
                duration = null,
                thumbnail =
                    renderer.thumbnail?.musicThumbnailRenderer?.getThumbnailUrl()
                        ?: return null,
                explicit =
                    renderer.badges?.find {
                        it.musicInlineBadgeRenderer.icon.iconType == "MUSIC_EXPLICIT_BADGE"
                    } != null,
            )
        }

        fun fromMusicTwoRowItemRenderer(renderer: MusicTwoRowItemRenderer): YTItem? {
            return when {
                renderer.isAlbum ->
                    AlbumItem(
                        browseId =
                            renderer.navigationEndpoint.browseEndpoint?.browseId
                                ?: return null,
                        playlistId =
                            renderer.thumbnailOverlay?.musicItemThumbnailOverlayRenderer
                                ?.content?.musicPlayButtonRenderer?.playNavigationEndpoint
                                ?.watchPlaylistEndpoint?.playlistId
                                ?: renderer.thumbnailOverlay?.musicItemThumbnailOverlayRenderer
                                    ?.content?.musicPlayButtonRenderer?.playNavigationEndpoint
                                    ?.watchEndpoint?.playlistId
                                ?: renderer.navigationEndpoint.browseEndpoint.browseId.removePrefix("VL")
                                ?: return null,
                        title = renderer.title.runs?.firstOrNull()?.text ?: return null,
                        artists = null,
                        year = renderer.subtitle?.runs?.lastOrNull()?.text?.toIntOrNull(),
                        thumbnail =
                            renderer.thumbnailRenderer.musicThumbnailRenderer?.getThumbnailUrl()
                                ?: return null,
                        explicit =
                            renderer.subtitleBadges?.find {
                                it.musicInlineBadgeRenderer.icon.iconType == "MUSIC_EXPLICIT_BADGE"
                            } != null,
                    )

                renderer.isPlaylist ->
                    PlaylistItem(
                        id =
                            renderer.navigationEndpoint.browseEndpoint?.browseId?.removePrefix("VL")
                                ?: return null,
                        title = renderer.title.runs?.firstOrNull()?.text ?: return null,
                        author =
                            renderer.subtitle?.runs?.getOrNull(2)?.let {
                                Artist(
                                    name = it.text,
                                    id = it.navigationEndpoint?.browseEndpoint?.browseId,
                                )
                            },
                        songCountText = renderer.subtitle?.runs?.getOrNull(4)?.text,
                        thumbnail =
                            renderer.thumbnailRenderer.musicThumbnailRenderer?.getThumbnailUrl()
                                ?: return null,
                        playEndpoint =
                            renderer.thumbnailOverlay
                                ?.musicItemThumbnailOverlayRenderer?.content
                                ?.musicPlayButtonRenderer?.playNavigationEndpoint
                                ?.watchPlaylistEndpoint ?: return null,
                        shuffleEndpoint =
                            renderer.menu?.menuRenderer?.items?.find {
                                it.menuNavigationItemRenderer?.icon?.iconType == "MUSIC_SHUFFLE"
                            }?.menuNavigationItemRenderer?.navigationEndpoint?.watchPlaylistEndpoint
                                ?: return null,
                        radioEndpoint =
                            renderer.menu.menuRenderer.items.find {
                                it.menuNavigationItemRenderer?.icon?.iconType == "MIX"
                            }?.menuNavigationItemRenderer?.navigationEndpoint?.watchPlaylistEndpoint,
                    )

                renderer.isArtist -> {
                    ArtistItem(
                        id = renderer.navigationEndpoint.browseEndpoint?.browseId ?: return null,
                        title = renderer.title.runs?.firstOrNull()?.text ?: return null,
                        thumbnail =
                            renderer.thumbnailRenderer.musicThumbnailRenderer?.getThumbnailUrl()
                                ?: return null,
                        shuffleEndpoint =
                            renderer.menu?.menuRenderer?.items?.find {
                                it.menuNavigationItemRenderer?.icon?.iconType == "MUSIC_SHUFFLE"
                            }?.menuNavigationItemRenderer?.navigationEndpoint?.watchPlaylistEndpoint
                                ?: return null,
                        radioEndpoint =
                            renderer.menu.menuRenderer.items.find {
                                it.menuNavigationItemRenderer?.icon?.iconType == "MIX"
                            }?.menuNavigationItemRenderer?.navigationEndpoint?.watchPlaylistEndpoint
                                ?: return null,
                    )
                }

                else -> null
            }
        }
    }
}