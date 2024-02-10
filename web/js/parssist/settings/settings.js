const settingsBtn = document.getElementById('open-settings');
const settingsBlock = document.getElementById('settings');
const editorBlock = document.getElementById('editor');
const closeSettingsBtn = document.getElementById('close-settings');
const helpAlgorithmAuto = document.getElementById('help-algorithm-auto');
const helpAlgorithmll1 = document.getElementById('help-algorithm-ll1');

settingsBtn.addEventListener('click', () => {
    editorBlock.style.display = 'none';
    settingsBlock.style.display = 'flex';
});

closeSettingsBtn.addEventListener('click', () => {
    editorBlock.style.display = 'flex';
    settingsBlock.style.display = 'none';
});