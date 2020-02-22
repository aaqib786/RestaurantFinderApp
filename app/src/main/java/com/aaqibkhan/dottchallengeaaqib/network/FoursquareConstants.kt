package com.aaqibkhan.dottchallengeaaqib.network

object FoursquareConstants {

    const val API_VERSION_DATE = "20200220"
    /*
        Ideally these credentials would be securely stored either at Backend or stored encrypted on device
     */
    const val FOURSQUARE_CLIENT_ID = "15EEP4PRUZI4R12HUARNK3UGG3QXKY3K1Q0ICKW3CEBUK2LQ"
    const val FOURSQUARE_CLIENT_SECRET = "HBIKBR1NSAFWBS2FGZQC12VHP3UTETHMPD1JTM3TP0530AAG"
    /*
        The challenge states to show restaurants. There is no top level restaurant category in Foursquare api
        but a Food category exists which consists of many restaurants. To accurately pick up the restaurants
        from the food category, we would need to define what we consider as a restaurant, would a Bistro or
        a Pub that also serves food be considered? So for now I am just using the Food category.
     */
    const val FOOD_CATEGORY_ID = "4d4b7105d754a06374d81259"
    const val RESULTS_LIMIT = 50

}