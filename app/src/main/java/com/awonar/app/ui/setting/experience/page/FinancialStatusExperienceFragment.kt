package com.awonar.app.ui.setting.experience.page

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.MultiAutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.awonar.android.model.experience.QuestionOption
import com.awonar.android.model.experience.Topic
import com.awonar.app.R
import com.awonar.app.databinding.AwonarFragmentExperienceFinancialStatusBinding
import com.awonar.app.ui.setting.experience.ExperienceViewModel
import com.molysulfur.library.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import timber.log.Timber

class FinancialStatusExperienceFragment : Fragment() {

    private var topic: Topic? = null

    private val viewModel: ExperienceViewModel by activityViewModels()

    private val binding: AwonarFragmentExperienceFinancialStatusBinding by lazy {
        AwonarFragmentExperienceFinancialStatusBinding.inflate(layoutInflater)
    }

    companion object {

        private const val EXTRA_TOPIC =
            "com.awonar.app.ui.setting.experience.page.financial_status.extra.topic"

        fun newInstance(topic: Topic?) = FinancialStatusExperienceFragment().apply {
            arguments = Bundle().apply {
                putParcelable(EXTRA_TOPIC, topic)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        arguments?.let {
            topic = it.getParcelable(EXTRA_TOPIC)
        }
        launchAndRepeatWithViewLifecycle {
            viewModel.financialSourceIncomeState.collect { result ->
                val joinStringResult = result.joinToString()
                joinStringResult.contains("Other").also {
                    binding.awonarItemQuestionAutocompleteInputOther.visibility =
                        if (it) View.VISIBLE else View.GONE
                }
                (binding.awonarItemQuestionAutocompleteInputSelect.editText as? MultiAutoCompleteTextView)?.setText(
                    joinStringResult
                )
                initOccupation(result)
            }
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }


    private fun initOccupation(arrayList: ArrayList<String>) {
        val question = topic?.questions?.get(1)
        val newQuestion = question?.questionOption?.filter { option ->
            val choiceSelected: List<QuestionOption?> =
                topic?.questions?.get(0)?.questionOption?.filter { arrayList.contains(it?.text) }
                    ?: arrayListOf()
            if (option?.compatibleOptions == null) {
                true
            }
            val noChoice =
                choiceSelected.find { option?.compatibleOptions?.contains(it?.id) == false }
            noChoice == null
        }
        val list: List<String?> = newQuestion?.map { it?.text } ?: arrayListOf()
        val adapter = ArrayAdapter(
            binding.root.context,
            R.layout.awonar_item_list,
            list
        )
        Timber.e("$list")
        (binding.awonarItemQuestionAutocompleteInputOccupation.editText as? AutoCompleteTextView)?.apply {
            setAdapter(adapter)
        }
    }

    private fun initSourceIncome() {
        val question = topic?.questions?.get(0)
        val list: List<String?> = question?.questionOption?.map { it?.text } ?: arrayListOf()
        val adapter = ArrayAdapter(
            binding.root.context,
            R.layout.awonar_item_list,
            list
        )
        (binding.awonarItemQuestionAutocompleteInputSelect.editText as? MultiAutoCompleteTextView)?.apply {
            setAdapter(adapter)
            setTokenizer(tokenizer)
            setOnItemClickListener { parent, view, position, id ->
                val selected: String =
                    (parent.getItemAtPosition(position) as String).replace(",", "")
                viewModel.addFinancialIncome(selected)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSourceIncome()
    }

    private val tokenizer = object : MultiAutoCompleteTextView.Tokenizer {
        override fun findTokenStart(text: CharSequence?, cursor: Int): Int {
            var i = cursor

            while (i > 0 && text!![i - 1] != ',') {
                i--
            }
            while (i < cursor && text!![i] == ' ') {
                i++
            }
            return i
        }

        override fun findTokenEnd(text: CharSequence?, cursor: Int): Int {
            var i = cursor
            val len = text!!.length

            while (i < len) {
                if (text[i] == ',') {
                    return i
                } else {
                    i++
                }
            }

            return len
        }

        override fun terminateToken(text: CharSequence?): CharSequence {
            var i = text!!.length

            while (i > 0 && text[i - 1] == ' ') {
                i--
            }

            val text = if (i > 0 && text[i - 1] == ',') {
                text
            } else {
                if (text is Spanned) {
                    val sp = SpannableString("$text, ")
                    TextUtils.copySpansFrom(
                        text as Spanned?, 0, text.length,
                        Any::class.java, sp, 0
                    )
                    sp
                } else {
                    "$text, "
                }
            }

            return text
        }

    }


}