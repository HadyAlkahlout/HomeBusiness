package com.nurbk.ps.homebusness.repoistory

import com.nurbk.ps.homebusness.model.ClassID
import com.nurbk.ps.homebusness.model.address.Content
import com.nurbk.ps.homebusness.model.checkout.postCheckout
import com.nurbk.ps.homebusness.model.contact.Contact
import com.nurbk.ps.homebusness.model.contact.ContactMarket
import com.nurbk.ps.homebusness.model.myproduct.UpdateStatus
import com.nurbk.ps.homebusness.model.packages.PostPackageID
import com.nurbk.ps.homebusness.model.packages.PostProductPackage
import com.nurbk.ps.homebusness.model.profile.UpdateProfile
import com.nurbk.ps.homebusness.model.promocode.Data
import com.nurbk.ps.homebusness.model.status.Status
import com.nurbk.ps.homebusness.model.user.ActivateAccount
import com.nurbk.ps.homebusness.model.user.RegisterUser
import com.nurbk.ps.homebusness.newwork.RetrofitInstance
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path

class ApiRepository {

    suspend fun registerUser(registerUser: RegisterUser) =
        RetrofitInstance.api!!.registerUser(registerUser)

    suspend fun activateAccount(
        Authorization: String,
        activateAccount: ActivateAccount,
        lang: String
    ) =
        RetrofitInstance.api!!.activateAccount(lang, Authorization, activateAccount)

    suspend fun resendActivation(Authorization: String, lang: String) =
        RetrofitInstance.api!!.resendActivation(lang, Authorization)

    suspend fun getSetting(Authorization: String, lang: String) =
        RetrofitInstance.api!!.getSetting(lang, Authorization)


    suspend fun getCountry(lang: String) = RetrofitInstance.api!!.getCites(lang)
    suspend fun getFarRegions(lang: String) = RetrofitInstance.api!!.getFarRegions(lang)

    suspend fun getDataHome(auth: String, lang: String) =
        RetrofitInstance.api!!.getDataHome(auth, lang)

    suspend fun getCategories(auth: String, lang: String) =
        RetrofitInstance.api!!.getCategories(auth, lang)

    suspend fun getDataAllHomeProduct(auth: String, lang: String, page: String, keyword: String) =
        RetrofitInstance.api!!.getDataNewProduct(auth, lang, page, keyword)

    suspend fun getDataNewStore(auth: String, lang: String, page: String) =
        RetrofitInstance.api!!.getDataNewStore(auth, lang, page)

    suspend fun getDataSpecialProduct(auth: String, lang: String, page: String) =
        RetrofitInstance.api!!.getDataSpecialProduct(auth, lang, page)

    suspend fun getDataSpecialStore(auth: String, lang: String, page: String) =
        RetrofitInstance.api!!.getDataSpecialStore(auth, lang, page)

    suspend fun getDataStoreCategory(auth: String, lang: String, page: String, catId: String) =
        RetrofitInstance.api!!.getDataStoreCategory(auth, lang, page, catId)

    suspend fun getProductDetails(auth: String, lang: String, id: String) =
        RetrofitInstance.api!!.getProductDetails(auth, lang, id)

    suspend fun getNotification(auth: String, lang: String, page: String) =
        RetrofitInstance.api!!.getNotification(auth, lang, page)

    suspend fun readNotification(auth: String, lang: String, id: String) =
        RetrofitInstance.api!!.readNotification(auth, lang, id)

    suspend fun getStoreDetails(auth: String, lang: String, id: String) =
        RetrofitInstance.api!!.getStoreDetails(auth, lang, id)

    suspend fun postContactUs(contact: Contact, authorization: String, lang: String) =
        RetrofitInstance.api!!.postContactUs(lang, authorization, contact)

    suspend fun sendMarket(contact: ContactMarket, authorization: String, lang: String) =
        RetrofitInstance.api!!.sendMarket(lang, authorization, contact)

    suspend fun addFav(authorization: String, lang: String, id: ClassID) =
        RetrofitInstance.api!!.addFav(lang, authorization, id)

    suspend fun deleteFav(authorization: String, lang: String, id: ClassID) =
        RetrofitInstance.api!!.deleteFav(lang, authorization, id)

    suspend fun addFollowStore(authorization: String, lang: String, id: ClassID) =
        RetrofitInstance.api!!.addFollowStore(lang, authorization, id)

    suspend fun deleteFollowStore(authorization: String, lang: String, id: ClassID) =
        RetrofitInstance.api!!.deleteFollowStore(lang, authorization, id)

    ///////////////
    suspend fun getPrivacy(
        Authorization: String, lang: String
    ) = RetrofitInstance.api!!
        .getPrivacy(lang, Authorization)


    suspend fun getConditions(
        Authorization: String, lang: String
    ) = RetrofitInstance.api!!.getConditions(lang, Authorization)


    suspend fun getFavorite(Authorization: String, page: String, lang: String) =
        RetrofitInstance.api!!.getFavorite(lang, Authorization, page)

    suspend fun getAddress(
        Authorization: String, lang: String
    ) = RetrofitInstance.api!!.getAddress(lang, Authorization)

    suspend fun getMyOrder(Authorization: String, page: String, lang: String) =
        RetrofitInstance.api!!.getMyOrder(lang, Authorization, page)

    suspend fun getOrder(Authorization: String, page: String, lang: String) =
        RetrofitInstance.api!!.getOrder(lang, Authorization, page)

    suspend fun getMyCart(Authorization: String, page: String, lang: String) =
        RetrofitInstance.api!!.getMyCart(lang, Authorization, page)


    suspend fun getQuestion(Authorization: String, lang: String) =
        RetrofitInstance.api!!.getQuestion(lang, Authorization)

    suspend fun getProfile(Authorization: String, lang: String) =
        RetrofitInstance.api!!.getProfile(lang, Authorization)

    suspend fun getSubCategory(Authorization: String, lang: String, id: String) =
        RetrofitInstance.api!!.getSubCategories(lang, Authorization, id)

    suspend fun getDeliveryTime(Authorization: String, lang: String) =
        RetrofitInstance.api!!.getDeliveryTime(lang, Authorization)


    suspend fun postAddress(
        address: com.nurbk.ps.homebusness.model.postaddress.Content,
        authorization: String,
        lang: String
    ) =
        RetrofitInstance.api!!.postAddress(lang, authorization, address)

    suspend fun updateAvatar(authorization: String, images: MultipartBody.Part, lang: String) =
        RetrofitInstance.api!!.updateAvatar(lang, authorization, images)

    suspend fun updateAccount(
        authorization: String,
        lang: String,
        params: Map<String, RequestBody>,
        images: MultipartBody.Part
    ) =
        RetrofitInstance.api!!.updateAccount(
            lang, authorization, params,
            images
        )

    suspend fun addToCart(
        authorization: String,
        lang: String,
        params: Map<String, RequestBody>,
    ) = RetrofitInstance.api!!.addToCart(authorization, lang, params)

    suspend fun addRate(
        authorization: String,
        lang: String,
        params: Map<String, RequestBody>,
    ) = RetrofitInstance.api!!.addRate(authorization, lang, params)

    suspend fun addProduct(
        lang: String,
        Authorization: String,
        params: Map<String, RequestBody>,
        images: List<MultipartBody.Part>
    ) = RetrofitInstance.api!!.addProduct(lang, Authorization, params, images)

    suspend fun deleteImageProduct(
        lang: String,
        Authorization: String,
        id: String
    ) = RetrofitInstance.api!!.deleteImageProduct(lang, Authorization, id)

    suspend fun updateProduct(
        lang: String,
        Authorization: String,
        params: Map<String, RequestBody>,
        images: List<MultipartBody.Part>
    ) = RetrofitInstance.api!!.updateProduct(lang, Authorization, params, images)


    //new in apiRepostroy
    suspend fun deleteAddress(authorization: String, lang: String, id: String) =
        RetrofitInstance.api!!.deleteAddress(lang, authorization, id)

    suspend fun updateAddress(
        address: com.nurbk.ps.homebusness.model.postaddress.Content,
        authorization: String,
        lang: String
    ) =
        RetrofitInstance.api!!.updateAddress(lang, authorization, address)

    suspend fun postCheckout(
        params: Map<String, RequestBody>,
        authorization: String, lang: String
    ) =
        RetrofitInstance.api!!.addCheckout(lang, authorization, params)

    suspend fun addStory(
        images: MultipartBody.Part,
        authorization: String, lang: String
    ) =
        RetrofitInstance.api!!.addStory(lang, authorization, images)

    suspend fun getMyProduct(Authorization: String, lang: String) =
        RetrofitInstance.api!!.getMyProduct(lang, Authorization)

    suspend fun UpdateProductStatus(
        updateStatus: UpdateStatus,
        authorization: String,
        lang: String
    ) =
        RetrofitInstance.api!!.updateProductStatus(lang, authorization, updateStatus)

    suspend fun getSubscribe(Authorization: String, lang: String) =
        RetrofitInstance.api!!.getSubscribe(lang, Authorization)


    suspend fun getTameezStore(Authorization: String, lang: String) =
        RetrofitInstance.api!!.getTameezStore(lang, Authorization)


    suspend fun getTameezproduct(Authorization: String, lang: String) =
        RetrofitInstance.api!!.getTameezproduct(lang, Authorization)


    suspend fun upgradeaccount(postPackageID: PostPackageID, authorization: String, lang: String) =
        RetrofitInstance.api!!.upgradeaccount(lang, authorization, postPackageID)

    suspend fun upgradeToSpecialAccount(
        postPackageID: PostPackageID,
        authorization: String,
        lang: String
    ) =
        RetrofitInstance.api!!.upgradeToSpecialAccount(lang, authorization, postPackageID)

    suspend fun upgradeToSpecialProduct(
        postProductPackage: PostProductPackage,
        authorization: String,
        lang: String
    ) =
        RetrofitInstance.api!!.upgradeToSpecialProduct(lang, authorization, postProductPackage)


    suspend fun getPayment(Authorization: String, lang: String) =
        RetrofitInstance.api!!.getPayment(lang, Authorization)


    suspend fun postPromoCode(code: Data, authorization: String, lang: String) =
        RetrofitInstance.api!!.PromoCode(lang, authorization, code)


    suspend fun deleteOrder(classID: ClassID, authorization: String, lang: String) =
        RetrofitInstance.api!!.deleteOrder(lang, authorization, classID)


    suspend fun updateProfileAccount(
        authorization: String,
        lang: String,
        updateProfile: UpdateProfile
    ) =
        RetrofitInstance.api!!.updateProfileAccount(
            lang, authorization, updateProfile
        )


    suspend fun getMyMessage(Authorization: String, page: String, lang: String) =
        RetrofitInstance.api!!.getMyMessage(lang, Authorization, page)


}