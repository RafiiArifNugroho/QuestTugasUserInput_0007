package com.example.questtugasuserinput4

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.questtugasuserinput4.ui.theme.Questtugasuserinput4Theme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Questtugasuserinput4Theme {
                BiodataScreen()
            }
        }
    }
}

@Composable
private fun dimenSp(id: Int): TextUnit =
    dimensionResource(id).value.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BiodataScreen() {
    val context = LocalContext.current

    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var birthDate by rememberSaveable { mutableStateOf("") }
    var experience by rememberSaveable { mutableStateOf("") }
    val selectedSkills = remember { mutableStateListOf<String>() }

    var showDatePicker by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    val skills = stringArrayResource(R.array.skill_list)
    val experiences = stringArrayResource(R.array.experience_list)
    val datePattern = stringResource(R.string.date_pattern)
    val successMessage = stringResource(R.string.message_success)

    val isFormValid = firstName.isNotBlank() &&
            lastName.isNotBlank() &&
            email.isNotBlank() &&
            birthDate.isNotBlank() &&
            experience.isNotBlank() &&
            selectedSkills.isNotEmpty()

    val fieldShape = RoundedCornerShape(dimensionResource(R.dimen.field_corner))
    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = colorResource(R.color.purple_title),
        unfocusedBorderColor = colorResource(R.color.field_border),
        focusedTextColor = colorResource(R.color.text_primary),
        unfocusedTextColor = colorResource(R.color.text_primary),
        focusedPlaceholderColor = colorResource(R.color.text_hint),
        unfocusedPlaceholderColor = colorResource(R.color.text_hint)
    )
    val fieldTextStyle = LocalTextStyle.current.copy(
        fontSize = dimenSp(R.dimen.text_field)
    )

    Scaffold(
        containerColor = colorResource(R.color.screen_background)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // ===== Banner + indikator =====
            val bannerCount = integerResource(R.integer.banner_count)
            val pagerState = rememberPagerState(pageCount = { bannerCount })

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.banner_height))
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(R.drawable.bg_banner),
                        contentDescription = stringResource(R.string.cd_banner),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = dimensionResource(R.dimen.card_top_offset) + dimensionResource(R.dimen.spacing_small)),
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.dot_spacing))
                ) {
                    repeat(bannerCount) { index ->
                        val color = if (pagerState.currentPage == index)
                            colorResource(R.color.dot_active)
                        else colorResource(R.color.dot_inactive)
                        Box(
                            modifier = Modifier
                                .size(dimensionResource(R.dimen.dot_size))
                                .clip(CircleShape)
                                .background(color)
                        )
                    }
                }
            }

            // ===== Box mengapung di atas Scaffold =====
            val cardShape = RoundedCornerShape(
                topStart = dimensionResource(R.dimen.card_corner),
                topEnd = dimensionResource(R.dimen.card_corner)
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(
                        top = dimensionResource(R.dimen.banner_height) -
                                dimensionResource(R.dimen.card_top_offset)
                    )
                    .shadow(
                        elevation = dimensionResource(R.dimen.card_elevation),
                        shape = cardShape
                    )
                    .clip(cardShape)
                    .background(colorResource(R.color.card_background))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(dimensionResource(R.dimen.card_padding)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.form_title),
                        color = colorResource(R.color.purple_title),
                        fontSize = dimenSp(R.dimen.text_title),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(dimensionResource(R.dimen.spacing_small)))
                    Text(
                        text = stringResource(R.string.form_subtitle),
                        color = colorResource(R.color.text_subtitle),
                        fontSize = dimenSp(R.dimen.text_subtitle),
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(dimensionResource(R.dimen.spacing_large)))

                    // Nama depan & belakang
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))
                    ) {
                        OutlinedTextField(
                            value = firstName,
                            onValueChange = { firstName = it },
                            placeholder = { Text(stringResource(R.string.hint_first_name)) },
                            singleLine = true,
                            shape = fieldShape,
                            colors = fieldColors,
                            textStyle = fieldTextStyle,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = lastName,
                            onValueChange = { lastName = it },
                            placeholder = { Text(stringResource(R.string.hint_last_name)) },
                            singleLine = true,
                            shape = fieldShape,
                            colors = fieldColors,
                            textStyle = fieldTextStyle,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(Modifier.height(dimensionResource(R.dimen.spacing_medium)))

                    // Email
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = { Text(stringResource(R.string.hint_email)) },
                        singleLine = true,
                        shape = fieldShape,
                        colors = fieldColors,
                        textStyle = fieldTextStyle,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(dimensionResource(R.dimen.spacing_medium)))

                    // Tanggal lahir & experience
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))
                    ) {
                        OutlinedTextField(
                            value = birthDate,
                            onValueChange = {},
                            readOnly = true,
                            placeholder = { Text(stringResource(R.string.hint_birth_date)) },
                            trailingIcon = {
                                IconButton(onClick = { showDatePicker = true }) {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = stringResource(R.string.cd_pick_date),
                                        modifier = Modifier.size(dimensionResource(R.dimen.icon_size))
                                    )
                                }
                            },
                            singleLine = true,
                            shape = fieldShape,
                            colors = fieldColors,
                            textStyle = fieldTextStyle,
                            modifier = Modifier.weight(1f)
                        )

                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = it },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = experience,
                                onValueChange = {},
                                readOnly = true,
                                placeholder = { Text(stringResource(R.string.hint_experience)) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                singleLine = true,
                                shape = fieldShape,
                                colors = fieldColors,
                                textStyle = fieldTextStyle,
                                modifier = Modifier
                                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                experiences.forEach { item ->
                                    DropdownMenuItem(
                                        text = { Text(item) },
                                        onClick = {
                                            experience = item
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(dimensionResource(R.dimen.spacing_medium)))

                    // Skill
                    Text(
                        text = stringResource(R.string.label_skill),
                        color = colorResource(R.color.text_primary),
                        fontSize = dimenSp(R.dimen.text_label),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        skills.forEach { skill ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Checkbox(
                                    checked = selectedSkills.contains(skill),
                                    onCheckedChange = { checked ->
                                        if (checked) selectedSkills.add(skill)
                                        else selectedSkills.remove(skill)
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = colorResource(R.color.purple_title),
                                        uncheckedColor = colorResource(R.color.field_border)
                                    )
                                )
                                Text(
                                    text = skill,
                                    color = colorResource(R.color.text_primary),
                                    fontSize = dimenSp(R.dimen.text_checkbox)
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(dimensionResource(R.dimen.spacing_large)))

                    // Tombol Proceed
                    Button(
                        onClick = {
                            Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
                        },
                        enabled = isFormValid,
                        shape = RoundedCornerShape(dimensionResource(R.dimen.button_corner)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.button_enabled),
                            contentColor = colorResource(R.color.button_text),
                            disabledContainerColor = colorResource(R.color.button_disabled),
                            disabledContentColor = colorResource(R.color.button_text)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(dimensionResource(R.dimen.button_height))
                    ) {
                        Text(
                            text = stringResource(R.string.button_proceed),
                            fontSize = dimenSp(R.dimen.text_button),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

    // ===== Date Picker =====
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val sdf = SimpleDateFormat(datePattern, Locale.getDefault()).apply {
                            timeZone = TimeZone.getTimeZone("UTC")
                        }
                        birthDate = sdf.format(Date(millis))
                    }
                    showDatePicker = false
                }) {
                    Text(stringResource(R.string.dialog_ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(R.string.dialog_cancel))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BiodataScreenPreview() {
    Questtugasuserinput4Theme {
        BiodataScreen()
    }
}

