package app.fatal.androidassignment_2_mini_project.playerActivities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.fatal.androidassignment_2_mini_project.LoginActivity
import app.fatal.androidassignment_2_mini_project.R
import app.fatal.androidassignment_2_mini_project.TournamentActivities.TournamentAnswerActivity
import app.fatal.androidassignment_2_mini_project.TournamentActivities.TournamentFormActivity
import app.fatal.androidassignment_2_mini_project.adapters.TournamentAdapter
import app.fatal.androidassignment_2_mini_project.models.Tournament
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PlayerHomeActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val fireStore = FirebaseFirestore.getInstance()

    private lateinit var indicator: LinearProgressIndicator
    private lateinit var txtUserEmail: TextView
    private lateinit var btnLogout: Button

    private lateinit var recyclerView: RecyclerView
    private var listData: List<Tournament> = listOf()
    private lateinit var adapter: TournamentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_player_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        indicator = findViewById(R.id.player_home_progress)
        txtUserEmail = findViewById<TextView>(R.id.player_home_user_email_txt)
        btnLogout = findViewById<Button>(R.id.player_home_logout_btn)

        val currentUser = auth.currentUser
        if (currentUser != null) {
            txtUserEmail.text = currentUser.email
        }

        btnLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(this@PlayerHomeActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        // list display
        recyclerView = findViewById(R.id.player_home_recycler)
        adapter = TournamentAdapter(listData, false, { id ->

            val intent = Intent(this@PlayerHomeActivity, TournamentAnswerActivity::class.java).apply {
                putExtra("id", id)
            }
            startActivity(intent)

        }, {})
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        refreshTournaments()
    }

    private fun refreshTournaments() {
        indicator.isIndeterminate = true
        fireStore.collection("tournaments").get()
            .addOnSuccessListener {
                val list = it.documents.mapNotNull { doc ->
                    doc.toObject(Tournament::class.java)
                }
                adapter.update(list)
                indicator.isIndeterminate = false
            }
    }
}