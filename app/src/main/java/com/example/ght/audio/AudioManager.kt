package com.example.ght.audio

import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.RawRes
import com.example.ght.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AudioManager(context: Context) {

    private val appContext = context.applicationContext
    private var mediaPlayer: MediaPlayer? = null

    @RawRes
    private var musicaAtualResId: Int? = null

    private val _musicaMutada = MutableStateFlow(false)
    val musicaMutada: StateFlow<Boolean> = _musicaMutada.asStateFlow()

    fun iniciarMusica(@RawRes resId: Int? = null) {
        val idParaTocar = resId ?: musicaAtualResId ?: R.raw.musica_fundo // confira o nome em res/raw
        val trocouDeMusica = musicaAtualResId != null && musicaAtualResId != idParaTocar
        musicaAtualResId = idParaTocar

        try {
            if (mediaPlayer == null || trocouDeMusica) {
                mediaPlayer?.release()
                mediaPlayer = MediaPlayer.create(appContext, idParaTocar)?.apply {
                    isLooping = true
                }
            }

            val volume = if (_musicaMutada.value) 0f else 1f
            mediaPlayer?.setVolume(volume, volume)

            if (!_musicaMutada.value && mediaPlayer?.isPlaying == false) {
                mediaPlayer?.start()
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    fun pausarMusica() {
        try {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    fun alternarMuteMusica() {
        val vaiMutar = !_musicaMutada.value
        _musicaMutada.value = vaiMutar

        try {
            if (vaiMutar) {
                mediaPlayer?.setVolume(0f, 0f)
                if (mediaPlayer?.isPlaying == true) {
                    mediaPlayer?.pause()
                }
            } else {
                if (mediaPlayer == null) {
                    iniciarMusica(musicaAtualResId)
                } else {
                    mediaPlayer?.setVolume(1f, 1f)
                    if (mediaPlayer?.isPlaying == false) {
                        mediaPlayer?.start()
                    }
                }
            }
        } catch (e: IllegalStateException) {
            mediaPlayer?.release()
            mediaPlayer = null
            iniciarMusica(musicaAtualResId)
        }
    }

    fun liberar() {
        try {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.stop()
            }
            mediaPlayer?.release()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } finally {
            mediaPlayer = null
        }
    }
}