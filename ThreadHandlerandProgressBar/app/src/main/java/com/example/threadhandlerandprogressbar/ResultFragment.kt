package com.example.threadhandlerandprogressbar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class ResultFragment : Fragment() {

    private val vm: WorkViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_result, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val resultText = view.findViewById<TextView>(R.id.txtResult)
        vm.status.observe(viewLifecycleOwner) { s ->
            when {
                s.startsWith("準備中") -> resultText.text = "準備中…"
                s.startsWith("Working") -> resultText.text = "工作中…"
                s.contains("結束") -> resultText.text = "背景工作結束"
                s.contains("取消") -> resultText.text = "工作已取消"
            }
        }
    }
}
