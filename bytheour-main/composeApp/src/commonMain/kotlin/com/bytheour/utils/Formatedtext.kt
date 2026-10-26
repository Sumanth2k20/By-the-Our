package com.bytheour.utils

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import kotlinx.datetime.LocalDate

fun formatTextWithNewLines(text: String): AnnotatedString {
    val cleanedText = text.replace(Regex("<.*?>"), "")
        .replace("\r\n", "\n")
        .replace("&nbsp;", " ")
        .trim()

    val urlRegex = Regex("""https?://\S+""")

    return buildAnnotatedString {
        var lastIndex = 0
        for (match in urlRegex.findAll(cleanedText)) {
            val start = match.range.first
            val end = match.range.last + 1
            val url = match.value

            if (start > lastIndex) {
                append(cleanedText.substring(lastIndex, start))
            }

            pushStringAnnotation(tag = "URL", annotation = url)
            withStyle(style = SpanStyle(color = Color.Blue)) {
                append(url)
            }
            pop()
            lastIndex = end
        }

        if (lastIndex < cleanedText.length) {
            append(cleanedText.substring(lastIndex))
        }
    }
}


@Composable
fun FormattedTextDisplay(text: String) {
    val uriHandler = LocalUriHandler.current
    val annotatedText = formatTextWithNewLines(text)

    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

    Text(
        text = annotatedText,
        style = TextStyle(
            fontSize = 10.sp,
            lineHeight = 13.sp,
            fontWeight = W700,
            fontFamily = JtBtoFonts.getOpenSansRegular(),
            textAlign = TextAlign.Justify,
            color = Color.Black
        ),
        modifier = Modifier.pointerInput(Unit) {
            // 'pointerInput' allows us to detect gestures like taps.
            detectTapGestures { offsetPosition ->
                // When the user taps somewhere in the text,
                // use the stored layout info to figure out which character was tapped.
                textLayoutResult?.let { layoutResult ->
                    // Convert the tapped pixel offset to a character offset in the text.
                    val position = layoutResult.getOffsetForPosition(offsetPosition)
                    // Check if this character position has an annotation tagged as "URL".
                    annotatedText
                        .getStringAnnotations("URL", position, position)
                        .firstOrNull()
                        ?.let { annotation ->
                            // If a URL annotation is found, open it using the system handler.
                            uriHandler.openUri(annotation.item)
                        }
                }
            }
        },
        // Called after text layout is calculated.
        // We save the layout result so we can later map touch positions to text characters.
        onTextLayout = { textLayoutResult = it }
    )
}


fun formatDateString(dateStr: String, showYear: Boolean = false): String {
    return try {
        val date = LocalDate.parse(dateStr) // from "2024-10-26"
        val dayOfWeek = date.dayOfWeek.name.lowercase()
            .replaceFirstChar { it.uppercase() }
            .take(3)
        val month = date.month.name.lowercase()
            .replaceFirstChar { it.uppercase() }
            .take(3)

        if (showYear) {
            "$dayOfWeek, ${date.dayOfMonth} $month ${date.year}"
        } else {
            "$dayOfWeek, ${date.dayOfMonth} $month"
        }
    } catch (e: Exception) {
        dateStr
    }
}