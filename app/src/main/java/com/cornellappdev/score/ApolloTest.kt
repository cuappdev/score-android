package com.cornellappdev.score

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.score.TeamsQuery


@Composable
fun TeamsScreen(viewModel: TeamsViewModel = hiltViewModel()) {
    val teams = viewModel.getTeams()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(text = "Teams", style = MaterialTheme.typography.headlineMedium)

        if (teams == null) {
            CircularProgressIndicator()
        } else {
            val teamList = teams // Ensure non-null list

            if (teamList.isEmpty()) {
                Text(text = "No teams found")
            } else {
                teamList.filterNotNull().forEach { team ->
                    TeamCard(
                        name = team.name ?: "Unknown",
                        color = team.color ?: "#000000",
                        imageUrl = team.image
                    )
                }
            }
        }
    }
}


@Composable
fun TeamCard(name: String, color: String, imageUrl: String?) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = androidx.compose.ui.graphics.Color(
                android.graphics.Color.parseColor(
                    color
                )
            )
        )
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            imageUrl?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = name,
                    modifier = Modifier.size(50.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = name, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Preview
@Composable
fun ApolloTestPreview() {
    TeamsScreen()
}