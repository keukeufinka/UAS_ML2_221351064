package com.example.uas_ml2_221351064.ui.model

import android.graphics.ColorSpace.Model
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.uas_ml2_221351064.R
import com.example.uas_ml2_221351064.databinding.FragmentHomeBinding
import com.example.uas_ml2_221351064.databinding.FragmentModelBinding

class ModelFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_model, container, false)
    }
}
