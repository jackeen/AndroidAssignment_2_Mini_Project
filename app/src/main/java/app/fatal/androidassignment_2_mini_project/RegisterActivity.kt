package app.fatal.androidassignment_2_mini_project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import app.fatal.androidassignment_2_mini_project.role.PlayerManage
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var playerManage: PlayerManage

    private lateinit var progress: LinearProgressIndicator
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()
        playerManage = PlayerManage()

        progress = findViewById(R.id.register_progress)
        etEmail = findViewById(R.id.register_email_et)
        etPassword = findViewById(R.id.register_password_et)
        etConfirmPassword = findViewById(R.id.register_confirm_password_et)
        btnLogin = findViewById(R.id.register_login_btn)
        btnRegister = findViewById(R.id.register_register_btn)

        val ctx = this

        btnRegister.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(ctx, "Email and Password cannot be empty.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(ctx, "Please check password", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            progress.isIndeterminate = true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    it.user?.uid?.let { uid ->
                        playerManage.savePlayer(uid, callback = {
                            progress.isIndeterminate = false
                            val intent = Intent(ctx, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        })
                    }
                }
                .addOnFailureListener {
                    progress.isIndeterminate = false
                    Toast.makeText(ctx, it.message, Toast.LENGTH_LONG).show()
                }
        }

        btnLogin.setOnClickListener {
            val intent = Intent(ctx, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


}