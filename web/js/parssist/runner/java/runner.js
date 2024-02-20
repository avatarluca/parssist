let buffer = "";
function $rt_putStdoutCustom(msg, manager) {
    let index = 0;

    let code = "";

    while (true) {
        let next = msg.indexOf('\n', index);
        if (next < 0) {
            break;
        }
        let line = buffer + msg.substring(index, next);
        code += line;
        buffer = "";
        index = next + 1;
    }
    buffer += msg.substring(index);

    if(manager) manager(code);
}

let javaParsergenerator = null;
function loadParserGenerator() {
    buffer = "";
    TeaVM.wasm.load("js/parssist/runner/java/wasm/app.wasm", {
        installImports(o, controller) {
            function putwchars(address, count) {
                let instance = controller.instance;
                let memory = new Uint8Array(instance.exports.memory.buffer);

                let charCodes = [];
                for (let i = 0; i < count; ++i) {
                    charCodes.push(memory[address++]);
                }

                let string = String.fromCharCode.apply(null, charCodes);
                $rt_putStdoutCustom(string, manageCodeGenerator);
            }
            o.teavm.putwcharsOut = putwchars;
            o.teavm.putwcharsErr = putwchars;
        }, 
    }).then(teavm => {
        this.instance = teavm.instance;
        javaParsergenerator = (cmd, n) => teavm.main([cmd, n.lexerCode, n.grammarCode, n.name, n.module, n.algorithm]).catch(e => handleWasmGenerationException(e)).finally(() => finalTask());
        console.log("> Java WASM handleCodeGeneration module loaded successfully!");
    })
};
loadParserGenerator();

let javaTokentableGenerator = null;
(function() {
    buffer = "";
    TeaVM.wasm.load("js/parssist/runner/java/wasm/app.wasm", {
        installImports(o, controller) {
            function putwchars(address, count) {
                let instance = controller.instance;
                let memory = new Uint8Array(instance.exports.memory.buffer);

                let charCodes = [];
                for (let i = 0; i < count; ++i) {
                    charCodes.push(memory[address++]);
                }

                let string = String.fromCharCode.apply(null, charCodes);
                $rt_putStdoutCustom(string, manageTokentable);
            }
            o.teavm.putwcharsOut = putwchars;
            o.teavm.putwcharsErr = putwchars;
        }, 
    }).then(teavm => {
        this.instance = teavm.instance;
        javaTokentableGenerator = (cmd, lexerCode, input) => teavm.main([cmd, lexerCode, input]).catch(e => handleWasmInputException(e)).finally(() => finalTask());
        console.log("> Java WASM handleTokenTable module loaded successfully!");
    })
})();

let javaParsetreeGenerator = null;
(function () {
    buffer = "";
    TeaVM.wasm.load("js/parssist/runner/java/wasm/app.wasm", {
        installImports(o, controller) {
            function putwchars(address, count) {
                let instance = controller.instance;
                let memory = new Uint8Array(instance.exports.memory.buffer);

                let charCodes = [];
                for (let i = 0; i < count; ++i) {
                    charCodes.push(memory[address++]);
                }

                let string = String.fromCharCode.apply(null, charCodes);
                $rt_putStdoutCustom(string, manageParsetree);
            }
            o.teavm.putwcharsOut = putwchars;
            o.teavm.putwcharsErr = putwchars;
        }, 
    }).then(teavm => {
        this.instance = teavm.instance;
        javaParsetreeGenerator = (cmd, lexerCode, grammarCode, input, algorithm) => teavm.main([cmd, lexerCode, grammarCode, input, algorithm]).catch(e => handleWasmInputException(e)).finally(() => finalTask());
        console.log("> Java WASM handleParseTree module loaded successfully!");
    })
})();

let javaValidationGenerator = null;
(function() {
    buffer = "";
    TeaVM.wasm.load("js/parssist/runner/java/wasm/app.wasm", {
        installImports(o, controller) {
            function putwchars(address, count) {
                let instance = controller.instance;
                let memory = new Uint8Array(instance.exports.memory.buffer);

                let charCodes = [];
                for (let i = 0; i < count; ++i) {
                    charCodes.push(memory[address++]);
                }

                let string = String.fromCharCode.apply(null, charCodes);
                $rt_putStdoutCustom(string, manageValidation);
            }
            o.teavm.putwcharsOut = putwchars;
            o.teavm.putwcharsErr = putwchars;
        }, 
    }).then(teavm => {
        this.instance = teavm.instance;
        javaValidationGenerator = (cmd, lexerCode, grammarCode, input, algorithm) => teavm.main([cmd, lexerCode, grammarCode, input, algorithm]).catch(e => handleWasmInputException(e)).finally(() => finalTask());
        console.log("> Java WASM handleValidation module loaded successfully!");
    })
})();

function computeJavaParsergenerator(args, parsetree, tokentable, validation, input) {
    output.setValue("");
    output.clearHistory();

    javaParsergenerator("codegeneration", args);

    if(input == null) input = "";

    if(parsetree) javaParsetreeGenerator("parsetree", args.lexerCode, args.grammarCode, input, args.algorithm);
    if(tokentable) javaTokentableGenerator("tokentable", args.lexerCode, input);
    if(validation) javaValidationGenerator("validate", args.lexerCode, args.grammarCode, input, args.algorithm);
}

function handleWasmGenerationException(e) {
    output.setValue("");
    output.clearHistory();
    TeaVM.wasm.refresh
    console.warn(e);

    loadParserGenerator();

    output.setValue("WebAssembly Exception:\nCould not generate a parser with these input files :(.\n\nIt is possible that no suitable algorithm has yet been found for the grammar entered.\nIf you are not sure about this, don't hesitate to contact us or create an issue with the grammar in the Github project.");
}

function handleWasmInputException(e) {
    console.warn("Input warning: " + e);
}

function finalTask() {
    document.getElementById("results").style.display = "flex";
    output.refresh();
}

const manageTokentable = (code) => {
    try {
        if(code != "" && code != null) {
            const json = JSON.parse(code);
            const lex_table = document.getElementById("tokentable-lex");
            const token_table = document.getElementById("tokentable-tokens");

            const lex_json = json.tokentypes;
            const token_json = json.tokens;

            for(const property in lex_json) {
                const tr = document.createElement("tr");

                let td = document.createElement("td");
                td.textContent = property;
                tr.appendChild(td);

                td = document.createElement("td");
                td.textContent = lex_json[property]["type"];
                tr.appendChild(td);

                td = document.createElement("td");
                td.textContent = lex_json[property]["regex"];
                tr.appendChild(td);

                td = document.createElement("td");
                td.textContent = lex_json[property]["priority"];
                tr.appendChild(td);

                td = document.createElement("td");
                td.textContent = lex_json[property]["ignore"];
                tr.appendChild(td);

                lex_table.appendChild(tr);
            }

            for(const property in token_json) {
                const tr = document.createElement("tr");

                let td = document.createElement("td");
                td.textContent = property;
                tr.appendChild(td);

                td = document.createElement("td");
                td.textContent = token_json[property]["type"];
                tr.appendChild(td);

                td = document.createElement("td");
                td.textContent = token_json[property]["symbol"];
                tr.appendChild(td);

                token_table.appendChild(tr);
            }
        }
    } catch (e) {
        console.warn("(!) skip current tokentable parsing iteration: " + code);
    }
}
const manageCodeGenerator = (code) => {
    output.setValue(output.getValue() + code);
}
const manageParsetree = (code) => {
    try {
        console.log(code)
        if(code != "" && code != null) {
            console.log("Parsetree: " + code);
            const json = JSON.parse(code);

            const chart = Tree(json, {
                label: (d) => d.name,
                width: 400,
                dyNode: 50
            });

            document.getElementById("parsetree-board").appendChild(chart);
            parsetree += code;
        }
    } catch (e) {
        console.warn("(!) skip current parsetree parsing iteration: " + code);
    }
}
const manageValidation = (code) => {
    console.log("Validation: " + code);
    if(code != "" && code != null) {
        console.log("Validation: " + code);
        document.getElementById("validation-board").innerHTML = code;
    }   
}