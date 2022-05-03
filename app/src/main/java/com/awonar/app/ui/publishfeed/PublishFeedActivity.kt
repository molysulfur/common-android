package com.awonar.app.ui.publishfeed

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.R
import com.awonar.app.databinding.AwonarActivityPublishFeedBinding
import com.awonar.app.dialog.loading.LoadingDialog
import com.awonar.app.utils.ImageUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.linkedin.android.spyglass.mentions.Mentionable
import com.linkedin.android.spyglass.suggestions.SuggestionsResult
import com.linkedin.android.spyglass.suggestions.interfaces.Suggestible
import com.linkedin.android.spyglass.suggestions.interfaces.SuggestionsResultListener
import com.linkedin.android.spyglass.suggestions.interfaces.SuggestionsVisibilityManager
import com.linkedin.android.spyglass.tokenization.QueryToken
import com.linkedin.android.spyglass.tokenization.impl.WordTokenizer
import com.linkedin.android.spyglass.tokenization.impl.WordTokenizerConfig
import com.linkedin.android.spyglass.tokenization.interfaces.QueryTokenReceiver
import com.molysulfur.library.activity.BaseActivity
import com.molysulfur.library.extension.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.InputStream
import java.util.*

@AndroidEntryPoint
class PublishFeedActivity : BaseActivity(), QueryTokenReceiver, SuggestionsResultListener,
    SuggestionsVisibilityManager {

    companion object {
        const val RESULT_CODE = 2000
        const val EXTRA_FEED = "com.awonar.app.ui.publishfeed.extra.feed"
        private const val DIALOG_KEY = "com.awonar.app.ui.publishfeed.dialog.key.loading"
        private const val BUCKET = "people-network"
    }

    private val binding: AwonarActivityPublishFeedBinding by lazy {
        AwonarActivityPublishFeedBinding.inflate(layoutInflater)
    }

    private val dialog: LoadingDialog = LoadingDialog.Builder()
        .setKey(DIALOG_KEY)
        .setTitle("Loading...")
        .build()

    private val viewModel: PublishFeedViewModel by viewModels()
    private lateinit var adapter: SuggestionListAdapter

    private val tokenizerConfig = WordTokenizerConfig.Builder()
        .setWordBreakChars(", ")
        .setExplicitChars("@$")
        .setMaxNumKeywords(2)
        .setThreshold(1)
        .build()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.publishFeedState.collectLatest { feed ->
                    if (feed != null) {
                        val intent = Intent()
                        val bundle = Bundle().apply {
                            putParcelable(EXTRA_FEED, feed)
                        }
                        intent.putExtras(bundle)
                        setResult(RESULT_CODE, intent)
                        toast(getString(R.string.awoanr_publish_feed_text_success))
                        finish()
                    } else {
                        toast(getString(R.string.awoanr_publish_feed_text_error))
                    }
                }
            }
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.suggestionState.collectLatest { result ->
                    onReceiveSuggestionsResult(result, BUCKET)
                }
            }
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loadingState.collectLatest { isLoading ->
                    if (isLoading) {
                        dialog.show(supportFragmentManager)
                    } else {
                        dialog.dismiss()
                    }
                }
            }
        }
        binding.lifecycleOwner = this
        setContentView(binding.root)
        with(binding.awonarPublishFeedButtonRemovePreviews) {
            setOnClickListener {
                binding.awonarPublishFeedGroupPreview.visibility = View.GONE
            }
        }
        with(binding.awonarPublishFeedEditorPost) {
            setQueryTokenReceiver(this@PublishFeedActivity)
            setQueryTokenReceiver(this@PublishFeedActivity)
            tokenizer = WordTokenizer(tokenizerConfig)
            hint = "Writing here..."
            doAfterTextChanged { text ->
                viewModel.saveDaft(text)
            }
        }
        with(binding.awonarPublishFeedRecyclerList) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        with(binding.awonarPublishFeedToolbar) {
            setNavigationOnClickListener {
                onBackPressed()
            }
        }

        with(binding.awonarPublishFeedButtonPost) {
            setOnClickListener {
                viewModel.submit()
            }
        }
        with(binding.awonarPublishFeedButtonGrallery) {
            setOnClickListener {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 200)
            }
        }
    }

    override fun onQueryReceived(queryToken: QueryToken): MutableList<String> {
        val buckets: MutableList<String> = Collections.singletonList(BUCKET)
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            when (queryToken.explicitChar) {
                '@' -> viewModel.getUserSuggestion(queryToken)
                '$' -> viewModel.getInstrumentSuggestion(queryToken)
            }

        }, 2000)
        return buckets
    }

    override fun onReceiveSuggestionsResult(result: SuggestionsResult, bucket: String) {
        val suggestions = result.suggestions
        adapter = SuggestionListAdapter(suggestions as MutableList<Suggestible>).apply {
            onClick = { mention ->
                mention.getTextForDisplayMode(Mentionable.MentionDisplayMode.FULL)
                binding.awonarPublishFeedEditorPost.insertMention(mention)
                binding.awonarPublishFeedRecyclerList.swapAdapter(
                    SuggestionListAdapter(
                        mutableListOf()
                    ), true
                );
                displaySuggestions(false)
                viewModel.saveDaft(binding.awonarPublishFeedEditorPost.text)
                binding.awonarPublishFeedEditorPost.requestFocus()
            }
        }
        binding.awonarPublishFeedRecyclerList.swapAdapter(adapter, true)
        val display = suggestions.size > 0
        displaySuggestions(display)
    }

    override fun displaySuggestions(display: Boolean) {
        if (display) {
            binding.awonarPublishFeedRecyclerList.visibility = RecyclerView.VISIBLE
        } else {
            binding.awonarPublishFeedRecyclerList.visibility = RecyclerView.GONE
        }
    }

    override fun isDisplayingSuggestions(): Boolean =
        binding.awonarPublishFeedRecyclerList.visibility == RecyclerView.VISIBLE

    override fun onBackPressed() {
        if (!viewModel.canBack()) {
            MaterialAlertDialogBuilder(this)
                .setMessage(getString(R.string.awonar_hint_darf_post))
                .setNegativeButton("Discard") { _, _ ->

                }
                .setPositiveButton("Continue") { _, _ ->
                    super.onBackPressed()
                }
                .show()
        } else {
            super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200 && data?.clipData != null) {
            val mClipData = data.clipData
            val count = mClipData?.itemCount ?: 0
            val bitmaps = mutableListOf<Bitmap?>()
            val files = mutableListOf<File>()
            for (i in 0 until count) {
                val imageUri = mClipData?.getItemAt(0)?.uri
                imageUri?.let {
                    val imageStream: InputStream? = contentResolver.openInputStream(imageUri)
                    val bm: Bitmap = BitmapFactory.decodeStream(imageStream)
                    bitmaps.add(bm)
                    val file = ImageUtil.bitmapToFile(bm, "")
                    file?.let {
                        files.add(it)
                    }
//                    val encodedImage: String = ImageUtil.convertBase64(bm)
                }
            }
            viewModel.saveFiles(files)
            binding.awonarPublishFeedImagePreviews.setImageUrlList(bitmaps)
            binding.awonarPublishFeedGroupPreview.visibility = View.VISIBLE
        }
    }


}