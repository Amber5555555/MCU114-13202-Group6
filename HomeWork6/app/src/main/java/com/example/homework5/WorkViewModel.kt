package com.example.homework5

import android.app.Application
import android.media.MediaPlayer // 匯入 MediaPlayer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*

class WorkViewModel(application: Application) : AndroidViewModel(application) {
    private var job: Job? = null
    private val _progress = MutableLiveData(0)
    val progress: LiveData<Int> = _progress
    private val _status = MutableLiveData("")
    val status: LiveData<String> = _status

    // 建立一個可為空的 MediaPlayer 變數
    private var mediaPlayer: MediaPlayer? = null

    // 在 ViewModel 被清除時，釋放 MediaPlayer 資源
    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun start() {
        if (job?.isActive == true) return // 如果工作正在進行，則不執行任何操作
        _status.value = "背景工作執行中..."
        // 初始化並播放音樂
        playMusic()
        job = CoroutineScope(Dispatchers.Default).launch {
            for (i in 1..100) {
                if (!isActive) {
                    _status.postValue("背景工作已中斷")
                    // 停止音樂
                    stopMusic()
                    return@launch
                }
                delay(100)
                _progress.postValue(i)
            }
            _status.postValue("背景工作已完成")
            // 停止音樂
            stopMusic()
        }
    }

    fun cancel() {
        job?.cancel()
    }

    // 播放音樂的函式
    private fun playMusic() {
        // 如果正在播放，先停止並釋放舊的資源
        mediaPlayer?.release()
        // 從 res/raw 資料夾建立新的 MediaPlayer 實例
        mediaPlayer = MediaPlayer.create(getApplication(), R.raw.background_music).apply {
            isLooping = true // 設定為循環播放
            start() // 開始播放
        }
    }

    // 停止音樂的函式
    private fun stopMusic() {
        // 在主執行緒上安全地停止和釋放 MediaPlayer
        CoroutineScope(Dispatchers.Main).launch {
            mediaPlayer?.apply {
                if (isPlaying) {
                    stop()
                }
                release() // 釋放資源
            }
            mediaPlayer = null
        }
    }
}
