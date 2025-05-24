package site.geniyz.ots.jwt

import com.auth0.jwt.algorithms.Algorithm
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
fun getAlgo() = Algorithm.HMAC256("POROL")
