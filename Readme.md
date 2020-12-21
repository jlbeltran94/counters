# Android development Test

This is a sample app that provides a search functionality through the MeLi products catalog, also allows the users to see the description and images of a selected product. 
For the development of this project were used:
 
 * [Kotlin](http://kotlinlang.org/)
 * [Android ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
 * [Android Navigation component](https://developer.android.com/guide/navigation/navigation-getting-started)
 * [Retrofit](https://square.github.io/retrofit/)
 * [Coil](https://github.com/coil-kt/coil)
 * [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
 * [Paging 3 library](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)
 * [mockito](https://site.mockito.org/)
 
 The project structure is listed bellow:

 <details>
   <summary>Example</summary>
 
   ```
   ¦   App.kt
    ¦   
    +---data
    ¦   +---api
    ¦   ¦   ¦   Endpoints.kt
    ¦   ¦   ¦   ProductsService.kt
    ¦   ¦   ¦   
    ¦   ¦   +---models
    ¦   ¦           DescriptionResponseModel.kt
    ¦   ¦           PagingResponseModel.kt
    ¦   ¦           PictureResponseModel.kt
    ¦   ¦           ProductDescriptionResponseModel.kt
    ¦   ¦           ProductResponseModel.kt
    ¦   ¦           SearchProductResponseModel.kt
    ¦   ¦           SearchResponseModel.kt
    ¦   ¦           
    ¦   +---interactors
    ¦   ¦       ProductsInteractor.kt
    ¦   ¦       
    ¦   +---mappers
    ¦   ¦       ProductMapper.kt
    ¦   ¦       
    ¦   +---providers
    ¦   ¦       RecentSearchProvider.kt
    ¦   ¦       
    ¦   +---sources
    ¦           ProductsDataSource.kt
    ¦           
    +---di
    ¦       AppModule.kt
    ¦       
    +---ui
    ¦   ¦   MainActivity.kt
    ¦   ¦   ProductsViewModel.kt
    ¦   ¦   
    ¦   +---adapters
    ¦   ¦       ImagesAdapter.kt
    ¦   ¦       ProductsAdapter.kt
    ¦   ¦       ProductsLoadStateAdapter.kt
    ¦   ¦       
    ¦   +---fullScreenCarousel
    ¦   ¦       FullScreenCarouselDialog.kt
    ¦   ¦       
    ¦   +---models
    ¦   ¦       Product.kt
    ¦   ¦       ProductListItem.kt
    ¦   ¦       SearchProductsResult.kt
    ¦   ¦       
    ¦   +---navigation
    ¦   ¦       NavigationDispatcher.kt
    ¦   ¦       
    ¦   +---productDetail
    ¦   ¦       ProductDetailFragment.kt
    ¦   ¦       ProductDetailFragmentState.kt
    ¦   ¦       
    ¦   +---products
    ¦   ¦       ProductsFragment.kt
    ¦   ¦       ProductsFragmentState.kt
    ¦   ¦       
    ¦   +---search
    ¦   ¦       SearchFragment.kt
    ¦   ¦       
    ¦   +---views
    ¦           ImageCarouselView.kt
    ¦           
    +---utils
            Event.kt
            Ext.kt
   ```
 </details>
 
 