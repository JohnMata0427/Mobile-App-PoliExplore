package com.jidasea.poliexplore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class FAQFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_f_a_q, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val question1 = view.findViewById<TextView>(R.id.question_1)
        val question2 = view.findViewById<TextView>(R.id.question_2)

        val answer1 = view.findViewById<TextView>(R.id.answer_1)
        val answer2 = view.findViewById<TextView>(R.id.answer_2)

        question1.setOnClickListener { toggleVisibility(answer1) }
        question2.setOnClickListener { toggleVisibility(answer2) }
    }

    private fun toggleVisibility(view: View) {
        view.visibility = if (view.visibility == View.VISIBLE) View.GONE else View.VISIBLE
    }
}