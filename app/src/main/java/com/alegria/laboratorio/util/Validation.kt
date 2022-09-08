package com.alegria.laboratorio.util


fun EcuatorianDocumentValid(ci: String): Boolean {
    var sum: Byte = 0
    try {
        if (ci.trim().length !== 10) return false
        val data: List<String> = ci.split("")
        var verifier = (data[0] + data[1]).toByte()
        if (verifier < 1 || verifier > 24) return false
        val digits = ByteArray(data.size)
        for (i in digits.indices) digits[i] = data[i].toByte()
        if (digits[2] > 6) return false
        for (i in 0 until digits.size - 1) {
            if (i % 2 == 0) {
                verifier = (digits[i] * 2).toByte()
                if (verifier > 9) verifier = (verifier - 9).toByte()
            } else verifier = (digits[i] * 1).toByte()
            sum = (sum + verifier).toByte()
        }
        if (sum - sum % 10 + 10 - sum == digits[9].toInt()) return true
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return false
}
