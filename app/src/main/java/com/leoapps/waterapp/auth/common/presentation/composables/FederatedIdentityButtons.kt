package com.leoapps.waterapp.auth.common.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.leoapps.waterapp.R

@Composable
fun FederatedIdentityButtons(
    onGoogleLoginClicked: () -> Unit,
    onFacebookLoginClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        MiddleScreenDivider()

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(top = 24.dp)
        ) {
            OutlinedButton(
                onClick = onGoogleLoginClicked,
                modifier = Modifier.weight(1f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
            }
            OutlinedButton(
                onClick = onFacebookLoginClicked,
                modifier = Modifier.weight(1f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_facebook),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
private fun MiddleScreenDivider(
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Spacer(
            modifier = Modifier
                .weight(1f)
                .height(2.dp)
                .background(color = Color.Gray)
        )
        Text(
            text = "or",
            color = Color.Gray
        )
        Spacer(
            modifier = Modifier
                .weight(1f)
                .height(2.dp)
                .background(color = Color.Gray)
        )
    }
}