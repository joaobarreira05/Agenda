package com.agenda.app.presentation.record

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.agenda.app.databinding.FragmentRecordBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecordFragment : Fragment() {

    private var _binding: FragmentRecordBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecordViewModel by viewModels()
    private var speechRecognizer: SpeechRecognizer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        
        setupSpeechRecognizer()

        binding.fabRecordToggle.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                if (viewModel.isRecording.value) {
                    stopListening()
                } else {
                    startListening()
                }
            } else {
                requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 100)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.isRecording.collect { isRecording ->
                        if (isRecording) {
                            binding.voiceWaveView.startAnimation()
                            binding.fabRecordToggle.setImageResource(android.R.drawable.ic_media_pause)
                            binding.tvInstruction.text = "A ouvir..."
                        } else {
                            binding.voiceWaveView.stopAnimation()
                            binding.fabRecordToggle.setImageResource(android.R.drawable.ic_btn_speak_now)
                        }
                    }
                }
                
                launch {
                    viewModel.transcriptionResult.collect { text ->
                        if (!text.isNullOrBlank()) {
                            viewModel.clearTranscriptionResult()
                            val bundle = Bundle().apply {
                                putString("transcribedText", text)
                            }
                            findNavController().navigate(com.agenda.app.R.id.action_recordFragment_to_confirmationFragment, bundle)
                        }
                    }
                }
            }
        }
    }
    
    private fun setupSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(requireContext())) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext())
            speechRecognizer?.setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {}
                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {
                    binding.voiceWaveView.updateAmplitude(rmsdB * 1000)
                }
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() {
                    viewModel.setRecordingState(false)
                    binding.tvInstruction.text = "A processar..."
                }
                override fun onError(error: Int) {
                    viewModel.setRecordingState(false)
                    binding.tvInstruction.text = "Erro ao ouvir. Tenta novamente."
                }
                override fun onResults(results: Bundle?) {
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    if (!matches.isNullOrEmpty()) {
                        val text = matches[0]
                        binding.tvInstruction.text = text
                        viewModel.processRecordingAndSave(text)
                    }
                }
                override fun onPartialResults(partialResults: Bundle?) {
                    val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    if (!matches.isNullOrEmpty()) {
                        binding.tvInstruction.text = matches[0]
                    }
                }
                override fun onEvent(eventType: Int, params: Bundle?) {}
            })
        } else {
            binding.tvInstruction.text = "Reconhecimento de voz não disponível neste dispositivo."
            binding.fabRecordToggle.isEnabled = false
        }
    }

    private fun startListening() {
        viewModel.setRecordingState(true)
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pt-PT")
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        }
        speechRecognizer?.startListening(intent)
    }
    
    private fun stopListening() {
        viewModel.setRecordingState(false)
        speechRecognizer?.stopListening()
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListening()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        speechRecognizer?.destroy()
        _binding = null
    }
}
