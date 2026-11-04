package com.cornellappdev.score.screen

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.score.R
import com.cornellappdev.score.components.ButtonPrimary
import com.cornellappdev.score.components.NavigationHeader
import com.cornellappdev.score.theme.CrimsonPrimary
import com.cornellappdev.score.theme.GrayLight
import com.cornellappdev.score.theme.GrayMedium
import com.cornellappdev.score.theme.GrayPrimary
import com.cornellappdev.score.theme.GrayStroke
import com.cornellappdev.score.theme.Style
import com.cornellappdev.score.theme.White

enum class ReportIssueType(@StringRes val label: Int) {
    INACCURATE_SCORES(R.string.report_inaccurate_scores),
    WRONG_GAME_TIME(R.string.report_wrong_game_time),
    MISSING_GAME(R.string.report_missing_game),
    OTHER(R.string.report_other)
}

enum class ReportSport(@StringRes val label: Int) {
    BASKETBALL(R.string.report_basketball),
    ICE_HOCKEY(R.string.report_ice_hockey),
    LACROSSE(R.string.report_lacrosse),
    SOCCER(R.string.report_soccer),
    FIELD_HOCKEY(R.string.report_field_hockey),
    BASEBALL(R.string.report_baseball),
    FOOTBALL(R.string.report_football)
}

data class IssueReport(
    val issue: ReportIssueType,
    val sport: ReportSport,
    val description: String
)

/** Sending stays disabled until the caller supplies a reporting integration. */
@Composable
fun ReportIssueScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    onSubmit: ((IssueReport) -> Unit)? = null
) {
    var issue by rememberSaveable { mutableStateOf<ReportIssueType?>(null) }
    var sport by rememberSaveable { mutableStateOf<ReportSport?>(null) }
    var description by rememberSaveable { mutableStateOf("") }
    val descriptionLabel = stringResource(R.string.report_description)

    Column(modifier.fillMaxSize().background(White).imePadding()) {
        NavigationHeader(stringResource(R.string.report_title), onBack)
        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp, 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            ReportField(stringResource(R.string.report_issue_question)) {
                ReportDropdown(
                    label = stringResource(R.string.report_issue_question),
                    selected = issue,
                    options = ReportIssueType.entries,
                    optionLabel = { stringResource(it.label) },
                    onSelect = { issue = it }
                )
            }
            ReportField(stringResource(R.string.report_sport_question)) {
                ReportDropdown(
                    label = stringResource(R.string.report_sport_question),
                    selected = sport,
                    options = ReportSport.entries,
                    optionLabel = { stringResource(it.label) },
                    onSelect = { sport = it }
                )
            }
            ReportField(descriptionLabel) {
                BasicTextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 194.dp)
                        .background(GrayStroke, RoundedCornerShape(8.dp)).padding(12.dp)
                        .semantics { contentDescription = descriptionLabel },
                    textStyle = Style.bodyMedium.copy(color = GrayPrimary, lineHeight = 20.sp),
                    cursorBrush = SolidColor(CrimsonPrimary),
                    decorationBox = { innerTextField ->
                        Box {
                            if (description.isEmpty()) {
                                Text(
                                    stringResource(R.string.report_description_hint),
                                    style = Style.bodyMedium,
                                    color = GrayMedium
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
        }
        ButtonPrimary(
            text = stringResource(R.string.report_submit),
            icon = null,
            modifier = Modifier.align(Alignment.CenterHorizontally)
                .padding(top = 16.dp, bottom = 76.dp),
            enabled = onSubmit != null && issue != null && sport != null && description.isNotBlank(),
            onClick = {
                val selectedIssue = issue
                val selectedSport = sport
                if (selectedIssue != null && selectedSport != null && description.isNotBlank()) {
                    onSubmit?.invoke(IssueReport(selectedIssue, selectedSport, description.trim()))
                }
            }
        )
    }
}

@Composable
private fun ReportField(label: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            label,
            style = Style.bodySemibold.copy(fontSize = 16.sp, fontWeight = FontWeight.Bold),
            color = GrayPrimary
        )
        content()
    }
}

@Composable
private fun <T> ReportDropdown(
    label: String,
    selected: T?,
    options: List<T>,
    optionLabel: @Composable (T) -> String,
    onSelect: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedLabel = selected?.let { optionLabel(it) }
        ?: stringResource(R.string.report_choose_option)
    Box {
        Row(
            Modifier.fillMaxWidth().background(GrayStroke, RoundedCornerShape(8.dp))
                .clickable(role = Role.Button, onClick = { expanded = !expanded })
                .semantics {
                    contentDescription = label
                    stateDescription = selectedLabel
                }
                .heightIn(min = 48.dp).padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                selectedLabel,
                style = Style.bodyMedium,
                color = if (selected == null) GrayMedium else GrayPrimary
            )
            Spacer(Modifier.weight(1f))
            Icon(
                painterResource(R.drawable.report_chevron),
                contentDescription = null,
                modifier = Modifier.size(20.dp).rotate(if (expanded) -90f else 90f),
                tint = GrayLight
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = White
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            optionLabel(option), style = Style.bodyMedium,
                            color = if (selected == option) CrimsonPrimary else GrayPrimary
                        )
                    },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 805)
@Composable
private fun ReportIssueScreenPreview() {
    ReportIssueScreen(onBack = {})
}
