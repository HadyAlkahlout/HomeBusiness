package com.nurbk.ps.homebusness.newwork

import com.nurbk.ps.homebusness.model.ClassID
import com.nurbk.ps.homebusness.model.Country.Country
import com.nurbk.ps.homebusness.model.DataCategories.DataCategoires
import com.nurbk.ps.homebusness.model.DataCategories.StoreCategory.StoreCategory
import com.nurbk.ps.homebusness.model.DataHome.DataAllNewProduct.DataNewProduct
import com.nurbk.ps.homebusness.model.DataHome.DataAllNewStore.DataNewStore
import com.nurbk.ps.homebusness.model.DataHome.DataAllSpecialProduct.DataAllSpecialProduct
import com.nurbk.ps.homebusness.model.DataHome.DataAllSpecialStore.DataAllSpecialStore
import com.nurbk.ps.homebusness.model.DataHome.DataHome
import com.nurbk.ps.homebusness.model.DataNotification.DataNotification
import com.nurbk.ps.homebusness.model.ProductDetails.ProductDetails
import com.nurbk.ps.homebusness.model.StoreDetails.StoreDetails
import com.nurbk.ps.homebusness.model.address.Address
import com.nurbk.ps.homebusness.model.avatar.UpdateAvatar
import com.nurbk.ps.homebusness.model.cart.Cart
import com.nurbk.ps.homebusness.model.conditions.Conditions
import com.nurbk.ps.homebusness.model.contact.Contact
import com.nurbk.ps.homebusness.model.contact.ContactMarket
import com.nurbk.ps.homebusness.model.delivery.DelevieryData
import com.nurbk.ps.homebusness.model.favorite.Favorite
import com.nurbk.ps.homebusness.model.mymessage.Message
import com.nurbk.ps.homebusness.model.myorder.MyOrder
import com.nurbk.ps.homebusness.model.myproduct.UpdateStatus
import com.nurbk.ps.homebusness.model.packages.Packages
import com.nurbk.ps.homebusness.model.packages.PostPackageID
import com.nurbk.ps.homebusness.model.packages.PostProductPackage
import com.nurbk.ps.homebusness.model.payment.Payment
import com.nurbk.ps.homebusness.model.postaddress.PostAddress
import com.nurbk.ps.homebusness.model.privacy.Privacy
import com.nurbk.ps.homebusness.model.profile.Profile
import com.nurbk.ps.homebusness.model.profile.UpdateProfile
import com.nurbk.ps.homebusness.model.promocode.Data
import com.nurbk.ps.homebusness.model.promocode.PromoCode
import com.nurbk.ps.homebusness.model.question.QuestionAnswer
import com.nurbk.ps.homebusness.model.user.ActivateAccount
import com.nurbk.ps.homebusness.model.user.RegisterUser
import com.nurbk.ps.homebusness.model.user.resendActivtion.Resend
import com.nurbk.ps.homebusness.model.user.userActivate.DataActivate
import com.nurbk.ps.homebusness.model.setting.Setting
import com.nurbk.ps.homebusness.model.status.Status
import com.raiyansoft.eata.model.user.UserToken
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface Api {


    @POST("user/register")
    suspend fun registerUser(
        @Body registerUser: RegisterUser
    ): Response<UserToken>

    @POST("user/activateAccount")
    suspend fun activateAccount(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Body activateAccount: ActivateAccount
    ): Response<DataActivate>

    @POST("user/resendActivation")
    suspend fun resendActivation(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String
    ): Response<Resend>

    @GET("setting")
    suspend fun getSetting(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String
    ): Response<Setting>

    @GET("cities")
    suspend fun getCites(@Header("lang") lang: String): Response<Country>

    @GET("far_regions")
    suspend fun getFarRegions(@Header("lang") lang: String): Response<Country>

    @GET("home")
    suspend fun getDataHome(
        @Header("Authorization") Authorization: String,
        @Header("lang") lang: String
    ): Response<DataHome>

    @GET("products/categories")
    suspend fun getCategories(
        @Header("Authorization") Authorization: String,
        @Header("lang") lang: String
    ): Response<DataCategoires>

    @GET("products/index/all")
    suspend fun getDataNewProduct(
        @Header("Authorization") Authorization: String,
        @Header("lang") lang: String,
        @Query("page") page: String,
        @Query("keyword") keyword: String
    ): Response<DataNewProduct>

    @GET("products/users/all")
    suspend fun getDataNewStore(
        @Header("Authorization") Authorization: String,
        @Header("lang") lang: String,
        @Query("page") page: String
    ): Response<DataNewStore>


    @GET("products/index/special")
    suspend fun getDataSpecialProduct(
        @Header("Authorization") Authorization: String,
        @Header("lang") lang: String,
        @Query("page") page: String
    ): Response<DataAllSpecialProduct>


    @GET("products/users/special")
    suspend fun getDataSpecialStore(
        @Header("Authorization") Authorization: String,
        @Header("lang") lang: String,
        @Query("page") page: String
    ): Response<DataAllSpecialStore>

    @GET("products/users/all")
    suspend fun getDataStoreCategory(
        @Header("Authorization") Authorization: String,
        @Header("lang") lang: String,
        @Query("page") page: String,
        @Query("cat_id") catId: String
    ): Response<StoreCategory>

    @GET("products/details/{id}")
    suspend fun getProductDetails(
        @Header("Authorization") Authorization: String,
        @Header("lang") lang: String,
        @Path("id") id: String
    ): Response<ProductDetails>


    @GET("user/notification")
    suspend fun getNotification(
        @Header("Authorization") Authorization: String,
        @Header("lang") lang: String,
        @Query("page") page: String
    ): Response<DataNotification>

    @GET("user/read/{id}")
    suspend fun readNotification(
        @Header("Authorization") Authorization: String,
        @Header("lang") lang: String,
        @Path("id") id: String
    ): Response<Status>

    @GET("products/market/{id}")
    suspend fun getStoreDetails(
        @Header("Authorization") Authorization: String,
        @Header("lang") lang: String,
        @Path("id") id: String
    ): Response<StoreDetails>


    @POST("contactUs")
    suspend fun postContactUs(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Body contact: Contact
    ): Response<Status>

    @POST("products/sendMessage")
    suspend fun sendMarket(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Body contact: ContactMarket
    ): Response<Status>

    @POST("products/addFav")
    suspend fun addFav(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Body id: ClassID
    ): Response<Status>

    @POST("products/deleteFav")
    suspend fun deleteFav(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Body id: ClassID
    ): Response<Status>

    ///////////////////////

    @GET("privacy")
    suspend fun getPrivacy(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String
    ): Response<Privacy>


    @GET("conditions")
    suspend fun getConditions(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String
    ): Response<Conditions>


    @GET("products/getFav")
    suspend fun getFavorite(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Query("page") page: String
    ): Response<Favorite>

    @GET("address/get")
    suspend fun getAddress(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String
    ): Response<Address>


    // get order

    @GET("orders/getUserOrders/owner")
    suspend fun getMyOrder(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Query("page") page: String
    ): Response<MyOrder>

    @GET("orders/getUserOrders/user")
    suspend fun getOrder(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Query("page") page: String
    ): Response<MyOrder>

    @GET("orders/getCart")
    suspend fun getMyCart(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Query("page") page: String
    ): Response<Cart>

    // address

    @POST("address/add")
    suspend fun postAddress(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Body donate: com.nurbk.ps.homebusness.model.postaddress.Content
    ): Response<PostAddress>


    @GET("faq")
    suspend fun getQuestion(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String
    ): Response<QuestionAnswer>

    @GET("user/profile")
    suspend fun getProfile(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String
    ): Response<Profile>

    @Multipart
    @POST("user/update/avatar")
    suspend fun updateAvatar(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Part avatar: MultipartBody.Part
    ): Response<UpdateAvatar>

    //////////////


    @GET("products/categories")
    suspend fun getSubCategories(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Query("parent_id") parent_id: String
    ): Response<DataCategoires>

    @Multipart
    @POST("user/update")
    suspend fun updateAccount(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part image: MultipartBody.Part
    ): Response<Profile>


    @Multipart
    @POST("orders/cart/add")
    suspend fun addToCart(
        @Header("Authorization") Authorization: String,
        @Header("lang") lang: String,
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
    ): Response<Status>


    @Multipart
    @POST("orders/rate")
    suspend fun addRate(
        @Header("Authorization") Authorization: String,
        @Header("lang") lang: String,
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
    ): Response<Status>

    @Multipart
    @POST("products/createProduct")
    suspend fun addProduct(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part images: List<MultipartBody.Part>
    ): Response<Status>


    @GET("products/deleteImage/{id}")
    suspend fun deleteImageProduct(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Path("id") id: String
    ): Response<Status>

    @Multipart
    @POST("products/updateProduct")
    suspend fun updateProduct(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part images: List<MultipartBody.Part>
    ): Response<Status>


    @GET("deliveryTimes")
    suspend fun getDeliveryTime(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
    ): Response<DelevieryData>

    @POST("products/follow")
    suspend fun addFollowStore(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Body classID: ClassID
    ): Response<Status>

    @POST("products/unFollow")
    suspend fun deleteFollowStore(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Body classID: ClassID
    ): Response<Status>


    //new In api
    @GET("address/delete/{id}")
    suspend fun deleteAddress(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Path(value = "id") id: String
    ): Response<Status>


    @POST("address/edit")
    suspend fun updateAddress(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Body donate: com.nurbk.ps.homebusness.model.postaddress.Content
    ): Response<Status>

    @Multipart
    @POST("orders/checkout")
    suspend fun addCheckout(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
    ): Response<Status>

    @Multipart
    @POST("user/addStory")
    suspend fun addStory(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Part images: MultipartBody.Part
    ): Response<Status>

    @GET("user/myProducts")
    suspend fun getMyProduct(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String
    ): Response<com.nurbk.ps.homebusness.model.myproduct.ProductDetails>


    @POST("products/updateStatus")
    suspend fun updateProductStatus(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Body updateStatus: UpdateStatus
    ): Response<Status>

    @GET("packages/1")
    suspend fun getSubscribe(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String
    ): Response<Packages>


    @GET("packages/2")
    suspend fun getTameezStore(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String
    ): Response<Packages>

    @GET("packages/3")
    suspend fun getTameezproduct(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String
    ): Response<Packages>


    @POST("user/upgrade")
    suspend fun upgradeaccount(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Body postPackageID: PostPackageID
    ): Response<Status>

    @POST("orders/cart/delete")
    suspend fun deleteOrder(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Body classID: ClassID
    ): Response<Status>

    @POST("user/specialUpgrade")
    suspend fun upgradeToSpecialAccount(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Body postPackageID: PostPackageID
    ): Response<Status>

    @POST("user/specialProduct")
    suspend fun upgradeToSpecialProduct(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Body postProductPackage: PostProductPackage
    ): Response<Status>


    @GET("products/payment")
    suspend fun getPayment(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String
    ): Response<Payment>

    @POST("orders/checkPromo")
    suspend fun PromoCode(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Body code: Data
    ): Response<PromoCode>


    @POST("user/update")
    suspend fun updateProfileAccount(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Body updateProfile: UpdateProfile
    ): Response<Profile>

    @GET("products/getMessages")
    suspend fun getMyMessage(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Query("page") page: String
    ): Response<Message>

}