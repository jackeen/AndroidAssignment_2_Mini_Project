package app.fatal.androidassignment_2_mini_project

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import app.fatal.androidassignment_2_mini_project.managerActivities.ManagerHomeActivity
import app.fatal.androidassignment_2_mini_project.playerActivities.PlayerHomeActivity
import app.fatal.androidassignment_2_mini_project.role.PlayerManage
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var playerManage: PlayerManage

    private lateinit var progressIndicator: LinearProgressIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        progressIndicator = findViewById(R.id.main_progress)

        playerManage = PlayerManage()
        auth = FirebaseAuth.getInstance()

        val user = auth.currentUser
        if (user != null) {
            progressIndicator.isIndeterminate = true
            playerManage.findPlayer(user.uid, onSuccess = {
                val intent = Intent(this@MainActivity, PlayerHomeActivity::class.java)
                startActivity(intent)
                finish()
            }, onFailed = {
                val intent = Intent(this@MainActivity, ManagerHomeActivity::class.java)
                startActivity(intent)
                finish()
            })
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}