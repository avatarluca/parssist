import { grammarModeName, lexerModeName, changeGrammar, changeLexer, refreshMainEditor} from "../editor/editor.js";

const graSample1 = "S -> TS | $\nT -> (S)";
const lexSample1 = "%\" \", \"\\t\", \"\\n\", \"\\s\", \"\\r\"\nTERMINAL := \"\\(\"\nTERMINAL := \"\\)\"\nEMPTY_SYMBOL := \"\\$\"\nNONTERMINAL := \"S\"\nNONTERMINAL := \"T\"";
const graSample2 = "E -> TE_\nE_ -> +TE_ | $\nT -> FT_\nT_ -> *FT_ | $\nF -> (E) | id";
const lexSample2 = "%\" \", \"\\t\", \"\\n\", \"\\s\", \"\\r\"\nTERMINAL := \"id\"\nTERMINAL := \"\\+\"\nTERMINAL := \"\\*\"\nTERMINAL := \"\\(\"\nTERMINAL := \"\\)\"\nEMPTY_SYMBOL := \"\\$\"\nNONTERMINAL := \"E_\"\nNONTERMINAL := \"E\"\nNONTERMINAL := \"T_\"\nNONTERMINAL := \"T\"\nNONTERMINAL := \"F\"";
const graSample3 = "S -> iEtSS_ | a\nS_ -> eS | $\nE -> b";
const lexSample3 = "%\" \", \"\\t\", \"\\n\", \"\\s\", \"\\r\"\nTERMINAL := \"a\"\nTERMINAL := \"b\"\nTERMINAL := \"e\"\nTERMINAL := \"i\"\nTERMINAL := \"t\"\nEMPTY_SYMBOL := \"\\$\"\nNONTERMINAL := \"S\"\nNONTERMINAL := \"S_\"\nNONTERMINAL := \"E\"";
const graSample4 = "E -> E+T | T\nT -> T*F | F\nF -> (E) | id";
const lexSample4 = "%\" \", \"\\t\", \"\\n\", \"\\s\", \"\\r\"\nTERMINAL := \"id\"\nTERMINAL := \"\\+\"\nTERMINAL := \"\\*\"\nTERMINAL := \"\\(\"\nTERMINAL := \"\\)\"\nEMPTY_SYMBOL := \"\\$\"\nNONTERMINAL := \"E\"\nNONTERMINAL := \"T\"\nNONTERMINAL := \"F\"";


const lexCode1 = document.getElementById('lex-sample-1');
const lexCode2 = document.getElementById('lex-sample-2');
const lexCode3 = document.getElementById('lex-sample-3');
const lexCode4 = document.getElementById('lex-sample-4');
const graCode1 = document.getElementById('gra-sample-1');
const graCode2 = document.getElementById('gra-sample-2');
const graCode3 = document.getElementById('gra-sample-3');
const graCode4 = document.getElementById('gra-sample-4');

const editors = [];
const settingsBlock = document.getElementById('settings');
const editorBlock = document.getElementById('editor');

editors.push(createEditor(graSample1, graCode1, grammarModeName));
editors.push(createEditor(graSample2, graCode2, grammarModeName));
editors.push(createEditor(graSample3, graCode3, grammarModeName));
editors.push(createEditor(graSample4, graCode4, grammarModeName));
editors.push(createEditor(lexSample1, lexCode1, lexerModeName));
editors.push(createEditor(lexSample2, lexCode2, lexerModeName));
editors.push(createEditor(lexSample3, lexCode3, lexerModeName));
editors.push(createEditor(lexSample4, lexCode4, lexerModeName));


function createEditor(code, element, mode) {
    const editor = CodeMirror.fromTextArea(element, {
        lineNumbers: true,
        theme: 'material',
        mode: mode
    });
    editor.setValue(code);

    return editor;
}

function closeSamplesChooser() {
    samplesChooser.style.display = "none";
    editorBlock.style.display = 'flex';
    settingsBlock.style.display = 'none';

    refreshMainEditor();
}

document.getElementById("open-samples").addEventListener("click", function() {
    samplesChooser.style.display = "block";
    
    for(let editor of editors) {
        editor.refresh();
        editor.setOption('readOnly', true);
    }
});
document.getElementById("open-sample-1").addEventListener("click", function() {
    changeGrammar(editors[0].getValue());
    changeLexer(editors[4].getValue());
    closeSamplesChooser();
});
document.getElementById("open-sample-2").addEventListener("click", function() {
    changeGrammar(editors[1].getValue());
    changeLexer(editors[5].getValue());
    closeSamplesChooser();
});
document.getElementById("open-sample-3").addEventListener("click", function() {
    changeGrammar(editors[2].getValue());
    changeLexer(editors[6].getValue());
    closeSamplesChooser();
});
document.getElementById("open-sample-4").addEventListener("click", function() {
    changeGrammar(editors[3].getValue());
    changeLexer(editors[7].getValue());
    closeSamplesChooser();
});
window.addEventListener('keydown', function (event) {
    if (event.key === 'Escape') {
        samplesChooser.style.display = "none";
    }
});