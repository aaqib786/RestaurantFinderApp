package com.aaqibkhan.dottchallengeaaqib.util

import com.aaqibkhan.dottchallengeaaqib.data.TestData
import org.junit.Assert.assertEquals
import org.junit.Test

class ExtensionsKtTest {

    @Test
    fun toCommaSeparatedString() {
        val expected = "${TestData.southWestLatLng.latitude},${TestData.southWestLatLng.longitude}"
        val actual = TestData.southWestLatLng.toCommaSeparatedString()
        assertEquals(expected, actual)
    }

    @Test
    fun getDisplayString() {
        val expected = TestData.foursquareCategoryList.joinToString { it.shortName }
        val actual = TestData.foursquareCategoryList.getDisplayString()
        assertEquals(expected, actual)
    }
}