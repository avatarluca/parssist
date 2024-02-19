package parssist.parser.bottom_up_analysis.lrparser.lr1parser.parser.exception;


/**
 * Exception for no SLR(1) grammar.
 */
public class NoSLR1GrammarException extends Exception {
   public NoSLR1GrammarException() {
       super();
   }

   public NoSLR1GrammarException(String message) {
       super(message);
   }
}