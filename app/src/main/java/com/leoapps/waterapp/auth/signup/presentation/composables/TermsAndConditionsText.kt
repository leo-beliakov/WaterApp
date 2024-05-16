package com.leoapps.waterapp.auth.signup.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leoapps.waterapp.R

@Composable
internal fun TermsAndConditionsCheckBox(
    isChecked: Boolean,
    onTermsChecked: (Boolean) -> Unit,
    onTermsClicked: () -> Unit

) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(top = 24.dp)
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onTermsChecked
        )
        TermsAndConditionsText(
            onTermsClicked = onTermsClicked
        )
    }
}

@Composable
private fun TermsAndConditionsText(onTermsClicked: () -> Unit) {
    val resources = LocalContext.current.resources
    val fullText = resources.getString(R.string.signup_terms_and_conditions_full)
    val termsText = resources.getString(R.string.signup_terms_and_conditions)

    val annotatedString = buildAnnotatedString {
        append(fullText.substringBefore(termsText))
        pushStringAnnotation(tag = "URL", annotation = "open_terms")
        pushStyle(SpanStyle(textDecoration = TextDecoration.Underline))
        append(termsText)
        pop()  // Pop style for underlining
        pop()  // Pop annotation
        append(fullText.substringAfter(termsText))
    }

    ClickableText(
        text = annotatedString,
        style = TextStyle(fontSize = 16.sp),
        onClick = { offset ->
            val clickedUrl = annotatedString
                .getStringAnnotations(tag = "URL", start = offset, end = offset)
                .firstOrNull()

            if (clickedUrl?.item == "open_terms") {
                onTermsClicked.invoke()
            }
        }
    )
}
