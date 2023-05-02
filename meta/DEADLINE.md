Project description:
    Please provide a firendly description of your app, including the the primary functions available to users of the app. Be sure to describe exactly
    what APIs you are using and how they are connected in a meaningful way.
    Also, include the GitHub https URL to your repository.

        City Dump is a geography quiz game that pulls random city photos from majors cities around the world and prompts the user to select an answer
        that they think best matches the photo (i.e., matching Paris, France to a photo of the Eiffel tower). The first API used is Google Maps'
        Autocomplete API, which takes a random city name from my List<String> and returns a list of predictions of cities. Each element of predictions
        contains a place_id variable that is used for the next API, which is the Place Details API. The Place Details API takes the place_id and returns
        a photo_reference variable, given the right parameters. The photo_reference variable is used for the last API, Place Photos API, which returns
        an image URL that is then set onto the game scene.

        Github URL: https://github.com/agt79496/cs1302-api


Part 2: What is something new and/or exciting that you learned from working on this project?
    I learned how to use google maps API's. I learned the hard way that they are not so simple to use because of how much is required to perform a simple    task such as pulling a photo from google's database and putting it into my program. However, now that I have done this project, I will be able to use    RESTful API's with much more ease in the future. In doing this project, I also learned how to set a background image and change the font, as well as     other visual aspects that I hadn't used before.


Part 3: Retrospect
    If I could start over, I would definitely do a little more scene graph planning before starting the actual code. This would have saved me a lot of
    time later on. Going back to add more instance variables, initialize them, and style them in init started making my code less clean, and made it
    harder to debug/fix visual aspects of my application. Also, I would probably be better at naming my variables. A lot of my HBox's and VBox's had very    similar names to their children, which made me confuse them a lot of the time. I did have a lot of instance variables to initialize and name, which
    made naming individual nodes and components very difficult, however, I would definitely try to do a better job with that next time so as to not mix
    them up, which ended up being a bug that was a nightmare to spot at certain points.