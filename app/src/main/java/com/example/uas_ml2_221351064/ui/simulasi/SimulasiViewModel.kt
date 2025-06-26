package com.example.uas_ml2_221351064.ui.simulasi

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.text.SimpleDateFormat
import java.util.*

class SimulasiViewModel(application: Application) : AndroidViewModel(application) {

    private val interpreter: Interpreter
    private val _hasilPrediksi = MutableLiveData<String>()
    val hasilPrediksi: LiveData<String> = _hasilPrediksi

    private val _riwayatPrediksi = MutableLiveData<List<String>>(emptyList())
    //val riwayatPrediksi: LiveData<List<String>> = _riwayatPrediksi

    init {
        interpreter = Interpreter(loadModelFile("stroke_model.tflite"))
    }

    fun predict(input: FloatArray): Int {
        val inputBuffer = ByteBuffer.allocateDirect(4 * input.size)
        inputBuffer.order(ByteOrder.nativeOrder())
        input.forEach { inputBuffer.putFloat(it) }
        inputBuffer.rewind()

        val output = Array(1) { FloatArray(1) }
        interpreter.run(inputBuffer, output)

        val hasil = if (output[0][0] > 0.5f) 1 else 0
        val hasilString = if (hasil == 1) "Pasien Diprediksi TERKENA Stroke" else "Pasien Diprediksi TIDAK terkena Stroke"

        _hasilPrediksi.postValue(hasilString)

        val ringkasan = "[${getTimestamp()}] - $hasilString\nInput: ${input.joinToString(", ")}"
        val newHistory = listOf(ringkasan) + (_riwayatPrediksi.value ?: emptyList())
        _riwayatPrediksi.postValue(newHistory)

        return hasil
    }

    private fun loadModelFile(filename: String): MappedByteBuffer {
        val fileDescriptor = getApplication<Application>().assets.openFd(filename)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, fileDescriptor.startOffset, fileDescriptor.declaredLength)
    }

    private fun getTimestamp(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date())
    }
}
