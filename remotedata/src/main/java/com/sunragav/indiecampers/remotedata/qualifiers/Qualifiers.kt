package com.sunragav.indiecampers.remotedata.qualifiers

import javax.inject.Qualifier

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class PublicKey

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class PrivateKey