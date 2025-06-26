package com.example.uas_ml2_221351064.ui.simulasi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.uas_ml2_221351064.R
import com.google.android.material.button.MaterialButton

class SimulasiFragment : Fragment() {

    private val viewModel: SimulasiViewModel by viewModels()

    private lateinit var genderButton: MaterialButton
    private lateinit var hypertensionButton: MaterialButton
    private lateinit var heartButton: MaterialButton
    private lateinit var marriedButton: MaterialButton
    private lateinit var workButton: MaterialButton
    private lateinit var residenceButton: MaterialButton
    private lateinit var smokingButton: MaterialButton

    private lateinit var ageInput: EditText
    private lateinit var glucoseInput: EditText
    private lateinit var bmiInput: EditText

    private lateinit var predictButton: Button
    private lateinit var resultText: TextView
    private lateinit var historyText: TextView

    private var gender = 0f
    private var hypertension = 0f
    private var heartDisease = 0f
    private var everMarried = 0f
    private var workType = 0f
    private var residenceType = 0f
    private var smokingStatus = 0f

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_simulasi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Bind views
        genderButton      = view.findViewById(R.id.button_gender)
        hypertensionButton= view.findViewById(R.id.button_hypertension)
        heartButton       = view.findViewById(R.id.button_heart)
        marriedButton     = view.findViewById(R.id.button_married)
        workButton        = view.findViewById(R.id.button_work)
        residenceButton   = view.findViewById(R.id.button_residence)
        smokingButton     = view.findViewById(R.id.button_smoking)

        ageInput    = view.findViewById(R.id.input_age)
        glucoseInput= view.findViewById(R.id.input_glucose)
        bmiInput    = view.findViewById(R.id.input_bmi)

        predictButton = view.findViewById(R.id.button_predict)
        resultText    = view.findViewById(R.id.text_result)

        // Observers
        viewModel.hasilPrediksi.observe(viewLifecycleOwner) { hasil ->
            resultText.text = "Hasil: $hasil"

            if (hasil.contains("TIDAK", ignoreCase = true)) {
                resultText.setBackgroundColor(resources.getColor(android.R.color.holo_green_dark, null))
                resultText.setTextColor(resources.getColor(android.R.color.white, null))
            } else if (hasil.contains("TERKENA", ignoreCase = true)) {
                resultText.setBackgroundColor(resources.getColor(android.R.color.holo_red_dark, null))
                resultText.setTextColor(resources.getColor(android.R.color.white, null))
            } else {
                resultText.setBackgroundColor(resources.getColor(android.R.color.transparent, null))
                resultText.setTextColor(resources.getColor(android.R.color.black, null))
            }
        }



        genderButton.setOnClickListener {
            showOptionDialog("Pilih Jenis Kelamin", listOf("Laki-laki", "Perempuan")) { idx ->
                gender = idx.toFloat()
                genderButton.text = "Jenis Kelamin: ${if (idx == 0) "Laki-laki" else "Perempuan"}"
            }
        }

        hypertensionButton.setOnClickListener {
            showOptionDialog("Hipertensi", listOf("Tidak", "Ya")) { idx ->
                hypertension = idx.toFloat()
                hypertensionButton.text = "Hipertensi: ${if (idx == 0) "Tidak" else "Ya"}"
            }
        }

        heartButton.setOnClickListener {
            showOptionDialog("Penyakit Jantung", listOf("Tidak", "Ya")) { idx ->
                heartDisease = idx.toFloat()
                heartButton.text = "Penyakit Jantung: ${if (idx == 0) "Tidak" else "Ya"}"
            }
        }

        marriedButton.setOnClickListener {
            showOptionDialog("Status Pernikahan", listOf("Belum Menikah", "Sudah Menikah")) { idx ->
                everMarried = idx.toFloat()
                marriedButton.text = "Pernikahan: ${if (idx == 0) "Belum Menikah" else "Sudah Menikah"}"
            }
        }

        workButton.setOnClickListener {
            val options = listOf("Pegawai Negeri", "Pegawai Swasta", "Wiraswasta", "Anak-anak", "Belum Pernah Bekerja")
            showOptionDialog("Jenis Pekerjaan", options) { idx ->
                workType = idx.toFloat()
                workButton.text = "Pekerjaan: ${options[idx]}"
            }
        }

        residenceButton.setOnClickListener {
            showOptionDialog("Tipe Tempat Tinggal", listOf("Perkotaan", "Pedesaan")) { idx ->
                residenceType = idx.toFloat()
                residenceButton.text = "Tempat Tinggal: ${if (idx == 0) "Perkotaan" else "Pedesaan"}"
            }
        }

        smokingButton.setOnClickListener {
            val options = listOf("Tidak Pernah Merokok", "Pernah Merokok", "Masih Merokok", "Tidak Diketahui")
            showOptionDialog("Status Merokok", options) { idx ->
                smokingStatus = idx.toFloat()
                smokingButton.text = "Merokok: ${options[idx]}"
            }
        }

        predictButton.setOnClickListener {
            val age     = ageInput.text.toString().toFloatOrNull()
            val glucose = glucoseInput.text.toString().toFloatOrNull()
            val bmi     = bmiInput.text.toString().toFloatOrNull()

            if (age==null || glucose==null || bmi==null) {
                Toast.makeText(requireContext(), "Isi semua input numerik!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val input = floatArrayOf(
                gender, age, hypertension, heartDisease, everMarried,
                workType, residenceType, glucose, bmi, smokingStatus
            )
            viewModel.predict(input)
        }
    }

    private fun showOptionDialog(title: String, options: List<String>, onSelected: (Int) -> Unit) {
        android.app.AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setItems(options.toTypedArray()) { _, which -> onSelected(which) }
            .show()
    }
}
