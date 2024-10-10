package app.fatal.androidassignment_2_mini_project.managerActivities

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import app.fatal.androidassignment_2_mini_project.LoginActivity
import app.fatal.androidassignment_2_mini_project.R
import app.fatal.androidassignment_2_mini_project.TournamentActivities.TournamentFormActivity
import app.fatal.androidassignment_2_mini_project.adapters.TournamentAdapter
import app.fatal.androidassignment_2_mini_project.models.Tournament
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlin.math.log

class ManagerHomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var fireStore: FirebaseFirestore

    private lateinit var indicator: LinearProgressIndicator
    private lateinit var txtUserEmail: TextView
    private lateinit var btnLogout: Button
    private lateinit var btnAdd: Button

    private lateinit var recyclerView: RecyclerView
    private var listData: List<Tournament> = listOf()
    private lateinit var adapter: TournamentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_manager_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()
        fireStore = FirebaseFirestore.getInstance()

        indicator = findViewById(R.id.manager_home_progress)
        txtUserEmail = findViewById<TextView>(R.id.manager_home_user_email_txt)
        btnLogout = findViewById<Button>(R.id.manager_home_logout_btn)
        btnAdd = findViewById<Button>(R.id.manager_home_add_btn)

        val currentUser = auth.currentUser
        if (currentUser != null) {
            txtUserEmail.text = currentUser.email
        }

        btnLogout.setOnClickListener { logout() }
        btnAdd.setOnClickListener{ gotoEditOrAdd() }

        // list display
        recyclerView = findViewById(R.id.manager_home_recycler)
        adapter = TournamentAdapter(listData, true, {}, {
            val intent = Intent(this@ManagerHomeActivity, TournamentFormActivity::class.java)
            intent.putExtra("isEdit", true)
            startActivity(intent)
        })
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        refreshTournaments()
    }

    override fun onResume() {
        super.onResume()
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

    private fun logout() {
        auth.signOut()
        val intent = Intent(this@ManagerHomeActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun gotoEditOrAdd() {
        val intent = Intent(this@ManagerHomeActivity, TournamentFormActivity::class.java)
        intent.putExtra("isEdit", false)
        startActivity(intent)
    }
}