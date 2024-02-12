document.getElementById("open-download").addEventListener("click", () => {
    const java = new Blob([output.getValue()], { type: 'java' });
    
    const link1 = document.createElement('a');
    link1.href = URL.createObjectURL(java);
    link1.download = 'Parser.java';

    document.body.appendChild(link1);

    link1.click();

    document.body.removeChild(link1);
});

document.getElementById("open-download-json").addEventListener("click", () => {
    const json = new Blob([parsetree], { type: 'json' });
    
    const link1 = document.createElement('a');
    link1.href = URL.createObjectURL(json);
    link1.download = 'Tree.json';

    document.body.appendChild(link1);

    link1.click();

    document.body.removeChild(link1);
});