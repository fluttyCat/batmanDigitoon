package com.core.base


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.core.dto.Status

abstract class ParentDialogFragment<T : BaseViewModel, E : ViewDataBinding> : DialogFragment() {

    lateinit var viewModel: T

    lateinit var dataBinding: E

    abstract fun getResourceLayoutId(): Int

    abstract fun getViewModelClass(): Class<T>

    abstract fun getFactory(): ViewModelProvider.Factory

    abstract fun inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
        viewModel = ViewModelProvider(this, getFactory()).get(getViewModelClass())
        viewModel.getNetworkStatus().observe(this, Observer {
            when (it.status) {
                Status.RUNNING -> showProgress(it.tag ?: "")
                Status.SUCCESS -> hideProgress(it.tag ?: "")
                else -> showError(it.tag ?: "", it.msg ?: getString(it.event))
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = DataBindingUtil.inflate(LayoutInflater.from(context), getResourceLayoutId(), container, false)
        lifecycle.addObserver(viewModel)
        viewModel.onCreated()
        return dataBinding.root
    }

    protected fun hideKeyboard(view: View) {
        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    open fun showProgress(tag: String) {}
    open fun hideProgress(tag: String) {}
    open fun showError(tag: String, error: String) {}
}