import com.pmsk.pemasokita.ui.adapter.ProductsAdapter
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.pmsk.pemasokita.data.Products

class RecommendedProductsManager(private val adapter: ProductsAdapter, private val loadingListener: PaginateProducts.LoadingListener) {

    private var currentPage = 1
    private val recommendedItemsPerPage = 3
    private var isLoading = false
    private var lastDocument: DocumentSnapshot? = null

    private var productList: List<Products> = emptyList()


    fun searchProducts(query: String): List<Products> {
        val filteredProducts = productList.filter { product ->
            product.product_name!!.contains(query, ignoreCase = true)
        }
        return filteredProducts
    }

    fun loadMoreRecommendedItems() {
        if (isLoading || lastDocument == null) return

        isLoading = true
        loadingListener.onLoadingStarted()

        val firestore = Firebase.firestore
        val productsCollection = firestore.collection("products")
        val query = productsCollection.whereEqualTo("product_category", "recommended")
            .orderBy(FieldPath.documentId())
            .startAfter(lastDocument?.id)
            .limit(recommendedItemsPerPage.toLong())

        query.get()
            .addOnSuccessListener { querySnapshot ->
                val itemList = mutableListOf<Products>()
                for (document in querySnapshot) {
                    val product = document.toObject(Products::class.java)
                    itemList.add(product)
                }
                adapter.addItems(itemList)
                lastDocument = querySnapshot.documents.lastOrNull()
                isLoading = false
                loadingListener.onLoadingFinished()
            }
            .addOnFailureListener { exception ->
                isLoading = false
                loadingListener.onLoadingFinished()
            }
    }

    fun loadRecommendedItems() {
        if (isLoading) return

        isLoading = true
        loadingListener.onLoadingStarted()

        val firestore = Firebase.firestore
        val productsCollection = firestore.collection("products")
        val query = productsCollection.whereEqualTo("product_category", "recommended")
            .orderBy(FieldPath.documentId())
            .limit(recommendedItemsPerPage.toLong())

        if (currentPage > 1 && lastDocument != null) {
            query.startAfter(lastDocument)
        }

        query.get()
            .addOnSuccessListener { querySnapshot ->
                val itemList = mutableListOf<Products>()
                for (document in querySnapshot) {
                    val product = document.toObject(Products::class.java)
                    itemList.add(product)
                }
                if (currentPage == 1) {
                    adapter.setItems(itemList)
                } else {
                    adapter.addItems(itemList)
                }
                lastDocument = querySnapshot.documents.lastOrNull()
                currentPage++
                isLoading = false
                loadingListener.onLoadingFinished()
            }
            .addOnFailureListener { exception ->
                isLoading = false
                loadingListener.onLoadingFinished()
            }
    }
}
