package site.geniyz.ots.jwt

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


@OptIn(ExperimentalEncodingApi::class)
fun String.fromBase64()= String(
    Base64.withPadding(Base64.PaddingOption.PRESENT_OPTIONAL).decode(
        this
            .replace('-', '+')
            .replace('_', '/')
    ),
    charset = Charsets.UTF_8
)

