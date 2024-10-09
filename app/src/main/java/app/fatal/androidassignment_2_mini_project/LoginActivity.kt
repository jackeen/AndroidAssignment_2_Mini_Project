package app.fatal.androidassignment_2_mini_project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var progress: LinearProgressIndicator
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()
        progress = findViewById(R.id.login_progress)
        etEmail = findViewById(R.id.login_email_et)
        etPassword = findViewById(R.id.login_password_et)
        btnLogin = findViewById(R.id.login_login_btn)
        btnRegister = findViewById(R.id.login_register_btn)


        val ctx = this

        btnLogin.setOnClickListener {
            val email: String = etEmail.text.toString().trim()
            val password: String = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(ctx, "Email or Password cannot be empty.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            progress.isIndeterminate = true
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    progress.isIndeterminate = false
                    val intent = Intent(ctx, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    progress.isIndeterminate = false
                    Toast.makeText(ctx, it.message, Toast.LENGTH_LONG).show()
                }
        }

        btnRegister.setOnClickListener {
            val intent = Intent(ctx, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}