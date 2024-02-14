import { saveInLocalStorage, getInLocalStorage } from '../persistence/localStorageHandler.js';
import {fetchSettings} from '../settings/settings.js';

import './grammar.js';
import './lexer.js';

const saveModal = document.getElementById('saveModal');
const saveChangesBtn = document.getElementById('saveChanges');
const discardChangesBtn = document.getElementById('discardChanges');
const closeModalBtn = document.querySelector('.close');
const grammarModeName = "grammar", lexerModeName = "lexer";

let grammarCode = "";
let lexerCode = "";
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

window.addEventListener('keydown', function (event) {
    if ((event.ctrlKey || event.metaKey) && event.key === 'e') {
        event.preventDefault();
        run()
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

document.getElementById("open-run").addEventListener("click", function() {
    run();
});


function run() {
    console.log("(!) Running...");
    console.log("> Lexer:\n" + lexerCode);
    console.log("> Grammar:\n" + grammarCode);

    const parsetreeDiv = document.getElementById("parsetree-board");
    parsetreeDiv.innerHTML = "";
    parsetree = "";

    const rows_lex = document.querySelectorAll("#tokentable-lex tr");
    const rows_tokens = document.querySelectorAll("#tokentable-tokens tr");

    for(let row of rows_lex) {       
        if(row.className != "table-head") row.parentNode.removeChild(row);
    }
    for(let row of rows_tokens) {
        if(row.className != "table-head") row.parentNode.removeChild(row);
    }

    const settings = fetchSettings();
    const [name, module, algorithm] = [settings.name, settings.module, settings.algorithm];

    let input = null;

    document.getElementById("output-banner").style.display = "none";

    if(settings.parsetree || settings.tokentable || settings.validation) input = askForInput();
    
    if(settings.parsetree) document.getElementById("parsetree").style.display = "flex";
    else document.getElementById("parsetree").style.display = "none";

    if(settings.tokentable) document.getElementById("tokentable").style.display = "flex";
    else document.getElementById("tokentable").style.display = "none";

    if(settings.validation) document.getElementById("validation").style.display = "flex";
    else document.getElementById("validation").style.display = "none";

    document.getElementById("parsetree-title").innerHTML = input;
    document.getElementById("validation-title").innerHTML = input;

    switch(settings.language) {
        case "java":
        default:
            computeJavaParsergenerator({lexerCode, grammarCode, name, module, algorithm}, settings.parsetree, settings.tokentable, settings.validation, input);
            break;
    }

    console.log("(!) ... Finish.");
}

function askForInput() {
    return prompt("Please, provide the input to parse:");
}

export function changeGrammar(code) {
    grammarCode = code;

    if(editor.getOption('mode') === grammarModeName) {
        editor.setValue(code);
    }
}
export function changeLexer(code) {
    lexerCode = code;

    if(editor.getOption('mode') === lexerModeName) {
        editor.setValue(code);
    }
}
export function refreshMainEditor() {
    editor.refresh();
}
export { grammarModeName, lexerModeName };