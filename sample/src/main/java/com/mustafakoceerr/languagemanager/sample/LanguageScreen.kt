package com.mustafakoceerr.languagemanager.sample

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mustafakoceerr.languagemanager.LanguageMode

/** Language options offered by the sample. Labels are endonyms, so they are not translated. */
private val customLanguages = listOf(
    LanguageMode.Custom("en") to "English",
    LanguageMode.Custom("tr") to "Türkçe",
    LanguageMode.Custom("pt-BR") to "Português (Brasil)",
)

@Composable
fun LanguageScreen(viewModel: LanguageViewModel = hiltViewModel()) {
    val languageMode by viewModel.languageMode.collectAsStateWithLifecycle()
    LanguageContent(
        selected = languageMode,
        onSelect = viewModel::onLanguageSelected,
    )
}

@Composable
private fun LanguageContent(
    selected: LanguageMode,
    onSelect: (LanguageMode) -> Unit,
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
        ) {
            Text(stringResource(R.string.title), style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(8.dp))
            Text(stringResource(R.string.greeting), style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(16.dp))
            Column(Modifier.selectableGroup()) {
                LanguageOption(
                    label = stringResource(R.string.system_default),
                    selected = selected == LanguageMode.SystemDefault,
                    onClick = { onSelect(LanguageMode.SystemDefault) },
                )
                customLanguages.forEach { (mode, label) ->
                    LanguageOption(
                        label = label,
                        selected = selected == mode,
                        onClick = { onSelect(mode) },
                    )
                }
            }
        }
    }
}

@Composable
private fun LanguageOption(label: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = selected, onClick = onClick, role = Role.RadioButton)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(selected = selected, onClick = null)
        Spacer(Modifier.width(8.dp))
        Text(label, style = MaterialTheme.typography.bodyLarge)
    }
}

@Preview(showBackground = true)
@Composable
private fun LanguageContentPreview() {
    MaterialTheme {
        LanguageContent(selected = LanguageMode.Custom("tr"), onSelect = {})
    }
}
