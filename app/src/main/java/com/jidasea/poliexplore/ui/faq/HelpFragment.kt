package com.jidasea.poliexplore.ui.faq
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.jidasea.poliexplore.R

class HelpFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        return inflater.inflate(R.layout.fragment_help, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtén las vistas
        val question1 = view.findViewById<TextView>(R.id.question_1)
        val answer1 = view.findViewById<TextView>(R.id.answer_1)

        val question2 = view.findViewById<TextView>(R.id.question_2)
        val answer2 = view.findViewById<TextView>(R.id.answer_2)

        // Configura la lógica de expandir/colapsar
        question1.setOnClickListener {
            toggleVisibility(answer1)
        }

        question2.setOnClickListener {
            toggleVisibility(answer2)
        }
    }

    private fun toggleVisibility(view: View) {
        view.visibility = if (view.visibility == View.VISIBLE) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

}