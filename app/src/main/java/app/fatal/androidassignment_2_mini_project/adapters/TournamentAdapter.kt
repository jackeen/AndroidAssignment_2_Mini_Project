package app.fatal.androidassignment_2_mini_project.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.fatal.androidassignment_2_mini_project.R
import app.fatal.androidassignment_2_mini_project.models.Difficulty
import app.fatal.androidassignment_2_mini_project.models.Tournament
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.logging.SimpleFormatter

class TournamentAdapter(

    private var data: List<Tournament>,
    private var canEdit: Boolean,
    private val onAnswer: () -> Unit,
    private val onEdit: () -> Unit

) : RecyclerView.Adapter<TournamentAdapter.TournamentHolder>() {

    fun update(data: List<Tournament>) {
        this.data = data
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TournamentHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_tournament, parent, false)
        return TournamentHolder(view)
    }

    override fun getItemCount(): Int {
        return data.count()
    }

    override fun onBindViewHolder(holder: TournamentHolder, position: Int) {
        val d = data[position]
        if (d.difficulty == Difficulty.EASY) {
            holder.easySign.visibility = View.VISIBLE
        }
        if (d.difficulty == Difficulty.MEDIUM) {
            holder.mediumSign.visibility = View.VISIBLE
        }
        if (d.difficulty == Difficulty.HARD) {
            holder.hardSign.visibility = View.VISIBLE
        }
        holder.nameTxt.text = d.name
        holder.cateTxt.text = d.category.text

        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val startDateStr = format.format(d.startDate)
        val endDateStr = format.format(d.endDate)
        val isPast = Date() > d.endDate
        val isComing = Date() < d.startDate
        val isOnGoing = Date() in d.startDate..d.endDate

        var stateStr = ""
        if (isPast) {
            stateStr = "Past"
        }
        if (isOnGoing) {
            stateStr = "On Going"
            holder.btnAnswer.isEnabled = true
        }
        if (isComing) {
            stateStr = "Coming"
        }
        holder.dateRangeTxt.text = "[${stateStr}] ${startDateStr} - ${endDateStr}"

        if (canEdit) {
            holder.btnEdit.visibility = View.VISIBLE
        } else {
            holder.btnAnswer.visibility = View.VISIBLE
        }
        holder.btnEdit.setOnClickListener { onEdit() }
        holder.btnAnswer.setOnClickListener { onAnswer() }
    }

    inner class TournamentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view = itemView
        val hardSign = view.findViewById<TextView>(R.id.tournament_item_hard_sign)
        val mediumSign = view.findViewById<TextView>(R.id.tournament_item_medium_sign)
        val easySign = view.findViewById<TextView>(R.id.tournament_item_easy_sign)
        val nameTxt = view.findViewById<TextView>(R.id.tournament_item_name)
        val dateRangeTxt = view.findViewById<TextView>(R.id.tournament_item_date_range)
        val cateTxt = view.findViewById<TextView>(R.id.tournament_item_cate)
        val btnEdit = view.findViewById<Button>(R.id.tournament_item_edit_btn)
        val btnAnswer = view.findViewById<Button>(R.id.tournament_item_answer_btn)
    }
}