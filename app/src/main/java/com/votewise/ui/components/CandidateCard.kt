package com.votewise.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.votewise.data.model.Candidate
import com.votewise.data.model.CandidateStatus
import com.votewise.data.model.ElectionType

@Composable
fun CandidateCard(
    candidate: Candidate,
    matchPercentage: Double? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Candidate Photo
                AsyncImage(
                    model = candidate.imageUrl,
                    contentDescription = "Candidate photo",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Name and Party
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = candidate.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        // Party Badge
                        Surface(
                            color = getPartyColor(candidate.party),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = candidate.party,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White
                            )
                        }
                    }

                    // Office and District
                    Text(
                        text = buildString {
                            append(candidate.office ?: "Unknown Office")
                            candidate.district?.let { append(" - District $it") }
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Status and Type
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        candidate.status?.let { StatusChip(status = it) }
                        candidate.electionType?.let { ElectionTypeChip(type = it) }
                    }
                }

                // Match Percentage (if available)
                matchPercentage?.let { percentage ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${percentage.toInt()}%",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = getMatchColor(percentage)
                        )
                        Text(
                            text = "Match",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Consistency Score (if available)
            candidate.consistencyScore?.let { score ->
                Spacer(modifier = Modifier.height(8.dp))
                ConsistencyScoreBar(score = score)
            }
        }
    }
}

@Composable
private fun StatusChip(status: CandidateStatus) {
    val (text, color) = when (status) {
        CandidateStatus.DECLARED -> "Declared" to Color.Blue
        CandidateStatus.EXPLORATORY -> "Exploring" to Color.Cyan
        CandidateStatus.QUALIFIED -> "Qualified" to Color.Green
        CandidateStatus.WITHDRAWN -> "Withdrawn" to Color.Red
        CandidateStatus.DEFEATED -> "Defeated" to Color.Gray
        CandidateStatus.ELECTED -> "Elected" to Color.Green
    }

    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
    }
}

@Composable
private fun ElectionTypeChip(type: ElectionType) {
    val text = when (type) {
        ElectionType.FEDERAL -> "Federal"
        ElectionType.STATE -> "State"
        ElectionType.COUNTY -> "County"
        else -> "Unknown"
    }

    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ConsistencyScoreBar(score: Double) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Consistency Score",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "${(score * 10).toInt()}/10",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        LinearProgressIndicator(
            progress = score.toFloat(),
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = getConsistencyColor(score),
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

private fun getPartyColor(party: String): Color {
    return when (party.uppercase()) {
        "DEMOCRAT", "DEMOCRATIC" -> Color.Blue
        "REPUBLICAN", "GOP" -> Color.Red
        "INDEPENDENT", "I" -> Color.Gray
        "GREEN" -> Color.Green
        "LIBERTARIAN" -> Color.Yellow
        else -> Color.Gray
    }
}

private fun getMatchColor(percentage: Double): Color {
    return when {
        percentage >= 80 -> Color.Green
        percentage >= 60 -> Color(0xFF4CAF50)
        percentage >= 40 -> Color(0xFFFF9800)
        else -> Color.Red
    }
}

private fun getConsistencyColor(score: Double): Color {
    return when {
        score >= 0.8 -> Color.Green
        score >= 0.6 -> Color(0xFF4CAF50)
        score >= 0.4 -> Color(0xFFFF9800)
        else -> Color.Red
    }
}