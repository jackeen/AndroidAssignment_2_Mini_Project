package app.fatal.androidassignment_2_mini_project.adapters

import android.content.Context
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.fatal.androidassignment_2_mini_project.R
import app.fatal.androidassignment_2_mini_project.models.Question

class AnswerAdapter(

    private var ctx: Context,
    private var data: List<Question>,
    private var callback: (Int, Boolean) -> Unit

) : RecyclerView.Adapter<AnswerAdapter.AnswerHolder>() {

    inner class AnswerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view = itemView
        val txtName = view.findViewById<TextView>(R.id.question_item_question)
        val answer = view.findViewById<TextView>(R.id.question_item_answer)
        val optionGroup = view.findViewById<RadioGroup>(R.id.question_item_options)
        val optionTure = view.findViewById<RadioButton>(R.id.question_item_option_true)
        val optionFalse = view.findViewById<RadioButton>(R.id.question_item_option_false)
        val optionNone = view.findViewById<RadioButton>(R.id.question_item_option_none)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_question, parent, false)
        return AnswerHolder(view)
    }

    override fun getItemCount(): Int {
        return data.count()
    }

    override fun onBindViewHolder(holder: AnswerHolder, position: Int) {
        val d = data.get(position)
        holder.txtName.setText("${position + 1}. ${getNormalStringFromHtml(d.question)}")
        holder.answer.setText("Answer: " + getNormalStringFromHtml(d.correctAnswer))

        // reset for recycling
        holder.optionNone.isChecked = true
        holder.answer.visibility = View.GONE


        holder.optionGroup.setOnCheckedChangeListener { group, checkedId ->
            val selectedRadioButton: RadioButton = group.findViewById(checkedId)
            val selectedText = selectedRadioButton.text
            
            if (selectedText != d.correctAnswer) {
                holder.answer.visibility = View.VISIBLE
                callback(position, false)
            } else {
                holder.answer.visibility = View.GONE
                callback(position, true)
            }
        }

    }

    private fun getNormalStringFromHtml(s: String) : String {
        return Html.fromHtml(s, Html.FROM_HTML_MODE_LEGACY).toString()
    }

    public fun update(data: List<Question>) {
        this.data = data
        this.notifyDataSetChanged()
    }
}

