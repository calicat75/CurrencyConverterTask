package com.example.currencyconverter.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.currencyconverter.data.dataSource.room.transaction.dbo.TransactionDbo
import com.example.currencyconverter.ui.viewmodel.TransactionsViewModel
import java.time.format.DateTimeFormatter

@Composable
fun TransactionsScreen(
    transactionsViewModel: TransactionsViewModel,
    navController: NavController
) {
    val transactions by transactionsViewModel.transactions.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Transaction History",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
        )

        if (transactions.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No transactions yet.")
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(transactions) { transaction ->
                    TransactionCard(transaction)
                }
            }
        }
    }
}

@Composable
fun TransactionCard(transaction: TransactionDbo) {
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "${transaction.from} → ${transaction.to}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${transaction.fromAmount} ${transaction.from} → ${transaction.toAmount} ${transaction.to}",
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = transaction.dateTime.format(formatter),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
