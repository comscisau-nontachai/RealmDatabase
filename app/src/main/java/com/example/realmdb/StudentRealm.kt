package com.example.realmdb

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class StudentRealm(
    @PrimaryKey
    var studentID: Int? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var age: Int? = null,
    var gender: String? = null,
    var city: String? = null
) : RealmObject()