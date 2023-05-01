package cs1302.api;

import com.google.gson.annotations.SerializedName;

/**
 * Represents the GSON-parsed results from the Place Autocomplete API response.
 */
public class AutocompleteResult {

    @SerializedName("place_id") String placeId;

} // AutocompleteResult
