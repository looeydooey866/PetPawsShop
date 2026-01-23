package com.example.petpawsdemo

import kotlin.math.min

// the minimum amount of character changes, additions or deletions to change s to t.
// this function provides an approximate metric for the similarity of 2 words
// if the user makes a spelling mistake, or forgot the exact spelling, etc
// this will provide a close enough keyword match to the erroneous input
fun levenshteinDistance(s: String, t: String): Int{
    val n = s.length
    val m = t.length
    val dp = Array(n + 1){ IntArray(m + 1){Int.MAX_VALUE - 1} }
    dp[0][0] = 0
    for (i in 0..n){
        for (j in 0..m){
            if (i + 1 <= n){
                dp[i + 1][j] = min(dp[i + 1][j], dp[i][j] + 1)
            }
            if (j + 1 <= m){
                dp[i][j + 1] = min(dp[i][j + 1], dp[i][j] + 1)
            }
            if (i + 1 <= n && j + 1 <= m){
                dp[i + 1][j + 1] = min(dp[i + 1][j + 1], dp[i][j] + if (s[i] == t[j]) 0 else 1)
            }
        }
    }
    return dp[n][m]
}

infix fun String.distanceTo(t: String): Int = levenshteinDistance(this, t)

/*
    dp[i][j] = the minimum number of edits to edit s[1..i] and t[1..j] to be equal
    dp[0][0] = 0
    for all i in 1..n and j in 1..m, carry out the procedure
    chmin(dp[i + 1][j + 1], dp[i][j] + (s[i] != t[j]))
    chmin(dp[i + 1][j], dp[i][j] + 1)
    chmin(dp[i][j + 1], dp[i][j] + 1)

    TODO write proof and stress test
 */