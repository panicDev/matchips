package id.paniclabs.matchips.sample

import android.content.Context
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import id.paniclabs.matchips.MaterialChipsEditText
import id.paniclabs.matchips.chip.ChipSpan
import id.paniclabs.matchips.chip.ChipSpanChipCreator
import id.paniclabs.matchips.sample.databinding.ActivityMainBinding
import id.paniclabs.matchips.terminator.ChipTerminatorHandler
import id.paniclabs.matchips.token.SpanChipTokenizer
import id.paniclabs.matchips.validator.ChipifyingChipsValidator
import java.util.*

/**
 * @author panicDev
 * @created 24/04/18.
 * @project matchips.
 */
class MainActivity : AppCompatActivity() {

    private val TAG = "MatChips"
    private val SUGGESTIONS_LIST = arrayOf("MatChips", "Chip", "Tortilla Chips", "Melted Cheese", "Salsa", "Guacamole", "Cheddar", "Mozzarella", "Mexico", "Jalapeno")
    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)


        val infoText: Spanned = when {
            android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N -> Html.fromHtml(getString(R.string.info_text_body), Html.FROM_HTML_MODE_LEGACY)
            else -> Html.fromHtml(getString(R.string.info_text_body))
        }

        mainBinding.infoBody.text = infoText

        setupMaterialChipsEdiText(mainBinding.matchipsEditText)
        setupMaterialChipsEdiText(mainBinding.matchipsEditTextWithIcons)


        val testList = ArrayList<String>()
        testList.add("testing")
        testList.add("setText")
        mainBinding.matchipsEditText.setText(testList)

        mainBinding.matchipsEditTextWithIcons.chipTokenizer = SpanChipTokenizer(this, object : ChipSpanChipCreator() {
            override fun createChip(context: Context, text: CharSequence, data: Any?): ChipSpan {
                return ChipSpan(context, text, ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_edit), data)
            }

        }, ChipSpan::class.java)


        //Get Chips Values
        mainBinding.getListChipValues.setOnClickListener {
            alertStringList("Chip Values", mainBinding.matchipsEditText.chipValues)
        }

        //Get Chips and Token Values
        mainBinding.getListChipAndTokenValues.setOnClickListener {
            alertStringList("Chip and Token Values", mainBinding.matchipsEditText.chipValues)
        }


        //To String
        mainBinding.getRawString.setOnClickListener {
            val strings = mutableListOf<String>()
            strings.add(mainBinding.matchipsEditText.toString())
            alertStringList("toString()", strings)
        }

    }

    private fun alertStringList(title: String, list: List<String>) {
        val alertBody: String
        alertBody = when {
            !list.isEmpty() -> {
                println("VALUEE = ${list.toString().replace("[\\[\\]\\(\\)]".toRegex(), "")}")
                val builder = StringBuilder()
                for (chipValue in list) {
                    println("VALUEE2 = $chipValue")
                    builder.append(chipValue)
                    builder.append("\n")
                }
                builder.deleteCharAt(builder.length - 1)
                builder.toString()
            }
            else -> "No strings"
        }

        val dialog = AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(alertBody)
                .setCancelable(true)
                .setNegativeButton("Close", null)
                .create()

        dialog.show()
    }

    private fun setupMaterialChipsEdiText(materialChipsEditText: MaterialChipsEditText) {
        val adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_dropdown_item_1line, SUGGESTIONS_LIST)
        materialChipsEditText.apply {
            setAdapter(adapter)
            setAdapter(adapter)
            setIllegalCharacters('\"')
            addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL)
            addChipTerminator(',', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR)
            addChipTerminator(';', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_CURRENT_TOKEN)
            setChipsValidator(ChipifyingChipsValidator())
            enableEditChipOnTouch(false, true)
            setOnChipClickListener { chip, _ -> Log.d(TAG, "onChipClick: " + chip.text) }
            setOnChipRemoveListener { chip -> Log.d(TAG, "onChipRemoved: " + chip.text) }
        }

    }
}