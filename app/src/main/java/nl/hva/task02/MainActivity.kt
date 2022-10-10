package nl.hva.task02

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import nl.hva.task02.databinding.ActivityMainBinding

/**
 * 0: manual generation
 * 1: random generation
 */
const val TABLE_GENERATION_MODE = 1

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var tvRows: MutableList<TextView>
    private lateinit var etRows: MutableList<EditText>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tvRows = mutableListOf(
            binding.tvRow1A,
            binding.tvRow1B,
            binding.tvRow2A,
            binding.tvRow2B,
            binding.tvRow3A,
            binding.tvRow3B,
            binding.tvRow4A,
            binding.tvRow4B
        )

        etRows = mutableListOf(
            binding.etRow1Answer,
            binding.etRow2Answer,
            binding.etRow3Answer,
            binding.etRow4Answer
        )

        initViews()
    }

    private fun initViews() {
        /**
         * Unfortunately, random functions seem to generate the same values over and over again
         * That's why I had to choose my own values
         */
        if (TABLE_GENERATION_MODE == 0) {
            createTableManually()

            binding.btnSubmit.setOnClickListener {
                checkAnswersFromManualTable()
            }
        } else {
            createTableRandomly()

            binding.btnSubmit.setOnClickListener {
                checkAnswersFromRandomTable()
            }
        }
    }

    private fun createTableManually() {
        // row #1
        tvRows.elementAt(0).text = "T"
        tvRows.elementAt(1).text = "T"

        // row #2
        tvRows.elementAt(2).text = "T"
        tvRows.elementAt(3).text = "F"

        // row #3
        tvRows.elementAt(4).text = "F"
        tvRows.elementAt(5).text = "T"

        // row #4
        tvRows.elementAt(6).text = "F"
        tvRows.elementAt(7).text = "F"
    }

    private fun createTableRandomly() {
        val rand = java.util.Random()

        for (row in tvRows) {
            if (rand.nextBoolean()) {
                row.text = "T"
            } else {
                row.text = "F"
            }
        }
    }

    private fun checkAnswersFromManualTable() {
        if (checkFieldsFilled()) {
            val correctAnswers: MutableList<String> = mutableListOf("T", "F", "F", "F")

            var counter = 0
            for (i in etRows.indices) {
                if (etRows.elementAt(i).text.toString().uppercase() == correctAnswers.elementAt(i)) {
                    counter++
                }
            }

            showAnswersFeedback(counter)
        } else {
            showAnswersFeedback(-1)
        }
    }

    private fun checkAnswersFromRandomTable() {
        if (checkFieldsFilled()) {
            val correctAnswers: MutableList<String> = mutableListOf()

            for (i in tvRows.indices step 2) {
                correctAnswers.add(calculateCorrectAnswer(
                    tvRows.elementAt(i).text.toString(),
                    tvRows.elementAt(i + 1).text.toString()))
            }

            var counter = 0
            for (i in etRows.indices) {
                if (etRows.elementAt(i).text.toString().uppercase() == correctAnswers.elementAt(i)) {
                    counter++
                }
            }

            showAnswersFeedback(counter)
        } else {
            showAnswersFeedback(-1)
        }
    }

    private fun checkFieldsFilled(): Boolean {
        if (binding.etRow1Answer.text.isNotEmpty() && binding.etRow2Answer.text.isNotEmpty() &&
            binding.etRow3Answer.text.isNotEmpty() && binding.etRow4Answer.text.isNotEmpty()) {
            return true
        }

        return false
    }

    private fun calculateCorrectAnswer(valueA: String, valueB: String): String {
        return if (valueA == "T" && valueB == "T") {
            "T"
        } else {
            "F"
        }
    }

    private fun showAnswersFeedback(counter: Int) {
        val message = if (counter == -1) {
            "Not all fields have been filled in"
        } else {
            "The number of correct answers is: $counter"
        }

        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}