package app.fatal.androidassignment_2_mini_project.managerActivities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import app.fatal.androidassignment_2_mini_project.LoginActivity
import app.fatal.androidassignment_2_mini_project.R
import app.fatal.androidassignment_2_mini_project.TournamentActivities.TournamentFormActivity
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.log

class ManagerHomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var txtUserEmail: TextView
    private lateinit var btnLogout: Button
    private lateinit var btnAdd: Button

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

        txtUserEmail = findViewById<TextView>(R.id.manager_home_user_email_txt)
        btnLogout = findViewById<Button>(R.id.manager_home_logout_btn)
        btnAdd = findViewById<Button>(R.id.manager_home_add_btn)

        val currentUser = auth.currentUser
        if (currentUser != null) {
            txtUserEmail.text = currentUser.email
        }

        btnLogout.setOnClickListener { logout() }
        btnAdd.setOnClickListener{ gotoEditOrAdd() }

    }

    override fun onResume() {
        super.onResume()

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