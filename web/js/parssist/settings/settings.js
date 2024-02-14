import { getInLocalStorage, saveInLocalStorage } from "../persistence/localStorageHandler.js";

const settingsBtn = document.getElementById('open-settings');
const settingsBlock = document.getElementById('settings');
const editorBlock = document.getElementById('editor');
const closeSettingsBtn = document.getElementById('close-settings');

settingsBtn.addEventListener('click', () => {
    editorBlock.style.display = 'none';
    settingsBlock.style.display = 'flex';
});

closeSettingsBtn.addEventListener('click', () => {
    editorBlock.style.display = 'flex';
    settingsBlock.style.display = 'none';
});

export function fetchSettings() {
    return {
        algorithm: document.querySelector('input[name="parse-algorithm"]:checked').value,
        parsetree: document.getElementById("output-parsetree").checked,
        language: document.getElementById('output-language').value,
        name: document.getElementById('parser-name').value,
        module: document.getElementById('module-name').value,
        tokentable: document.getElementById('output-tokentable').checked,
        validation: document.getElementById('output-validation').checked
    };
}

function saveSettings() {
    const settings = fetchSettings();
    saveInLocalStorage('settings', settings);
}

(function () {
    const settings = getInLocalStorage('settings');
    if(settings) {
        document.getElementById(`algorithm-${settings.algorithm}`).checked = true;
        document.getElementById('output-parsetree').checked = settings.parsetree;
        document.getElementById('output-language').value = settings.language;
        document.getElementById('parser-name').value = settings.name;
        document.getElementById('module-name').value = settings.module;
        document.getElementById('output-tokentable').checked = settings.tokentable;
        document.getElementById('output-validation').checked = settings.validation;
    }
})();

document.getElementById("algorithm-form").addEventListener('change', function() {
    saveSettings();
});
document.getElementById("output-parsetree").addEventListener('change', function() {
    saveSettings();
});
document.getElementById("output-language").addEventListener('change', function() {
    saveSettings();
});
document.getElementById("parser-name").addEventListener('change', function() {
    saveSettings();
});
document.getElementById("module-name").addEventListener('change', function() {
    saveSettings();
});
document.getElementById("output-tokentable").addEventListener('change', function() {
    saveSettings();
});
document.getElementById("output-validation").addEventListener('change', function() {
    saveSettings();
});