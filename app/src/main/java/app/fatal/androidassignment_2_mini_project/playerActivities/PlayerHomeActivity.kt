package app.fatal.androidassignment_2_mini_project.playerActivities

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
import com.google.firebase.auth.FirebaseAuth

class PlayerHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_player_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val auth = FirebaseAuth.getInstance()

        val txtUserEmail = findViewById<TextView>(R.id.player_home_user_email_txt)
        val btnLogout = findViewById<Button>(R.id.player_home_logout_btn)

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
    }
}