package cs1302.api;

/**
 * Represents the response from the Place Autocomplete API request.
 * Used by GSON to make a new array with the attributes from
 * AutocompleteResult.java.
 */
public class AutocompleteResponse {

    AutocompleteResult[]predictions;

} // AutocompleteResponse
