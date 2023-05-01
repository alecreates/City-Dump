package cs1302.api;

import com.google.gson.annotations.SerializedName;

/**
 * Represents the results from the Place Details API that GSON parses and
 * returns in a PlaceDetailsResponse object.
 */
public class PlaceDetailsResult {

    PlaceDetailsResult [] photos;
    @SerializedName("photo_reference") String photoReference;

} // PlaceDetailsResult
