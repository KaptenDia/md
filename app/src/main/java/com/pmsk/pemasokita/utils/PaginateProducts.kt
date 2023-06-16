import com.pmsk.pemasokita.ui.adapter.ProductsAdapter

interface PaginateProducts {

    interface LoadingListener {
        fun onLoadingStarted()
        fun onLoadingFinished()
    }

    fun loadMorePopularItems(adapter: ProductsAdapter, loadingListener: LoadingListener)
    fun loadPopularItems(adapter: ProductsAdapter, loadingListener: LoadingListener)
    fun loadMoreRecommendedItems(adapter: ProductsAdapter, loadingListener: LoadingListener)
    fun loadRecommendedItems(adapter: ProductsAdapter, loadingListener: LoadingListener)
}
