package com.example.petpawsdemo

fun levenshteinDistance(s: String, t: String): Int{
    val n = s.length
    val m = t.length
    val dp = Array(n + 1){ IntArray(m + 1){Int.MAX_VALUE - 1} }

    return 0
}

/*
    dp[i][j] = the minimum number of edits to edit s[1..i] and t[1..j] to be equal
    
 */