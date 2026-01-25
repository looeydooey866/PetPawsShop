package com.example.petpawsdemo.UIComponents

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.ImeAction

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onFocus: (Boolean) -> Unit
){
    TextField(
        value = query,
        onValueChange = onQueryChange,
        singleLine = true,
        placeholder = {Text("Search on Pet Paws Online Market...")},
        modifier = Modifier.onFocusChanged{
            onFocus(it.isFocused)
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            onSearch()
        }),
    )
}