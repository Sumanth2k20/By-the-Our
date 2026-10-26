package com.bytheour.utils


import androidx.compose.runtime.Composable
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFAudio.AVAudioEngine
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryRecord
import platform.AVFAudio.AVAudioSessionModeMeasurement
import platform.AVFAudio.setActive
import platform.Speech.SFSpeechAudioBufferRecognitionRequest
import platform.Speech.SFSpeechRecognitionTask
import platform.Speech.SFSpeechRecognizer
import platform.Speech.SFSpeechRecognizerAuthorizationStatus


private class IOSVoiceSearch(
    private val onResult: (String?) -> Unit,
    private val onComplete: () -> Unit,
    private val isListening: (Boolean) -> Unit
) {
    private var speechRecognizer: SFSpeechRecognizer? = null
    private var recognitionRequest: SFSpeechAudioBufferRecognitionRequest? = null
    private var recognitionTask: SFSpeechRecognitionTask? = null
    private val audioEngine = AVAudioEngine()

//    private var lastResultTime = Clock.System.now()
//    private var silenceTimer: NSTimer? = null
//    private fun resetSilenceTimer(timeout: Duration = 3.seconds) {// 3 seconds gap after result it will stop recognizing text
//        silenceTimer?.invalidate()
//        silenceTimer = NSTimer.scheduledTimerWithTimeInterval(timeout.inWholeSeconds.toDouble(), repeats = false) { _ ->
//            val now = Clock.System.now()
//            if (now - lastResultTime > timeout) {
//                stop()
//                onComplete()
//            }
//        }
//    }
    @OptIn(ExperimentalForeignApi::class)
    fun start() {
        SFSpeechRecognizer.requestAuthorization { status ->

            if (status == SFSpeechRecognizerAuthorizationStatus.SFSpeechRecognizerAuthorizationStatusAuthorized) {
                speechRecognizer = SFSpeechRecognizer()
                recognitionRequest = SFSpeechAudioBufferRecognitionRequest().apply {
                    shouldReportPartialResults = true
                }

                recognitionTask = speechRecognizer?.recognitionTaskWithRequest(recognitionRequest!!) { result, error ->
                    println("result of voice $result")
                    when {
                        result != null -> {
//                            lastResultTime = Clock.System.now()  // update on each result
//                            resetSilenceTimer(3.seconds)

                            val text = result.bestTranscription.formattedString
                            println("Recognized text: $text")   //  This shows only the spoken words
                            onResult(text)
                            if (result.isFinal()) {
                                stop()
                                onComplete()
                            }
                        }
                        error != null -> {
                            stop()
                            onResult(null)
                            onComplete()
                        }
                    }
                }

                // 1. Configure audio session first
                val session = AVAudioSession.sharedInstance()
                session.setCategory(AVAudioSessionCategoryRecord, error = null)//tells iOS this session will record from the mic.
                session.setMode(AVAudioSessionModeMeasurement, error = null)
                session.setActive(true, error = null)//actually activates the session (turns on the mic).

                // 2. Then configure input & tap
                val inputNode = audioEngine.inputNode
                val recordingFormat = inputNode.inputFormatForBus(0u)

                inputNode.installTapOnBus(
                    bus = 0u,//asks the system what format the mic audio comes in
                    bufferSize = 1024u,
                    format = recordingFormat
                ) { buffer, _ ->
                    buffer?.let {
                        recognitionRequest?.appendAudioPCMBuffer(it)//every captured buffer gets appended into the speech recognition request, so the recognizer can process it in real time.
                    }
                }

                // 3. Now prepare and start engine
                audioEngine.prepare()
                audioEngine.startAndReturnError(null)//starts recording from the mic and begins delivering audio buffers through the tap.

                isListening(true)//tells your UI (via the callback) that the engine is now actively listening.
            } else {
                onResult(null)
                onComplete()
            }
        }
    }


    fun stop() {
        if (audioEngine.isRunning()) {
            audioEngine.stop()
            recognitionRequest?.endAudio()
            audioEngine.inputNode.removeTapOnBus(0u)
        }
        recognitionTask?.cancel()
        recognitionTask = null
        recognitionRequest = null
        speechRecognizer = null
        isListening(false)
    }
}

@Composable
actual fun VoiceSearch(
    onResult: (String?) -> Unit,
    onComplete: () -> Unit,
    isListening: (Boolean) -> Unit
) {
    val search = IOSVoiceSearch(onResult, onComplete, isListening)
    search.start()
}

