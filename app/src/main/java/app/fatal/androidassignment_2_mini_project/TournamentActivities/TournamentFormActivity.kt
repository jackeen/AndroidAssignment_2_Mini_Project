package app.fatal.androidassignment_2_mini_project.TournamentActivities

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import app.fatal.androidassignment_2_mini_project.R
import app.fatal.androidassignment_2_mini_project.models.Category
import app.fatal.androidassignment_2_mini_project.models.Difficulty
import app.fatal.androidassignment_2_mini_project.models.Question
import app.fatal.androidassignment_2_mini_project.models.QuestionResponse
import app.fatal.androidassignment_2_mini_project.models.Tournament
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TournamentFormActivity : AppCompatActivity() {

    private lateinit var fireStore: FirebaseFirestore;
    private val collectionName = "tournaments"

    // for editing
    private var currentData: Tournament = Tournament()

    private lateinit var progressIndicator: LinearProgressIndicator
    private lateinit var toolbar: Toolbar
    private lateinit var txtName: TextInputEditText
    private lateinit var selectorCategory: AutoCompleteTextView
    private lateinit var selectorDifficulty: AutoCompleteTextView
    private lateinit var txtStartDate: TextInputEditText
    private lateinit var txtEndDate: TextInputEditText

    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tournament_form)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // get activity-between params for editing
        val isEdit = intent.getBooleanExtra("isEdit", false)
        val tournamentId = intent.getStringExtra("id")

        // init firebase
        fireStore = FirebaseFirestore.getInstance()

        // init of objects of components from UI
        progressIndicator = findViewById(R.id.tournament_form_progress)
        toolbar = findViewById(R.id.tournament_form_toolbar)
        txtName = findViewById(R.id.tournament_form_name)
        selectorCategory = findViewById(R.id.tournament_form_category)
        selectorDifficulty = findViewById(R.id.tournament_form_difficulty)
        txtStartDate = findViewById(R.id.tournament_form_start_date)
        txtEndDate = findViewById(R.id.tournament_form_end_date)
        btnSave = findViewById(R.id.tournament_form_save_btn)
        btnCancel = findViewById(R.id.tournament_form_cancel_btn)

        toolbar.setNavigationOnClickListener { finish() }
        if (isEdit) {
            toolbar.title = "Edit Tournament"
        } else {
            toolbar.title = "Add Tournament"
        }

        // Category selector
        var currentCate: Category = Category.ANY
        val categoryTexts = Category.entries.map { it.text }
        val categoryIds = Category.entries.map { it.id }
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categoryTexts)
        selectorCategory.setAdapter(categoryAdapter)
        selectorCategory.setOnItemClickListener { _, _, position, _ ->
            currentCate = Category.entries[position]
        }

        // Difficulty selector
        var currentDifficulty = Difficulty.ANY
        val difficultyTexts = Difficulty.entries.map { it.text }
        val difficultyValues = Difficulty.entries.map { it.value }
        val difficultyAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, difficultyTexts)
        selectorDifficulty.setAdapter(difficultyAdapter)
        selectorDifficulty.setOnItemClickListener { _, _, position, _ ->
            currentDifficulty = Difficulty.entries[position]
        }

        // Date event
        txtStartDate.setOnClickListener { showDatePicker(txtStartDate, currentData.startDate) }
        txtEndDate.setOnClickListener { showDatePicker(txtEndDate, currentData.endDate) }

        // save and cancel btn event
        btnSave.setOnClickListener { save(isEdit, currentCate, currentDifficulty) }
        btnCancel.setOnClickListener { finish() }

        // if edit tournament, to load current tournament
        if (isEdit && tournamentId != null) {
            loadTournament(tournamentId) { it ->
                currentData = it
                txtName.setText(it.name)
                selectorCategory.setText(it.category.text, false)
                selectorDifficulty.setText(it.difficulty.text, false)
                txtStartDate.setText(getDateString(currentData.startDate))
                txtEndDate.setText(getDateString(currentData.endDate))
            }
        }
    }

    private fun showDatePicker(view: TextInputEditText, d: Date) {
        val calender = Calendar.getInstance()
        calender.time = d
        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH)
        val day = calender.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this@TournamentFormActivity,
            {_, y, m, d ->
                val selectedDate = String.format("%04d-%02d-%02d", y, m + 1, d)
                view.setText(selectedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun loadTournament(id: String, callback: (Tournament) -> Unit) {
        progressIndicator.isIndeterminate = true
        fireStore.collection(collectionName).document(id).get()
            .addOnSuccessListener { doc ->
                if (doc != null) {
                    val tournament = doc.toObject<Tournament>(Tournament::class.java)
                    if (tournament != null) {
                        callback(tournament)
                    }
                    progressIndicator.isIndeterminate = false
                }
            }
    }

    private fun getDateString(d: Date): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return formatter.format(d)
    }

    private fun save(isEdit: Boolean, cate: Category, difficulty: Difficulty) {

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val startDate = dateFormat.parse(txtStartDate.text.toString())
        val endDate = dateFormat.parse(txtEndDate.text.toString())

        if (isEdit) {

            val updateMap = hashMapOf<String, Any>()
            val name = txtName.text.toString().trim()
            if (name != currentData.name) {
                updateMap["name"] = name
            }
            if (startDate != currentData.startDate) {
                updateMap["startDate"] = startDate?:currentData.startDate
            }
            if (endDate != currentData.endDate) {
                updateMap["endDate"] = endDate?:currentData.endDate
            }
            if (cate != currentData.category) {
                updateMap["category"] = cate
            }
            if (difficulty != currentData.difficulty) {
                updateMap["difficulty"] = difficulty
            }

            // regenerate questions
            progressIndicator.isIndeterminate = true
            val docRef = fireStore.collection(collectionName).document(currentData.id)
            docRef.update(updateMap)
                .addOnSuccessListener {
                    if (cate != currentData.category || difficulty != currentData.difficulty) {
                        currentData.category = cate
                        currentData.difficulty = difficulty
                        getQuestions { questionList ->
                            docRef.update("questions", questionList)
                                .addOnSuccessListener {
                                    progressIndicator.isIndeterminate = false
                                    finish()
                                }
                        }
                    } else {
                        progressIndicator.isIndeterminate = false
                        finish()
                    }
                }



        } else {

            val data = Tournament(
                name = txtName.text.toString().trim(),
                category = cate,
                difficulty = difficulty,
                startDate = startDate ?: Date(),
                endDate = endDate ?: Date(),
            )

            // The process of saving tournament
            // 1. Save a new document in firebase by properties
            // 2. Load questions by these properties
            // 3. Update "questions" of this new document
            progressIndicator.isIndeterminate = true
            fireStore.collection(collectionName).add(data)
                .addOnSuccessListener { docRef ->
                    docRef.update("id", docRef.id)
                        .addOnSuccessListener {
                            currentData = data
                            getQuestions { questionList ->
                                docRef.update("questions", questionList)
                                    .addOnSuccessListener {
                                        progressIndicator.isIndeterminate = false
                                        finish()
                                    }
                            }
                        }
                }
        }
    }

    private fun getQuestions(callback: (List<Question>) -> Unit) {
        val amount = currentData.amount
        val categoryId = currentData.category.id
        val difficultyValue = currentData.difficulty.value
        val type = currentData.type.value
        val url = "https://opentdb.com/api.php?amount=${amount}&category=${categoryId}&difficulty=${difficultyValue}&type=${type}"

        val queue = Volley.newRequestQueue(this@TournamentFormActivity)
        val stringRequest = StringRequest(Request.Method.GET, url, Response.Listener<String> {
            val gson = Gson()
            val resType = object : TypeToken<QuestionResponse>() {}.type
            val response = gson.fromJson<QuestionResponse>(it, resType)
            callback(response.results)
        }) {

        }
        queue.add(stringRequest)
    }



}