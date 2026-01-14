package com.cornellappdev.score.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun AdvancedFilterBottomSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onApply: (
        price: PriceFilter?,
        location: LocationFilter?,
        date: DateFilter?
    ) -> Unit,
    onReset: () -> Unit
) {
    var selectedPrice by remember { mutableStateOf<PriceFilter?>(null) }
    var selectedLocation by remember { mutableStateOf<LocationFilter?>(null) }
    var selectedDate by remember { mutableStateOf<DateFilter?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 32.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.padding(end = 12.dp)
            ) {
                ExpandableSection(
                    title = "Price",
                    options = PriceFilter.entries,
                    selectedOption = selectedPrice,
                    onOptionSelected = { selectedPrice = it }
                )

                ExpandableSection(
                    title = "Location",
                    options = LocationFilter.entries,
                    selectedOption = selectedLocation,
                    onOptionSelected = { selectedLocation = it }
                )

                ExpandableSection(
                    title = "Date of Game",
                    options = DateFilter.entries,
                    selectedOption = selectedDate,
                    onOptionSelected = { selectedDate = it }
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                ButtonPrimary(
                    text = "Apply",
                    icon = null,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onApply(
                            selectedPrice,
                            selectedLocation,
                            selectedDate
                        )
                        onDismiss()
                    }
                )

                Text(
                    text = "Reset",
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedPrice = null
                            selectedLocation = null
                            selectedDate = null
                            onReset()
                            onDismiss()
                        },
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
