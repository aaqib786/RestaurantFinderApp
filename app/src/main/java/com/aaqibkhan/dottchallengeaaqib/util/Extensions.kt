package com.aaqibkhan.dottchallengeaaqib.util

import com.aaqibkhan.dottchallengeaaqib.model.FoursquareCategory
import com.google.android.gms.maps.model.LatLng
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream
import java.io.Serializable

fun LatLng.toCommaSeparatedString(): String = "${this.latitude},${this.longitude}"

fun Serializable.getSizeInBytes(): Int {
    val result: Int
    val baos = ByteArrayOutputStream()
    val oos = ObjectOutputStream(baos)
    result = try {
        oos.writeObject(this)
        baos.size()
    } catch (ex: Exception) {
        1
    } finally {
        oos.close()
        baos.close()
    }
    return result
}

fun List<FoursquareCategory>.getDisplayString() = this.joinToString { it.shortName }