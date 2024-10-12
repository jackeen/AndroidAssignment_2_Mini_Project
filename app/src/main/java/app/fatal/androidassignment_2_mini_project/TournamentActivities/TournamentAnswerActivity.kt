package app.fatal.androidassignment_2_mini_project.TournamentActivities

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.widget.Button
import android.widget.TextView
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import app.fatal.androidassignment_2_mini_project.R
import app.fatal.androidassignment_2_mini_project.adapters.AnswerAdapter
import app.fatal.androidassignment_2_mini_project.adapters.NoScrollRecyclerView
import app.fatal.androidassignment_2_mini_project.models.Question
import app.fatal.androidassignment_2_mini_project.models.Tournament
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TournamentAnswerActivity : AppCompatActivity() {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private lateinit var indicator: LinearProgressIndicator
    private lateinit var toolbar: Toolbar
    private lateinit var tournamentNameTxt: TextView
    private lateinit var tournamentScoreTxt: TextView
    private lateinit var btnNext: Button
    private lateinit var btnPrevious: Button
    private lateinit var btnShowScore: Button

    private lateinit var recyclerView: NoScrollRecyclerView
    private var listData: List<Question> = listOf()
    private lateinit var adapter: AnswerAdapter

    private var pageNumber: Int = 0
    private var score: Int = 0
    private var scoreMap = mutableMapOf<Int, Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tournament_answer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tournamentId = intent.getStringExtra("id") ?: return

        btnNext = findViewById(R.id.answer_next_btn)
        btnPrevious = findViewById(R.id.answer_previous_btn)
        btnShowScore = findViewById(R.id.answer_show_score_btn)
        indicator = findViewById(R.id.progress)
        toolbar = findViewById(R.id.toobar)
        tournamentNameTxt = findViewById(R.id.tournament_name_txt)
        tournamentScoreTxt = findViewById(R.id.tournament_score_txt)
        recyclerView = findViewById(R.id.question_recycler)

        // score
        adapter = AnswerAdapter(this, listData) { index, isCorrect ->
            if (isCorrect) {
                scoreMap[index] = isCorrect
            }
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        toolbar.setNavigationOnClickListener { finish() }

        // load questions
        indicator.isIndeterminate = true
        val doc = fireStore.collection("tournaments").document(tournamentId).get()
        doc.addOnSuccessListener {
            if (it != null) {
                indicator.isIndeterminate = false
                val tournament = it.toObject<Tournament>(Tournament::class.java)
                if (tournament != null) {
                    adapter.update(tournament.questions)
                    pageNumber = tournament.amount
                    tournamentNameTxt.setText(tournament.name)
                }
            }
        }

        btnNext.setOnClickListener {
            answerScroll(1)
            pageNumber -= 1
            changePageButtonEnable()
        }

        // disable previous page
        btnPrevious.visibility = View.GONE
        btnPrevious.setOnClickListener {
            answerScroll(-1)
            pageNumber += 1
            changePageButtonEnable()
        }

        btnShowScore.setOnClickListener { showScore() }
    }

    private fun changePageButtonEnable() {
        if (pageNumber == 1) {
            btnNext.isEnabled = false
            btnShowScore.isEnabled = true
        } else {
            btnNext.isEnabled = true
        }
        if (pageNumber == 10) {
            btnPrevious.isEnabled = false
        } else {
            btnPrevious.isEnabled = true
        }
    }

    private fun showScore() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Total Score")
            .setMessage("Your score is ${scoreMap.entries.size}")
            .setPositiveButton("Finish") { dialog, _ ->
                dialog.dismiss()
                finish()
            }

        // Create and show the dialog
        val dialog = builder.create()
        dialog.show()
    }

    private fun answerScroll(i: Int) {
        val recyclerViewHeight = recyclerView.height
        val scrollDistance = recyclerViewHeight * i
        recyclerView.smoothScrollBy(0, scrollDistance)
    }
}