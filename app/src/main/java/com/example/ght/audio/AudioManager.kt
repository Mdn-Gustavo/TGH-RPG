package com.example.ght.audio

import android.content.Context
import android.media.MediaPlayer
import com.example.ght.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AudioManager(context: Context) {
    private val appContext = context.applicationContext

    private val _musicaMutada = MutableStateFlow(false)
    val musicaMutada = _musicaMutada.asStateFlow()

    private var mediaPlayer: MediaPlayer? = null

    fun iniciarMusica() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(appContext, R.raw.musica_fundo)?.apply {
                isLooping = true
                setVolume(0.6f, 0.6f)
            }
        }
        if (!_musicaMutada.value && mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
        }
    }

    fun pausarMusica() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
        }
    }

    fun alternarMuteMusica() {
        val novoEstado = !_musicaMutada.value
        _musicaMutada.value = novoEstado
        if (novoEstado) {
            pausarMusica()
        } else {
            iniciarMusica()
        }
    }

    fun liberar() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
