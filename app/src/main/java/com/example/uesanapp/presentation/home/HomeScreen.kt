package com.example.uesanapp.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.uesanapp.data.model.CountryModel

val mockCountries = listOf(
    CountryModel("Colombia", 5, "https://flagpedia.net/data/flags/w1160/co.webp"),
    CountryModel("Perú", 3, "https://flagpedia.net/data/flags/w1160/pe.webp"),
    CountryModel("Argentina", 4, "https://flagpedia.net/data/flags/w1160/ar.webp"),
    CountryModel("Brasil", 2, "https://flagpedia.net/data/flags/w1160/br.webp"),
    CountryModel("Chile", 6, "https://flagpedia.net/data/flags/w1160/cl.webp"),
    CountryModel("Bolivia", 7, "https://flagpedia.net/data/flags/w1160/bo.webp"),
    CountryModel("Portugal", 8, "https://flagpedia.net/data/flags/w1160/pt.webp"),
    CountryModel("Russia", 9, "https://flagpedia.net/data/flags/w1160/ru.webp"),

    )

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier.padding(16.dp).fillMaxSize().statusBarsPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Ranking FIFA 2026")
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn {
            items(mockCountries) { country ->
                Card(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                ) {
                    Row(modifier = Modifier.padding(12.dp)){
                        Image(
                            contentDescription = country.name,
                            modifier = Modifier.size(64.dp),
                            contentScale = ContentScale.Crop,
                            painter = rememberAsyncImagePainter(country.imageUrl)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Column {
                            Text(country.name, style = MaterialTheme.typography.titleMedium)
                            Text("Ranking FIFA 2026: ${country.ranking}")
                        }
                    }
                }
            }
        }
    }

}


