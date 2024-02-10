CodeMirror.defineMode("lexer", function () {
    return {
      token: function (stream) {
        if (stream.match(":=") || stream.match("%") || stream.match(",")) {
            return "lexer-operator";
        } else if(stream.match('"')) {
            return "lexer-regex";
        } else if(stream.sol() && stream.match("#")) {
            stream.skipToEnd();
            return "lexer-comment";
        } else if(stream.sol() && stream.match(/([A-Z]+ )/)) {
            return "lexer-token";
        } 

        stream.next();
        return null;
      },
    };
});
  
CodeMirror.defineMIME("text/x-custom", "custom");