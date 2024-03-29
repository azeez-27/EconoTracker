package com.example.econotracker

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.econotracker.ui.theme.EconoTrackerTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EconoTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val sharedPreferences = PreferenceManager(this)
                    MyApp(prefs = sharedPreferences)
                }
            }
        }
    }
}

@Composable
fun MyApp(modifier: Modifier = Modifier, prefs: PreferenceManager) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash"){
        composable(route="splash") {SplashScreen(navController = navController)}
        composable(route="home"){ Home(prefs = prefs, navController = navController)}
        composable(route="add-transaction"){ AddTransaction(prefs = prefs, navController = navController)}
        composable(route="overview"){ Overview(prefs = prefs, navController = navController)}
    }

}

class PreferenceManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("budget", Context.MODE_PRIVATE)

    fun getBalance(): Float{
        val cur_balance = prefs.getFloat("balance", 0F)
        return cur_balance
    }

    fun setBalance(balance: Float) {
        val editor = prefs.edit()
        editor.putFloat("balance", balance)
        editor.apply()
    }

    fun getIncome(): Float{
        val income= prefs.getFloat("income", 0F)
        return income
    }

    fun setIncome(income: Float) {
        val editor = prefs.edit()
        editor.putFloat("income", income)
        editor.apply()
    }

    fun getExpense(): Float{
        val expense= prefs.getFloat("expense", 0F)
        return expense
    }

    fun setExpense(expense: Float) {
        val editor = prefs.edit()
        editor.putFloat("expense", expense)
        editor.apply()
    }


}

@Composable
fun SplashScreen(navController: NavController){
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(id = com.example.econotracker.R.drawable.logo), contentDescription = "" ,
            modifier = Modifier.size(200.dp))

        Text("EconoTracker")
        Text("Your Personal Budget Tracker App")

        LaunchedEffect(Unit){
            delay(3000)
            navController.navigate("home")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavController, prefs: PreferenceManager) {
    var balance by remember { mutableStateOf(prefs.getBalance()) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "EconoTracker")
                }

            )
        },
    ) { it ->
        Column(modifier = Modifier.padding(it)) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(20.dp)) {
                Text("Current Balance: ")
                Text(balance.toString())

                Spacer(Modifier.height(26.dp))

                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Button(onClick = {
                        navController.navigate("add-transaction")
                    }) {
                        Text(text = "Add Transaction")
                    }

                    Button(onClick = {
                        navController.navigate("overview")
                    }) {
                        Text(text = "View Details")
                    }

                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransaction(prefs: PreferenceManager, navController: NavController){
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    var id by remember { mutableStateOf(1) }
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                                IconButton(onClick = {navController.popBackStack()}) {
                                   Icons.Outlined.ArrowBack
                                }
               },
                title = {
                    Text(text = "Add Transaction")
                }

            )
        },

    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(10.dp), verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {

                TextField(value = title, onValueChange = {title = it} ,label = {Text("Title")} )
                Spacer(Modifier.height(5.dp))
                TextField(value = amount, onValueChange = {amount = it},label = {Text("Amount")} )
                Spacer(Modifier.height(5.dp))
                TextField(value = type, onValueChange = {type = it},label = {Text("Transaction Type")} )
                Spacer(Modifier.height(5.dp))
                TextField(value = reason, onValueChange = {reason = it} ,label = {Text("Reason")})

                Spacer(Modifier.height(20.dp))

                Button(onClick = {

                        navController.popBackStack()
                    }) {
                        Text(text = "Add Transaction")
                    }


            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Overview(prefs: PreferenceManager, navController: NavController){
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icons.Outlined.ArrowBack
                    }
                },
                title = {
                    Text(text = "Overview")
                }

            )
        },

        ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(10.dp), verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {

                Text("Incomes")
                Text(prefs.getIncome().toString())
                Spacer(Modifier.height(20.dp))

                Text("Expenses")
                Text(prefs.getExpense().toString())
                Spacer(Modifier.height(20.dp))



                Text("Total Balance")
                Text(prefs.getBalance().toString())

                Spacer(Modifier.height(30.dp))

                Text("Last Transactions")
                Spacer(Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth()){
                    Text("Description")
                    Text("Amount")
                }
            }
        }

    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    EconoTrackerTheme {
    }
}