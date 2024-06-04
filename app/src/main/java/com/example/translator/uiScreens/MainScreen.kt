@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.translator.uiScreens
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.translator.DE
import com.example.translator.EN
import com.example.translator.ES
import com.example.translator.FR
import com.example.translator.HR
import com.example.translator.TranslatorClass
import com.example.translator.navigation.Screens
import com.example.translator.outputText
import com.example.translator.favorites.FavoritesRepository
import com.example.translator.favorites.FavoritesViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

var speechLanguage =  "HR"
var baseLanguage = mutableStateOf(HR)
var secondLanguage  = mutableStateOf(EN)
var x = ""
val textInput =  mutableStateOf("")

@Composable
fun TranslatorScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val database = FavoritesRepository()
    val viewModel:  FavoritesViewModel = viewModel()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            Spacer(modifier = Modifier.width(16.dp))
            LogoutUser(navController)
            Spacer(modifier = Modifier.width(16.dp))
            MenuButton(navController)
        }

        ScreenTitle("Translator")

        Row {
            Spacer(modifier = Modifier.width(16.dp))
            LanguageDropdownMenu(
                selectedLanguage = baseLanguage.value,
                onLanguageSelected = { newLanguage ->
                    baseLanguage.value = newLanguage
                    speechLanguage = newLanguage
                },
                languages = listOf(HR, EN, DE, ES, FR),
                label = "Base"
            )

            Spacer(modifier = Modifier.width(30.dp))

            LanguageDropdownMenu(
                selectedLanguage = secondLanguage.value,
                onLanguageSelected = { secondLanguage.value = it },
                languages = listOf(HR, EN, DE, ES, FR),
                label = "Target"
            )
            Spacer(modifier = Modifier.width(16.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row{
            x = getTextInput()
        }

        ButtonForTranslating(
            translation = TranslatorClass(), x
        )

        TranslatedTextBox(database,viewModel,context)
    }
}


@Composable
fun ScreenTitle(
    title: String
) {
    Box(
        modifier = Modifier
            .padding(all = 14.dp)
            .background(color = Color.White)
            .fillMaxWidth()
    ){
        Text(
            text = title,
            style = TextStyle(color = Color.Black, fontSize = 26.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 16.dp)
                .align(Alignment.Center)

        )
    }
}

@Composable
fun getTextInput(
): String{
    TextField(
        value = textInput.value, onValueChange = { textInput.value = it},
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .width(250.dp),
        label = { Text("Enter your text here:")},
    )
    return textInput.value
}


@Composable
fun TranslatedTextBox(
    database: FavoritesRepository,
    favoritesViewModel: FavoritesViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    context: Context
) {
    val coroutineScope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .padding(all = 16.dp)
            .background(color = Color.White)
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = MaterialTheme.shapes.medium
            )
            .height(300.dp)
            .fillMaxWidth()


    ) {
        Text(
            text = "Translated Text",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp),
            style = TextStyle(color = Color.Cyan, fontSize = 24.sp),
        )
        Text(
            outputText.value,
            modifier = Modifier.padding(top = 48.dp, start = 8.dp,end = 8.dp),
            style = TextStyle(color = Color.Black, fontSize = 20.sp)
        )
        OutlinedButton(
            onClick = {
                if(outputText.value!="") {
                    coroutineScope.launch {
                        database.saveTextToFirestore(outputText.value, context)
                    }
                }
            },
            modifier = Modifier.align(Alignment.BottomEnd),
        ) {
            Icon(
                Icons.Default.Star,
                contentDescription = "Star",
                tint = Color.Blue,
            )
        }
    }

}
@Composable
fun LogoutUser(
    navController: NavController
){
    val firebaseAuth: FirebaseAuth
    firebaseAuth = FirebaseAuth.getInstance()
    var email = firebaseAuth.currentUser?.email // Get the current user's email from FirebaseAuth.
    email = email?.substringBefore("@")
    Row {
        Text("Hello, $email!", fontSize = 20.sp, style = TextStyle(Color.Black))
    }
    Button(
        onClick = {
            firebaseAuth.signOut()
            navController.navigate(Screens.SignInScreen.route)
        }
    ) {
        Text("SignOut")
    }
}
@Composable
fun MenuButton(
    navController: NavController
) {
    IconButton(
        onClick = {
            navController.navigate(Screens.FavoritesScreen.route)
        }
    ) {
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Menu",
            tint = Color.Black
        )
    }
}


@Composable
fun ButtonForTranslating(translation: TranslatorClass, text:String) {
    val context = LocalContext.current
    Button(
        onClick = {
            translation.onTranslateButtonClick(text,context)
        },
        modifier = Modifier
            .padding(all = 16.dp)
            .fillMaxWidth()
    ) {
        Text(text = "Translate", style = TextStyle(fontSize = 20.sp))
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageDropdownMenu(
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit,
    languages: List<String>,
    label: String
) {
    var expanded by remember { mutableStateOf(false) }
    var currentSelectedLanguage by remember { mutableStateOf(selectedLanguage) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.width(150.dp)
    ) {
        TextField(
            readOnly = true,
            value = currentSelectedLanguage,
            onValueChange = {},
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            languages.forEach { language ->
                DropdownMenuItem(
                    onClick = {
                        currentSelectedLanguage = language
                        onLanguageSelected(language) // Update the selected language
                        expanded = false
                    },
                    text = {language.let { Text(it) }}
                )
            }
        }
    }
}