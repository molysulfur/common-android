package com.awonar.app.ui.publishfeed

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.awonar.app.databinding.AwonarActivityPublishFeedBinding
import com.linkedin.android.spyglass.suggestions.SuggestionsResult
import com.linkedin.android.spyglass.suggestions.interfaces.SuggestionsResultListener
import com.linkedin.android.spyglass.tokenization.QueryToken
import com.linkedin.android.spyglass.tokenization.impl.WordTokenizer
import com.linkedin.android.spyglass.tokenization.impl.WordTokenizerConfig
import com.linkedin.android.spyglass.tokenization.interfaces.QueryTokenReceiver
import com.molysulfur.library.activity.BaseActivity
import com.molysulfur.library.result.successOr
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*


@AndroidEntryPoint
class PublishFeedActivity : BaseActivity(), QueryTokenReceiver {
    companion object {
        private const val BUCKET = "people-network"
    }

    private val binding: AwonarActivityPublishFeedBinding by lazy {
        AwonarActivityPublishFeedBinding.inflate(layoutInflater)
    }

    private val viewModel: PublishFeedViewModel by viewModels()

    private val tokenizerConfig = WordTokenizerConfig.Builder()
        .setWordBreakChars(", ")
        .setExplicitChars("@#")
        .setMaxNumKeywords(2)
        .setThreshold(1)
        .build()

    private var suggestionListener: SuggestionsResultListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.lifecycleOwner = this
        setContentView(binding.root)
        with(binding.awonarPublishFeedEditorPost) {
            suggestionListener = this
            setTokenizer(WordTokenizer(tokenizerConfig))
            setQueryTokenReceiver(this@PublishFeedActivity)
            setHint("Writing here...")
        }
    }

    override fun onQueryReceived(queryToken: QueryToken): MutableList<String> {
        val buckets: MutableList<String> = Collections.singletonList(BUCKET)
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.getSuggestion(queryToken.keywords).collectLatest { response ->
                        val data = response.successOr(listOf())
                        val suggestions = data?.map {
                            MentionSuggestion(it.username ?: "")
                        }
                        val result = SuggestionsResult(queryToken,
                            suggestions?.toMutableList() ?: mutableListOf())
                        suggestionListener?.onReceiveSuggestionsResult(result, BUCKET)
                    }
                }
            }
        }, 2000)
        return buckets
    }


}