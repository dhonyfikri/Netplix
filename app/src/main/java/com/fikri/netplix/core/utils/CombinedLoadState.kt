package com.fikri.netplix.core.utils

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState

object CombinedLoadState {
    fun CombinedLoadStates.decideOnState(
        itemCount: Int,
        showLoading: ((Boolean) -> Unit)? = null,
        adapterAnyLoadingStateLoading: ((Boolean) -> Unit)? = null,
        showEmptyState: ((Boolean) -> Unit)? = null,
        showError: ((String) -> Unit)? = null,
        showAnyError: ((String) -> Unit)? = null
    ) {
        showLoading?.invoke(refresh is LoadState.Loading)

        adapterAnyLoadingStateLoading?.invoke(
            refresh is LoadState.Loading ||
                    prepend is LoadState.Loading ||
                    append is LoadState.Loading ||
                    source.refresh is LoadState.Loading ||
                    source.prepend is LoadState.Loading ||
                    source.append is LoadState.Loading
        )

        showEmptyState?.invoke(
            source.append.endOfPaginationReached && itemCount == 0
        )

        val errorState = refresh as? LoadState.Error

        errorState?.let { showError?.invoke(it.error.toString()) }

        val anyErrorState = source.append as? LoadState.Error
            ?: source.prepend as? LoadState.Error
            ?: source.refresh as? LoadState.Error
            ?: append as? LoadState.Error
            ?: prepend as? LoadState.Error
            ?: refresh as? LoadState.Error

        anyErrorState?.let { showAnyError?.invoke(it.error.toString()) }
    }
}