package com.dalhousie.habit.ui.home.fragments

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.dalhousie.habit.R
import com.dalhousie.habit.adapter.SearchUserAdapter
import com.dalhousie.habit.databinding.FragmentSearchBinding
import com.dalhousie.habit.ui.base.BaseFragment
import com.dalhousie.habit.util.extensions.observeEvent
import com.dalhousie.habit.viewmodel.home.HomeViewModel
import com.dalhousie.habit.viewmodel.home.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>() {

    override val viewModel: SearchViewModel by viewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()

    private val searchUserAdapter by  lazy {
        SearchUserAdapter(viewModel::navigateToUserData)
    }

    override fun getLayoutResId(): Int = R.layout.fragment_search

    override fun initialize() {
        binding.recyclerView.adapter = searchUserAdapter
    }

    override fun setupViewModel() {
        super.setupViewModel()
        viewModel.userList.observe(this) { userList ->
            searchUserAdapter.setItems(userList)
        }
        viewModel.query.observe(this) {
            viewModel.searchUsers(it)
        }
        viewModel.navigateToUserData.observeEvent(this) {
            homeViewModel.navigateToUserDataActivity(it)
        }
    }
}
