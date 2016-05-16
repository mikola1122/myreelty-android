package com.helio.myreelty.network;

import com.helio.myreelty.network.models.BookmarkHouseModel;
import com.helio.myreelty.network.models.HouseModelObject;
import com.helio.myreelty.network.models.LikeModel;
import com.helio.myreelty.network.models.LogInToken;
import com.helio.myreelty.network.models.NetworkHouseModel;
import com.helio.myreelty.network.models.NetworkLikeModel;
import com.helio.myreelty.network.models.NetworkPinsModel;
import com.helio.myreelty.network.models.NetworkReviewModel;
import com.helio.myreelty.network.models.NetworkUserModel;
import com.helio.myreelty.network.models.RegistrationModel;
import com.helio.myreelty.network.models.RegistrationResponseModel;
import com.helio.myreelty.network.models.SignInModel;
import com.helio.myreelty.network.models.VideoAddCommentModel;
import com.helio.myreelty.network.models.VideoCommentResponseModel;
import com.helio.myreelty.network.models.VideoCommentsModel;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Fedir on 22.02.2016.
 */
public interface Api {

    String TOKEN = "Authentication-Token";
    String BASE_URL = "http://162.243.77.207";

    @Headers("Content-Type: application/json")
    @GET("api/reviews?page_size=25")
    Observable<NetworkHouseModel> getAllHouses(@Header(TOKEN) String token);

    @Headers("Content-Type: application/json")
    @GET("api/reviews?page")
    Observable<NetworkHouseModel> getAllHouses(@Query("page") int page);

    @Headers("Content-Type: application/json")
    @GET("api/reviews/{review_id}")
    Observable<HouseModelObject> getReview(@Path("review_id") int reviewId, @Header(TOKEN) String token);

    @Headers("Content-Type: application/json")
    @GET("api/reviews/{review_id}/comments?page")
    Observable<VideoCommentsModel> getVideoComments(@Path("review_id") int reviewId,@Query("page") int page);


    @Headers("Content-Type: application/json")
    @POST("api/reviews/{review_id}/comments")
    Observable<VideoCommentResponseModel> addComment(@Path("review_id") int reviewId, @Header(TOKEN) String token,
                                                     @Body VideoAddCommentModel addCommentModel);

    @Headers("Content-Type: application/json")
    @GET("api/account/bookmarks?page")
    Observable<BookmarkHouseModel> getBookmarks(@Header(TOKEN) String token, @Query("page") int page);

    @Headers("Content-Type: application/json")
    @POST("api/sign_up")
    Observable<RegistrationResponseModel> register(@Body RegistrationModel registrationModel);

    @Headers("Content-Type: application/json")
    @POST("api/sign_in")
    Observable<LogInToken> signIn(@Body SignInModel registrationModel);

    @Headers("Content-Type: application/json")
    @POST("api/reviews/{review_id}/bookmark")
    Observable<Object> addToBookmarks(@Path("review_id") int reviewId, @Header(TOKEN) String token);

    @Headers("Content-Type: application/json")
    @DELETE("api/reviews/{review_id}/bookmark")
    Observable<Object> removeFromBookmarks(@Path("review_id") int reviewId, @Header(TOKEN) String token);

    @GET("api/reviews/nearest?")
    Observable<NetworkHouseModel> getReviewsNear(@Query("address") String address);


    @GET("api/account/reviews?page")
    Observable<NetworkHouseModel> getAccountReviews(@Header(TOKEN) String token, @Query("page") int page);

    @GET("api/users/{user_id}/reviews?page")
    Observable<NetworkHouseModel> getUsersReviews(@Path("user_id") int userId,@Query("page") int page);

    @GET("api/account")
    Observable<NetworkUserModel> getAccount(@Header(TOKEN) String token);

    @Headers("Content-Type: application/json")
    @POST("api/reviews/{review_id}/like")
    Observable<LikeModel> likeReview(@Path("review_id") int reviewId, @Header(TOKEN) String token);

    @Headers("Content-Type: application/json")
    @DELETE("api/reviews/{review_id}/like")
    Observable<LikeModel> dislikeReview(@Path("review_id") int reviewId, @Header(TOKEN) String token);

    @Headers("Content-Type: application/json")
    @GET("api/reviews/{review_id}/likes")
    Observable<NetworkLikeModel> getLikes(@Path("review_id") int reviewId);

    @Headers("Content-Type: application/json")
    @GET("api/reviews/pins")
    Observable<NetworkPinsModel> getAllPins();

    @Headers("Content-Type: application/json")
    @GET("api/reviews/{review_id}")
    Observable<NetworkReviewModel> getHouse(@Path("review_id") int houseId);

    @Headers("Content-Type: application/json")
    @GET("/api/reviews/nearest?")
    Observable<NetworkHouseModel> applyFilters(@Query("address") String address
            , @Query("range") Integer range
            , @Query("property_type") String propertyType
            , @Query("beds") Integer beds
            , @Query("beds_from") Integer bedsFrom
            , @Query("baths") Integer baths
            , @Query("availability") boolean availability
            , @Query("baths_from") Integer bathsFrom
            , @Query("square_from") Integer squareFrom
            , @Query("square_to") Integer squareTo
            , @Query("price_from") Integer priceFrom
            , @Query("price_to") Integer priceTo);
}
