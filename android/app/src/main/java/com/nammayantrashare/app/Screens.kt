package com.nammayantrashare.app

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoleSelectionScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var village by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
        Text("Namma-Yantra Share", style = MaterialTheme.typography.headlineLarge, color = Color(0xFF1B4332))
        Spacer(modifier = Modifier.height(24.dp))
        
        Text("Who are you?", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            RoleCard("Farmer", Icons.Default.Agriculture, Modifier.weight(1f)) {
                if (name.isNotEmpty() && phone.isNotEmpty()) {
                    Repository.userSession.value = UserSession(name, phone, village, Role.FARMER)
                    navController.navigate("farmer_browse")
                }
            }
            RoleCard("Owner", Icons.Default.Engineering, Modifier.weight(1f)) {
                if (name.isNotEmpty() && phone.isNotEmpty()) {
                    Repository.userSession.value = UserSession(name, phone, village, Role.OWNER)
                    navController.navigate("owner_dashboard")
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = village, onValueChange = { village = it }, label = { Text("Village") }, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun RoleCard(title: String, icon: ImageVector, modifier: Modifier, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = modifier.height(120.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(48.dp))
            Text(title, fontWeight = FontWeight.Bold)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmerBrowseScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    val filters = listOf("All", "Tractor", "Harvester", "Power Tiller", "Sprayer")
    var selectedFilter by remember { mutableStateOf("All") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Browse Machines") }, actions = { IconButton(onClick = { navController.navigate("profile") }) { Icon(Icons.Default.Person, null) } }) },
        bottomBar = { FarmerBottomBar(navController) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            OutlinedTextField(value = searchQuery, onValueChange = { searchQuery = it }, modifier = Modifier.fillMaxWidth().padding(16.dp), placeholder = { Text("Search machines...") }, leadingIcon = { Icon(Icons.Default.Search, null) })
            
            LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filters) { filter ->
                    FilterChip(selected = selectedFilter == filter, onClick = { selectedFilter = filter }, label = { Text(filter) })
                }
            }

            LazyColumn(modifier = Modifier.weight(1f), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                val filtered = Repository.machines.filter { 
                    (selectedFilter == "All" || it.type == selectedFilter) && it.name.contains(searchQuery, true)
                }
                items(filtered) { machine ->
                    MachineCard(machine) { navController.navigate("booking/${machine.id}") }
                }
            }
        }
    }
}

@Composable
fun MachineCard(machine: Machine, onBook: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(machine.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Badge(containerColor = if (machine.isAvailable) Color(0xFF2D6A4F) else Color.Red) {
                    Text(if (machine.isAvailable) "Available" else "Booked", color = Color.White)
                }
            }
            Text("Owner: ${machine.ownerName}", style = MaterialTheme.typography.bodySmall)
            Text("Phone: ${machine.ownerPhone}", color = MaterialTheme.colorScheme.primary, modifier = Modifier.clickable { /* Call action */ })
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, null, tint = Color(0xFFF59E0B), modifier = Modifier.size(16.dp))
                Text(" ${machine.rating} | ${machine.distance} away", style = MaterialTheme.typography.bodySmall)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("₹${machine.hourlyRate}/hr", fontWeight = FontWeight.Bold)
                    Text("₹${machine.dailyRate}/day", style = MaterialTheme.typography.bodySmall)
                }
                Button(onClick = onBook, enabled = machine.isAvailable) {
                    Text("Book Now")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(navController: NavController, machineId: String?) {
    val machine = Repository.machines.find { it.id == machineId } ?: return
    val session by Repository.userSession
    var date by remember { mutableStateOf("Select Date") }
    var isDaily by remember { mutableStateOf(false) }
    var duration by remember { mutableStateOf(1) }
    val totalPrice = if (isDaily) machine.dailyRate * duration else machine.hourlyRate * duration

    Scaffold(topBar = { TopAppBar(title = { Text("Book ${machine.name}") }) }) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp).verticalScroll(rememberScrollState())) {
            Text("Owner: ${machine.ownerName}", style = MaterialTheme.typography.titleLarge)
            Text(machine.ownerPhone)
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { /* Call */ }, modifier = Modifier.weight(1f)) { Icon(Icons.Default.Call, null); Text(" Call") }
                Button(onClick = { /* WhatsApp */ }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366))) { Text("WhatsApp") }
            }
            
            Divider(Modifier.padding(vertical = 16.dp))
            
            Text("Farmer Details", fontWeight = FontWeight.Bold)
            Text("Name: ${session.name}")
            Text("Phone: ${session.phone}")
            
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(onClick = { date = "15 May 2026" }, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.DateRange, null)
                Text(" $date")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Hourly")
                Switch(checked = isDaily, onValueChange = { isDaily = it })
                Text("Daily")
            }
            
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
                Text("Duration: ")
                IconButton(onClick = { if (duration > 1) duration-- }) { Icon(Icons.Default.Remove, null) }
                Text("$duration ${if (isDaily) "Days" else "Hrs"}", fontWeight = FontWeight.Bold)
                IconButton(onClick = { duration++ }) { Icon(Icons.Default.Add, null) }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total Price", style = MaterialTheme.typography.titleLarge)
                    Text("₹$totalPrice", style = MaterialTheme.typography.headlineMedium, color = Color(0xFF1B4332), fontWeight = FontWeight.Bold)
                }
            }
            
            Button(onClick = {
                Repository.addBooking(Booking(UUID.randomUUID().toString(), session.name, session.phone, machine.ownerPhone, machine.name, date, "$duration ${if (isDaily) "days" else "hrs"}", totalPrice, BookingStatus.PENDING))
                navController.navigate("my_bookings")
            }, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                Text("Confirm Booking")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBookingsScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Pending", "Accepted", "Declined")
    val session by Repository.userSession

    Scaffold(
        topBar = { TopAppBar(title = { Text("My Bookings") }) },
        bottomBar = { FarmerBottomBar(navController) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(selected = selectedTab == index, onClick = { selectedTab = index }, text = { Text(title) })
                }
            }
            
            val status = when(selectedTab) {
                0 -> BookingStatus.PENDING
                1 -> BookingStatus.ACCEPTED
                else -> BookingStatus.DECLINED
            }
            
            val myBookings = Repository.bookings.filter { it.farmerPhone == session.phone && it.status == status }
            
            LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(myBookings) { booking ->
                    BookingCard(booking, isOwner = false)
                }
            }
        }
    }
}

@Composable
fun BookingCard(booking: Booking, isOwner: Boolean, onAccept: (() -> Unit)? = null, onDecline: (() -> Unit)? = null) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(booking.machineName, fontWeight = FontWeight.Bold)
                Text(booking.status.name, color = when(booking.status) {
                    BookingStatus.PENDING -> Color(0xFFF59E0B)
                    BookingStatus.ACCEPTED -> Color(0xFF2D6A4F)
                    BookingStatus.DECLINED -> Color.Red
                })
            }
            Text(if (isOwner) "Farmer: ${booking.farmerName}" else "Owner Phone: ${booking.ownerPhone}")
            Text("Date: ${booking.date} | ${booking.duration}")
            Text("Cost: ₹${booking.cost}", fontWeight = FontWeight.Bold)
            
            if (isOwner && booking.status == BookingStatus.PENDING) {
                Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = onAccept!!, modifier = Modifier.weight(1f)) { Text("Accept") }
                    OutlinedButton(onClick = onDecline!!, modifier = Modifier.weight(1f)) { Text("Decline") }
                }
            } else {
                Button(onClick = { /* Call */ }, modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                    Icon(Icons.Default.Call, null)
                    Text(" Call ${if (isOwner) "Farmer" else "Owner"}")
                }
            }
        }
    }
}

@Composable
fun FarmerBottomBar(navController: NavController) {
    NavigationBar {
        NavigationBarItem(icon = { Icon(Icons.Default.Search, null) }, label = { Text("Browse") }, selected = true, onClick = { navController.navigate("farmer_browse") })
        NavigationBarItem(icon = { Icon(Icons.Default.List, null) }, label = { Text("Bookings") }, selected = false, onClick = { navController.navigate("my_bookings") })
        NavigationBarItem(icon = { Icon(Icons.Default.Calculate, null) }, label = { Text("Price") }, selected = false, onClick = { navController.navigate("price_calculator") })
        NavigationBarItem(icon = { Icon(Icons.Default.Person, null) }, label = { Text("Profile") }, selected = false, onClick = { navController.navigate("profile") })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceCalculatorScreen(navController: NavController) {
    var selectedType by remember { mutableStateOf("Tractor") }
    val types = listOf("Tractor", "Harvester", "Power Tiller", "Sprayer")
    val rates = mapOf("Tractor" to (500.0 to 3500.0), "Harvester" to (800.0 to 5500.0), "Power Tiller" to (300.0 to 2000.0), "Sprayer" to (200.0 to 1200.0))
    
    var isDaily by remember { mutableStateOf(false) }
    var duration by remember { mutableStateOf(1) }
    val currentRates = rates[selectedType]!!
    val total = if (isDaily) currentRates.second * duration else currentRates.first * duration

    Scaffold(topBar = { TopAppBar(title = { Text("Price Calculator") }) }, bottomBar = { FarmerBottomBar(navController) }) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text("Select Machine Type")
            types.forEach { type ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().clickable { selectedType = type }.padding(8.dp)) {
                    RadioButton(selected = selectedType == type, onClick = { selectedType = type })
                    Text(type)
                }
            }
            
            Divider(Modifier.padding(vertical = 16.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Hourly (₹${currentRates.first})")
                Switch(checked = isDaily, onValueChange = { isDaily = it })
                Text("Daily (₹${currentRates.second})")
            }
            
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 16.dp)) {
                Text("Duration: ")
                IconButton(onClick = { if (duration > 1) duration-- }) { Icon(Icons.Default.Remove, null) }
                Text("$duration ${if (isDaily) "Days" else "Hrs"}", style = MaterialTheme.typography.headlineSmall)
                IconButton(onClick = { duration++ }) { Icon(Icons.Default.Add, null) }
            }
            
            Card(modifier = Modifier.fillMaxWidth().height(150.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)) {
                Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Total Estimate", color = Color.White.copy(alpha = 0.7f))
                    Text("₹$total", style = MaterialTheme.typography.displayMedium, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerDashboardScreen(navController: NavController) {
    val myMachines = Repository.machines.size // Simplified
    val pending = Repository.bookings.count { it.status == BookingStatus.PENDING }
    
    Scaffold(
        topBar = { TopAppBar(title = { Text("Owner Dashboard") }) },
        bottomBar = { OwnerBottomBar(navController) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp).verticalScroll(rememberScrollState())) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatCard("Machines", myMachines.toString(), Modifier.weight(1f))
                StatCard("Pending", pending.toString(), Modifier.weight(1f))
                StatCard("Earned", "₹12,400", Modifier.weight(1f))
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            Text("Recent Requests", style = MaterialTheme.typography.titleLarge)
            Repository.bookings.take(3).forEach { booking ->
                BookingCard(booking, isOwner = true, onAccept = { Repository.updateBookingStatus(booking.id, BookingStatus.ACCEPTED) }, onDecline = { Repository.updateBookingStatus(booking.id, BookingStatus.DECLINED) })
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, modifier: Modifier) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, style = MaterialTheme.typography.bodySmall)
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color(0xFF1B4332))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerMachinesScreen(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("My Machines") }) },
        floatingActionButton = { FloatingActionButton(onClick = { /* Add Form */ }) { Icon(Icons.Default.Add, null) } },
        bottomBar = { OwnerBottomBar(navController) }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(Repository.machines) { machine ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(machine.name, fontWeight = FontWeight.Bold)
                            Switch(checked = machine.isAvailable, onValueChange = { /* Toggle */ })
                        }
                        Text("Rate: ₹${machine.hourlyRate}/hr | ₹${machine.dailyRate}/day")
                        Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.End) {
                            IconButton(onClick = { /* Edit */ }) { Icon(Icons.Default.Edit, null) }
                            IconButton(onClick = { /* Delete */ }) { Icon(Icons.Default.Delete, null, tint = Color.Red) }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerRequestsScreen(navController: NavController) {
    val session by Repository.userSession
    val requests = Repository.bookings.filter { it.ownerPhone == session.phone }

    Scaffold(topBar = { TopAppBar(title = { Text("Booking Requests") }) }, bottomBar = { OwnerBottomBar(navController) }) { padding ->
        LazyColumn(modifier = Modifier.padding(padding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(requests) { booking ->
                BookingCard(booking, isOwner = true, onAccept = { Repository.updateBookingStatus(booking.id, BookingStatus.ACCEPTED) }, onDecline = { Repository.updateBookingStatus(booking.id, BookingStatus.DECLINED) })
            }
        }
    }
}

@Composable
fun OwnerBottomBar(navController: NavController) {
    NavigationBar {
        NavigationBarItem(icon = { Icon(Icons.Default.Dashboard, null) }, label = { Text("Home") }, selected = true, onClick = { navController.navigate("owner_dashboard") })
        NavigationBarItem(icon = { Icon(Icons.Default.Agriculture, null) }, label = { Text("Machines") }, selected = false, onClick = { navController.navigate("owner_machines") })
        NavigationBarItem(icon = { Icon(Icons.Default.Notifications, null) }, label = { Text("Requests") }, selected = false, onClick = { navController.navigate("owner_requests") })
        NavigationBarItem(icon = { Icon(Icons.Default.Person, null) }, label = { Text("Profile") }, selected = false, onClick = { navController.navigate("profile") })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val session by Repository.userSession
    Scaffold(topBar = { TopAppBar(title = { Text("Profile") }) }) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.AccountCircle, null, modifier = Modifier.size(100.dp), tint = Color(0xFF1B4332))
            Text(session.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(session.phone, style = MaterialTheme.typography.bodyLarge)
            Text(session.village, style = MaterialTheme.typography.bodyMedium)
            Text("Role: ${session.role.name}", color = MaterialTheme.colorScheme.primary)
            
            Spacer(modifier = Modifier.height(40.dp))
            Button(onClick = { 
                Repository.userSession.value = UserSession()
                navController.navigate("role_selection") { popUpTo(0) }
            }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                Text("Logout", color = Color.White)
            }
        }
    }
}
