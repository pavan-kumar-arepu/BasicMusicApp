package com.ppam.basicmusicapp

import androidx.annotation.DrawableRes

data class Lib(@DrawableRes val icon: Int, val name: String)

val libraries = listOf<Lib>(
    Lib(R.drawable.baseline_playlist_add_24, "Playlist"),
    Lib(R.drawable.baseline_article_24, "Artist"),
    Lib(R.drawable.baseline_album_24, "Album"),
    Lib(R.drawable.baseline_music_note_24, "Songs"),
    Lib(R.drawable.baseline_category_24, "Genre"),
)