package com.awonar.app.ui.setting.experience.page

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.MultiAutoCompleteTextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.awonar.android.model.experience.Answer
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
                setOccupationAdapter(result)
                makeAnswer(
                    questionId = topic?.questions?.get(0)?.id,
                    data = result
                )
            }
        }
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    private fun makeAnswer(questionId: String?, data: ArrayList<String>) {
        val answers = arrayListOf<Answer>()
        data.forEach { item ->
            val option =
                topic?.questions?.get(0)?.questionOption?.find { it?.text?.equals(item) == true }
            Timber.e("$item $option ${topic?.questions?.get(0)?.questionOption}")
            if (option != null) {
                answers.add(
                    Answer(
                        id = option.id
                    )
                )
            }
        }
        viewModel.addAnswer(questionId, answers)
    }


    private fun setOccupationAdapter(arrayList: ArrayList<String>) {
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
        (binding.awonarItemQuestionAutocompleteInputOccupation.editText as? AutoCompleteTextView)?.apply {
            setAdapter(adapter)
        }
    }

    private fun initSourceIncomeInput() {
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
        binding.awonarItemQuestionAutocompleteInputOther.editText?.doAfterTextChanged {
            val text: String? = it?.toString()
            val questions = topic?.questions?.get(0)
            val questionId: String? = questions?.id
            val option = questions?.questionOption?.find { it?.text?.lowercase() == "other" }
            viewModel.addOtherAnswer(questionId, option?.id, text)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSourceIncomeInput()
        initNetIncomeInput()
        initAssersInput()
        (binding.awonarItemQuestionAutocompleteInputOccupation.editText as? AutoCompleteTextView)?.apply {
            setOnItemClickListener { parent, view, position, id ->
                binding.awonarItemQuestionAutocompleteInputCompany.visibility =
                    if (hasCompanyCompatible(parent, position)) View.VISIBLE else View.GONE
                val selected: String = (parent.getItemAtPosition(position) as String)
                val questionOption =
                    topic?.questions?.get(1)?.questionOption?.find { it?.text == selected }
                viewModel.addAnswer(topic?.questions?.get(1)?.id, Answer(id = questionOption?.id))
            }
        }
        binding.awonarItemQuestionAutocompleteInputCompany.editText?.doAfterTextChanged {
            val question = topic?.questions?.get(2)
            viewModel.addOtherAnswer(
                questionId = question?.id,
                answerId = question?.questionOption?.get(0)?.id,
                text = it?.toString()
            )
        }
    }

    private fun initAssersInput() {
        val question = topic?.questions?.get(4)
        val list: List<String?> = question?.questionOption?.map { it?.text } ?: arrayListOf()
        val adapter = ArrayAdapter(
            binding.root.context,
            R.layout.awonar_item_list,
            list
        )
        (binding.awonarItemQuestionAutocompleteInputAssets.editText as AutoCompleteTextView).setAdapter(
            adapter
        )
        (binding.awonarItemQuestionAutocompleteInputAssets.editText as AutoCompleteTextView).apply {
            setOnItemClickListener { parent, view, position, id ->
                val selected: String = (parent.getItemAtPosition(position) as String)
                val questionOption =
                    topic?.questions?.get(4)?.questionOption?.find { it?.text == selected }
                viewModel.addAnswer(topic?.questions?.get(4)?.id, Answer(id = questionOption?.id))
            }
        }
    }

    private fun initNetIncomeInput() {
        val question = topic?.questions?.get(3)
        val list: List<String?> = question?.questionOption?.map { it?.text } ?: arrayListOf()
        val adapter = ArrayAdapter(
            binding.root.context,
            R.layout.awonar_item_list,
            list
        )
        (binding.awonarItemQuestionAutocompleteInputIncome.editText as AutoCompleteTextView).setAdapter(
            adapter
        )
        (binding.awonarItemQuestionAutocompleteInputIncome.editText as AutoCompleteTextView).apply {
            setOnItemClickListener { parent, view, position, id ->
                val selected: String = (parent.getItemAtPosition(position) as String)
                val questionOption =
                    topic?.questions?.get(3)?.questionOption?.find { it?.text == selected }
                viewModel.addAnswer(topic?.questions?.get(3)?.id, Answer(id = questionOption?.id))
            }
        }
    }

    private fun hasCompanyCompatible(parent: AdapterView<*>, position: Int): Boolean {
        val question = topic?.questions?.get(2)
        val selected: String = (parent.getItemAtPosition(position) as String)
        val questionOption =
            topic?.questions?.get(1)?.questionOption?.find { it?.text == selected }
        if (question?.compatibleOptions == null) {
            return true
        }
        return question.compatibleOptions?.contains(questionOption?.id) == true
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