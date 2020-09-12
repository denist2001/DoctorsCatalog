# Android Coding Task

Please implement the stories below. The code will be used as a basis of discussion on the next step
of the interview with the current Android developers.

## User Stories - Vivy Doctor List

As a user I want to see
* a list of doctors.
* last 3 recently selected doctors on top of my doctors list.
* more information whenever I select any doctor.

[Endpoint - Doctors](https://vivy.com/interviews/challenges/android/doctors.json)
To fetch the next page you need to take the "lastKey" in the response and add it in the url path so
it will become ​doctors-​{insert last key here}​.json
e.g ​https://vivy.com/interviews/challenges/android/doctors-lastkey.json

## Acceptance criteria:
* As I scroll down the doctor list , more results should be added until there are no more results left.
* The list of doctors should have two sections
    * List displaying recently selected doctors sorted by most recent. This section should have heading
        "Recent Doctors"
    * List displaying Vivy doctors (followed by doctor list excluding recently selected doctor) sorted
        by highest rating. This section should have heading "Vivy Doctors"
* Detailed doctor screen should display name, address and picture of selected doctor.
* Code should be covered by tests.
* Use Stable android studio version to do this challenge. Please don’t use release candidates or canary
    builds.
* Please use Kotlin.

## Some tips:
* Only vertical screen orientation is required.
* There is no need to cache the doctor list locally which is fetched by the api.
* Don't spend too much time on UI. Keep it bare minimum.
* Project should be developed with production quality. Our technical team isn’t just checking the end
    result, they will be evaluating code challenge based on code quality, architecture and problem
    solving skills.

## Bonus Task:
* Add search bar that filter search results by doctor name as user types name of the doctor. (no endpoint
    required for this)