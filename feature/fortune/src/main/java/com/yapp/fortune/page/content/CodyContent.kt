package com.yapp.fortune.page.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yapp.fortune.CodyItem
import com.yapp.fortune.component.CodyBox

/**
 * 코디 정보
 */
@Composable
fun CodyContent(codyList: List<CodyItem>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(36.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        codyList.chunked(2).forEach { rowItems ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                rowItems.forEach { item ->
                    CodyBox(
                        resId = item.resId,
                        categoryLabel = item.categoryLabel,
                        clothsNameLabel = item.clothsNameLabel,
                    )
                }
            }
        }
    }
}
