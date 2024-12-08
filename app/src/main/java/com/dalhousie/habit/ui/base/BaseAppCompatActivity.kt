package com.dalhousie.habit.ui.base

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.dalhousie.habit.BR
import com.dalhousie.habit.R
import com.dalhousie.habit.util.extensions.observeEvent

/**
 * Base activity for all activities
 */
abstract class BaseAppCompatActivity<Binding : ViewDataBinding, ViewModel : BaseViewModel> :
    AppCompatActivity() {

    protected lateinit var binding: Binding
    protected abstract val viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayoutResId())
        setupUi()
        setupViewModel()
        initialize()
    }

    /**
     * Get the layout resource ID for the activity
     */
    @LayoutRes
    abstract fun getLayoutResId(): Int

    /**
     * Setup [ViewModel]
     * Note: Call super.viewModel() in child classes implementation for toast to work
     */
    open fun setupViewModel() {
        viewModel.toastMessage.observeEvent(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Called when onCreate() is executed.
     * This method will be called after setupUi() and setupViewModel() has been executed
     * @see setupUi
     * @see setupViewModel
     */
    open fun initialize() {}

    /**
     * Setup the UI
     */
    private fun setupUi() {
        with(binding) {
            lifecycleOwner = this@BaseAppCompatActivity
            setVariable(BR.viewModel, viewModel)
            executePendingBindings()
        }
        window.statusBarColor = ContextCompat.getColor(this, R.color.app_background)
    }
}