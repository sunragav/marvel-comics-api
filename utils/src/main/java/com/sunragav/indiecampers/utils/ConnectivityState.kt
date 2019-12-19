package com.sunragav.indiecampers.utils

import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

class ConnectivityState @Inject constructor() {
    var connected = AtomicBoolean()
}
