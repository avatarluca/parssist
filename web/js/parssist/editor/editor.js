import { saveInLocalStorage, getInLocalStorage } from '../persistence/localStorageHandler.js';

import './grammar.js';
import './lexer.js';

const saveModal = document.getElementById('saveModal');
const saveChangesBtn = document.getElementById('saveChanges');
const discardChangesBtn = document.getElementById('discardChanges');
const closeModalBtn = document.querySelector('.close');
const grammarModeName = "grammar", lexerModeName = "lexer";

let grammarCode = "# Test grammar\nS -> TS | eps\nT -> (S)", lexerCode = "# Test lexer\n%\"hi\", \"\\n\"\nTEST := \"Hallo\"";
let grammarCursor = {line: 0, ch: 0, sticky: null}, lexerCursor = {line: 0, ch: 0, sticky: null};
let editorData = getInLocalStorage("editor");

if(editorData) {
    grammarCode = editorData.grammarCode;
    lexerCode = editorData.lexerCode;
    grammarCursor = editorData.grammarCursor;
    lexerCursor = editorData.lexerCursor;
}

const editor = CodeMirror.fromTextArea(document.getElementById('codeboard'), {
    lineNumbers: true,
    theme: 'material',
    mode: grammarModeName,
    cursor: grammarCursor
});
editor.setValue(grammarCode);

editor.on('change', (instance) => {
    const code = instance.getValue();
    const mode = instance.getOption('mode');
    const cursor = instance.getCursor();

    if (mode === grammarModeName) {
        grammarCode = code;
        grammarCursor = cursor;
    }
    else if (mode === lexerModeName) {
        lexerCode = code;
        lexerCursor = cursor;
    }

    saveInLocalStorage("editor", {grammarCode, lexerCode, grammarCursor, lexerCursor});
});

function changeMode(editor, mode, code, cursor) {
    editor.setOption('mode', mode);
    editor.setValue(code);
    editor.setOption('cursor', cursor);
}

document.getElementById('open-grammar').addEventListener('click', () => {
    changeMode(editor, grammarModeName, grammarCode, grammarCursor);
});
document.getElementById('open-lexer').addEventListener('click', () => {
    changeMode(editor, lexerModeName, lexerCode, lexerCursor);
});

window.addEventListener('keydown', function (event) {
    if ((event.ctrlKey || event.metaKey) && event.key === 's') {
        event.preventDefault();
        saveModal.style.display = 'block';
    }
});

saveChangesBtn.addEventListener('click', function () {
    saveModal.style.display = 'none';

    const grammar = new Blob([grammarCode], { type: 'text/plain' });
    const lexer = new Blob([lexerCode], { type: 'text/plain' });
    
    const link1 = document.createElement('a');
    link1.href = URL.createObjectURL(grammar);
    link1.download = 'grammar.gra';

    const link2 = document.createElement('a');
    link2.href = URL.createObjectURL(lexer);
    link2.download = 'lexer.lex';

    document.body.appendChild(link1);
    document.body.appendChild(link2);

    link1.click();
    link2.click();

    document.body.removeChild(link1);
    document.body.removeChild(link2);
});

discardChangesBtn.addEventListener('click', function () {
    saveModal.style.display = 'none';
});

closeModalBtn.addEventListener('click', function () {
    saveModal.style.display = 'none';
});

window.addEventListener('click', function (event) {
    if (event.target === saveModal) {
        saveModal.style.display = 'none';
    }
});