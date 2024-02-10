CodeMirror.defineMode("grammar", function () {
    return {
      token: function (stream) {
        if (stream.match("->") || stream.match("|")) {
            return "grammar-operator";
        } else if(stream.sol() && stream.match("#")) {
            stream.skipToEnd();
            return "grammar-comment";
        } else if(stream.match("eps")) {
            return "grammar-empty";
        } else if(stream.sol() && stream.match(/([A-Z]+ )/)) {
            return "grammar-non-terminal";
        } 

        stream.next();
        return null;
      },
    };
});
  
CodeMirror.defineMIME("text/x-custom", "custom");