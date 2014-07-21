MetroMate for Android.
=========

A beautifully designed guide to the Dubai Metro (iOS version <a href="https://github.com/MachOSX/metromate-ios">here</a>).**

This is my first ever Android app. It's also the first time I publicly release my code. While I admit it might not look beautiful, it got the necessary job done. Everything is stuffed into 1 view controller class because I needed fine control over view transitions. This seemed like the quickest way back then. :P

If someone out there is looking to build a better guide to Dubai's public transport systems, I hope this'll help.

<h2>Features</h2>
* Train timings.
* ETA at destination.
* Nol Card balance checker.
* Automatic station detection. <em>(you'll need a Foursquare client ID &amp; secret for this)</em>

<h2>Why the hardcoded timings for one station only?</h2>
Because I lived close to the Noor Bank station. All the other timings are based on the timings at Noor Bank. The RTA has no public API for fetching train timings.

<h2>About the Nol Card balance checker</h2>
As you might've guessed, the RTA doesn't provide public APIs for Nol Card services either. I had to figure out the server endpoint used by their official iOS app. Sometimes the service dies for no reason.

** <em>This project is not affiliated with the Roads &amp; Transport Authority of Dubai.</em>
